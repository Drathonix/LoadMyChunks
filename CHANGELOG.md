# Load My Chunks Version 1.1.0
Available for Fabric on 1.16.5,1.18.2,1.19.2,1.19.4,1.20.1,1.20.4,1.20.6,1.21, and 1.21.1
Available for Forge on 1.16.5,1.18.2,1.19.2,1.19.4,1.20.1,1.20.4
Available for Neoforge on 1.20.4,1.20.6,1.21,and 1.21.1

First major update behind the CCT support update!

## Chunk Loader Extensions
Chunk Loaders can now have their range increased using a chunk loader extension item. Just click the loader block with the extension item. Range increases in a square shape meaning one upgrade increases coverage from 1x1 chunks to 3x3 chunks with the chunk loader at the center.  Chunks covered by the extension will be considered individually when considering if they exceed the lag threshold. This means that if one chunk in the 3x3 area is laggy it will temporarily disable but the other 8 chunks will continue to be loaded.  The default maximum expansions is 1 but can be increased to 10 via the config file.

Turtles cannot have their load range extended.

## Chunk Loader Item Costs
Requested by TeraNovaLP here [#26](https://github.com/Drathonix/LoadMyChunks/issues/26)

This is disabled by default and can be enabled via the config. You can configure this feature to use any item, amount, and time that you want.  For now, you just need to put an inventory block (chest/hopper/dispenser, modded blocks may not work) on top of the chunk loader. In the future I will make this system cleaner.  For turtle chunk loaders the item must be in the turtle inventory, it will be automatically consumed.

**Costs are per chunk** so extended loaders will consume more items to maintain the additional chunks. 

Items will be consumed before the time is up to ensure the chunk remains loaded.

## Other changes

Swapped to Persist V-1.2.2 for configuration and switched to a JSON5 file. Your old config file will automatically be converted to json5 so do not worry about copying it over yourself if you made changes.
Fixed an unreported breaking issue with MixinServerLevel in MC 1.20.1

Drastically improved multi-versioning utilities and cleaned up a lot of stonecutter logic statements. Some of this multiversioned code will be moved to a library eventually.
# Future Concerns:

LoadMyChunks is very much an experimental project of mine. I intended to develop code for multiple mod loaders and multiple versions of the game and also include support at the same time. A lot of this has been successful, but it is beginning to become much more difficult.  I will continue to support all versions, but I will be taking sometime to improve my multiversioning infrastructure.  This includes updating to stonecutter 5 or 6-alpha which may fix some of the issues I encountered on my recent dev session (mostly parsing bugs and class forgetting) and developing a library mod for my purposes.  

Things I want to add: an advanced GUI (will need multi-versioned code :( ), commands for server lag detection, and chunk-entity ticking.