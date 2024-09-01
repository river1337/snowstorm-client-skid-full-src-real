package today.snowstorm.client.module.visuals.clickgui.sub.components.implementations;

import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class EnumComponent extends SettingComponent<ModeSetting> {

    public EnumComponent(ModeSetting setting, float x, float y, float width, float height) {
        super(setting, x, y, width, height);
    }

    public EnumComponent(ModeSetting setting, float x, float y, float width, float height, boolean visible) {
        super(setting, x, y, width, height, visible);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if(!visible) return;


        String label = setting.getName();
        RenderUtils.drawRect(x, y, width, height, new Color(13, 13, 13).getRGB());
        FontUtil.poppinsNormal18.drawString(label + ": " + setting.getMode(), x + 2, y + height() / 2 - 3, -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(visible) {
            if(HoverUtils.inBounds(x, y, x + width, y + height, mouseX, mouseY)) {
                if(mouseButton == 0)
                    setting.cycle(false);
                else
                    setting.cycle(true);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
