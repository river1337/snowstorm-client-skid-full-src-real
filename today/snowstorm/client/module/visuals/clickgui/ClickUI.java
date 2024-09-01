package today.snowstorm.client.module.visuals.clickgui;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import today.snowstorm.client.module.visuals.clickgui.sub.components.Component;
import today.snowstorm.client.module.visuals.clickgui.sub.components.implementations.BooleanComponent;
import today.snowstorm.client.module.visuals.clickgui.sub.components.implementations.SliderComponent;
import today.snowstorm.client.module.visuals.clickgui.sub.frame.implementations.CategoryPanel;
import today.snowstorm.client.module.visuals.clickgui.sub.frame.implementations.ModulePanel;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.api.settings.Setting;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.module.visuals.clickgui.sub.components.implementations.EnumComponent;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.settings.NumberSetting;
import today.snowstorm.client.utils.animations.impl.ElasticAnimation;
import today.snowstorm.client.utils.render.RenderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author G8LOL
 * @since 6/5/2022
 */
public class ClickUI extends GuiScreen {

    private List<Component> objects = new ArrayList<>();

    private float componentWidth = 110;
    private float componentHeight = 25;

    private float ComponentHeightTwo = 20;

    private ElasticAnimation loadAnimation;

    public ClickUI() {
        float posX = 6;
        float posY = 4;
        for(ModuleType category : ModuleType.values()) {
            objects.add(new CategoryPanel(category, posX, posY, componentWidth, componentHeight) {
                @Override
                public void init() {
                    for(Module modulee : Snowstorm.INSTANCE.getModuleManager().getModules()) {
                        if(modulee.getModuleType() == category) {
                            components.add(new ModulePanel(modulee, x - 2, y, componentWidth + 4, ComponentHeightTwo) {
                                @Override
                                public void init() {
                                    for(Setting setting : modulee.getSettings()) {
                                        if(setting instanceof BooleanSetting) {
                                            components.add(new BooleanComponent((BooleanSetting) setting, x, y, componentWidth, ComponentHeightTwo, true));
                                        }
                                        if(setting instanceof ModeSetting) {
                                            components.add(new EnumComponent((ModeSetting) setting, x, y, componentWidth, ComponentHeightTwo, true));
                                        }
                                        if(setting instanceof NumberSetting) {
                                            components.add(new SliderComponent((NumberSetting) setting, x, y, componentWidth, ComponentHeightTwo, true));
                                        }
                                    }
                                    updateComponents();
                                }
                            });
                        }
                    }
                }
            });
            posX += componentWidth + 3;
        }
    }

    @Override
    public void initGui() {
        loadAnimation = new ElasticAnimation(400, 0.3D, 8f, 1f, true);
        objects.forEach(Component::reset);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);

        RenderUtils.scale(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, (float) loadAnimation.getOutput() + 0.7f, () -> {
            objects.forEach(panel -> {
                if(panel.visible())
                    panel.drawScreen(mouseX, mouseY);
            });
        });
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        objects.forEach(panel -> {
            if(panel.visible())
                panel.mouseClicked(mouseX, mouseY, mouseButton);
        });
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        objects.forEach(panel -> { if(panel.visible()) panel.mouseReleased(mouseX, mouseY, state); });
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if(keyCode == 1 || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            Snowstorm.INSTANCE.getModuleManager().getModuleByName("ClickGUI").setEnabled(false);
        }

        boolean focused = false;
        for(Component panel : objects)
            if(panel.visible() && panel.focused())
                focused = true;
        if(!focused) {
            super.keyTyped(typedChar, keyCode);
        }

        objects.forEach(panel -> { if(panel.visible()) panel.keyTyped(typedChar, keyCode); });
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
