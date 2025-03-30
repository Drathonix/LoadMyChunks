//? if cc-tweaked {
package com.drathonix.loadmychunks.common.integ.cct.turtle;

import com.drathonix.loadmychunks.common.system.loaders.extension.AtomicExtensionChunkLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class TurtleExtensionChunkLoader extends AtomicExtensionChunkLoader<TurtleChunkLoader> {
    public TurtleExtensionChunkLoader(){}
    public TurtleExtensionChunkLoader(ChunkPos loadedChunk, AtomicReference<TurtleChunkLoader> host){
        super(host,loadedChunk);
    }

    //Turtle chunk loader extensions need to be recreated often and will not com.vicious.com.vicious.persist.
    @Override
    public boolean shouldPersist() {
        return false;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        return tag;
    }

    @Override
    public void load(@NotNull CompoundTag tag, ServerLevel level) {

    }
}
//?}
