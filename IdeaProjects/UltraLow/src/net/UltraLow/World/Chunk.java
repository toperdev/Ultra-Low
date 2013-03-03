package net.UltraLow.World;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */

import net.UltraLow.Main.Player;
import net.UltraLow.Main.UltraLow;
import net.UltraLow.Vectors.Vector3;
import net.UltraLow.Vectors.Vector3f;

public class Chunk {

    static Terrain chunks[][][];
    public static float size;

    public void generateTerrain(int size) {
        int SIZE = size * 16;
        this.size = SIZE;
        Terrain.createArray(size);
        chunks = new Terrain[SIZE][SIZE][SIZE];

        for (int y = 0; y < 16; y += 16) {
            for (int x = 0; x < SIZE; x += 16) {
                for (int z = 0; z < SIZE; z += 16) {
                    chunks[x][y][z] = new Terrain(new Vector3(16, 16, 16), new Vector3f(1f, 1.0f, 1.0f), new Vector3f(x, y, z), UltraLow.store);
                }
            }
        }
        Terrain.generateTerrain(SIZE, UltraLow.TERRAIN_MAX_HEIGHT, UltraLow.TERRAIN_MIN_HEIGHT, UltraLow.TERRAIN_SMOOTH_LEVEL, UltraLow.TERRAIN_GEN_SEED, UltraLow.TERRAIN_GEN_NOISE_SIZE, UltraLow.TERRAIN_GEN_PERSISTENCE, (int) UltraLow.TERRAIN_GEN_OCTAVES, UltraLow.TEXTURES);
        UltraLow.camera = new Player(new Vector3f(size * 15, Terrain.heightData[size][size] + 1, size*15), new Vector3f(0.0f, 0.0f, 0.0f));
        Terrain.update();
    }

    public void render() {
        chunks[0][0][0].render();
    }

    public static boolean collide(Vector3f coords) {
        boolean colliding = false;
        for (int y = 0; y < 16; y += 16) {
            for (int x = 0; x < size; x += 16) {
                for (int z = 0; z < size; z += 16) {
                    colliding = Terrain.solidAt(coords);
                }
            }
        }
        return colliding;
    }
}