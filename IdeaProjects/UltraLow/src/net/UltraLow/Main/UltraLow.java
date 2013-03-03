package net.UltraLow.Main;

import net.UltraLow.Profiling.Profiling;
import net.UltraLow.Profiling.ProfilingPart;
import net.UltraLow.Textures.TextureStore;
import net.UltraLow.Vectors.Vector3f;
import net.UltraLow.World.Chunk;
import net.UltraLow.World.Terrain;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.util.Random;

public class UltraLow {

    private static Random r = new Random();

    public static final boolean TEXTURES = false;
    public static final int TERRAIN_MAX_HEIGHT = 24;
    public static final int TERRAIN_MIN_HEIGHT = -64;
    public static final int TERRAIN_SMOOTH_LEVEL = 5;
    public static final int TERRAIN_GEN_SEED = r.nextInt(99999);
    public static final float TERRAIN_GEN_NOISE_SIZE = 3.0f;
    public static final float TERRAIN_GEN_PERSISTENCE = 0.35f;
    public static final float TERRAIN_GEN_OCTAVES = 1f;
    private static final float MOUSE_SPEED_SCALE = 0.125f;
    private static final float MOVEMENT_SPEED = 6.0f;
    private static final float MOVEMENT_SPEED_FLYMODE = 15.0f;
    private static final float FALSE_GRAVITY_SPEED = 5.0f;
    private static final boolean FULLSCREEN = false;
    private static final boolean VSYNC = false;

    public static float fov = 75.0f;
    public static float aspect;
    public static float zNear = 0.1f;
    public static float zFar = 35.0f;

    // UltraLow components9
    public static Player camera;
    public static TextureStore store;
    public static float deltaTime;
    static Terrain terrain;
    static Chunk chunk = new Chunk();
    private static int fogMode;
    private final FloatBuffer position = BufferUtils.createFloatBuffer(4);
    // Toggles
    private boolean flyMode = true;
    private boolean doCollisionChecking = true;
    private boolean renderSkybox = false;
    private boolean wireframe = false;
    // Profiling
    private Profiling profiling = new Profiling();
    private ProfilingPart displayUpdate = new ProfilingPart("DISPLAY UPDATE");
    private ProfilingPart inputHandling = new ProfilingPart("INPUT HANDLING");

    public static void main(String[] args) {
        UltraLow cubeUltraLow = new UltraLow();
        cubeUltraLow.start();
    }

    public static float getDelta() {
        return deltaTime;
    }

    public static void setUpGL() {

        // Initialize OpenGL
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(fov, aspect, zNear, zFar);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Set OpenGL options
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

    }

    public void start() {
        int width = 1280;
        int height = width / 16 * 9;

        // Create the display
        try {
            Display.setDisplayMode(new DisplayMode(width, width / 16 * 9));
            Display.setFullscreen(FULLSCREEN);
            Display.setVSyncEnabled(VSYNC);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            // System.exit(0);
        }
        setUpGL();
        aspect = (float) Display.getWidth() / (float) Display.getHeight();
        // Hide the mouse
        Mouse.setGrabbed(true);

        // Create the terrain

        chunk.generateTerrain(4);
        // Load the OBJ model
        store = new TextureStore();
        // Main loop
        long lastFrame = System.currentTimeMillis();

        while (!Display.isCloseRequested()) {
            // Calculate delta time
            long t = System.currentTimeMillis();
            deltaTime = (t - lastFrame) * 0.001f;

            profiling.frameBegin();

            // Clear the screen
            GL11.glClearColor(0.25f, 0.8f, 1.0f, 1.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glLoadIdentity();

            if (wireframe)
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            else
                GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

            // Apply the camera matrix
            camera.applyMatrix();

            // Render the terrain
            chunk.render();
            camera.init2D();
            camera.renderCrosshair();
            camera.initProjection();
            // Updates the display, also polls the mouse and keyboard
            profiling.partBegin(displayUpdate);
            Display.update();
            profiling.partEnd(displayUpdate);

            // Handle mouse movement
            camera.addRotation(new Vector3f(Mouse.getDY() * MOUSE_SPEED_SCALE, -Mouse.getDX() * MOUSE_SPEED_SCALE, 0.0f));

            float movementSpeed = flyMode ? MOVEMENT_SPEED_FLYMODE : MOVEMENT_SPEED;

            profiling.partBegin(inputHandling);
            // Handle keypresses

            if (!flyMode) {
                camera.fall();
                camera.jump();
            } else {
                camera.jumping = false;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                break;
            if (Keyboard.isKeyDown(Keyboard.KEY_W))
                camera.move(movementSpeed * deltaTime, Player.FORWARD, 0, doCollisionChecking, flyMode);
            if (Keyboard.isKeyDown(Keyboard.KEY_S))
                camera.move(movementSpeed * deltaTime, Player.BACKWARD, 0, doCollisionChecking, flyMode);
            if (Keyboard.isKeyDown(Keyboard.KEY_A))
                camera.move(movementSpeed * deltaTime, Player.LEFT, 0, doCollisionChecking, flyMode);
            if (Keyboard.isKeyDown(Keyboard.KEY_D))
                camera.move(movementSpeed * deltaTime, Player.RIGHT, 0, doCollisionChecking, flyMode);
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                if (!flyMode) {
                    camera.jumping = true;
                } else {
                    camera.move(movementSpeed * deltaTime, Player.UP, 0, doCollisionChecking, flyMode);
                }
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                if (flyMode)
                    camera.move(movementSpeed * deltaTime, Player.DOWN, 0, doCollisionChecking, flyMode);
            // Check for pressed keys
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                        flyMode = !flyMode;
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_C) {
                        doCollisionChecking = !doCollisionChecking;
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_B) {
                        renderSkybox = !renderSkybox;
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                        wireframe = !wireframe;
                    }
                }
            }
            profiling.partEnd(inputHandling);

            // Apply gravity
            if (!flyMode)
                camera.move(0, Player.FORWARD, deltaTime * FALSE_GRAVITY_SPEED, doCollisionChecking, flyMode);

            // Update the coordinates and velocity of the moving cube

            profiling.frameEnd();

            // Set title to debug info
            Display.setTitle(" BlockWorld - x: " + (int) camera.coordinates.x + " y: " + (int) camera.coordinates.y + " z: " + (int) camera.coordinates.z + " xRot: " + (int) camera.rotation.x + " yRot: " + (int) camera.rotation.y + " zRot: " + camera.rotation.z + " FPS: " + Math.round(profiling.fps()));

            lastFrame = t;
        }

        // Cleanup
        chunk = null;
        Display.destroy();
    }
}


