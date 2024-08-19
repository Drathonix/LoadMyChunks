# Load My Chunks Version 1.0.6-hf2

1.0.6
Fixes a rare NPE in the CDM likely caused by Multithreaded chunk loading (C^2ME as an example).
This also resulted in an elimination of some extraneous code.

1.0.6-hf1
Fixes Missing refmap issue introduced in 1.0.6 for an unknown reason

1.0.6-hf2
Marks many methods within the ChunkDataManager as synchronized to reduce the chance of a multithreading missync occuring and causing a crash.

1.0.6+1.21.1
Updated to a stable architectury version for 1.21.1. Load My Chunks is now available for 1.21.1 without cct integration.