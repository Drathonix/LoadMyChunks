# Load My Chunks Version 1.2.4 - Fixes an unusual crash caused by a null value being present in the updating chunk map (should be impossible but who knows how that happens)

# Load My Chunks Version 1.2.3 - Neoforge 1.21.1 only - Sable/Create Aeronautics Compatibility partial fix
1. Fixes an incompatibility with Sable which broke tile entity ticking on physics objects.

DO NOT CHUNK LOAD PHYSICS OBJECTS! ATTEMPTING TO DO SO WILL CAUSE WORLD CORRUPTION ON EXIT! THIS IS NOT AN LMC ISSUE!
[Related Sable issue](https://github.com/ryanhcode/sable/issues/144)

# Load My Chunks Version 1.2.2.1 - Neoforge Hotfix
1. Fixes a mixin crash on neoforge due to some multiversioning issues.
# Load My Chunks Version 1.2.2 - Entity Ticking Bug Fix
**IMPORTANT**: If you are planning to continue using a world previously running an LMC version OLDER THAN 1.2.0 [do not update to this version without reading this](https://github.com/Drathonix/LoadMyChunks/issues/31)
1. Fixes [#38](https://github.com/Drathonix/LoadMyChunks/issues/38), a bug where chunks previously unloaded could no longer tick entities when reloaded.

Apologies to everyone who has been affected by this, it must have been frustrating to debug.

# Load My Chunks Version 1.2.1 - Turtle Chunkloading Bug Fix

# Fixes
1. Fixes [#33](https://github.com/Drathonix/LoadMyChunks/issues/33), a bug where chunkloading turtles would not reload when added to the world in an area that is too far away from a player.

# Load My Chunks Version 1.2.0 - The Big One: Entity ticking and other QOL improvements.
# Changes
1. LMC no longer uses the same method of forcing chunks as the vanilla command. Instead, the chunk is forced using Tickets. 
2. Entity ticking is now possible with the "Lifeforce Broadcaster" upgrade. This enables entity ticking and random block ticking.
   1. This also means that entity ticking is now handled chunkwise and contributes to lag measuring.
   2. Entity lag will still contribute to lag measurements even if no entity ticking chunk loader is present. Be aware that chunks loaded by players are temporarily entity ticking and will have their entities contribute to the tick limit, this may result in overticking before the player leaves the chunk.
3. [Turtles can now chunk load without a peripheral](https://github.com/Drathonix/LoadMyChunks/issues/27), this is configurable and OFF by default. Turtles will also automatically turn on if they are self-chunk loaded. You must open the turtle GUI first for it to be loading chunks!!!
4. Added some new config features. Also added the ability to /loadmychunks reload the config.
5. All config reloads will now request a CDM reload.

# Fixes
1. Made a mixin less intrusive as it was breaking other mods. Fixes [32](https://github.com/Drathonix/LoadMyChunks/issues/32)
2. Made another mixin less intrusive as it was conflicting with Forgified-fabric/sinytra connector breaking the mod on forge/neo only. Fixes [35](https://github.com/Drathonix/LoadMyChunks/issues/35) and the long problematic [24](https://github.com/Drathonix/LoadMyChunks/issues/24)
3. Fixed another deadlocking issue with C2ME. Fixes [30](https://github.com/Drathonix/LoadMyChunks/issues/30)


# Internal Changes
1. Chunk Loader Types are now actually registered to a Mapped Registry, this registry is also registered to the minecraft root registry.
2. Load States are now dynamic and also stored in their own registry.
3. CCT turtle chunk loaders have been reworked internally.
4. Swapped to a heavily modified Rockbreaker buildscript.


## Possible Problems:
1. Performance with C2ME installed may be worse than normal. If you notice this please give me a SPARK PROFILE!!! PLEASE I AM BEGGING FOR A SPARK PROFILE!!!
2. A future NeoForge update in 1.21.2+ may break LMC [Due to this issue](https://github.com/neoforged/NeoForge/issues/2679). 
# Support dropped for 1.16.5 Forge
Sadly I have to drop support for forge 1.16.5, this is because it crashes without providing ANY information. So I'm screwed there.
