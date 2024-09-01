package today.snowstorm.client.module.ghost;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.settings.NumberSetting;

public class AimAssist extends Module {

    private ModeSetting aimMode = new ModeSetting("Accuracy", "Super Legit", "SuperLegit"/*, "Legit", "Good Player", "MVP++", "MLG"*/);

    private BooleanSetting visualize = new BooleanSetting("Visualize Range", false);

    private NumberSetting range = new NumberSetting("Range", 2, 0, 3, 0.1);

    public AimAssist() {
        super("AimAssist", "Get better without flagging!", ModuleType.GHOST, Keyboard.KEY_EQUALS);
        addSetting(aimMode, range, visualize);
    }

    @EventHandler
    public Listener<UpdateEvent> onImBadSoHelpMeAim = e -> {



    };

}
