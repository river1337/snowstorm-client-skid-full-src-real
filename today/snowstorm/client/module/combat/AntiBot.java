package today.snowstorm.client.module.combat;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.utils.Wrapper;
import today.snowstorm.client.utils.server.OnServerUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiBot extends Module {

    private static final List<Entity> novalid = new CopyOnWriteArrayList<>();
    private static final List<Entity> valid = new CopyOnWriteArrayList<>();

    private ModeSetting mode = new ModeSetting("Mode", "Watchdog", "Watchdog", "Matrix");

    public AntiBot() {
        super("AntiBot", "Watchdog", ModuleType.COMBAT, Keyboard.KEY_L);
        setEnabled(true);
        addSetting(mode);
    }

    public static boolean isBot(Entity e) {
        if (!(e instanceof EntityPlayer) || !Snowstorm.INSTANCE.getModuleManager().getModuleByName("AntiBot").isEnabled())
            return false;
        EntityPlayer player = (EntityPlayer) e;

        return (!inTab(player) && !valid.contains(player));
    }

    private static boolean inTab(EntityLivingBase entity) {
        for (NetworkPlayerInfo info : Wrapper.getMc().getNetHandler().getPlayerInfoMap())
            if (info != null && info.getGameProfile() != null && info.getGameProfile().getName().contains(entity.getName()))
                return true;
        return false;
    }

    @EventHandler
    public Listener<UpdateEvent> onUpdateEvent = e -> {
        if(Wrapper.getMc().isSingleplayer()) return;

        switch (mode.getMode()) {
            case "Hypixel":
                if (OnServerUtils.isOnHypixel()) {

                    if (Wrapper.getMc().isSingleplayer()) return;

                    if (Wrapper.getMc().thePlayer.ticksExisted < 5) {
                        valid.clear();
                    }

                    if (Wrapper.getMc().thePlayer.ticksExisted % 60 == 0) {
                        valid.clear();
                    }

                    if (!Wrapper.getMc().theWorld.getLoadedEntityList().isEmpty()) {
                        for (Entity entity : Wrapper.getMc().theWorld.getLoadedEntityList()) {
                            if (entity instanceof EntityPlayer) {
                                String str = entity.getDisplayName().getFormattedText();

                                if (str.startsWith("\\247r\\2478[NPC]")) return;

                                if (!entity.isInvisible()) {
                                    valid.add(entity);
                                }

                                if (entity.hurtResistantTime == 8) {
                                    valid.add(entity);
                                }
                            }
                        }
                    }
                } else {
                    Wrapper.addChatMessage("you are not on hypixel, antibot going bye bye.", false);
                    toggle();
                }
                break;

            case "Matrix":



                break;
        }

    };

}
