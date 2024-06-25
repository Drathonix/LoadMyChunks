package com.vicious.loadmychunks.common.integ.cct.peripheral;

//? if !cct {
/*public class LagometerPeripheral {

}
*///?}

//? if cct {
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LagometerPeripheral extends AbstractLagometerPeripheral {
    protected final BlockPos pos;
    protected final ServerLevel level;
    private ChunkDataModule cdm;

    public LagometerPeripheral(BlockPos pos, Level level){
        this.pos = pos;
        this.level = (ServerLevel)level;
        this.cdm = ChunkDataManager.getOrCreateChunkData(this.level,pos);
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return equals((Object)other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LagometerPeripheral that = (LagometerPeripheral) o;
        return Objects.equals(pos, that.pos) && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, level);
    }

    @Override
    public ChunkDataModule getChunkDataModule() {
        return this.cdm;
    }

    @Override
    protected ServerLevel getLevel() {
        return level;
    }

    @Override
    protected BlockPos getPosition() {
        return pos;
    }
}
//?}