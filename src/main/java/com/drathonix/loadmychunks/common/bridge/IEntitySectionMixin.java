package com.drathonix.loadmychunks.common.bridge;

public interface IEntitySectionMixin {
    void lmc$setChunkPos(long pos);
    static void setChunkPos(Object inst, long pos){
        if(inst instanceof IEntitySectionMixin){
            ((IEntitySectionMixin) inst).lmc$setChunkPos(pos);
        } else {
            throw new IllegalStateException("EntitySection mixin not applied!");
        }
    }
}
