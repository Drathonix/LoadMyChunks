//? if computercraft {
/*package com.drathonix.loadmychunks.common.integ.cct.peripheral;

import com.drathonix.loadmychunks.common.system.ChunkDataManager;
import com.drathonix.loadmychunks.common.system.ChunkDataModule;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LagometerPeripheral extends AbstractLagometerPeripheral {
    protected final @NotNull BlockPos pos;
    protected final @NotNull ServerLevel level;
    private final @NotNull ChunkDataModule cdm;

    public LagometerPeripheral(@NotNull BlockPos pos, @NotNull Level level){
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
    public @NotNull ChunkDataModule getChunkDataModule() {
        return this.cdm;
    }

    @Override
    protected @NotNull ServerLevel getLevel() {
        return level;
    }

    @Override
    protected @NotNull BlockPos getPosition() {
        return pos;
    }
}
*///?}