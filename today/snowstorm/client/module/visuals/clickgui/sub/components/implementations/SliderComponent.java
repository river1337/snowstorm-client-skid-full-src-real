package today.snowstorm.client.module.visuals.clickgui.sub.components.implementations;

import net.minecraft.util.MathHelper;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.settings.NumberSetting;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComponent extends SettingComponent<NumberSetting> {

    private boolean sliding;

    public SliderComponent(NumberSetting setting, float x, float y, float width, float height) {
        super(setting, x, y, width, height);
    }

    public SliderComponent(NumberSetting setting, float x, float y, float width, float height, boolean visible) {
        super(setting, x, y, width, height, visible);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        if(!visible) return;

        RenderUtils.drawRect(x, y, width, height, new Color(11, 11, 11).getRGB());

        double deltaMaxMin = setting.getMaximum() - setting.getMinimum();
        float startX = x;
        float length = MathHelper.floor_double((setting.getValueAsDouble() - setting.getMinimum()) / deltaMaxMin * width - 1);
        if(sliding) {
            setting.setValue(round(((mouseX - startX) * deltaMaxMin / width + setting.getMinimum()), 2));
        }

        RenderUtils.drawRect(x + 1, y + 1, length - 1, height - 1, new Color(85, 85, 255).getRGB());

        FontUtil.poppinsNormal18.drawString(setting().getName() + " " + round(setting().getValueAsDouble(), 2), (int) x + 2, (int) (y + height / 2 - 3), -1);
    }

    private double round(final double val, final int places) {
        final double value = Math.round(val / setting.getIncrement()) * setting.getIncrement();
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (visible && !sliding && mouseButton == 0 && HoverUtils.isHovering(x, y, width, height, mouseX, mouseY))
            sliding = true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        sliding = false;
        if(!visible) return;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
