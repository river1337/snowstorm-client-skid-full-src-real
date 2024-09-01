package today.snowstorm.client.ui.altManager.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.ui.altManager.util.Alt;
import today.snowstorm.client.utils.Wrapper;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class AltBox extends GuiScreen {

    private String username, email, password;

    private float x, y, width, height;

    public boolean isSelected = false;

    public Runnable action;

    private Alt alt;

    public AltBox(Alt alt, float x, float y, float width, float height) {
        this.username = alt.getUsername();
        this.email = alt.getEmail();
        this.password = alt.getPassword();

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    public Alt getAlt() {
        return alt;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float part) {
        RenderUtils.drawBorderRect((int) x, (int) y, (int) width, (int) height, new Color(30, 30, 30), new Color(15, 15, 15));

        // Draw the skin face
        renderPlayerHead(x + 2, y + 2, 55, 56, DefaultPlayerSkin.getDefaultSkinLegacy());

        // Draw Username
        FontUtil.poppinsNormal18.drawString(username, x + 63, y + 4, -1);
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static void renderPlayerHead(final double x, final double y, final double width, final double height, final ResourceLocation skin) {
        // Bind skin texture
        Wrapper.getMc().getTextureManager().bindTexture(skin);
        // Colour solid
        GL11.glColor4f(1, 1, 1, 1);
        final float fds = 1.0F / 8;

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(fds, fds);
            GL11.glVertex2d(x, y);
            GL11.glTexCoord2f(fds, fds * 2);
            GL11.glVertex2d(x, y + height);
            GL11.glTexCoord2f(fds * 2, fds * 2);
            GL11.glVertex2d(x + width, y + height);
            GL11.glTexCoord2f(fds * 2, fds);
            GL11.glVertex2d(x + width, y);
        }
        GL11.glEnd();
    }
}
