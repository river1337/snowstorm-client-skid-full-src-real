package today.snowstorm.client.module.visuals.clickgui.sub.frame;

import today.snowstorm.client.module.visuals.clickgui.sub.components.Component;
import today.snowstorm.client.module.visuals.clickgui.sub.components.implementations.SettingComponent;
import today.snowstorm.client.module.visuals.clickgui.sub.frame.implementations.ModulePanel;
import today.snowstorm.api.settings.Setting;
import today.snowstorm.client.utils.other.HoverUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends Component {

    protected boolean extended;
    protected List<Component> components = new ArrayList<>();

    public Panel(float x, float y, float width, float height, boolean visible) {
        super(x, y, width, height, true);
        init();
    }

    @Override
    public void reset() {
        for (Component component : components) {
            component.reset();
        }
    }

    @Override
    public boolean focused() {
        for (Component component : components) {
            if (component.visible() && component.focused()) {
                return true;
            }
        }
        return focused;
    }

    public void drawScreen(int mouseX, int mouseY) {
        if (extended) {
            updatePositions();
            for (Component component : components) {
                if (component.visible()) {
                    component.drawScreen(mouseX, mouseY);
                }
            }
        }
    };

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (HoverUtils.isHovering(x, y, width, height, mouseX, mouseY)) {
            if (mouseButton == 1) {
                extended = !extended;
                if (extended) reset();

                for (Component component : components) {
                    component.visible(!(component instanceof SettingComponent) || extended);
                }
            }
        } else if (HoverUtils.isHovering(x, y, x + width, y + height, mouseX, mouseY)) {
            if (extended) {
                for (Component component : components) {
                    if (component.visible()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (extended) {
            for (Component component : components) {
                if (component.visible()) {
                    component.mouseReleased(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (extended) {
            for (Component component : components) {
                if (component.visible()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
    }

    public void updateComponents() {
        for (Component component : components) {
            if (component instanceof SettingComponent) {
                component.visible(true);
            }
        }
    }

    // Calculate the y-offset
    public void updatePositions() {
        float yOffset = height;

        for (Component component : components) {
            // Skip invisible components
            if (!component.visible()) {
                continue;
            }

            // Update the component's position
            component.setPosition(x, y + yOffset, component.width(), component.height());

            // Calculate the new y-offset
            if (component instanceof Panel) {
                if(!((Panel) component).extended) {
                    yOffset += component.height();
                } else {
                    if(component instanceof ModulePanel) {
                        ModulePanel mod = (ModulePanel) component;
                        for(Setting component1 : mod.module.getSettings()) {
                            yOffset += 20;
                        }
                        yOffset += component.height();
                    }
                }
            } else {
               yOffset += 20;
            }
        }
    }
}