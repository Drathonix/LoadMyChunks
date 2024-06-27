//? if cct {
package com.vicious.loadmychunks.common.integ.cct.peripheral;

import com.mojang.authlib.GameProfile;
import com.vicious.loadmychunks.common.bridge.IDestroyable;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.control.LoadState;
import com.vicious.loadmychunks.common.system.loaders.IChunkLoader;
import com.vicious.loadmychunks.common.system.loaders.IOwnable;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.minecraft.server.level.ServerLevel;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractChunkLoaderPeripheral extends AbstractLagometerPeripheral {
    public abstract IChunkLoader getChunkLoader();

    @Override
    public String getType() {
        return "lmc_chunk_loader";
    }

    private static final Set<String> additional = new HashSet<>();
    static {
        additional.add("lmc_lagometer");
    }

    @Override
    public Set<String> getAdditionalTypes() {
        return additional;
    }

    public void setActive(boolean active){
        if(active){
            getChunkLoader().setLoadState(LoadState.TICKING);
            if(getChunkDataModule().addLoader(getChunkLoader())){
                getChunkDataModule().updateChunkLoadState(getLevel());
            }
            ChunkDataManager.setDirty(getLevel());
        }
        else{
            getChunkLoader().setLoadState(LoadState.DISABLED);
            if(getChunkDataModule().removeLoader(getChunkLoader())){
                getChunkDataModule().updateChunkLoadState(getLevel());
            }
            ChunkDataManager.setDirty(getLevel());
        }
    }

    @LuaFunction
    public final boolean isActive(ILuaContext context, IComputerAccess access, IArguments arguments) {
        return getChunkLoader().getLoadState().shouldLoad();
    }

    @LuaFunction
    public final void setActive(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        setActive(arguments.getBoolean(0));
    }

    @LuaFunction
    public final void activate(ILuaContext context, IComputerAccess access, IArguments arguments) {
        setActive(true);
    }

    @LuaFunction
    public final void deactivate(ILuaContext context, IComputerAccess access, IArguments arguments) {
        setActive(false);
    }

    @LuaFunction
    public final void toggleActive(ILuaContext context, IComputerAccess access, IArguments arguments) {
        setActive(!isActive(context,access,arguments));
    }

    @LuaFunction
    public final String getOwnerName(ILuaContext context, IComputerAccess access, IArguments arguments) {
        if(getChunkLoader() instanceof IOwnable){
            IOwnable ownable = (IOwnable) getChunkLoader();
            if(!ownable.hasOwner()){
                return null;
            }
            //? >1.16.5 {
            /*Optional<GameProfile> profile = getLevel().getServer().getProfileCache().get(ownable.getOwner());
            return profile.map(GameProfile::getName).orElse(null);
            *///?}
            //? <=1.16.5 {
            GameProfile profile = getLevel().getServer().getProfileCache().get(ownable.getOwner());
            return profile == null ? null : profile.getName();
            //?}
        }
        return null;
    }

    @LuaFunction
    public final UUID getOwnerUUID(ILuaContext context, IComputerAccess access, IArguments arguments) {
        if(getChunkLoader() instanceof IOwnable){
            IOwnable ownable = (IOwnable) getChunkLoader();
            if(!ownable.hasOwner()){
                return null;
            }
            //? >1.16.5 {
            /*Optional<GameProfile> profile = getLevel().getServer().getProfileCache().get(ownable.getOwner());
            return profile.map(GameProfile::getId).orElse(null);
            *///?}
            //? <=1.16.5 {
            GameProfile profile = getLevel().getServer().getProfileCache().get(ownable.getOwner());
            return profile == null ? null : profile.getId();
            //?}
        }
        return null;
    }

    public boolean shouldLoad(){
        return getChunkLoader().getLoadState().shouldLoad();
    }
}
//?}
