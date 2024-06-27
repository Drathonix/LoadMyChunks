# Load My Chunks Version 1.0.5

## Load My Computers - Computercraft Integration

Chunk Loaders (of all colors) can now be attached to turtles! They will chunkload any chunks they enter while following the LMC Chunkloading rules.

Chunk Loader and Lagometer blocks now have peripherals with various functions. See [this page](https://github.com/Drathonix/LoadMyChunks/wiki/peripherals)

Finishes [#3](https://github.com/Drathonix/LoadMyChunks/issues/3)

## Bug Fixes

Patched [#13](https://github.com/Drathonix/LoadMyChunks/issues/13): LevelChunkMixin causing Block Entities to double tick occasionally.

Patched [#12](https://github.com/Drathonix/LoadMyChunks/issues/12) Made the mod's chunk data thread safe, to fix a relevant conflict on *some* systems with mods like C^2ME which thread chunk loading.

Patched [#15](https://github.com/Drathonix/LoadMyChunks/issues/15): An issue that likely created ghost chunk loaders on the server side (especially on 1.16.5) when breaking them. If you are running load my chunks on a server you may want to consider deleting <world>/data/loadmychunks_manager.dat and replace your chunk loaders.

Fixed an issue (specifically introduced in the alpha/beta builds) that caused the wrong chunks to be stored to storage. 