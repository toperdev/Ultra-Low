package net.UltraLow.Objects;

import net.UltraLow.Textures.TextureStore;
import net.UltraLow.Vectors.Vector4f;
import org.newdawn.slick.opengl.Texture;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpruceObstacle extends Obstacle {

    private Texture trunk, leaf;

    public SpruceObstacle(TextureStore textureStore) {
        super(textureStore);
        trunk = textureStore.getTexture("res/log.png");
        // Color<>
        leaf = textureStore.getTexture("res/leaf.png");
        // Color^^
    }

    public void createSpruce(boolean textures) {

        // Defines how the spruce tree will look
        int stemHeight = 8;
        int stemThickness = 1;
        int crownStartY = 5; // Counting from 0
        int crownHeightPerLevel = 1;

        // Calculate the obstacle array size
        xLength = (int) Math.ceil((float) (stemHeight - crownStartY) / (float) crownHeightPerLevel) * 2 + stemThickness;
        zLength = (int) Math.ceil((float) (stemHeight - crownStartY) / (float) crownHeightPerLevel) * 2 + stemThickness;
        yLength = stemHeight + crownHeightPerLevel;

        // Create the array
        obstacleArray = new Cube[xLength][yLength][zLength];

        // Create the green crown/leaves
        for (int y = stemHeight; y < stemHeight + crownHeightPerLevel; y++) {
            for (int x = 0; x < stemThickness; x++) {
                for (int z = 0; z < stemThickness; z++) {
                    obstacleArray[(xLength - stemThickness) / 2 + x][y][(zLength - stemThickness) / 2 + z] = new Cube(null, null, new Vector4f(0.15f, 0.20f, 0.04f, 1.0f), leaf, leaf, leaf, 7);
                }
            }
        }

        for (int y = stemHeight - 1; y >= crownStartY; y--) {
            int rectSize = (int) Math.ceil((float) (stemHeight - y) / (float) crownHeightPerLevel) * 2 + stemThickness;

            for (int x = 0; x < rectSize; x++) {
                for (int z = 0; z < rectSize; z++) {
                    obstacleArray[(xLength - rectSize) / 2 + x][y][(zLength - rectSize) / 2 + z] = new Cube(null, null, new Vector4f(0.15f, 0.20f, 0.04f, 1.0f), leaf, leaf, leaf, 7);
                }
            }

        }

        // Create the stem
        for (int y = 0; y < stemHeight; y++) {
            for (int x = 0; x < stemThickness; x++) {
                for (int z = 0; z < stemThickness; z++) {
                    obstacleArray[(xLength - stemThickness) / 2 + x][y][(zLength - stemThickness) / 2 + z] = new Cube(null, null, new Vector4f(0.15f, 0.125f, 0.0f, 1.0f), trunk, trunk, trunk, 6);
                }
            }

        }
    }

}
