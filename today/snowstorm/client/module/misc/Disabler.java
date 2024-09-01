package today.snowstorm.client.module.misc;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.settings.ModeSetting;

public class Disabler extends Module {

    private ModeSetting mode = new ModeSetting("Disabler", "PingSpoof", "Watchdog1", "PingSpoof");

    public Disabler() {
        super("Disabler", "Disable AntiCheats", ModuleType.MISC, Keyboard.KEY_I);
        addSetting(mode);
    }

}
