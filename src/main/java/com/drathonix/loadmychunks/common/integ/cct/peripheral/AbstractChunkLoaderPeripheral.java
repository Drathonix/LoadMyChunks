//? if cc-tweaked {
package com.drathonix.loadmychunks.common.integ.cct.peripheral;

import com.mojang.authlib.GameProfile;
import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.control.LoadStateEnum;
import com.drathonix.loadmychunks.common.system.loaders.IChunkLoader;
import com.drathonix.loadmychunks.common.system.loaders.IOwnable;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Peripheral for chunk loaders. Extends the lagometer peripheral.
 * @since 1.2.0
 * @author Jack Andersen
 */
public abstract class AbstractChunkLoaderPeripheral extends AbstractLagometerPeripheral {
    /**
     * Gets the chunk loader instance, must not be null.
     * @return the peripheral's target chunk loader.
     */
    public abstract @NotNull IChunkLoader getChunkLoader();

    /**
     * The peripheral type.
     * @return unique string.
     */
    @Override
    public String getType() {
        return "lmc_chunk_loader";
    }

    /**
     * List of additional types.
     */
    private static final Set<String> additional = new HashSet<>();
    static {
        additional.add("lmc_lagometer");
    }

    /**
     * Gets the additional peripheral types.
     * @return [lmc_lagometer]
     */
    @Override
    public Set<String> getAdditionalTypes() {
        return additional;
    }

    /**
     * Toggles the chunk loader's activity state.
     * @param active when false disables the chunk loader, when true enables it.
     */
    public void setActive(boolean active){
        if(active){
            getChunkLoader().setActiveState(getChunkLoader().getDefaultState());
            getChunkDataModule().consumeLoadState(previous->{
                if(getChunkDataModule().addLoader(getLevel(),getChunkLoader())){
                    getChunkDataModule().updateChunkLoadState(getLevel(),previous);
                }
                ChunkDataManager.setDirty(getLevel());
            });
        }
        else{
            getChunkLoader().setActiveState(LoadStateEnum.DISABLED);
            getChunkDataModule().consumeLoadState(previous-> {
                if (getChunkDataModule().removeLoader(getLevel(), getChunkLoader())) {
                    getChunkDataModule().updateChunkLoadState(getLevel(),previous);
                }
            });
            ChunkDataManager.setDirty(getLevel());
        }
    }

    /**
     * Lua method for getting the chunk loader's active state.
     * This is not necessarily the chunk's actual state.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return whether the chunk loader state will load the chunk.
     */
    @LuaFunction
    public final boolean isActive(ILuaContext context, IComputerAccess access, IArguments arguments) {
        return getChunkLoader().getActiveState().shouldLoad();
    }

    /**
     * Lua method for setting the chunk loader's active state.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects a boolean.
     * @throws LuaException if invalid arguments are provided.
     */
    @LuaFunction
    public final void setActive(ILuaContext context, IComputerAccess access, IArguments arguments) throws LuaException {
        setActive(arguments.getBoolean(0));
    }

    /**
     * Syntactic sugar for setting the chunk loader's default state to active.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     */
    @LuaFunction
    public final void activate(ILuaContext context, IComputerAccess access, IArguments arguments) {
        setActive(true);
    }

    /**
     * Syntactic sugar for setting the chunk loader's default state to inactive.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     */
    @LuaFunction
    public final void deactivate(ILuaContext context, IComputerAccess access, IArguments arguments) {
        setActive(false);
    }

    /**
     * Syntactic sugar for setting the chunk loader's default state to its current opposite.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     */
    @LuaFunction
    public final void toggleActive(ILuaContext context, IComputerAccess access, IArguments arguments) {
        setActive(!isActive(context,access,arguments));
    }

    /**
     * Gets the owner profile of the chunk loader. Possibly not present.
     * @return an empty or full optional.
     */
    public final Optional<GameProfile> getOwnerProfile(){
        if(getChunkLoader() instanceof IOwnable) {
            return Optional.ofNullable(((IOwnable) getChunkLoader()).getOwner()).flatMap(owner -> {
                //? >1.16.5 {
                return getLevel().getServer().getProfileCache().get(owner);
                //?} else {
                /*return getLevel().getServer().getProfileCache().get(ownable.getOwner());
                 *///?}
            });
        }
        return Optional.empty();
    }


    /**
     * Gets the chunk loader's owner name. Can be nil.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return nil or a player name String
     */
    @LuaFunction
    public final String getOwnerName(ILuaContext context, IComputerAccess access, IArguments arguments) {
        return getOwnerProfile().map(GameProfile::getName).orElse(null);
    }

    /**
     * Gets the chunk loader's owner UUID. Can be nil.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return nil or a player UUID
     */
    @LuaFunction
    public final UUID getOwnerUUID(ILuaContext context, IComputerAccess access, IArguments arguments) {
        return getOwnerProfile().map(GameProfile::getId).orElse(null);
    }

    /**
     * Checks if the chunk loader should load the chunk in its current state.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return true if the chunk loader should load the chunk in its current state.
     */
    @LuaFunction
    public boolean shouldLoad(ILuaContext context, IComputerAccess access, IArguments arguments){
        return getChunkLoader().getActiveState().shouldLoad();
    }

    /**
     * Checks if the chunk loader should tick entities in the chunk in its current state.
     * @param context the execution context.
     * @param access the computer calling the method.
     * @param arguments a list of arguments. Expects none.
     * @return true if the chunk loader should tick entities in the chunk in its current state.
     */
    @LuaFunction
    public boolean shouldEntityTick(ILuaContext context, IComputerAccess access, IArguments arguments){
        return getChunkLoader().getActiveState().shouldForceEntities();
    }
}
//?}
