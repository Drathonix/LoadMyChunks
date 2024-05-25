package com.vicious.loadmychunks.system.control;

import com.vicious.loadmychunks.LoadMyChunks;
import com.vicious.loadmychunks.system.ThreadSafetyHelper;
import com.vicious.loadmychunks.system.TickDelayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

public enum LoadState {
    DISABLED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    },
    TICKING,
    OVERTICKED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    },
    PERMANENT{
        @Override
        public boolean permanent() {
            return true;
        }
    },
    PERMANENTLY_DISABLED{
        @Override
        public boolean shouldLoad() {
            return false;
        }
    };

    public boolean shouldLoad(){
        return true;
    }

    public boolean permanent(){
        return false;
    }

    public LoadState getSuperiorLoadState(LoadState loadState) {
        if(this.ordinal() > loadState.ordinal()){
            return this;
        }
        else{
            return loadState;
        }
    }

    //TODO: make sure this is thread safe.
    public void apply(ServerLevel level, ChunkPos pos){
        if(shouldLoad()){
            LoadMyChunks.logger.log(LoadMyChunks.debugLevel,"Forceloading Chunk at: (" + pos.x + "," + pos.z + ") at level: " + name());
            ThreadSafetyHelper.forceChunk(level,pos);
        }
        else{
            LoadMyChunks.logger.log(LoadMyChunks.debugLevel,"Unforceloading Chunk at: (" + pos.x + "," + pos.z + ")");
            ThreadSafetyHelper.unforceChunk(level,pos);
        }
    }

    public void apply(ServerLevel level, long pos){
        apply(level,new ChunkPos(pos));
    }
}
