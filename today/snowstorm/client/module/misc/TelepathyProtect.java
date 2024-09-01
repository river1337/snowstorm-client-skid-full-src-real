package today.snowstorm.client.module.misc;

import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.settings.BooleanSetting;

public class SnowstormProtect extends Module {

    public BooleanSetting waterChecks = new BooleanSetting("Water Check", false),
                            ladderChecks = new BooleanSetting("Ladder Check", false),
                                deathToggle = new BooleanSetting("Death Toggles", false);


    public SnowstormProtect() {
        super("TeleProtect", "Extra-Protection!", ModuleType.MISC, Keyboard.KEY_COMMA);
    }

}
