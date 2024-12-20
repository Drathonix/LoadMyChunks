package com.vicious.loadmychunks.common.integ.cct.turtle;

import com.vicious.loadmychunks.common.registry.LoaderTypes;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IChunkPositioned;
import com.vicious.loadmychunks.common.system.loaders.PlacedChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.ExtensionChunkLoaders;
import com.vicious.loadmychunks.common.system.loaders.extension.IExtensionChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.extension.PlacedExtensionChunkLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TurtleChunkLoader extends PlacedChunkLoader {
    @Nullable protected TurtleChunkLoaderPeripheral peripheral;
    public TurtleChunkLoader() {}
    public TurtleChunkLoader(BlockPos pos, @Nullable TurtleChunkLoaderPeripheral peripheral, int r, ExtensionChunkLoaders extensions) {
        super(pos);
        this.extensionRange=r;
        this.extensions = extensions;
    }

    public TurtleChunkLoader(BlockPos pos) {
        super(pos);
    }

    @Override
    public ResourceLocation getTypeId() {
        return LoaderTypes.CCT_TURTLE_LOADER;
    }

    public TurtleChunkLoader move(TurtleChunkLoaderPeripheral peripheral, BlockPos newPosition) {
        TurtleChunkLoader moved = new TurtleChunkLoader(newPosition, peripheral, extensionRange, extensions);
        if(moved.extensions != null) {
            moved.extensions.recompute(TurtleExtensionChunkLoader.class, extensionRange, this::createTurtleExtension);
        }
        return moved;
    }

    public TurtleExtensionChunkLoader createTurtleExtension(ChunkPos position) {
        if(peripheral == null) {
            throw new IllegalStateException("No peripheral.");
        }
        return new TurtleExtensionChunkLoader(position, peripheral.getMutableChunkLoader());
    }

    @Override
    public ExtensionChunkLoaders.Factory<?> getExtensionFactory() {
        return this::createTurtleExtension;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IExtensionChunkLoader<?>> Class<T> getExtensionClass() {
        return (Class<T>) TurtleExtensionChunkLoader.class;
    }

    @Override
    public boolean supportsExtensions() {
        return true;
    }

    public void setPeripheral(@Nullable TurtleChunkLoaderPeripheral peripheral) {
        this.peripheral = peripheral;
    }
}
