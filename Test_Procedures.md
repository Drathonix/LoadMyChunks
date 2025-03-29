Because this is an MC mod, tests need to be done manually.

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

# 1.2.0 Test Table

| Version \| Procedure | Boots | Placed Chunk Loader | Removed Chunk Loader | Placed & Extended Chunk Loader | Removed Extended Chunk Loader | Chunk Loader Item Consumption | Test forceload command | CCT: Repeat tasks 2,3 & 5 |
|----------------------|-------|---------------------|----------------------|--------------------------------|-------------------------------|-------------------------------|------------------------|---------------------------|
| 1.16.5-Forge         |       |                     |                      |                                |                               |                               |                        |                           |
| 1.16.5-Fabric        |       |                     |                      |                                |                               |                               |                        | 2                         |
| 1.18.2-Forge         |       |                     |                      |                                |                               |                               |                        |                           |
| 1.18.2-Fabric        |       |                     |                      |                                |                               |                               |                        | 2                         |
| 1.19.2-Forge         |       |                     |                      |                                |                               |                               |                        |                           |
| 1.19.2-Fabric        |       |                     |                      |                                |                               |                               |                        | 2                         |
| 1.19.4-Forge         |       |                     |                      |                                |                               |                               |                        |                           |
| 1.19.4-Fabric        |       |                     |                      |                                |                               |                               |                        |                           |
| 1.20.1-Forge         |       |                     |                      |                                |                               |                               |                        |                           |
| 1.20.1-Fabric        |       |                     |                      |                                |                               |                               |                        |                           |
| 1.20.4-Forge         |       |                     |                      |                                |                               |                               |                        | 2                         |
| 1.20.4-Fabric        |       |                     |                      |                                |                               |                               |                        |                           |
| 1.20.4-Neoforge      |       |                     |                      |                                |                               |                               |                        |                           |
| 1.20.6-Fabric        |       |                     |                      |                                |                               |                               |                        |                           |
| 1.20.6-Neoforge      |       |                     |                      |                                |                               |                               |                        |                           |
| 1.21-Fabric          |       |                     |                      |                                |                               |                               |                        |                           |
| 1.21-Neoforge        |       |                     |                      |                                |                               |                               |                        |                           |
| 1.21.1-Fabric        |       |                     |                      |                                |                               |                               |                        |                           |
| 1.21.1-Neoforge      |       |                     |                      |                                |                               |                               |                        |                           |