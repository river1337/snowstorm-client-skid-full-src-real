package today.snowstorm.client.module.combat;

import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.settings.ModeSetting;

public class Velocity extends Module {

    public static ModeSetting mode = new ModeSetting("Mode", "CancelPacket", "CancelPacket", "1tickcancelpack");

    public Velocity() {
        super("Velocity", "Disable Knockback", ModuleType.COMBAT, Keyboard.KEY_O);
        addSetting(mode);
    }

}
