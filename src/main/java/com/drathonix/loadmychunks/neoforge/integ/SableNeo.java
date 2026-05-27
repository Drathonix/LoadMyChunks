//? if neoforge && sable {
package com.drathonix.loadmychunks.neoforge.integ;

import com.drathonix.loadmychunks.common.registry.ILoadedChunkProvider;
import com.drathonix.loadmychunks.common.registry.custom.LoadedChunkProviders;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;

public class SableNeo {
    public static void init(){
        LoadedChunkProviders.addLoadedChunkProvider(new ILoadedChunkProvider() {
            @Override
            public void iterateChunks(ServerLevel level, Consumer<ChunkHolder> consumer) {
                for (ServerSubLevel subLevel : SubLevelContainer.getContainer(level).getAllSubLevels()) {
                    subLevel.getPlot().getLoadedChunks().forEach(v -> {
                        if(v != null){
                            consumer.accept(v);
                        }
                    });
                }
            }

            @Override
            public void iterateChunksEntityTicking(ServerLevel level, Consumer<ChunkHolder> consumer) {
                iterateChunks(level,consumer);
            }

            @Override
            public void iterateChunksBlockEntityTicking(ServerLevel level, Consumer<ChunkHolder> consumer) {
                iterateChunks(level,consumer);
            }

            @Override
            public String getName() {
                return "sable:plot_chunks";
            }
        });

    }
}
//?}
