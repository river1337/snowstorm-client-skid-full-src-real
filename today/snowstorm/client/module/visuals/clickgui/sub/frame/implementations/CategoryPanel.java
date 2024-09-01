package today.snowstorm.client.module.visuals.clickgui.sub.frame.implementations;

import org.lwjgl.opengl.GL11;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.module.visuals.clickgui.sub.frame.Panel;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class CategoryPanel extends Panel {
    private float dragX, dragY;
    private ModuleType category;
    private boolean dragging;

    public CategoryPanel(ModuleType category, float x, float y, float width, float height) {
        super(x, y, width, height, true);
        this.category = category;
    }

    @Override
    public void reset() {
        origHeight = height;
        super.reset();
    }

    private int nicecol;
    @Override
    public void drawScreen(int mouseX, int mouseY) {

        nicecol = new Color(12, 12, 12).getRGB();

        if (dragging) {
            x = mouseX + dragX;
            y = mouseY + dragY;
            origX = x;
            origY = y;
            nicecol = new Color(8, 8, 8).getRGB();
        }

        if (origHeight > height) origHeight = height;
        if (origHeight < 0) origHeight = 0;

        String name = category.name().toUpperCase();
        RenderUtils.drawRect((int) x - 2,(int)  y - 2,(int)  (width + 4), (int) (height + 2), nicecol);
        int textWidth = (int) FontUtil.poppinsFat24.getStringWidth(name);
        int textX = (int) (x + width / 2 - textWidth / 2);
        FontUtil.poppinsFat24.drawString(name, textX, (int) (y + height() / 2 - FontUtil.poppinsNormal18.getHeight() / 2 - 2), -1);
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (HoverUtils.isHovering(x, y, width, height, mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragging = true;
                dragX = (x - mouseX);
                dragY = (y - mouseY);
            } else if (mouseButton == 1) {
                nicecol = new Color(20, 20, 20, 240).getRGB();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        dragging = false;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    public ModuleType category() {
        return category;
    }

    public void category(ModuleType category) {
        this.category = category;
    }
}
