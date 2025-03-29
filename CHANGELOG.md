# Load My Chunks Version 1.2.0

# Changes
1. LMC no longer uses the same method of forcing chunks as the vanilla command. Instead, the chunk is forced using Tickets. Any chunks previously forced this way will be removed from the forced chunks list.
2. LMC now overrides the vanilla chunk forcing command to use LMC features. The vanilla command still works the same way but uses LMC's internal chunk loading manager.

# Internal Changes
1. Chunk Loader Types are now actually registered to a Mapped Registry, this registry is also registered to the miencraft root registry.
2. Load States are now dynamic and also stored in their own registry.
3. 
