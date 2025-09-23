package com.drathonix.loadmychunks.common.system;


import com.drathonix.loadmychunks.common.bridge.IChunkMapMixin;
import com.drathonix.loadmychunks.common.bridge.IInformable;
import com.drathonix.loadmychunks.common.bridge.ILevelChunkMixin;
import com.drathonix.loadmychunks.common.bridge.IServerLevelMixin;
import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.registry.custom.LoadStateRegistry;
import com.drathonix.loadmychunks.common.registry.custom.LoaderTypeRegistry;
import com.drathonix.loadmychunks.common.system.control.*;
import com.drathonix.loadmychunks.common.system.loaders.DoNotAddException;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.IOwnable;
import com.drathonix.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.drathonix.loadmychunks.common.util.ModResource;
import com.drathonix.loadmychunks.common.util.MultiversioningHelper;
import com.drathonix.loadmychunks.common.util.ProtectedEntityTickList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;

//? if forge {
/*import net.minecraftforge.entity.PartEntity;
*///?}
//? if neoforge {
/*import net.neoforged.neoforge.entity.PartEntity;
*///?}

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChunkDataModule {
    private final ProtectedEntityTickList entities = new ProtectedEntityTickList();
    private final CombinedTimings chunkTickTimer = new CombinedTimings();
    private Period gracePeriod;
    private Period disabledPeriod;
    public ILoadState defaultLoadState = LoadStateEnum.DISABLED;
    private ILoadState loadState = defaultLoadState;
    private final Set<IChunkLoader> loaders = new HashSet<>();
    private final ChunkPos position;
    //private ILevelChunkMixin chunk;
    private final Set<IInformable> recipients = new HashSet<>();
    private long nextGameTimeCheckTick = -1;

    public ChunkDataModule(ChunkPos position){
        this.position=position;
    }

    public ChunkDataModule(long position){
        this(new ChunkPos(position));
    }

    public void load(CompoundTag tag, ServerLevel level){
        if(tag.contains("grace")){
            gracePeriod = new Period(tag.getLong("grace"));
        }
        if(tag.contains("disabled")){
            disabledPeriod = new Period(tag.getLong("disabled"));
        }
        if(tag.contains("nextCheck")){
            nextGameTimeCheckTick = tag.getLong("nextCheck");
        }
        defaultLoadState = LoadStateRegistry.fromCompound("default",tag,LoadStateRegistry.DISABLED);
        loadState=defaultLoadState;
        ListTag loaders = tag.getList("loaders", 10);
        for (Tag loader : loaders) {
            if(loader instanceof CompoundTag){
                CompoundTag ct = (CompoundTag) loader;
                LoaderTypeRegistry.INSTANCE.getOptional(ModResource.parse(ct.getString("type_id")))
                .ifPresent(obj->{
                    IChunkLoader loaderInst = obj.create();
                    try {
                        loaderInst.load(ct, level);
                        addLoader(level,loaderInst);
                        try {
                            loaderInst.postLoad(level);
                        } catch (DoNotAddException ignored){
                            removeLoader(level,loaderInst);
                        }
                        //Delete loaders that explicitly request to not be added to the CDM (likely due to invalid data).
                    } catch (DoNotAddException ignored){}
                });
            }
        }
        if(loadState.shouldLoad()) {
            startGrace();
        }
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        if(gracePeriod != null){
            tag.putLong("grace",gracePeriod.getEnd());
        }
        if(disabledPeriod != null){
            tag.putLong("disabled",disabledPeriod.getEnd());
        }
        tag.putLong("nextCheck",nextGameTimeCheckTick);
        ListTag loaders = new ListTag();
        for (IChunkLoader loader : this.loaders) {
            if(loader.shouldPersist()) {
                CompoundTag data = new CompoundTag();
                data.putString("type_id", loader.getTypeId().toString());
                data = loader.save(data);
                loaders.add(data);
            }
        }
        tag.put("loaders",loaders);
        defaultLoadState.putCompound("default",tag);
        return tag;
    }

    /**
     * Adds a loader to the chunk data module if it is not already present.
     * @param loader the loader to be added
     * @return whether the chunk's loadstate has changed.
     */
    public boolean addLoader(ServerLevel level, @NotNull IChunkLoader loader){
        loaders.add(loader);
        ILoadState previous = loadState;
        if(onCooldown()){
            loadState = LoadStateEnum.OVERTICKED;
        }
        else{
            loadState = loader.getActiveState().getSuperiorLoadState(loadState);
        }
        nextGameTimeCheckTick=-1;
        if(loader instanceof IOwnable) {}
        return previous != loadState;
    }

    /**
     * Remove a loader from the chunk data module if it is present.
     * @param loader the loader to be removed
     * @return whether the chunk's loadstate has changed.
     */
    public boolean removeLoader(ServerLevel level, @NotNull IChunkLoader loader){
        if(loader.hasExtensions()){
            loader.getExtensionChunkLoaders().recompute(loader.getExtensionClass(),-1, null);
        }
        loaders.remove(loader);
        ILoadState previous = loadState;
        update();
        nextGameTimeCheckTick=-1;
        if(loader instanceof IOwnable){
            if(!getAllOwners().contains(((IOwnable)loader).getOwner())){
                ChunkDataManager.markChunkNotOwnedBy(level,position.toLong(),((IOwnable) loader).getOwner());
            }
        }
        return previous != loadState;
    }

    /**
     * Updates the LoadState based on the loaders in the chunk.
     */
    public void update(){
        loadState = defaultLoadState;
        if(!onCooldown()) {
            for (IChunkLoader loader : loaders) {
                loadState = loader.getActiveState().getSuperiorLoadState(loadState);
                if(loadState.blockEntityTickingPower() == LoaderPower.FORCED){
                    break;
                }
            }
        }
        else{
            loadState= LoadStateEnum.OVERTICKED;
            gracePeriod=null;
        }

    }

    public @NotNull CombinedTimings getTickTimer(){
        return chunkTickTimer;
    }

    public boolean isOverticked(){
        return chunkTickTimer.durationExceeds(LMCConfig.msPerChunk);
    }

    public @Nullable Period getGracePeriod(){
        return gracePeriod;
    }

    public @Nullable Period getDisabledPeriod(){
        return disabledPeriod;
    }

    public @NotNull ILoadState getLoadState(){
        return loadState;
    }

    public @NotNull Set<IChunkLoader> getLoaders() {
        return loaders;
    }

    public boolean shouldUseTimings() {
        return !recipients.isEmpty() || shouldApplyTimings();
    }

    public boolean shouldApplyTimings(){
        return loadState.blockEntityTickingPower().isManaged() && !inGrace();
    }

    public boolean isPermaLoaded(){
        return loadState.shouldLoad() && loadState.permanent();
    }

    public void startGrace(){
        gracePeriod = Period.after(TimeUnit.SECONDS.toMillis(LMCConfig.reloadGracePeriod));
    }

    public void startShutoff(){
        loadState = LoadStateEnum.OVERTICKED;
        disabledPeriod = Period.after(TimeUnit.SECONDS.toMillis(LMCConfig.delayBeforeReload));
    }

    public boolean onCooldown() {
        return disabledPeriod != null && !disabledPeriod.hasEnded();
    }

    public boolean inGrace() {
        return gracePeriod != null && !gracePeriod.hasEnded();
    }

    public @NotNull ChunkPos getPosition(){
        return position;
    }

    public boolean containsOwnedLoader(@NotNull UUID uuid) {
        for (IChunkLoader loader : loaders) {
            if(loader instanceof IOwnable && uuid.equals(((IOwnable)loader).getOwner())){
               return true;
            }
        }
        return false;
    }

    public void addRecipient(IInformable informable) {
        recipients.add(informable);
    }

    public void removeRecipient(IInformable informable){
        recipients.remove(informable);
    }

    public void inform(){
        Iterator<IInformable> iterator = recipients.iterator();
        float frac = chunkTickTimer.getLagFraction();
        while(iterator.hasNext()) {
            IInformable informable = iterator.next();
            informable.lmc$informLagFrac(frac);
            if(informable instanceof BlockEntity){
                if(((BlockEntity) informable).isRemoved()){
                    iterator.remove();
                }
            }
            if(informable instanceof Entity){
                //? if >1.16.5 {
                if(((Entity) informable).chunkPosition().toLong() != position.toLong()){
                    iterator.remove();
                }
                //?}
                //? if <=1.16.5 {
                /*Entity e = (Entity) informable;
                if(new ChunkPos(e.xChunk,e.zChunk).toLong() != position.toLong()){
                    iterator.remove();
                }
                *///?}
            }
        }
    }

    public Set<@NotNull UUID> getPlayerOwners() {
        HashSet<UUID> owners = new HashSet<>();
        for (IChunkLoader loader : loaders) {
            if(loader instanceof IOwnable){
                if(((IOwnable) loader).hasOwner()){
                    owners.add(((IOwnable) loader).getOwner());
                }
            }
        }
        return owners;
    }

    public Set<@Nullable UUID> getAllOwners() {
        HashSet<UUID> owners = new HashSet<>();
        for (IChunkLoader loader : loaders) {
            if(loader instanceof IOwnable){
                owners.add(((IOwnable) loader).getOwner());
            }
        }
        return owners;
    }

    public void clearCooldowns() {
        disabledPeriod=null;
        gracePeriod=null;
    }

    public boolean shouldPersist(){
        return (defaultLoadState != LoadStateEnum.DISABLED) || loadState.permanent() || !loaders.isEmpty();
    }

    @SuppressWarnings("all")
    public long getCooldownTime(){
        return onCooldown() ? getDisabledPeriod().getTimeRemaining() : 0;
    }

    public void updateChunkLoadState(@NotNull ServerLevel level, @NotNull ILoadState previous){
        if(getLoadState().shouldLoad()){
            startGrace();
        }
        getLoadState().apply(level, position.toLong(),previous);
    }

    public @Nullable PlacedChunkLoader getChunkLoaderAt(BlockPos blockPos) {
        for (IChunkLoader loader : loaders) {
            if(loader instanceof PlacedChunkLoader && ((PlacedChunkLoader)loader).getPosition().equals(blockPos)){
                return (PlacedChunkLoader) loader;
            }
        }
        return null;
    }

    public void preTick(ServerLevel level) {
        if(LMCConfig.cost.enabled && level.getGameTime() >= nextGameTimeCheckTick){
            boolean doStateUpdateCheck = false;
            for (IChunkLoader loader : loaders) {
                ILoadState pre = loader.getActiveState();
                loader.timingsCheck(level,this,level.getGameTime());
                if(pre != loader.getActiveState()){
                    doStateUpdateCheck=true;
                }
            }
            if(doStateUpdateCheck) {
                consumeLoadState(previous->{
                    update(()->updateChunkLoadState(level,previous));
                });
            }
        }
    }

    public void update(Runnable onChange){
        ILoadState pre = loadState;
        update();
        if(pre != loadState){
            onChange.run();
        }
    }

    public void updateCheckTime(long time) {
        this.nextGameTimeCheckTick = time;
    }

    public void consumeLoadState(Consumer<ILoadState> consumer){
        consumer.accept(loadState);
    }

    public void tickEntities(ServerLevel sl, ProfilerFiller profilerfiller){
        boolean applyTimings = shouldApplyTimings();
        boolean useTimings = applyTimings || shouldUseTimings();
        IServerLevelMixin mixin = (IServerLevelMixin) sl;
        if(useTimings){
            getTickTimer().startEntities();
        }
        entities.forEach(entity -> {
            if (!MultiversioningHelper.isRemoved(entity)
                //? if >=1.21.2 {
                /*&& !sl.tickRateManager().isEntityFrozen(entity)
                 *///?}
            ) {
                if (mixin.lmc$shouldDiscardEntity(entity)) {
                    //? if >1.16.5 {
                    entity.discard();
                    //?} else {
                    /*entity.remove();
                     *///?}
                } else {
                    profilerfiller.push("checkDespawn");
                    entity.checkDespawn();
                    profilerfiller.pop();
                    if (((IChunkMapMixin)sl.getChunkSource().chunkMap).lmc$inEntityTickingRange(MultiversioningHelper.chunkPosOf(entity).toLong())) {
                        Entity vehicle = entity.getVehicle();
                        if (vehicle != null) {
                            if (!MultiversioningHelper.isRemoved(vehicle) && vehicle.hasPassenger(entity)) {
                                return; // this continues the forEach for anyone confused.
                            }
                            entity.stopRiding();
                        }
                        // Anything here will not be a passenger.
                        profilerfiller.push("tick");
                        // Neoforge/forge specific
                        //? if neoforge || forge {
                        /*if(!(entity instanceof PartEntity))
                            *///?}
                            sl.guardEntityTick(sl::tickNonPassenger, entity);

                        profilerfiller.pop();
                    }
                }
            }
        });
        if(useTimings){
            getTickTimer().endEntities();
        }
    }

    public void lmc$removeEntity(Entity entity){
        entities.remove(entity);
    }
    public void lmc$addEntity(Entity entity){
        entities.add(entity);
    }
}
