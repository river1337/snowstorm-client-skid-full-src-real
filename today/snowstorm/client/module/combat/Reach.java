package today.snowstorm.client.module.combat;

import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.settings.NumberSetting;

public class Reach extends Module {

    public static NumberSetting numba = new NumberSetting("Distance", 4, 3, 7, 0.1);

    public Reach() {
        super("Reach", "Smack Em From Far", ModuleType.COMBAT, Keyboard.KEY_PRIOR);
        addSetting(numba);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
