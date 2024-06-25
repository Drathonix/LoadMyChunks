package com.vicious.loadmychunks.common.integ.cct.peripheral;

//? if !cct {
/*public class ChunkLoaderPeripheral {

}
*///?}

//? if cct {
import com.mojang.authlib.GameProfile;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IOwnable;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ChunkLoaderPeripheral extends AbstractChunkLoaderPeripheral {
    private final BlockPos pos;
    private final IChunkLoader loader;
    private final ServerLevel level;
    private final ChunkDataModule cdm;

    public ChunkLoaderPeripheral(BlockPos pos, Level level, IChunkLoader loader){
        this.pos = pos;
        this.loader = loader;
        this.level=(ServerLevel)level;
        this.cdm = ChunkDataManager.getOrCreateChunkData(this.level,pos);
    }

    @Override
    public IChunkLoader getChunkLoader() {
        return loader;
    }

    @Override
    public ChunkDataModule getChunkDataModule() {
        return this.cdm;
    }

    @Override
    public String getType() {
        return "lmc_chunk_loader";
    }

    @Override
    protected ServerLevel getLevel() {
        return level;
    }

    @Override
    protected BlockPos getPosition() {
        return pos;
    }

    private static final Set<String> additional = new HashSet<>();
    static {
        additional.add("lmc_lagometer");
    }

    @Override
    public Set<String> getAdditionalTypes() {
        return additional;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return false;
    }
}
//?}