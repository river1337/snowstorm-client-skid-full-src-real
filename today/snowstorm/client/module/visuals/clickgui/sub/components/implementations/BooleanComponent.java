package today.snowstorm.client.module.visuals.clickgui.sub.components.implementations;

import net.minecraft.client.gui.Gui;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class BooleanComponent extends SettingComponent<BooleanSetting> {

    private int opacity = 0;

    public BooleanComponent(BooleanSetting setting, float x, float y, float width, float height) {
        super(setting, x, y, width, height);
    }

    public BooleanComponent(BooleanSetting setting, float x, float y, float width, float height, boolean visible) {
        super(setting, x, y, width, height, visible);
    }

    @Override
    public void reset() {
        opacity = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if(!visible) return;
        if(setting.isEnabled()) {
            if(opacity < 255)
                opacity += 1;
        } else if(opacity > 0)
            opacity -= 1;

        int settingWidth = 9;
        int settingHeight = 9;

        RenderUtils.drawRect(x, y, width, height, new Color(13, 13, 13).getRGB());
        String label = setting.getName();
        FontUtil.poppinsNormal18.drawString(label, x + 2, y + height() / 2 - 7 / 2, -1);
        Gui.drawRect((int) (x + width - settingWidth - 2),(int) (y + height / 2 - settingHeight / 2),(int) (x + width - 1), (int) (y + height / 2 - settingHeight / 2 + settingHeight), new Color(40, 40, 40).getRGB());
        if(setting.isEnabled()) {
            settingWidth -= 1;
            settingHeight -= 1;
            Gui.drawRect((int) (x + width - settingWidth - 2.2f),(int)  (y + height / 2 - settingHeight / 2 + 0.2f),(int) (x + width - .1f),(int) (y + height / 2 - settingHeight / 2 + settingHeight + 1.1f), new Color(34, 37, 187).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(!visible) return;
        if(HoverUtils.inBounds(x + width - 9 - 1, y, x + width - 1, y + height, mouseX, mouseY)) {
            setting.setEnabled(!setting.isEnabled());
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if(!visible) return;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(!visible) return;
    }

    public BooleanSetting setting() {
        return setting;
    }

    public void setting(BooleanSetting setting) {
        this.setting = setting;
    }
}
