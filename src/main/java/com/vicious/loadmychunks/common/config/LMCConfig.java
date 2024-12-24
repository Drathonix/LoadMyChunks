package com.vicious.loadmychunks.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicious.loadmychunks.common.LoadMyChunks;
import com.vicious.loadmychunks.common.config.regis.ItemStackRetriever;
import com.vicious.persist.annotations.PersistentPath;
import com.vicious.persist.annotations.Save;
import com.vicious.persist.annotations.Range;
import com.vicious.persist.shortcuts.NotationFormat;
import com.vicious.persist.shortcuts.PersistShortcuts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LMCConfig {
    public static LMCConfig instance;
    @PersistentPath(NotationFormat.JSON5)
    private static String path = "config/loadmychunks.json5";
    public static void init(){
        PersistShortcuts.init(LMCConfig.class);
    }

    @Save(description = "The maximum time in milliseconds it can take to tick a chunk completely. If this time is exceeded the chunk will be considered 'overticked' and the loader temporarily disabled. Note these ranges are super arbitrary, Running with 0 will functionally disable the mod and running at 1000 will only disable chunks if they take an entire second to process (1tps) which is basically gameplay ruining tps lag. The default here is equal to ~2 TPS per loaded chunk max which should be fair for well-designed end game bases.")
    @Range(minimum = 1,maximum = 1000)
    public static long msPerChunk = 5;

    @Save(description = "The time in seconds an overticked chunk needs to wait before being reloaded by a chunk loader.")
    @Range(minimum = 1, maximum = 60*60*24*365)
    public static long delayBeforeReload = 300;

    @Save(description = "The time in seconds a reloaded chunk is allowed to exist before chunk tick times are checked.")
    @Range(minimum = 1, maximum = 60*60*24)
    public static long reloadGracePeriod = 5;

    @Save(description = "Allows limiting number of loaded chunks.")
    public static Limit limitSettings = new Limit();

    @Save(description = "The lagometer effectively allows xraying chunks to find bases on chunk lag. On pvp servers I highly recommend setting this to true. On pve servers the lagometer is relatively harmless to player base security. Keep this false.")
    public static boolean lagometerNeedsChunkOwnership = false;

    @Save
    public static boolean useDebugLogging = false;

    @Save(description = "When 2: Usage allowed in all computers. When 1: Usage banned in turtles and pocket computers. When 0: Usage banned in all computers")
    @Range(minimum = 0, maximum = 2)
    public static int lagometerComputerExposureLevel=2;

    @Save(description = "Maximum number of times a chunk loader's range can be extended.")
    @Range(minimum=0,maximum=10)
    public static int maximumRangeExtensions=1;

    @Save(description = "Configures chunk loader item consumption")
    public static Cost cost = new Cost();

    public static boolean isLagometerAllowedOnTurtle(){
        return lagometerComputerExposureLevel == 2;
    }

    public static boolean isLagometerAllowedOnComputer(){
        return lagometerComputerExposureLevel >= 1;
    }

    public static class Cost {
        @Save(description = "Set to true to enable this feature.")
        public boolean enabled = false;

        @Save(description = "The time in seconds each item grants per chunk loader.")
        @Range(minimum = 1)
        public long timeSecondsGained = 60*60*4;

        @Save(description = "Change this to set the itemstack consumed.")
        public ItemStackRetriever itemStack = new ItemStackRetriever(Items.ENDER_PEARL.getDefaultInstance());

      //  @Save(description = "When true, the cost is per loader rather than per chunk loaded. This applies only to extended loaders which load more chunks per loader.")
      //  public boolean useCostPerLoaderMode = false;
    }

    public static class Limit {
        @Save(description = "When enabled players can only have a certain number of concurrently forced chunks")
        public boolean enabledForPlayers = false;
        @Save(description = "When enabled, the environment can only have a certain number of concurrently forced chunks. This is specifically included as a safeguard against non-player placed loaders.")
        public boolean enabledForEnvironment = false;
        @Range(minimum = 0)
        @Save(description = "Maximum number of concurrently loaded chunks a player/the environment can have")
        public int limit = 63;
    }

    public static boolean consumeFuel(ServerLevel level, BlockPos invPos) {
        return consumeFuel(level,invPos,1);
    }
    public static boolean consumeFuel(ServerLevel level, BlockPos invPos, int multi) {
        BlockEntity above = level.getBlockEntity(invPos);
        if(above == null){
            return false;
        }
        if(above instanceof Container){
            Container container = (Container) above;
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if(LMCConfig.cost.itemStack.is(stack)){
                    if(stack.getCount() >= LMCConfig.cost.itemStack.size*multi){
                        stack.shrink(LMCConfig.cost.itemStack.size*multi);
                        container.setItem(i,stack);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
