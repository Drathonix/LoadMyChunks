Load My Chunks Version 1.0.4

# Features

Added the Lagometer block, comparators can interact with this block to read the lag level in the lagometer's chunk

# Fixes

Fixed an unreported bug that would disable chunk timings when it was actually necessary to keep them active

Fixed an unreported bug that would remove the cooldown timer when a chunk loader was removed from the chunk.

Fixed an unreported bug that would cause loss of chunk loader ownership. Also made ownership persist on placed chunk loader block entities to account movement.

Declared Architectury as a hard dependency.

Added a LICENSE file

# Repository Changes

Migrated all versions to Stonecutter, integrated the mod with the mod publisher plugin. This will greatly improve my ability to update the mod in the future.
