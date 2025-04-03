# Load My Chunks Version 1.2.0

# Changes
1. LMC no longer uses the same method of forcing chunks as the vanilla command. Instead, the chunk is forced using Tickets. Any chunks previously forced this way will be removed from the forced chunks list.
2. LMC now overrides the vanilla chunk forcing command to use LMC features. The vanilla command still works the same way but uses LMC's internal chunk loading manager.
3. Entity ticking is now possible with the "Lifeforce Broadcaster" upgrade. This enables entity ticking and random block ticking.
   1. This also means that entity ticking is now handled chunkwise and contributes to lag measuring.
   2. Entity lag will still contribute to lag measurements even if no entity ticking chunk loader is present. Be aware that chunks loaded by players are temporarily entity ticking and will have their entities contribute to the tick limit, this may result in overticking before the play leaves the chunk.

# Internal Changes
1. Chunk Loader Types are now actually registered to a Mapped Registry, this registry is also registered to the miencraft root registry.
2. Load States are now dynamic and also stored in their own registry.
3. 
