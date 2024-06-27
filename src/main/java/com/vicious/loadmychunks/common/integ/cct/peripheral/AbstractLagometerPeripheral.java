//? if cct {
package com.vicious.loadmychunks.common.integ.cct.peripheral;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.config.LMCConfig;
import com.vicious.loadmychunks.common.system.ChunkDataManager;
import com.vicious.loadmychunks.common.system.ChunkDataModule;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.pocket.IPocketAccess;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractLagometerPeripheral implements IPeripheral {
    public abstract ChunkDataModule getChunkDataModule();

    @Override
    public String getType() {
        return "lmc_lagometer";
    }
    
    protected abstract ServerLevel getLevel();
    protected abstract BlockPos getPosition();

    @LuaFunction
    public final long getChunkLastTickDuration(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().getTickTimer().getDuration();
    }

    @LuaFunction
    public final long getChunkTickDurationLimit(ILuaContext context, IComputerAccess access, IArguments arguments){
        return LMCConfig.instance.msPerChunk;
    }

    @LuaFunction
    public final long getChunkLastTickRatio(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkLastTickDuration(context, access, arguments) / LMCConfig.instance.msPerChunk;
    }

    @LuaFunction
    public final long getChunkCooldownTime(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().getCooldownTime();
    }

    @LuaFunction
    public final boolean isChunkOnCooldown(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().onCooldown();
    }

    @LuaFunction
    public final boolean isChunkForced(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().getLoadState().shouldLoad();
    }

    public void checkPerm(IComputerAccess access) throws LuaException {
        if(!isLagometerPermitted(access)){
            throw new LuaException("This feature is disabled for this computer type by the server.");
        }
    }

    public boolean isLagometerPermitted(IComputerAccess access){
        if(access instanceof ITurtleAccess){
            return LMCConfig.isLagometerAllowedOnTurtle();
        }
        else if(access instanceof IPocketAccess){
            IPocketAccess pa = (IPocketAccess) access;
            if(pa.getEntity() instanceof Player){
                Player plr = (Player) pa.getEntity();
                //TODO: integrate permissions with LP
                return !LMCConfig.instance.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || getChunkDataModule().containsOwnedLoader(plr.getUUID());
            }
            else{
                return LMCConfig.isLagometerAllowedOnTurtle();
            }
        }
        else{
            return LMCConfig.isLagometerAllowedOnComputer();
        }
    }
}
//?}
