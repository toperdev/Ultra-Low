package net.UltraLow.World;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */

import net.UltraLow.Objects.Cube;
import net.UltraLow.Objects.SpruceObstacle;
import net.UltraLow.Objects.TreeObstacle;
import net.UltraLow.Textures.TextureStore;
import net.UltraLow.Vectors.Vector3;
import net.UltraLow.Vectors.Vector3f;
import net.UltraLow.Vectors.Vector4f;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain {

    // Size of the terrain measured in cubes
    public static Vector3 arraySize;
    // Size of each cube
    public static Vector3f cubeSize, arrayPosition;
    // Optional translation
    public static Vector3f translation;
    // The 3d array containing the cubes
    public static Cube[][][] terrain;
    public static List<Light> lights = new ArrayList<Light>();
    public static SpruceObstacle spruceGen;
    public static TreeObstacle treeGen;
    public static int size;
    public static int heightData[][];
    // Textures
    protected static Texture stoneTexture, grassTexture, waterTexture, dirtTexture;
    // Display list
    static int displayList;
    private static TextureStore textureStore;

    public Terrain(Vector3 arraySize, Vector3f cubeSize, Vector3f translation, TextureStore textureStore) {
        this.arraySize = arraySize;
        this.cubeSize = cubeSize;
        this.translation = translation;
        this.textureStore = textureStore;

        // Create the cube array

        stoneTexture = textureStore.getTexture("res/stone.png");
        waterTexture = textureStore.getTexture("res/water.png");
    }

    public static void createArray(int size) {
        terrain = new Cube[size * 16][size * 16][size * 16];
    }

    public static void generateTerrain(int SIZE, int maxHeight, int minHeight, int smoothLevel, int seed, float noiseSize, float persistence, int octaves, boolean textures) {
        // Stores the height of each x, z coordinate

        heightData = new int[SIZE][SIZE];
        size = SIZE;
        // Randomize the heights using Perlin noise
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                heightData[x][z] = (int) (PerlinNoise2D.perlin2D(x, z, SIZE, SIZE, seed, noiseSize, persistence, octaves) * (maxHeight - minHeight) + minHeight);
            }
        }

        // Smoothen the terrain
        while (smoothLevel > 0) {
            for (int z = 0; z < SIZE; z += 1) {
                for (int x = 0; x < SIZE; x += 1) {
                    float totalHeight = 0.0f;
                    float count = 0;

                    if (z > 0) {
                        totalHeight += heightData[x][z - 1];
                        count++;
                    }

                    if (z < SIZE - 1) {
                        totalHeight += heightData[x][z + 1];
                        count++;
                    }

                    if (x > 0) {
                        totalHeight += heightData[x - 1][z];
                        count++;
                    }

                    if (x < SIZE - 1) {
                        totalHeight += heightData[x + 1][z];
                        count++;
                    }

                    heightData[x][z] = Math.round(totalHeight / count);
                }
            }

            smoothLevel--;
        }

        // Create the cubes
        for (int z = 0; z < SIZE; z++) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = heightData[x][z]; y >= 0; y--) {
                    terrain[x][y][z] = createCube(new Vector3(x, y, z), textures);
                }
            }
        }

        postGenerate();
        Random rand = new Random();

        // Create tree obstacles
        treeGen = new TreeObstacle(textureStore);
        int treeCount = size / 10;

        for (int treeIndex = 0; treeIndex < treeCount; treeIndex++) {
            // Select a random position on the terrain
            int x = rand.nextInt(size);
            int z = rand.nextInt(size);
            int y = heightData[x][z];

            // Create the tree

            treeGen.createTree();
            treeGen.placeObstacle(new Vector3(x, y, z), false);
        }

        // Create spruce obstacles
        spruceGen = new SpruceObstacle(textureStore);
        int spruceCount = size / 10;
        for (int spruceIndex = 0; spruceIndex < spruceCount; spruceIndex++) {
            // Select a random position on the terrain
            int x = rand.nextInt(SIZE);
            int z = rand.nextInt(SIZE);
            int y = heightData[x][z];
            // Create the spruce
            spruceGen.createSpruce(textures);
            spruceGen.placeObstacle(new Vector3(x, y, z), false);
        }
    }

    private static void postGenerate() {
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (terrain[x][y][z] != null) {
                        if (terrain[x][y + 1][z] != null && terrain[x][y + 1][z].blockID == 2) {
                            // Dirt
                            Vector4f color = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);
                            Texture textureTop = TextureStore.dirtTop;
                            Texture textureBottom = TextureStore.dirtBottom;
                            Texture textureSide = TextureStore.dirtSide;

                            Vector3f pos1 = new Vector3f(x * cubeSize.x, y * cubeSize.y, z * cubeSize.z);
                            Vector3f pos2 = Vector3f.add(pos1, cubeSize);

                            terrain[x][y][z] = new Cube(pos1, pos2, color, textureTop, textureBottom, textureSide, 3);
                            if (terrain[x][y - 1][z] != null && terrain[x][y - 1][z].blockID != 1) {
                                terrain[x][y - 1][z] = new Cube(pos1, pos2, color, textureTop, textureBottom, textureSide, 3);
                            }
                            if (terrain[x][y - 2][z] != null && terrain[x][y - 2][z].blockID != 1) {
                                terrain[x][y - 2][z] = new Cube(pos1, pos2, color, textureTop, textureBottom, textureSide, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void update() {
        lights.add(new Light(new Vector3f(25, 10, 25), 5.0f));
        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = heightData[x][z]; y >= 0; y--) {
                    boolean renderTop = (y == heightData[x][z]) || (y == 0);
                    boolean renderBottom = (y == 0) || (y == 3);
                    boolean renderFront = (z == size - 1) || (terrain[x][y][z + 1] == null);
                    boolean renderBack = (z == 0) || (terrain[x][y][z - 1] == null);
                    boolean renderRight = (x == size - 1) || (terrain[x + 1][y][z] == null);
                    boolean renderLeft = (x == 0) || (terrain[x - 1][y][z] == null);

                    if (terrain[x][y][z] != null) {
                        terrain[x][y][z].setVisibleSides(renderTop, renderBottom, renderFront, renderBack, renderRight, renderLeft);
                    }
                }
            }
        }
        displayList = GL11.glGenLists(4);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        GL11.glDisable(GL11.GL_LIGHTING);

        for (int z = 0; z < size; z++) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (terrain[x][y][z] != null) {
                        terrain[x][y][z].render();
                    }
                }
            }
        }

        GL11.glEndList();
    }

    private static Cube createCube(Vector3 arrayPosition, boolean textures) {
        // Calculate the coordinates
        Vector3f pos1 = new Vector3f(arrayPosition.x * cubeSize.x, arrayPosition.y * cubeSize.y, arrayPosition.z * cubeSize.z);
        Vector3f pos2 = Vector3f.add(pos1, cubeSize);

        // Set texture depending on y
        Vector4f color = null;
        Texture textureTop = null;
        Texture textureBottom = null;
        Texture textureSide = null;

        if (arrayPosition.y > 2) {
            // Grass
            color = new Vector4f(0.4f, 0.4f, 0.4f, 1.0f);
            textureTop = TextureStore.grassTop;
            textureBottom = TextureStore.grassBottom;
            textureSide = TextureStore.grassSide;
            return new Cube(pos1, pos2, color, textureTop, textureBottom, textureSide, 2);
        }
        if (arrayPosition.y == 0) {
            // Water
            color = new Vector4f(0.0f, 0.2f, 0.7f, 0.6f);
            textureTop = waterTexture;
            textureBottom = waterTexture;
            textureSide = waterTexture;
            return new Cube(pos1, pos2, color, textureTop, textureBottom, textureSide, 4);
        }
        // Stone
        color = new Vector4f(0.3f, 0.3f, 0.3f, 1.0f);
        textureTop = stoneTexture;
        textureBottom = stoneTexture;
        textureSide = stoneTexture;
        return new Cube(pos1, pos2, color, textureTop, textureBottom, textureSide, 1);
    }

    /* Returns true if there is a solid cube at the given coordinates. */
    public static boolean solidAt(Vector3f coordinates) {
        // Get the cube coordinates in the array
        Vector3 arrayCoordinates = new Vector3((int) ((coordinates.x - translation.x) / cubeSize.x), (int) ((coordinates.y - translation.y) / cubeSize.y), (int) ((coordinates.z - translation.z) / cubeSize.z));

        // Is this within the array bounds?
        if (arrayCoordinates.x >= 0 && arrayCoordinates.x < Chunk.size && arrayCoordinates.y >= 0 && arrayCoordinates.y < Chunk.size && arrayCoordinates.z >= 0 && arrayCoordinates.z < Chunk.size) {
            // Is there a cube at this coordinate?
            if (terrain[arrayCoordinates.x][arrayCoordinates.y][arrayCoordinates.z] != null) {
                return true;
            }
        }

        return false;
    }

    public static void render() {
        // Save the current matrix
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        // Add the translation matrix
        GL11.glTranslatef(translation.x, translation.y, translation.z);

        // Call the display list

        GL11.glCallList(displayList);

        for (int i = 0; i < lights.size(); i++) {
            if (lights.get(i) != null) {
                lights.get(i).render();
            }
        }

        // Restore the matrix
        GL11.glPopMatrix();

    }

    public void release() {
        GL11.glDeleteLists(displayList, 1);
    }
}