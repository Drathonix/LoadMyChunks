package com.vicious.loadmychunks.integ.cct;


import com.vicious.loadmychunks.block.BlockEntityChunkLoader;
import com.vicious.loadmychunks.system.ChunkDataManager;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public class ChunkLoaderPeripheral implements IPeripheral {
    private final BlockEntityChunkLoader loader;

    public ChunkLoaderPeripheral(BlockEntityChunkLoader loader){
        this.loader = loader;
    }


    @Override
    public String getType() {
        return "lmc_chunk_loader";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return false;
    }

    @LuaFunction
    public final long getChunkLastTickDuration(ILuaContext context, IComputerAccess access, IArguments arguments){
        return ChunkDataManager.getOrCreateChunkData((ServerLevel) loader.getLevel(),loader.getBlockPos()).getTickTimer().getDuration();
    }
}
