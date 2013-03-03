package net.UltraLow.World;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 8:51 PM
 * To change this template use File | Settings | File Templates.
 */

import net.UltraLow.Objects.Cube;
import net.UltraLow.Vectors.Vector3f;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Light {

    private Color c;
    private int displayList;
    private Vector3f cubeSize = new Vector3f(1.002f, 1.002f, 1.002f);
    private Vector3f pos;
    float diss = 0.06f;
    float n;

    Cube[][][] cubes;

    protected boolean renderTop = false, renderBottom = false, renderFront = false, renderBack = false, renderRight = false, renderLeft = false;
    private int brightness;

    public Light(Vector3f pos, float brightness) {
        this.pos = pos;
        n = brightness * 8;

        cubes = new Cube[(int) (pos.x + n)][(int) (pos.y + n)][(int) (pos.z + n)];
        generate();
        displayList = GL11.glGenLists(1);
        GL11.glNewList(displayList, GL11.GL_COMPILE);
        for (int z = 0; z < (int) pos.z + brightness; z++) {
            for (int x = 0; x < (int) pos.x + brightness; x++) {
                for (int y = 0; y < (int) pos.y + brightness; y++) {
                    if (cubes[x][y][z] != null)
                        cubes[x][y][z].render();
                }
            }
        }
        GL11.glEndList();

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /* Renders the cube. */
    public void render() {
        generate();
        for (int z = 0; z < (int) pos.z + brightness; z++) {
            for (int x = 0; x < (int) pos.x + brightness; x++) {
                for (int y = 0; y < (int) pos.y + brightness; y++) {
                    if (cubes[x][y][z] != null)
                        cubes[x][y][z].render();
                }
            }
        }
        GL11.glEnable(GL11.GL_FOG);
    }

    private void generate() {
        float brightX = 0f, brightZ = 0f, brightY = 0f;
        for (int x = (int) (pos.x - n / 2); x < pos.x + n / 2; x++) {
            brightZ = 0f;
            if (x < pos.x - n / 4) {
                brightX += diss;
            } else {
                brightX -= diss * 2;
            }
            for (int z = (int) (pos.z - n / 2); z < pos.z + n / 2; z++) {
                if (z < pos.z - n / 4) {
                    brightZ += diss;
                } else {
                    brightZ -= diss * 2;
                }
                if (x > -1 && z > -1 && x < Terrain.size && z < Terrain.size) {
                    for (int y = (int) (pos.y - n / 2); y < pos.y + n / 2; y++) {
                        brightY = 0f;
                        if (y < pos.y - n / 4) {
                            brightY += diss;
                        } else {
                            brightY -= diss * 2;
                        }
                        if (y > -1 && y < Terrain.size && y > pos.y - n / 2 && y < pos.y + n / 2) {
                            if (y == Terrain.heightData[x][z] || y < Terrain.heightData[x][z]) {
                                c = new Color(1.0f, 1.0f, 1.0f, (brightX + brightZ + brightY) * 8);
                                Vector3f pos1 = new Vector3f(x - 0.001f, y - 0.001f, z - 0.001f);
                                Vector3f pos2 = Vector3f.add(pos1, cubeSize);
                                cubes[x][y][z] = makeBlock(pos1, pos2, Terrain.terrain[x][y][z].textureTop, Terrain.terrain[x][y][z].textureBottom, Terrain.terrain[x][y][z].textureSide);
                                if (Terrain.terrain[x][y][z].renderTop) {
                                    renderTop = true;
                                }
                                if ((x < pos.x + n / 4) && Terrain.terrain[x][y][z].renderLeft) {
                                    renderLeft = true;
                                }
                                if (Terrain.terrain[x][y][z].renderBottom) {
                                    renderBottom = true;
                                }
                                if ((x > pos.x - n / 2) && Terrain.terrain[x][y][z].renderRight) {
                                    renderRight = true;
                                }
                                if (Terrain.terrain[x][y][z].renderFront) {
                                    renderFront = true;
                                }
                                if (Terrain.terrain[x][y][z].renderBack) {
                                    renderBack = true;
                                }
                                cubes[x][y][z].setVisibleSides(renderTop, renderBottom, renderFront, renderBack, renderRight, renderLeft);
                            }
                        }
                    }
                }
            }
        }
    }

    private Cube makeBlock(Vector3f pos1, Vector3f pos2, Texture top, Texture bottom, Texture side) {
        return new Cube(pos1, pos2, c, top, bottom, side, 64);
    }
}
