//? if cc-tweaked {
package com.drathonix.loadmychunks.common.integ.cct.peripheral;

import com.drathonix.loadmychunks.common.config.LMCConfig;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
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
import org.jetbrains.annotations.NotNull;

/**
 * Peripheral for the lagometer. Extended by {@link ChunkLoaderPeripheral}
 * @since 1.2.0
 * @author Jack Andersen
 */
public abstract class AbstractLagometerPeripheral implements IPeripheral {
    /**
     * Gets the chunk data module for the lagometer.
     * @return the peripheral's CDM
     */
    public abstract @NotNull ChunkDataModule getChunkDataModule();

    @Override
    public String getType() {
        return "lmc_lagometer";
    }

    /**
     * Gets the level for the lagometer.
     * @return the peripheral's level
     */
    protected abstract @NotNull ServerLevel getLevel();

    /**
     * Gets the pos for the lagometer.
     * @return the peripheral's block pos
     */
    protected abstract @NotNull BlockPos getPosition();

    /**
     * Gets the chunk's last tick length.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return the chunk's last tick length.
     * @throws LuaException if the lagometer measurement feature is permitted for this computer by the server side config.
     */
    @LuaFunction
    public final long getChunkLastTickDuration(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().getTickTimer().getDuration();
    }

    /**
     * Gets the chunk tick duration limit for the server.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return {@link LMCConfig#msPerChunk}
     */
    @LuaFunction
    public final long getChunkTickDurationLimit(ILuaContext context, IComputerAccess access, IArguments arguments){
        return LMCConfig.msPerChunk;
    }

    /**
     * Gets the chunk's last tick ratio.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return the chunk's last tick length divided by {@link LMCConfig#msPerChunk}.
     * @throws LuaException if the lagometer measurement feature is permitted for this computer by the server side config.
     */
    @LuaFunction
    public final long getChunkLastTickRatio(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkLastTickDuration(context, access, arguments) / LMCConfig.msPerChunk;
    }

    /**
     * Gets the chunk's current force loading cooldown time.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return the chunk's cooldown time or 0 if it is not on cooldown.
     * @throws LuaException if the lagometer measurement feature is permitted for this computer by the server side config.
     */
    @LuaFunction
    public final long getChunkCooldownTime(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().getCooldownTime();
    }

    /**
     * Checks if the chunk is on cooldown.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return if the chunk is on cooldown.
     * @throws LuaException if the lagometer measurement feature is permitted for this computer by the server side config.
     */
    @LuaFunction
    public final boolean isChunkOnCooldown(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().onCooldown();
    }

    /**
     * Checks if the chunk is forced.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return if the chunk is forced.
     * @throws LuaException if the lagometer measurement feature is permitted for this computer by the server side config.
     */
    @LuaFunction
    public final boolean isChunkForced(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        checkPerm(access);
        return getChunkDataModule().getLoadState().shouldLoad();
    }

    /**
     * Checks if the server permits lagometer lag checks.
     * @param access the computer calling the method - this is critically important as Turtles and Stationary Computers do have separate permission levels.
     * @throws LuaException if this feature is not permitted for the computer type.
     */
    public void checkPerm(IComputerAccess access) throws LuaException {
        if(!isLagometerPermitted(access)){
            throw new LuaException("This feature is disabled for this computer type by the server.");
        }
    }

    /**
     * Check if the lagometer can measure lag. Implemented primarily for PvP servers that do not want automated lag checks using loader turtles.
     * @param access the computer.
     * @return if this feature is not permitted for the computer type.
     */
    public boolean isLagometerPermitted(IComputerAccess access){
        if(access instanceof ITurtleAccess){
            return LMCConfig.isLagometerAllowedOnTurtle();
        }
        else if(access instanceof IPocketAccess){
            IPocketAccess pa = (IPocketAccess) access;
            if(pa.getEntity() instanceof Player){
                Player plr = (Player) pa.getEntity();
                //TODO: integrate permissions with LP
                return !LMCConfig.lagometerNeedsChunkOwnership || plr.hasPermissions(2) || getChunkDataModule().containsOwnedLoader(plr.getUUID());
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
