package com.vicious.loadmychunks.common.system.control;

import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.system.ThreadSafetyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class LoadStates {
    private static final List<ILoadState> states = new ArrayList<>();

    public static final ILoadState DISABLED = LoadStateEnum.DISABLED;
    public static final ILoadState TICKING = LoadStateEnum.TICKING;
    public static final ILoadState ENTITY_TICKING;
    public static final ILoadState OVERTICKED = LoadStateEnum.OVERTICKED;
    public static final ILoadState PERMANENT = LoadStateEnum.PERMANENT;
    public static final ILoadState PERMANENTLY_DISABLED = LoadStateEnum.PERMANENTLY_DISABLED;

    static {
        for (LoadStateEnum value : LoadStateEnum.values()) {
            registerLoadState(value);
        }
        ENTITY_TICKING = new ILoadState() {
            private final int id = getNextOpenId();
            @Override
            public Power blockEntityTickingPower() {
                return Power.FORCED_MANAGED;
            }

            @Override
            public Power entityForcingPower() {
                return Power.FORCED_MANAGED;
            }

            @Override
            public int id() {
                return id;
            }
        };
        registerLoadState(ENTITY_TICKING);
    }

    public static void registerLoadState(ILoadState state){
        states.add(state);
    }

    public static int getNextOpenId(){
        return states.size();
    }

    public static ILoadState fromCompound(String key, CompoundTag tag, ILoadState defaultState){
        if(tag.contains(key)){
            int k = tag.getInt(key);
            if(states.size() < k || k < 0){
                return defaultState;
            }
            return states.get(k);
        }
        return defaultState;
    }

    public static void putCompound(String key, CompoundTag tag, ILoadState loadState) {
        tag.putInt(key,loadState.id());
    }


    public interface ILoadState {
        /**
         * Controls if this state can force a chunk to remain loaded and tick block entities.
         * @return MAXIMUM if unrestricted by LMC chunk rules, RESTRICTED if follows chunk rules, DISABLED if not forced at all.
         */
        Power blockEntityTickingPower();
        /**
         * Controls if this state can force a chunk tick entities and do random ticks.
         * @return MAXIMUM if unrestricted by LMC chunk rules, RESTRICTED if follows chunk rules, DISABLED if not forced at all.
         */
        Power entityForcingPower();

        int id();

        /**
         * @param state some other load state.
         * @return If this state will override the other state.
         */
        default boolean overrides(ILoadState state){
            return blockEntityTickingPower().ordinal() >= state.blockEntityTickingPower().ordinal() && entityForcingPower().ordinal() >= state.entityForcingPower().ordinal();
        }

        default boolean shouldLoad(){
            return blockEntityTickingPower() == Power.FORCED_MANAGED || blockEntityTickingPower() == Power.FORCED;
        }

        default boolean shouldForceEntities(){
            return entityForcingPower() == Power.FORCED_MANAGED || entityForcingPower() == Power.FORCED;
        }

        default ILoadState getSuperiorLoadState(ILoadState loadState){
            if(loadState.overrides(this)){
                return loadState;
            }
            return this;
        }

        default boolean permanent(){
            return blockEntityTickingPower() == Power.FORCED;
        }

        default void apply(ServerLevel level, long pos){
            apply(level, new ChunkPos(pos));
        }
        default void apply(ServerLevel level, BlockPos pos){
            apply(level,new ChunkPos(pos));
        }
        //TODO: make sure this is thread safe.
        default void apply(ServerLevel level, ChunkPos pos){
            if(shouldLoad()){
                if(!shouldForceEntities()){
                    LoadMyChunks.logger.log(LoadMyChunks.debugLevel,"Forceloading Chunk at: (" + pos.x + "," + pos.z + ") with level " + blockEntityTickingPower().name());
                    ThreadSafetyHelper.forceChunk(level,pos);
                }
                else{
                    LoadMyChunks.logger.log(LoadMyChunks.debugLevel,"Entity Ticking Chunk at: (" + pos.x + "," + pos.z + ") with level " + entityForcingPower().name());
                    ThreadSafetyHelper.forceChunk(level,pos,true);
                }
            }
            else{
                LoadMyChunks.logger.log(LoadMyChunks.debugLevel,"Unforceloading Chunk at: (" + pos.x + "," + pos.z + ")");
                ThreadSafetyHelper.unforceChunk(level,pos);
            }
        }
    }

    public enum Power {
        DISABLED,
        FORCED_MANAGED,
        FORCED,
        DISABLED_PERMANENT;
    }
}
