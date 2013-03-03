package net.UltraLow.Main;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 *
 * **/

import net.UltraLow.Vectors.Vector3f;
import net.UltraLow.World.Chunk;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.ByteBuffer;

public class Player {

    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;
    public static final int UP = 4;
    public static final int DOWN = 5;
    public static float lookAtX, lookAtZ;
    public Vector3f coordinates;
    public Vector3f rotation;
    float speed = 0.35f;
    float gravity = 0.45f;
    float ogJumpGravity = 1f;
    float jumpGravity = ogJumpGravity;
    boolean jumping = false;
    boolean falling = false;
    float fallGravity = 0;

    public Player(Vector3f coordinates, Vector3f rotation) {
        this.coordinates = coordinates;
        this.rotation = rotation;
    }

    public void jump() {
        if (!collision(coordinates.x, coordinates.y, coordinates.z)) {
            if (jumping) {
                Vector3f newCoordinates = new Vector3f(coordinates.x, coordinates.y, coordinates.z);
                newCoordinates.y += jumpGravity * UltraLow.getDelta() * 10f;
                jumpGravity -= speed * UltraLow.getDelta() * 13f;
                if (!collision(coordinates.x, newCoordinates.y, coordinates.z)) {
                    coordinates.y = newCoordinates.y;
                } else {
                    jumping = false;
                    jumpGravity = ogJumpGravity;
                }
            }
        } else {
            jumping = false;
            jumpGravity = ogJumpGravity;
            falling = true;
        }
    }

    public void fall() {
        if (falling && !jumping) {
            Vector3f newCoordinates = new Vector3f(coordinates.x, coordinates.y, coordinates.z);
            newCoordinates.y += fallGravity * UltraLow.getDelta() * 10f;
            if (fallGravity > -0.75f)
                fallGravity -= speed * UltraLow.getDelta() * 20f;
            if (!collision(coordinates.x, newCoordinates.y, coordinates.z)) {
                coordinates.y = newCoordinates.y;
            } else {
                falling = false;
                fallGravity = 0;
            }
        }
    }

