package today.snowstorm.client.module.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.utils.player.MoveUtil;

public class Sprint extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public Sprint() {
        super("Sprint", "Sprint.", ModuleType.MOVEMENT, Keyboard.KEY_N);
    }

    @EventHandler
    public final Listener<UpdateEvent> onUpdateEvent = e -> {
        // Perma Sprint
        if(MoveUtil.isMoving() && MoveUtil.isMovingForward()) {
            mc.thePlayer.setSprinting(true);
        }
    };

}
