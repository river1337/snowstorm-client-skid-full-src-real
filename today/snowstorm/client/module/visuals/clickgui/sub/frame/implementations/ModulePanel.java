package today.snowstorm.client.module.visuals.clickgui.sub.frame.implementations;

import net.minecraft.client.renderer.GlStateManager;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.module.visuals.clickgui.sub.frame.Panel;
import today.snowstorm.client.utils.other.TimerUtil;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class ModulePanel extends Panel {
    public Module module;
    private final TimerUtil timer = new TimerUtil();

    public ModulePanel(Module module, float x, float y, float width, float height) {
        super(x, y, width, height, true);
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        if(this.visible) {
            RenderUtils.drawRect(x, y, width, height, new Color(18, 18, 18).getRGB());
            if (module.isEnabled()) {
                RenderUtils.drawRect(x, y, width, height, new Color(11, 11, 24).getRGB());
            }

            FontUtil.poppinsNormal18.drawString(module.getName(), (int) (x + 2), (int) (y + height / 2 - FontUtil.poppinsNormal18.getHeight() / 2 + 1), -1);
        }

        GlStateManager.popMatrix();
        // Removed unnecessary code
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY) && mouseButton == 0) {
            module.toggle();
            timer.reset();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
    }

    public Module module() {
        return module;
    }
    public void module(Module module) {
        this.module = module;
    }

}