    public void initProjection() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(UltraLow.fov, UltraLow.aspect, UltraLow.zNear, UltraLow.zFar);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void init2D() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public void renderCrosshair() {
        ByteBuffer rgb = BufferUtils.createByteBuffer(3);
        GL11.glReadPixels(Display.getWidth() / 2,Display.getHeight() / 2, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);

        GL11.glLoadIdentity();
        GL11.glPushMatrix();
        GL11.glLineWidth(2);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(255-(rgb.get(0) & 0xff), 255-(rgb.get(1) & 0xff), 255-(rgb.get(2) & 0xff));
        GL11.glVertex2f(Display.getWidth() / 2 - 15, Display.getHeight() / 2);
        GL11.glVertex2f(Display.getWidth() / 2 + 15, Display.getHeight() / 2);
        GL11.glVertex2f(Display.getWidth() / 2, Display.getHeight() / 2 - 15);
        GL11.glVertex2f(Display.getWidth() / 2, Display.getHeight() / 2 + 15);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public void move(float delta, int direction, float gravityDelta, boolean collisionChecking, boolean flyMode) {

        Vector3f newCoordinates = new Vector3f(coordinates.x, coordinates.y, coordinates.z);

        if (direction == FORWARD) {
            if (flyMode) {
                // Includes moving in the Y-direction
                newCoordinates.x += Math.cos(Math.toRadians(rotation.x)) * -Math.sin(Math.toRadians(rotation.y)) * delta;
                newCoordinates.y += Math.sin(Math.toRadians(rotation.x)) * delta;
                newCoordinates.z += Math.cos(Math.toRadians(rotation.x)) * -Math.cos(Math.toRadians(rotation.y)) * delta;
            } else {
                // No moving in the Y-direction. (2D version, use if flying is
                // forbidden)
                newCoordinates.x += -Math.sin(Math.toRadians(rotation.y)) * delta;
                newCoordinates.z += -Math.cos(Math.toRadians(rotation.y)) * delta;
            }
        } else if (direction == BACKWARD) {
            if (flyMode) {
                // Includes moving in the Y-direction
                newCoordinates.x -= Math.cos(Math.toRadians(rotation.x)) * -Math.sin(Math.toRadians(rotation.y)) * delta;
                newCoordinates.y -= Math.sin(Math.toRadians(rotation.x)) * delta;
                newCoordinates.z -= Math.cos(Math.toRadians(rotation.x)) * -Math.cos(Math.toRadians(rotation.y)) * delta;
            } else {
                // No moving in the Y-direction. (2D version, use if flying is
                // forbidden)
                newCoordinates.x -= -Math.sin(Math.toRadians(rotation.y)) * delta;
                newCoordinates.z -= -Math.cos(Math.toRadians(rotation.y)) * delta;
            }
        } else if (direction == RIGHT) {
            // Only move in the XZ-directions
            newCoordinates.x += Math.sin(Math.toRadians(rotation.y + 90)) * delta;
            newCoordinates.z += Math.sin(Math.toRadians(-rotation.y)) * delta;
        } else if (direction == LEFT) {
            // Only move in the XZ-directions
            newCoordinates.x -= Math.sin(Math.toRadians(rotation.y + 90)) * delta;
            newCoordinates.z -= Math.sin(Math.toRadians(-rotation.y)) * delta;
        } else if (direction == UP && flyMode) {
            newCoordinates.y += 1 * delta;
        } else if (direction == DOWN && flyMode) {
            newCoordinates.y -= 1 * delta;
        }

        // Collision detection
        if (collisionChecking) {
            if (!collision(newCoordinates.x, coordinates.y, coordinates.z)) {
                coordinates.x = newCoordinates.x;
            }
            if (!collision(coordinates.x, newCoordinates.y, coordinates.z)) {
                coordinates.y = newCoordinates.y;
                if (!jumping && !flyMode) {
                    falling = true;
                }
            } else {
                falling = true;
                jumping = false;
            }
            if (!collision(coordinates.x, coordinates.y, newCoordinates.z)) {
                coordinates.z = newCoordinates.z;
            }
        } else {
            coordinates.x = newCoordinates.x;
            coordinates.y = newCoordinates.y;
            coordinates.z = newCoordinates.z;
        }
    }

    public boolean collision(float x, float y, float z) {
        // Simulate a cube cross around the point
        float cubeSize = 1.0f;

        if (bottomCollision(x, y, z, cubeSize)) {
            return true;
        }
        if (topCollision(x, y, z, cubeSize)) {
            return true;
        }
        if (rightCollision(x, y, z, cubeSize)) {
            return true;
        }
        if (leftCollision(x, y, z, cubeSize)) {
            return true;
        }
        if (frontCollision(x, y, z, cubeSize)) {
            return true;
        }
        if (backCollision(x, y, z, cubeSize)) {
            return true;
        }
        return false;
    }

    boolean topCollision(float x, float y, float z, float cubeSize) {
        Vector3f c = new Vector3f(x, y - 1.5f, z);
        if (!Chunk.collide(c)) {
            return false;
        }
        return true;
    }

    boolean bottomCollision(float x, float y, float z, float cubeSize) {
        Vector3f c = new Vector3f(x, y + cubeSize / 2, z);
        if (!Chunk.collide(c)) {
            return false;
        }
        System.out.println("bottom");
        return true;
    }

    boolean rightCollision(float x, float y, float z, float cubeSize) {
        Vector3f c = new Vector3f(x - cubeSize / 2, y, z);
        if (!Chunk.collide(c)) {
            return false;
        }

        System.out.println("right");
        return true;
    }

    boolean leftCollision(float x, float y, float z, float cubeSize) {
        Vector3f c = new Vector3f(x + cubeSize / 2, y, z);
        if (!Chunk.collide(c)) {
            return false;
        }

        System.out.println("left");
        return true;
    }

    boolean frontCollision(float x, float y, float z, float cubeSize) {
        Vector3f c = new Vector3f(x, y, z - cubeSize / 2);
        if (!Chunk.collide(c)) {
            return false;
        }

        System.out.println("front");
        return true;
    }

    boolean backCollision(float x, float y, float z, float cubeSize) {
        Vector3f c = new Vector3f(x, y, z + cubeSize / 2);
        if (!Chunk.collide(c)) {
            return false;
        }

        System.out.println("back");
        return true;
    }

    /*
    * Use this when adding rotation instead of Vector3f.add since this function
    * makes the numbers stay under 360.
    */
    public void addRotation(Vector3f rot) {
        rotation.x += rot.x;
        rotation.y += rot.y;
        rotation.z += rot.z;

        if (rotation.x >= 360.0f || rotation.x <= 0.0f)
            rotation.x = rotation.x % 360.0f;

        if (rotation.y >= 360.0f || rotation.y <= 0.0f)
            rotation.y = rotation.y % 360.0f;

        if (rotation.z >= 360.0f || rotation.z <= 0.0f)
            rotation.z = rotation.z % 360.0f;

// Gimbal lock
        if (rotation.x <= -90.0f)
            rotation.x = -90.0f;
        else if (rotation.x >= 90.0f)
            rotation.x = 90.0f;
    }

    public int roundUp(float num) {
        return (int) (Math.ceil(num / 16d) * 16);
    }

    public void applyMatrix() {
        // Rotate
        GL11.glRotatef(-rotation.x, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(-rotation.y, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-rotation.z, 0.0f, 0.0f, 1.0f);

// Translate
        GL11.glTranslatef(-coordinates.x, -coordinates.y, -coordinates.z);
    }

}