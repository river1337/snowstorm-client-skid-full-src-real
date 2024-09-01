package today.snowstorm.client.ui.altManager.components;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class AltButton extends GuiScreen {

    private String text;
    private float x, y, width, height;
    private Color color;

    public Runnable action;

    public AltButton(String text, float x, float y, float width, float height, Color color) {
        this.text = text;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.enabled = true;
        this.color = color;
    }

    private boolean enabled;

    public AltButton(String text, float x, float y, float width, float height, Color color, boolean enabled) {
        this.text = text;

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.color = color;
        this.enabled = enabled;
    }

    public void render(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if(enabled) {

            RenderUtils.drawRect(x, y, width, height, color.getRGB());
            if(HoverUtils.isHovering(x, y, width, height, mouseX, mouseY)) {
                FontUtil.poppinsNormal18.drawString(text, (int) (x + ((width / 2) - (FontUtil.poppinsNormal18.getStringWidth(text) / 2))), (int) (y + (height / 2) - 4), new Color(248, 210, 139).getRGB());
            } else {
                FontUtil.poppinsNormal18.drawString(text, (int) (x + ((width / 2) - (FontUtil.poppinsNormal18.getStringWidth(text) / 2))), (int) (y + (height / 2) - 4), -1);
            }
        } else {
            RenderUtils.drawRect(x, y, width, height, new Color(10, 10, 10, 150).getRGB());
            FontUtil.poppinsNormal18.drawString(text, (int) (x + ((width / 2) - (FontUtil.poppinsNormal18.getStringWidth(text) / 2))), (int) (y + (height / 2) - 4), new Color(100, 100, 100).getRGB());
        }

        GlStateManager.popMatrix();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
