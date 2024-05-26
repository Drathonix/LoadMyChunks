package com.vicious.loadmychunks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private static final Path path = Paths.get("config/loadmychunks.json");
    public static void init(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        path.toFile().getParentFile().mkdirs();
        try {
            if(Files.exists(path)) {
                instance = gson.fromJson(new FileReader(path.toFile()), LMCConfig.class);
                revalidate();
            }
            else{
                instance = new LMCConfig();
            }
        } catch (FileNotFoundException e) {
            instance = new LMCConfig();
        }
        try(FileWriter fw = new FileWriter(path.toFile())){
            fw.write(gson.toJson(instance));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void revalidate(){
        for (Field field : LMCConfig.class.getFields()) {
            if(Modifier.isPublic(field.getModifiers()) && field.isAnnotationPresent(ConfigValue.class)){
                if(Number.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(Range.class)){
                    Range range = field.getAnnotation(Range.class);
                    try {
                        double current = ((Number)field.get(instance)).doubleValue();
                        if(current < range.min() || current > range.max()){
                            field.set(instance,field.getType().cast(range.value()));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    // The maximum time in milliseconds it can take to tick a chunk completely.
    // If this time is exceeded the chunk will be considered 'overticked' and the loader temporarily disabled.
    // Note these ranges are super arbitrary, Running with 0 will functionally disable the mod and running at 1000
    // will only disable chunks if they take an entire second to process (1tps) which is basically gameplay ruining tps
    // lag. The default here is equal to -2 TPS per loaded chunk max which should be fair for well-designed end game
    // bases.
    @ConfigValue
    @Range(value = 5,min = 0,max = 1000)
    public long msPerChunk = 5;

    //The time in seconds an overticked chunk needs to wait before being reloaded by a chunk loader.
    @ConfigValue
    @Range(value = 300,min = 1, max = 60*60*24*365)
    public long delayBeforeReload = 300;

    //The time in seconds a reloaded chunk is allowed to exist before chunk tick times are checked.
    @ConfigValue
    @Range(value = 5,min = 1, max = 60*60*24)
    public long reloadGracePeriod = 5;

    //The maximum concurrent chunks per player
    @ConfigValue
    @Range(value = 49,min = 0, max = 1000000)
    public int maxChunksPerPlayer = 49;

    //The lagometer effectively allows xraying chunks to find bases on chunk lag.
    //On pvp servers I highly recommend setting this to true.
    //On pve servers the lagometer is relatively harmless to player base security. Keep this false.
    @ConfigValue
    public boolean lagometerNeedsChunkOwnership = false;

    @ConfigValue
    public boolean useDebugLogging = false;
}
