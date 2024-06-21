package com.vicious.loadmychunks.integ.cct;


import com.mojang.authlib.GameProfile;
import com.vicious.loadmychunks.system.control.LoadState;
import com.vicious.loadmychunks.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.system.loaders.IOwnable;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public class ChunkLoaderPeripheral extends LagometerPeripheral {
    private final IChunkLoader loader;

    public ChunkLoaderPeripheral(BlockPos pos, Level level, IChunkLoader loader){
        super(pos,level);
        this.loader = loader;
    }

    @Override
    public String getType() {
        return "lmc_chunk_loader";
    }

    @LuaFunction
    public boolean setActive(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        boolean arg0 = arguments.getBoolean(0);
        boolean ret = loader.getLoadState().shouldLoad();
        loader.setLoadState(arg0 ? LoadState.TICKING : LoadState.DISABLED);
        return ret;
    }

    @LuaFunction
    public void activate(ILuaContext context, IComputerAccess access, IArguments arguments) {
        loader.setLoadState(LoadState.TICKING);
    }

    @LuaFunction
    public void deactivate(ILuaContext context, IComputerAccess access, IArguments arguments) {
        loader.setLoadState(LoadState.DISABLED);
    }

    @LuaFunction
    public void toggleActive(ILuaContext context, IComputerAccess access, IArguments arguments) {
        loader.setLoadState(!loader.getLoadState().shouldLoad() ? LoadState.TICKING : LoadState.DISABLED);
    }

    @LuaFunction
    public String getOwnerName(ILuaContext context, IComputerAccess access, IArguments arguments) {
        if(loader instanceof IOwnable ownable){
            if(!ownable.hasOwner()){
                return null;
            }
            if(level instanceof ServerLevel sl){
                Optional<GameProfile> profile = sl.getServer().getProfileCache().get(ownable.getOwner());
                return profile.map(GameProfile::getName).orElse(null);
            }
        }
        return null;
    }

    @LuaFunction
    public UUID getOwnerUUID(ILuaContext context, IComputerAccess access, IArguments arguments) {
        if(loader instanceof IOwnable ownable){
            if(!ownable.hasOwner()){
                return null;
            }
            if(level instanceof ServerLevel sl){
                Optional<GameProfile> profile = sl.getServer().getProfileCache().get(ownable.getOwner());
                return profile.map(GameProfile::getId).orElse(null);
            }
        }
        return null;
    }
}
