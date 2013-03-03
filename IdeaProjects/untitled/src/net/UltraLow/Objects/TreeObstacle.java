package net.UltraLow.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */

import net.UltraLow.Textures.TextureStore;
import net.UltraLow.Vectors.Vector4f;
import org.newdawn.slick.opengl.Texture;

public class TreeObstacle extends Obstacle {

    private Texture trunk, leaf;

    public TreeObstacle(TextureStore textureStore) {
        super(textureStore);
        trunk = textureStore.getTexture("res/log.png");
        // Color<>
        leaf = textureStore.getTexture("res/leaf.png");
        // Color^^
    }

    public void createTree() {
        // Specify size
        xLength = 5;
        zLength = 5;
        yLength = 10;

        // Create array
        obstacleArray = new Cube[xLength][yLength][zLength];

        for (int x = 0; x < xLength; x++) {
            for (int y = 0; y < yLength; y++) {
                for (int z = 0; z < zLength; z++) {
                    obstacleArray[x][y][z] = null;
                }
            }
        }
        // Create tree crown
        for (int x = 0; x < xLength; x++) {
            for (int y = yLength / 2; y < yLength - 1; y++) {
                for (int z = 0; z < zLength; z++) {
                    obstacleArray[x][y][z] = new Cube(null, null, new Vector4f(0.0f, 0.25f, 0.06f, 1.0f), leaf, leaf, leaf, 5);
                }
            }
        }

        for (int x = 1; x < xLength - 1; x++) {
            for (int y = yLength - 1; y < yLength; y++) {
                for (int z = 1; z < zLength - 1; z++) {
                    obstacleArray[x][y][z] = new Cube(null, null, new Vector4f(0.0f, 0.25f, 0.06f, 1.0f), leaf, leaf, leaf, 5);
                }
            }
        }

        for (int x = 1; x < xLength - 1; x++) {
            for (int y = yLength / 2; y < yLength / 2 + 1; y++) {
                for (int z = 1; z < zLength - 1; z++) {
                    obstacleArray[x][y][z] = null;
                }
            }
        }

        // Create stem
        for (int y = 0; y < yLength - 1; y++) {
            obstacleArray[xLength / 2][y][zLength / 2] = new Cube(null, null, new Vector4f(0.25f, 0.125f, 0.0f, 1.0f), TextureStore.trunkTop, TextureStore.trunkBottom, TextureStore.trunkSide, 6);
        }

    }

}

