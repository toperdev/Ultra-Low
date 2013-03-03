package net.UltraLow.Vectors;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Vector3 {
    public int x, y, z;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void multiply(Vector3 a) {
        x *= a.x;
        y *= a.y;
        z *= a.z;
    }

    public void add(Vector3 a) {
        x += a.x;
        y += a.y;
        z += a.z;
    }

    public static Vector3 multiply(Vector3 a, Vector3 b) {
        return new Vector3(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Vector3 add(Vector3 a, Vector3 b) {
        return new Vector3(a.x + b.x, a.y + b.y, a.z + b.z);
    }
}
