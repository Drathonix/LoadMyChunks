package com.vicious.loadmychunks.integ.cct;

import com.vicious.loadmychunks.config.LMCConfig;
import com.vicious.loadmychunks.system.ChunkDataManager;
import com.vicious.loadmychunks.system.ChunkDataModule;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LagometerPeripheral implements IPeripheral {
    protected final BlockPos pos;
    protected final Level level;
    public LagometerPeripheral(BlockPos pos, Level level){
        this.pos = pos;
        this.level = level;
    }

    @Override
    public String getType() {
        return "lmc_lagometer";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return super.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkLoaderPeripheral that = (ChunkLoaderPeripheral) o;
        return Objects.equals(pos, that.pos) && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, level);
    }

    @LuaFunction
    public final long getChunkLastTickDuration(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return ChunkDataManager.getOrCreateChunkData((ServerLevel) level, pos).getTickTimer().getDuration();
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
        return ChunkDataManager.getOrCreateChunkData((ServerLevel) level, pos).getCooldownTime();
    }

    @LuaFunction
    public final boolean isChunkOnCooldown(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return ChunkDataManager.getOrCreateChunkData((ServerLevel) level,pos).onCooldown();
    }

    @LuaFunction
    public final boolean isChunkForced(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return ChunkDataManager.getOrCreateChunkData((ServerLevel) level, pos).getLoadState().shouldLoad();
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
        else if(access instanceof IPocketAccess pa){
            if(pa.getEntity() instanceof Player plr){
                ChunkDataModule cdm = ChunkDataManager.getOrCreateChunkData((ServerLevel) plr.level(), plr.blockPosition());
                //TODO: integrate permissions with LP
                return !LMCConfig.instance.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || cdm.containsOwnedLoader(plr.getUUID());
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
