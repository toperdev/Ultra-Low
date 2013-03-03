package net.UltraLow.Objects;

import net.UltraLow.Vectors.Vector3f;
import net.UltraLow.Vectors.Vector4f;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Cube {

    /* pos1 contains the lowest x, y, z. pos2 contains the heighest x, y, z */
    public Vector3f pos1, pos2;

    // Color to use if no texture is present
    public Vector4f vColor;
    public Color color;

    // Texture class from Slick-Util library
    public Texture textureTop, textureBottom, textureSide;

    // Determines which sides to draw
    public boolean renderTop = true, renderBottom = true, renderFront = true, renderBack = true, renderRight = true, renderLeft = true;

    public int blockID;

    public Cube(Vector3f pos1, Vector3f pos2, Vector4f color, Texture top, Texture bottom, Texture side, int blockID) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.vColor = color;
        this.textureTop = top;
        this.textureSide = side;
        this.textureBottom = bottom;
        this.blockID = blockID;

    }

    public Cube(Vector3f pos1, Vector3f pos2, Color color, Texture top, Texture bottom, Texture side, int blockID) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.color = color;
        this.textureTop = top;
        this.textureSide = side;
        this.textureBottom = bottom;
        this.blockID = blockID;

    }

    public Cube(Vector3f pos1, Vector3f pos2, Color color, int blockID) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.color = color;
        this.blockID = blockID;

    }

    /* Sets information about which sides to draw. */
    public void setVisibleSides(boolean drawTop, boolean drawBottom, boolean drawFront, boolean drawBack, boolean drawRight, boolean drawLeft) {
        this.renderTop = drawTop;
        this.renderBottom = drawBottom;
        this.renderFront = drawFront;
        this.renderBack = drawBack;
        this.renderRight = drawRight;
        this.renderLeft = drawLeft;
    }

    public int getBlockID() {
        return blockID;
    }

    /* Renders the cube. */
    public void render() {
        // Set the texture
        GL11.glBegin(GL11.GL_QUADS);

        // Top
        if (renderTop) {
            if (textureTop != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureTop.getTextureID());
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
            if (color != null) {
                color.bind();
            } else if (vColor != null) {
                GL11.glColor4f(vColor.x, vColor.y, vColor.z, vColor.a);
            }
            GL11.glNormal3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(pos2.x, pos2.y, pos1.z);
            GL11.glNormal3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(pos1.x, pos2.y, pos1.z);
            GL11.glNormal3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(pos1.x, pos2.y, pos2.z);
            GL11.glNormal3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(pos2.x, pos2.y, pos2.z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        // Bottom
        if (renderBottom) {
            if (textureBottom != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureBottom.getTextureID());
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
            if (color != null) {
                color.bind();
            } else if (vColor != null) {
                GL11.glColor4f(vColor.x, vColor.y, vColor.z, vColor.a);
            }
            GL11.glNormal3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(pos2.x, pos1.y, pos2.z);
            GL11.glNormal3f(-1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(pos1.x, pos1.y, pos2.z);
            GL11.glNormal3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(pos1.x, pos1.y, pos1.z);
            GL11.glNormal3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(pos2.x, pos1.y, pos1.z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        // Front
        if (renderFront) {
            if (textureSide != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureSide.getTextureID());
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
            if (color != null) {
                color.bind();
            } else if (vColor != null) {
                GL11.glColor4f(vColor.x, vColor.y, vColor.z, vColor.a);
            }
            GL11.glNormal3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(pos2.x, pos2.y, pos2.z);
            GL11.glNormal3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(pos1.x, pos2.y, pos2.z);
            GL11.glNormal3f(-1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(pos1.x, pos1.y, pos2.z);
            GL11.glNormal3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(pos2.x, pos1.y, pos2.z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        // Back
        if (renderBack) {
            if (textureSide != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureSide.getTextureID());
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
            if (color != null) {
                color.bind();
            } else if (vColor != null) {
                GL11.glColor4f(vColor.x, vColor.y, vColor.z, vColor.a);
            }
            GL11.glNormal3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(pos1.x, pos2.y, pos1.z);
            GL11.glNormal3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(pos2.x, pos2.y, pos1.z);
            GL11.glNormal3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(pos2.x, pos1.y, pos1.z);
            GL11.glNormal3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(pos1.x, pos1.y, pos1.z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        // Right
        if (renderRight) {
            if (textureSide != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureSide.getTextureID());
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
            if (color != null) {
                color.bind();
            } else if (vColor != null) {
                GL11.glColor4f(vColor.x, vColor.y, vColor.z, vColor.a);
            }
            GL11.glNormal3f(1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(pos2.x, pos2.y, pos1.z);
            GL11.glNormal3f(1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(pos2.x, pos2.y, pos2.z);
            GL11.glNormal3f(1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(pos2.x, pos1.y, pos2.z);
            GL11.glNormal3f(1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(pos2.x, pos1.y, pos1.z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        // Left
        if (renderLeft) {
            if (textureSide != null) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureSide.getTextureID());
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
            if (color != null) {
                color.bind();
            } else if (vColor != null) {
                GL11.glColor4f(vColor.x, vColor.y, vColor.z, vColor.a);
            }
            GL11.glNormal3f(-1.0f, 1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 0.0f);
            GL11.glVertex3f(pos1.x, pos2.y, pos2.z);
            GL11.glNormal3f(-1.0f, 1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 0.0f);
            GL11.glVertex3f(pos1.x, pos2.y, pos1.z);
            GL11.glNormal3f(-1.0f, -1.0f, -1.0f);
            GL11.glTexCoord2f(0.0f, 1.0f);
            GL11.glVertex3f(pos1.x, pos1.y, pos1.z);
            GL11.glNormal3f(-1.0f, -1.0f, 1.0f);
            GL11.glTexCoord2f(1.0f, 1.0f);
            GL11.glVertex3f(pos1.x, pos1.y, pos2.z);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }

        GL11.glEnd();
        // Reset color if color was used
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

    }
}
