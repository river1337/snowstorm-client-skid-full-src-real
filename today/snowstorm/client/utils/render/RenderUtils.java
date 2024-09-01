package today.snowstorm.client.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {


    // Enable rendering 2D in GL11
    public static void enableRender2D() {
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
    }

    // Disable rendering 2D in GL11
    public static void disableRender2D() {
        GL11.glDisable(3042);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }


    // Draw a circle (clickgui?)
    public static void drawCircle(float x, float y, float radius, Color color) {
        enableRender2D();
        setColor(color.getRGB());
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        for (int i = 0; i <= 360; i++) {
            double angle = Math.toRadians(i);
            GL11.glVertex2d(x + (radius * Math.cos(angle)), y + (radius * Math.sin(angle)));
        }
        GL11.glEnd();
        disableRender2D();
    }

    // Draw a rectangle using x y width and height
    public static void drawRect(float x, float y, float width, float height, int color) {
        RenderUtils.enableRender2D();
        setColor(color);
        GL11.glBegin(7);
        glVertex2d((double) x, (double) y);
        glVertex2d((double) (x + width), (double) y);
        glVertex2d((double) (x + width), (double) (y + height));
        glVertex2d((double) x, (double) (y + height));
        GL11.glEnd();
        RenderUtils.disableRender2D();
    }

    // CURRENTLY NOT WORKING (i think)
    public static void drawGradientRect(int x, int y, int width, int height, Color color1, Color color2) {
        float r1 = (float) color1.getRed() / 255;
        float g1 = (float) color1.getGreen() / 255;
        float b1 = (float) color1.getBlue() / 255;
        float a1 = (float) color1.getAlpha() / 255;

        float r2 = (float) color2.getRed() / 255;
        float g2 = (float) color2.getGreen() / 255;
        float b2 = (float) color2.getBlue() / 255;
        float a2 = (float) color2.getAlpha() / 255;

        enableRender2D();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(r1, g1, b1, a1);
        GL11.glVertex2f(x, y);

        GL11.glColor4f((r1 + r2) / 2, (g1 + g2) / 2, (b1 + b2) / 2, (a1 + a2) / 2);
        GL11.glVertex2f(x + width, y);

        GL11.glColor4f(r2, g2, b2, a2);
        GL11.glVertex2f(x + width, y + height);

        GL11.glColor4f((r1 + r2) / 2, (g1 + g2) / 2, (b1 + b2) / 2, (a1 + a2) / 2);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
        disableRender2D();
    }

    public static void drawCirclePart(double x, double y, float fromAngle, float toAngle, float radius, int slices) {
        GlStateManager.enableBlend();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        final float increment = (toAngle - fromAngle) / slices;

        for (int i = 0; i <= slices; i++) {
            final float angle = fromAngle + i * increment;

            final float dX = MathHelper.sin(angle);
            final float dY = MathHelper.cos(angle);

            GL11.glVertex2d(x + dX * radius, y + dY * radius);
        }
        GL11.glEnd();
    }

    public static void drawFillRectangle(double x, double y, double width, double height) {

        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL_QUADS);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }
    public static void drawRoundedRect(double x, double y, double width, double height, float cornerRadius, Color color) {
        final int slices = 10;

        GlStateManager.color(((float) color.getRed()) / 255, ((float) color.getGreen()) / 255,
                ((float) color.getBlue()) / 255);
        drawFillRectangle(x + cornerRadius, y, width - 2 * cornerRadius, height);
        drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2 * cornerRadius);
        drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2 * cornerRadius);

        drawCirclePart(x + cornerRadius, y + cornerRadius, -MathHelper.PI, -MathHelper.PId2, cornerRadius, slices);
        drawCirclePart(x + cornerRadius, y + height - cornerRadius, -MathHelper.PId2, 0.0F, cornerRadius, slices);

        drawCirclePart(x + width - cornerRadius, y + cornerRadius, MathHelper.PId2, MathHelper.PI, cornerRadius, slices);
        drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0, MathHelper.PId2, cornerRadius, slices);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GlStateManager.disableBlend();

        //GlStateManager.enableAlpha();
        //GlStateManager.alphaFunc(GL11.GL_NOTEQUAL, 0);
    }

    // Draw a 3D line (soon to be moved to another util file)
    public static void drawLine(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        double renderPosX = Minecraft.getMinecraft().getRenderManager().renderPosX;
        double renderPosY = Minecraft.getMinecraft().getRenderManager().renderPosY;
        double renderPosZ = Minecraft.getMinecraft().getRenderManager().renderPosZ;

        minX -= renderPosX;
        minY -= renderPosY;
        minZ -= renderPosZ;
        maxX -= renderPosX;
        maxY -= renderPosY;
        maxZ -= renderPosZ;

        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        worldrenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        worldrenderer.pos(minX, minY, minZ).endVertex();
        worldrenderer.pos(maxX, maxY, maxZ).endVertex();
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    // Draw 3D box (soon to be moved to a new util file)
    public static void drawBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        setColor(new Color(103, 133, 153).getRGB());
        drawLine(minX, minY, minZ, maxX, minY, minZ); // bottom edge
        drawLine(maxX, minY, minZ, maxX, maxY, minZ); // front edge
        drawLine(maxX, maxY, minZ, minX, maxY, minZ); // top edge
        drawLine(minX, maxY, minZ, minX, minY, minZ); // back edge
        drawLine(minX, minY, maxZ, maxX, minY, maxZ); // bottom edge
        drawLine(maxX, minY, maxZ, maxX, maxY, maxZ); // front edge
        drawLine(maxX, maxY, maxZ, minX, maxY, maxZ); // top edge
        drawLine(minX, maxY, maxZ, minX, minY, maxZ); // back edge
        drawLine(minX, minY, minZ, minX, minY, maxZ); // left edge
        drawLine(maxX, minY, minZ, maxX, minY, maxZ); // right edge
        drawLine(maxX, maxY, minZ, maxX, maxY, maxZ); // right edge
        drawLine(minX, maxY, minZ, minX, maxY, maxZ); // left edge
    }

    // Same thing as above but with solid faces (oh, and its broken).
    public static void renderBox(float x, float y, float z, float width, float height, float depth) {
        GL11.glBegin(GL11.GL_QUADS);
        // Front face
        GL11.glVertex3f(x, y, z + depth);
        GL11.glVertex3f(x + width, y, z + depth);
        GL11.glVertex3f(x + width, y + height, z + depth);
        GL11.glVertex3f(x, y + height, z + depth);

        // Back face
        GL11.glVertex3f(x + width, y, z);
        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x, y + height, z);
        GL11.glVertex3f(x + width, y + height, z);

        // Left face
        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x, y, z + depth);
        GL11.glVertex3f(x, y + height, z + depth);
        GL11.glVertex3f(x, y + height, z);

        // Right face
        GL11.glVertex3f(x + width, y, z + depth);
        GL11.glVertex3f(x + width, y, z);
        GL11.glVertex3f(x + width, y + height, z);
        GL11.glVertex3f(x + width, y + height, z + depth);

        // Top face
        GL11.glVertex3f(x, y + height, z + depth);
        GL11.glVertex3f(x + width, y + height, z + depth);
        GL11.glVertex3f(x + width, y + height, z);
        GL11.glVertex3f(x, y + height, z);

        // Bottom face
        GL11.glVertex3f(x + width, y, z + depth);
        GL11.glVertex3f(x, y, z + depth);
        GL11.glVertex3f(x, y, z);
        GL11.glVertex3f(x + width, y, z);
        GL11.glEnd();
    }

    // Scale (max scale: 1.0) something. Proper Usage:
    // RenderUtils.scale(x, y, scale, () -> { // Runnable Code Here });
    public static void scale(float x, float y, float scale, Runnable data) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, 1);
        GL11.glTranslatef(-x, -y, 0);
        data.run();
        GL11.glPopMatrix();
    }

    // Draw rectangle with borders
    public static void drawBorderRect(int x, int y, int width, int height, Color borderColour, Color backgroundColor) {
        // left border
        Gui.drawRect(x - 1, y - 1, x + 1, y + height + 1, borderColour.getRGB());

        // right border
        Gui.drawRect(x + width, y - 1, x + width + 1, y + height + 1, borderColour.getRGB());

        // top border
        Gui.drawRect(x, y - 1, x + width, y + 1, borderColour.getRGB());

        // bottom border
        Gui.drawRect(x, y + height, x + width, y + height + 1, borderColour.getRGB());

        // background box
        Gui.drawRect(x, y, x + width, y + height, backgroundColor.getRGB());

    }

    // ???
    public static void drawInnerBorderRect(int x, int y, int width, int height, Color borderColour, Color backgroundColor) {
        // left border
        Gui.drawRect(x -1 , y + 1, x - 1, y + height - 1, borderColour.getRGB());

        // right border
        Gui.drawRect(x + width, y + 1, x + width - 1, y + height - 1, borderColour.getRGB());

        // top border
        Gui.drawRect(x, y + 1, x + width, y - 1, borderColour.getRGB());

        // bottom border
        Gui.drawRect(x, y + height, x + width, y + height - 1, borderColour.getRGB());

        // background box
        Gui.drawRect(x, y, x + width, y + height, backgroundColor.getRGB());

    }

    // Set GL color.
    public static void setColor(int colorHex) {
        float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
        float red = (float) (colorHex >> 16 & 255) / 255.0F;
        float green = (float) (colorHex >> 8 & 255) / 255.0F;
        float blue = (float) (colorHex & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    // Reset GL colour.
    public static void resetColor() {
        GlStateManager.color(1, 1, 1, 1);
    }

    public static void prepareScissorBox(float x, float y, float width, float height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        prepareScissorBox(x, y, width, height, scaledResolution);
    }

    public static void prepareScissorBox(float x, float y, float width, float height, ScaledResolution scaledResolution) {
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((scaledResolution.getScaledHeight() - height) * factor), (int)((width - x) * factor), (int)((height - y) * factor));
    }

}
