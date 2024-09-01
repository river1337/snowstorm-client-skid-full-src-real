package today.snowstorm.client.module.misc;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.ModeSetting;

public class NoFall extends Module {

    private ModeSetting server = new ModeSetting("Mode", "NCP", "Watchdog", "NCP");

    public NoFall() {
        super("NoFall", "Remove the ouch on fall.", ModuleType.MISC, Keyboard.KEY_H);
        addSetting(server);
    }

    @EventHandler
    public Listener<UpdateEvent> onSuckMyDick = e -> {

    };

}
