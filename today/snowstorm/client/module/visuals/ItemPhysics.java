package today.snowstorm.client.module.visuals;

import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import org.lwjgl.input.Keyboard;

public class ItemPhysics extends Module {

    public ItemPhysics() {
        super("ItemPhysics", "Makes items fall like they do in real life", ModuleType.VISUALS, Keyboard.KEY_I);
    }

}
