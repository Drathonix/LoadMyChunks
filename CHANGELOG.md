# Load My Chunks Version 1.2.0 - The Big One
**IMPORTANT**: If you are planning to continue using a world running an older LMC version [do not update to this version without reading this](https://github.com/Drathonix/LoadMyChunks/issues/31)

# Changes
1. LMC no longer uses the same method of forcing chunks as the vanilla command. Instead, the chunk is forced using Tickets. Any chunks previously forced this way will be removed from the forced chunks list.
2. Entity ticking is now possible with the "Lifeforce Broadcaster" upgrade. This enables entity ticking and random block ticking.
   1. This also means that entity ticking is now handled chunkwise and contributes to lag measuring.
   2. Entity lag will still contribute to lag measurements even if no entity ticking chunk loader is present. Be aware that chunks loaded by players are temporarily entity ticking and will have their entities contribute to the tick limit, this may result in overticking before the player leaves the chunk.
3. [Turtles can now chunk load without a peripheral](https://github.com/Drathonix/LoadMyChunks/issues/27), this is configurable and OFF by default. Turtles will also automatically turn on if they are self-chunk loaded. You must open the turtle GUI first for it to be loading chunks!!!
4. Added some new config features. Also added the ability to /loadmychunks reload the config.
5. All config reloads will now request a CDM reload.
# Fixes
1. Made a mixin less intrusive as it was breaking other mods. Fixes [32](https://github.com/Drathonix/LoadMyChunks/issues/32)
2. Fixed another deadlocking issue with C2ME. Fixes [30](https://github.com/Drathonix/LoadMyChunks/issues/30)
3. Hopefully
# Internal Changes
1. Chunk Loader Types are now actually registered to a Mapped Registry, this registry is also registered to the minecraft root registry.
2. Load States are now dynamic and also stored in their own registry.
3. CCT turtle chunk loaders have been reworked internally.
