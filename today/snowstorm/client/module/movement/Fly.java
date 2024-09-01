package today.snowstorm.client.module.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.TickEvent;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.utils.player.MoveUtil;

public class Fly extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public ModeSetting mode = new ModeSetting("Modes", "Watchdog", "Watchdog");

    public Fly() {
        super("Fly", "Fly hax", ModuleType.MOVEMENT, Keyboard.KEY_F);
        addSetting(mode);
    }

    private int ticks = 0;

    @EventHandler
    public Listener<UpdateEvent> onSuckYourNan = e -> {

        mc.thePlayer.motionY = 0.08;

        if (ticks == 2) {
           e.posY = ( mc.thePlayer.posY - 22 * Math.random() * 5);
        }

        if(ticks > 1) {
            MoveUtil.setSpeed(MoveUtil.getBaseSpeed());
        } else {
            MoveUtil.setSpeed(0.02f);
        }

    };

    @EventHandler
    public Listener<TickEvent> onTick = e -> {
        ticks++;
        if(ticks == 6) {
            ticks = 0;
        }
    };

    private double getGroundMan() {
        double y = mc.thePlayer.posY;
        AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1.0;
        for (double ground = y; ground > 0.0; ground -= blockHeight) {
            AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight,
                    playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);
            if (!mc.theWorld.checkBlockCollision(customBox)) continue;
            if (blockHeight <= 0.05) {
                return ground + blockHeight;
            }
            ground += blockHeight;
            blockHeight = 0.05;
        }
        return 0.0;
    }

}
