package today.snowstorm.client.module.visuals.clickgui.sub.components.implementations;

import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.module.visuals.clickgui.sub.components.Component;

import java.util.Objects;

public class BindComponent extends Component {

    private final Module module;

    public BindComponent(Module module, float x, float y, float width, float height) {
        this(module, x, y, width, height, true);
    }

    public BindComponent(Module module, float x, float y, float width, float height, boolean visible) {
        super(x, y, width, height, visible);
        this.module = Objects.requireNonNull(module);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        String text = "Bind: [" + (focused ? " " : "idk") + "]";
        FontUtil.poppinsNormal18.drawString(text, (int) (x + 2), (int) (y + height / 2 - 3 / 2), -1);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(isHovered(mouseX, mouseY) && mouseButton == 0) {
            focused = !focused;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        // Do nothing
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (focused) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                module.setKeybind(0);
                focused = false;
            } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                module.setKeybind(Character.toUpperCase(typedChar));
                focused = false;
            }
        }
    }
}