package com.vicious.loadmychunks.common.system.loaders.extension;

import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.control.LoadStateEnum;
import com.vicious.loadmychunks.common.system.control.LoadStates;
import com.vicious.loadmychunks.common.system.loaders.DoNotAddException;
import com.vicious.loadmychunks.common.system.loaders.IOwnable;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlacedExtensionChunkLoader extends ExtensionChunkLoader<PlacedChunkLoader> implements IOwnable {
    protected long activityEnd = -1;

    public PlacedExtensionChunkLoader(){}
    public PlacedExtensionChunkLoader(ChunkPos loadedChunk, PlacedChunkLoader host){
        super(host,loadedChunk);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag = super.save(tag);
        LongArrayTag hostsTag = new LongArrayTag(new long[0]);
        for (int i = 0; i < hosts.length; i++) {
            hostsTag.add(LongTag.valueOf(getHost(i).getPosition().asLong()));
        }
        tag.put("hosts",hostsTag);
        tag.putLong("duration",activityEnd);
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) throws DoNotAddException {
        long[] hostPoses = tag.getLongArray("host");
        this.hosts = new PlacedChunkLoader[hostPoses.length];
        if(hosts.length == 0){
            throw new DoNotAddException();
        }
        for (int i = 0; i < hosts.length; i++) {
            BlockPos p = BlockPos.of(hostPoses[i]);
            hosts[i] = ChunkDataManager.computeChunkLoaderIfAbsent(level,p, PlacedChunkLoader.class,loader->loader.getPosition().equals(p),()->new PlacedChunkLoader(p));
        }
        if(tag.contains("duration")){
            activityEnd = tag.getLong("duration");
        }
        super.load(tag, level);
    }

    @Override
    public @Nullable UUID getOwner() {
        PlacedChunkLoader pcl = getPrimaryHostLoader();
        if(pcl != null) {
            return pcl.getOwner();
        }
        else{
            return null;
        }
    }

    @Override
    public void setOwner(@NotNull UUID owner) {

    }

    @Override
    public LoadStates.ILoadState getActiveState() {
        if(hasExceededChunkLimit() || (LMCConfig.cost.enabled && activityEnd == -1)){
            return LoadStateEnum.DISABLED;
        }
        if(!isUnhosted() && getPrimaryHostLoader() != null && !getPrimaryHostLoader().getActiveState().shouldLoad()){
            return LoadStateEnum.DISABLED;
        }
        return loadState;
    }

    @Override
    public void timingsCheck(ServerLevel level, ChunkDataModule chunkDataModule, long gameTime) {
        long timeRemaining = activityEnd-gameTime;
        if(LMCConfig.cost.timeSecondsGained/10L >= timeRemaining){
            if(!isUnhosted() && LMCConfig.consumeFuel(level, getHost(0).getPosition().above())){
                activityEnd=gameTime+LMCConfig.cost.timeSecondsGained*20;
                chunkDataModule.updateCheckTime(activityEnd);
            }
        }
        timeRemaining = activityEnd-gameTime;
        if(timeRemaining <= 0){
            activityEnd = -1;
        }
    }

}
