package net.UltraLow.Textures;

/**
 * Created with IntelliJ IDEA.
 * User: James
 * Date: 3/2/13
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.MipMap;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureStore {

    private static Map<String, Texture> textureMap = new HashMap<String, Texture>();
    public static Texture dirtTop = getTexture("/res/dirt.png");
    public static Texture dirtSide = getTexture("/res/dirt.png");
    public static Texture dirtBottom = getTexture("/res/dirt.png");
    public static Texture grassTop = getTexture("/res/grass.png");
    public static Texture grassSide = getTexture("/res/grassSide.png");
    public static Texture grassBottom = getTexture("/res/dirt.png");
    public static Texture trunkTop = getTexture("/res/logTop.png");
    public static Texture trunkSide = getTexture("/res/log.png");
    public static Texture trunkBottom = getTexture("/res/logTop.png");

    public static Texture getTexture(String path) {
        // Return the texture if it already exists in the map
        if (textureMap.containsKey(path)) {
            return textureMap.get(path);
        } else {
            try {
                Texture tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));

                createMipmaps(tex);
                textureMap.put(path, tex);
                return tex;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /* Function which generates mipmaps. Found on the internet. */
    public static void createMipmaps(Texture tex) {
        tex.bind();

        int width = (int) tex.getImageWidth();
        int height = (int) tex.getImageHeight();

        byte[] texbytes = tex.getTextureData();
        int components = texbytes.length / (width * height);

        ByteBuffer texdata = ByteBuffer.allocateDirect(texbytes.length);
        texdata.put(texbytes);
        texdata.rewind();

        MipMap.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, components, width, height, components == 3 ? GL11.GL_RGB : GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texdata);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 8);
    }
}
