package com.vicious.loadmychunks.common.system;


import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.bridge.ILevelChunkMixin;
import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.system.control.Period;
import com.vicious.loadmychunks.common.system.control.Timings;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IOwnable;
//? if >1.16.5 {
import dev.architectury.networking.NetworkManager;
//?}
import io.netty.buffer.Unpooled;
//? if <=1.16.5 {
/*import me.shedaniel.architectury.networking.NetworkManager;
*///?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChunkDataModule {
    private final Timings chunkTickTimer = new Timings();
    private Period gracePeriod;
    private Period disabledPeriod;
    public LoadState defaultLoadState = LoadState.DISABLED;
    private LoadState loadState = defaultLoadState;
    private final Set<IChunkLoader> loaders = new HashSet<>();
    private final ChunkPos position;
    private ILevelChunkMixin chunk;
    private final Set<ServerPlayer> recipients = new HashSet<>();
    public boolean timeRegardless;

    public ChunkDataModule(ChunkPos position){
        this.position=position;
    }

    public ChunkDataModule(long position){
        this.position=new ChunkPos(position);
    }

    public void load(CompoundTag tag){
        chunkTickTimer.load(tag.getCompound("timings"));
        if(tag.contains("grace")){
            gracePeriod = new Period(tag.getLong("grace"));
        }
        if(tag.contains("disabled")){
            disabledPeriod = new Period(tag.getLong("disabled"));
        }
        defaultLoadState = LoadState.values()[tag.getInt("default")];
        loadState=defaultLoadState;
        ListTag loaders = tag.getList("loaders", 10);
        for (Tag loader : loaders) {
            if(loader instanceof CompoundTag){
                CompoundTag ct = (CompoundTag) loader;
                IChunkLoader inst = LoaderTypeRegistry.getFactory(new ResourceLocation(ct.getString("type_id"))).get();
                inst.load(ct);
                addLoader(inst);
            }
        }
        if(loadState.shouldLoad()) {
            startGrace();
        }
    }

    public CompoundTag save(){
        CompoundTag tag = new CompoundTag();
        tag.put("timings",chunkTickTimer.save());
        if(gracePeriod != null){
            tag.putLong("grace",gracePeriod.getEnd());
        }
        if(disabledPeriod != null){
            tag.putLong("disabled",disabledPeriod.getEnd());
        }
        ListTag loaders = new ListTag();
        for (IChunkLoader loader : this.loaders) {
            CompoundTag data = new CompoundTag();
            data.putString("type_id",loader.getTypeId().toString());
            data = loader.save(data);
            loaders.add(data);
        }
        tag.put("loaders",loaders);
        tag.putInt("default",defaultLoadState.ordinal());
        return tag;
    }

    /**
     * Adds a loader to the chunk data module if it is not already present.
     * @param loader the loader to be added
     * @return whether the chunk's loadstate has changed.
     */
    public boolean addLoader(@NotNull IChunkLoader loader){
        loaders.add(loader);
        LoadState previous = loadState;
        if(onCooldown()){
            loadState = LoadState.OVERTICKED;
        }
        else{
            loadState = loader.getLoadState().getSuperiorLoadState(loadState);
        }
        return previous != loadState;
    }

    /**
     * Remove a loader from the chunk data module if it is present.
     * @param loader the loader to be remove
     * @return whether the chunk's loadstate has changed.
     */
    public boolean removeLoader(@NotNull IChunkLoader loader){
        loaders.remove(loader);
        LoadState previous = loadState;
        update();
        return previous != loadState;
    }

    /**
     * Updates the LoadState based on the loaders in the chunk.
     */
    public void update(){
        loadState = defaultLoadState;
        if(!onCooldown()) {
            for (IChunkLoader loader : loaders) {
                loadState = loader.getLoadState().getSuperiorLoadState(loadState);
                if (loadState == LoadState.PERMANENT) {
                    break;
                }
            }
        }
        else{
            loadState=LoadState.OVERTICKED;
            disabledPeriod=null;
        }
        if(shouldUseTimings()){
            gracePeriod=null;
        }
    }

    public @NotNull Timings getTickTimer(){
        return chunkTickTimer;
    }

    public boolean isOverticked(){
        return chunkTickTimer.durationExceeds(LMCConfig.instance.msPerChunk);
    }

    public @Nullable Period getGracePeriod(){
        return gracePeriod;
    }

    public @Nullable Period getDisabledPeriod(){
        return disabledPeriod;
    }

    public @NotNull LoadState getLoadState(){
        return loadState;
    }

    public @NotNull Set<IChunkLoader> getLoaders() {
        return loaders;
    }

    public boolean shouldUseTimings() {
        return (!loadState.permanent() && loadState.shouldLoad()) && (gracePeriod == null || gracePeriod.hasEnded());
    }

    public void startGrace(){
        gracePeriod = Period.after(TimeUnit.SECONDS.toMillis(LMCConfig.instance.reloadGracePeriod));
    }

    public void startShutoff(){
        loadState = LoadState.OVERTICKED;
        disabledPeriod = Period.after(TimeUnit.SECONDS.toMillis(LMCConfig.instance.delayBeforeReload));
    }

    public boolean onCooldown() {
        return disabledPeriod != null && !disabledPeriod.hasEnded();
    }

    public @NotNull ChunkPos getPosition(){
        return position;
    }

    public void assignChunk(ILevelChunkMixin chunk) {
        this.chunk = chunk;
    }
    public boolean isAssigned(){
        return chunk != null;
    }

    public boolean containsOwnedLoader(@NotNull UUID uuid) {
        for (IChunkLoader loader : loaders) {
            if(loader instanceof IOwnable && uuid.equals(((IOwnable)loader).getOwner())){
               return true;
            }
        }
        return false;
    }

    public void addRecipient(ServerPlayer plr) {
        recipients.add(plr);
    }

    public void inform(){
        Iterator<ServerPlayer> iterator = recipients.iterator();
        float frac = chunkTickTimer.getLagFraction();
        while(iterator.hasNext()){
            ServerPlayer plr = iterator.next();
            FriendlyByteBuf newBuf = new FriendlyByteBuf(Unpooled.buffer());
            newBuf.writeFloat(frac);
            NetworkManager.sendToPlayer(plr, LoadMyChunks.LAG_READING_PACKET_ID, newBuf);
            iterator.remove();
        }
    }

    public Set<UUID> getOwners() {
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

    public void clearCooldowns() {
        disabledPeriod=null;
        gracePeriod=null;
    }
}