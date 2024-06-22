# LoadMyChunks 

[![Modrinth](https://img.shields.io/modrinth/dt/load-my-chunks?colour=00AF5C&label=downloads&logo=modrinth)](https://modrinth.com/mod/load-my-chunks) [![CurseForge](https://cf.way2muchnoise.eu/full_1024152_downloads.svg)](https://curseforge.com/minecraft/mc-mods/load-my-chunks)

### Load My Chunks is a standalone server-safe chunk loader mod that reduces server lag by refusing to load laggy chunks
 
## Functionality
Block Entity ticking is now executed chunk-wise rather than dimension wise. Chunks time their tick duration only if necessary.

Chunks are forceloaded only if they contain at least one chunk loader. If the chunk tick duration exceeds the server side maximum, then the chunk will cease being forceloaded and enter a dormant state for some time.

Dormant forced chunks will awaken once more at the end of the dormant period and enter a brief grace period where they are immune from tick duration checks, this is done to give multiblock structures a chance to reinitialize (I.E. Mekanism multiblocks which have an initial overhead on chunk load).

Chunk loaders work with or without the player being online.

## Utility Items
Advanced Information about a chunk's lag and load state can be obtained using the chunkometer.

## Configurability
For info on configurability see the [Wiki](https://github.com/Drathonix/LoadMyChunks/wiki).

## Conflicts and Dependencies
Load My Chunks depends Architectury on architectury for platform abstraction.

The mod supports Java 8 on MC1.16.5, Java 17 on 1.18.2-1.20.4, and Java 21 on 1.20.6+. It is recommended to use the correct Java version to avoid issues.

There are currently no known mod conflicts.
