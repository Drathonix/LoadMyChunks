//? if cct {
package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.system.loaders.extension.AtomicExtensionChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IOwnable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class TurtleExtensionChunkLoader extends AtomicExtensionChunkLoader<TurtleChunkLoader> {
    public TurtleExtensionChunkLoader(){}
    public TurtleExtensionChunkLoader(ChunkPos loadedChunk, AtomicReference<TurtleChunkLoader> host){
        super(host,loadedChunk);
    }

    //Turtle chunk loader extensions need to be recreated often and will not persist.
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

    @Override
    public LoadState getLoadState() {
        if(!isUnhosted() && getPrimaryHostLoader() != null) {
            return getPrimaryHostLoader().getExtensionLoadState();
        }
        return loadState;
    }
}
//?}
