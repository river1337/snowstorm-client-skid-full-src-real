package today.snowstorm.client.module.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.events.EventType;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.utils.player.MoveUtil;

public class NoFall extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    // Stays the same
    public ModeSetting mode = new ModeSetting("Mode", "Hypixel", "Hypixel");

    public NoFall() {
        // Super constructor slightly changes (old: super("NoFall", Category)).
        super("NoFall", "Negate Taking Fall Damage", ModuleType.PLAYER, Keyboard.KEY_SLASH);
        addSetting(mode);
    }

    // Setup your event Listener!
    @EventHandler
    public Listener<UpdateEvent> onSuckCock = e -> {
        if (!(mc.thePlayer.fallDistance > 2.69) || e.getType() != EventType.PRE  || isOverVoid()) return;
        mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
        mc.thePlayer.fallDistance = 0.0f;
    };

private boolean isOverVoid() {

        boolean isOverVoid = true;
        BlockPos block = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        for (double i = mc.thePlayer.posY + 1; i > 0; i -= 0.5) {

            if (isOverVoid) {
                try {
                    if (mc.theWorld.getBlockState(block).getBlock() != Blocks.air) {
                        isOverVoid = false;
                        break;

                    }
                } catch (Exception e) {
            }

        }

        block = block.add(0, -1, 0);

        }

        for (double i = 0; i < 10; i += 0.1) {
            if (MoveUtil.isOnGround(i) && isOverVoid) {
                isOverVoid = false;
                break;
            }
        }

        return isOverVoid;
    }
}
