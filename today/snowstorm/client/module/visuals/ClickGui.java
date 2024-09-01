package today.snowstorm.client.module.visuals;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import org.lwjgl.input.Keyboard;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.utils.Wrapper;

public class ClickGui extends Module {

    public ClickGui() {
        super("ClickGUI", "Click UI", ModuleType.VISUALS, Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        Wrapper.getMc().displayGuiScreen(Snowstorm.INSTANCE.guiClickGui);
        super.onEnable();
    }
    @Override
    public void onDisable() {
        Wrapper.getMc().displayGuiScreen(null);
        super.onDisable();
    }

}
