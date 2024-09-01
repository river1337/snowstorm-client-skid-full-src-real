package today.snowstorm.client.module.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.events.EventType;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.MoveEvent;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.utils.player.MoveUtil;

public class Speed extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    private ModeSetting modes = new ModeSetting("Mode", "NCP", "NCP", "Watchdog");

    public Speed() {
        super("Speed", "Like the drug!", ModuleType.MOVEMENT, Keyboard.KEY_B);
        addSetting(modes);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;

        super.onDisable();
    }

    @EventHandler
    public final Listener<MoveEvent> onSuckDick = e -> {

    };

    @EventHandler
    public final Listener<UpdateEvent> onUpdateEvent = e -> {
        if(modes.is("NCP")) {
            if(e.getType() == EventType.PRE) {
                if (MoveUtil.isMoving()) {
                    mc.timer.timerSpeed = 1.077f;
                    if(MoveUtil.isMovingForward()) {
                        MoveUtil.setMotion(0.31f);
                    } else {
                        MoveUtil.setMotion(0.28f);
                    }

                    if (MoveUtil.isOnGround(0.001f)) {
                        mc.thePlayer.jump();
                        //Wrapper.addChatMessage("jumped,", false);
                        mc.thePlayer.motionY -= 0.02f;
                        //mc.thePlayer.motionY += 0.2f;
                    }
                } else {
                    mc.timer.timerSpeed = 1.0f;
                }
            }
        }
    };

    public double getJumpMotion(float motionY) {
        final Potion potion = Potion.jump;

        if (mc.thePlayer.isPotionActive(potion)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(potion).getAmplifier();
            motionY += (amplifier + 1) * 0.1F;
        }

        return motionY;
    }

}
