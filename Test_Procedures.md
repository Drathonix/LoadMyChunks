Because this is an MC mod, tests need to be done manually.

# How I do tests
All tests are performed at least once on each Version-Loader combination using a prism instance and using the world file in testsaves.

# Procedures
### Boots
Self-explanatory, the game must successfully load and joining a world must work as expected.

### Placed Chunk Loader
1. Place chunk loader.
2. Use /loadmychunks list forced to verify the chunk is forced.

### Remove Chunk Loader
1. Remove chunk loader.
2. Use /loadmychunks list forced to verify the chunk is not forced anymore.

### Placed & Extended Chunk Loader
1. Place chunk loader.
2. Use an extender item on it.
3. Use /loadmychunks list forced to verify the expected chunks are forced.

### Remove Extended Chunk Loader
1. Remove chunk loader.
2. Use /loadmychunks list forced to verify the expected chunks are not forced anymore.

### Chunk Loader Item Consumption
1. Place a chunk loader.
2. Use /loadmychunks list forced to verify the chunk is not forced.
3. Place a chest on top, insert ender pearl.
4. Use /loadmychunks list forced to verify the chunk is forced.

### Test forceload command
After each command, run /loadmychunks list (forced/overticked) to view.
1. /loadmychunks forceload - enables normal LMC forceloading behavior
2. /loadmychunks forceload true - permanently forceloads a chunk.
3. /loadmychunks unforceload - Unforces a chunk if it has been forced via command. Loaders will still work.
4. /loadmychunks unforceload true - Permanently prevents a chunk from being forceloaded by loaders.

### Test entity ticking
1. Apply a lifeforce broadcaster to an existing chunk loader.
2. Place a random tickable block (some crop)
3. Teleport over 1000 blocks away. (Record current position before doing this)
4. Set random tick speed to 1000. Wait ~5 seconds.
5. Set RTS back to 3.
6. Return to chunk loader. If crop has grown, this feature is working.

### CCT Tests
Tests are executed on a turtle using this script:
```lua
turtle.refuel()

k = 64
while k >= 0 do
    turtle.forward(1)
    k=k-1
end
```
/loadmychunks list forced to verify.

# Test Table Syntax

0 means a failure
1 means success
2 means not applicable to the version.
3 means unchanged in this update.

# 1.2.0 Engine Tests (Engine is the same throughout all versions so these features are not dependent on MC Version)

Chunk Loader Item Consumption: Works
Chunk Loader Limit: Works
Forceload Command: Works

# 1.2.0 Test Table

| Version \| Procedure | Boots | Placed Chunk Loader | Removed Chunk Loader | Placed & Extended Chunk Loader | Removed Extended Chunk Loader | Test Entity Ticking | CCT: Repeat tasks 2,3 & 7 |
|----------------------|-------|---------------------|----------------------|--------------------------------|-------------------------------|---------------------|---------------------------|
| 1.16.5-Forge         | 0     |                     |                      |                                |                               |                     |                           |
| 1.16.5-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 2                         |
| 1.18.2-Forge         | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.18.2-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 2                         |
| 1.19.2-Forge         | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.19.2-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 2                         |
| 1.19.4-Forge         | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.19.4-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.20.1-Forge         | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.20.1-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.20.4-Forge         | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 2                         |
| 1.20.4-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.20.4-Neoforge      | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.20.6-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.20.6-Neoforge      | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.21-Fabric          | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.21-Neoforge        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.21.1-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.21.1-Neoforge      | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 1                         |
| 1.21.3-Fabric        | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 2                         |
| 1.21.3-Neoforge      | 1     | 1                   | 1                    | 1                              | 1                             | 1                   | 2                         |