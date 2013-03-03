package net.UltraLow.Objects;

import net.UltraLow.Textures.TextureStore;
import net.UltraLow.Vectors.Vector3;
import net.UltraLow.Vectors.Vector3f;
import net.UltraLow.World.Chunk;
import net.UltraLow.World.Terrain;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Obstacle {

    protected TextureStore textureStore;
    public static Cube[][][] obstacleArray;
    protected int xLength, yLength, zLength;

    public Obstacle() {

    }

    public Obstacle(TextureStore textureStore) {
        this.textureStore = textureStore;
    }

    public boolean placeObstacle(Vector3 position, boolean spaceMustBeEmpty) {
        // Make sure it fits the terrain bounds
        if (position.x >= 0 && position.x + xLength < Chunk.size && position.y >= 0 && position.y + yLength < Chunk.size && position.z >= 0 && position.z + zLength < Chunk.size) {

            if (spaceMustBeEmpty) {
                // Make sure the space where it should be put is empty
                for (int x = 0; x < xLength; x++) {
                    for (int y = 0; y < yLength; y++) {
                        for (int z = 0; z < zLength; z++) {
                            if (obstacleArray[x][y][z] != null && Terrain.terrain[position.x + x][position.y + y][position.z + z] != null) {
                                return false;
                            }
                        }
                    }
                }
            }

            // Place the obstacle
            for (int x = 0; x < xLength; x++) {
                for (int y = 0; y < yLength; y++) {
                    for (int z = 0; z < zLength; z++) {
                        if (obstacleArray[x][y][z] != null) {
                            Terrain.terrain[position.x + x][position.y + y][position.z + z] = obstacleArray[x][y][z];
                            Terrain.terrain[position.x + x][position.y + y][position.z + z].pos1 = new Vector3f((position.x + x) * Terrain.cubeSize.x, (position.y + y) * Terrain.cubeSize.y, (position.z + z) * Terrain.cubeSize.z);
                            Terrain.terrain[position.x + x][position.y + y][position.z + z].pos2 = Vector3f.add(Terrain.terrain[position.x + x][position.y + y][position.z + z].pos1, Terrain.cubeSize);
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }
}
