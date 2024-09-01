package today.snowstorm.client.module.misc;

import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.settings.ModeSetting;

public class KillSults extends Module {

    private BooleanSetting chatCooldown = new BooleanSetting("No Cooldown [MVP]", false);
    private ModeSetting message = new ModeSetting("Message", "Insult", "AutoLBozo", "Insult", "AutoL", "Advertise");

    private String[] defaultInsults = { "Your a failed abоrtiоn!", "{player_name} get clowned lol.", "Are you a furry {player_name}? You fight like a furry.",
                                        "POV: You die to the best player on Earth!", "Get good skid", "Imagine dying to a legit player lol, you suuuuck.",
                                        "wait your already dead? LOL", "easepeaz", "Congratulations {player_name}, you were my {kill_count} kill!",
                                        ""};

    public KillSults() {
        super("KillSults", "Fill your opponent with rage.", ModuleType.MISC, Keyboard.KEY_INSERT);
        addSetting(message, chatCooldown);
    }



}
