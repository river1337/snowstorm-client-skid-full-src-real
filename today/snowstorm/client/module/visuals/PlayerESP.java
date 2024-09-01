package today.snowstorm.client.module.visuals;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.events.EventType;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.Render3DEvent;
import today.snowstorm.client.settings.ModeSetting;

public class PlayerESP extends Module {

    private ModeSetting style = new ModeSetting("Style", "2D", "2D", "3D");

    public PlayerESP() {
        super("PlayerESP", "Better Eyesight", ModuleType.VISUALS, Keyboard.KEY_BACKSLASH);
        addSetting(style);
        setEnabled(true);
    }

    @Override
    public void onEnable() {

        super.onEnable();
    }

    @Override
    public void onDisable() {

        super.onDisable();
    }

    @EventHandler
    public Listener<Render3DEvent> onILoveCown = e -> {
        if(e.getType() != EventType.PRE) return;
    };

}
