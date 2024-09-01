package today.snowstorm.client.module.combat;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.optifine.reflect.Reflector;
import org.lwjgl.input.Keyboard;
import today.snowstorm.client.events.TickEvent;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.utils.other.TimerUtil;
import today.snowstorm.client.utils.player.RotationUtils;
import today.snowstorm.api.events.EventType;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.settings.NumberSetting;
import today.snowstorm.client.utils.server.OnServerUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KillAura extends Module {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private BooleanSetting modes = new BooleanSetting("Everything", true), devRotations = new BooleanSetting("Dev Rots", false), noSwing = new BooleanSetting("NoSwing", false);
    private ModeSetting rotationMode = new ModeSetting("Rotations", "Smooth", "Smooth", "Headlock"),
                        priorityMode = new ModeSetting("Priority", "Health", "Distance", "Health");
    private NumberSetting minCps = new NumberSetting("Min CPS", 10, 0, 25, 1), maxCps = new NumberSetting("Max CPS:", 16, 0, 30, 1),
                          rangeNum = new NumberSetting("Range", 4, 1, 7, 0.1);

    public List<EntityLivingBase> targets = new ArrayList<>();
    public static EntityLivingBase target;

    private TimerUtil timer;

    public KillAura() {
        super("KillAura", "For The Bad Players", ModuleType.COMBAT, Keyboard.KEY_R);
        addSetting(modes, devRotations, noSwing, rotationMode, priorityMode, rangeNum, minCps, maxCps);
        this.timer = new TimerUtil();
    }

    @Override
    public void onEnable() {
        targets.clear();
        target = null;

        super.onEnable();
    }

    @Override
    public void onDisable() {
        targets.clear();
        target = null;

        super.onDisable();
    }

    public boolean isValidEntity(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if(entity != mc.thePlayer && !mc.thePlayer.isDead && !(entity instanceof EntitySnowman || entity instanceof EntityArmorStand)) {
                if(entity instanceof EntityPlayer) {
                    if(!mc.thePlayer.canEntityBeSeen(entity)) return false;

                    if(entity.isInvisible()) return false;

                    return !AntiBot.isBot(entity) && !isOnSameTeam(entity);
                }

                if((entity instanceof EntityMob || entity instanceof EntitySlime) && modes.isEnabled()) {
                    return !AntiBot.isBot(entity);
                }

                if (entity instanceof EntityWither)
                    return !isOnSameTeam(entity);

                if ((entity instanceof EntityAnimal || entity instanceof EntityVillager)
                        && modes.isEnabled()) {
                    return !AntiBot.isBot(entity);
                }

            }
        }

        return false;
    }

    public static boolean isOnSameTeam(Entity entity) {
        if(OnServerUtils.isOnHypixel()) {
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("\247")) {
                if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2
                        || entity.getDisplayName().getUnformattedText().length() <= 2) {
                    return false;
                }
                return Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2)
                        .equals(entity.getDisplayName().getUnformattedText().substring(0, 2));
            }
        }
        return false;
    }

    TimerUtil timer3 = new TimerUtil();

    public void getTargets(UpdateEvent e) {
        targets.clear();

        for(Entity entity : mc.theWorld.getLoadedEntityList()) {
            if(entity instanceof EntityLivingBase) {
                if(isValidEntity(entity)) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    if(mc.thePlayer.getDistanceToEntity(entity) < rangeNum.getValueAsDouble()) {
                        targets.add(entityLivingBase);
                    } else {
                        targets.remove(entityLivingBase);
                    }
                }
            }
        }

        if(timer3.stopAfter(20, true)) {
            if (priorityMode.is("Distance")) {
                targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
            } else if (priorityMode.is("Health")) {
                if (targets instanceof EntityLivingBase) {
                    targets.sort(Comparator.comparing(EntityLivingBase::getHealth));
                }
            }
        }
    }

    private boolean shouldAttack = false;


    private float[] rotations = {0, 0};

    private boolean hasTargetChanged = false;

    @EventHandler
    public Listener<TickEvent> onTickEvent = e -> {

        if(mc.thePlayer == null && mc.theWorld == null & target == null) return;

        if(!targets.isEmpty()) {
            float lastYaw = mc.thePlayer.rotationYaw;
            float lastPitch = mc.thePlayer.rotationPitch;

            rotations = getEntRots(target);
            if (rotationMode.is("Smooth")) {
                rotations[0] = interpolateRotation(lastYaw, rotations[0], 1f);
                rotations[1] = interpolateRotation(lastPitch, rotations[1], 1f);
            }
        }
    };

    private TimerUtil legitCool = new TimerUtil();

    @EventHandler
    public Listener<UpdateEvent> onUpdateEvent = e -> {
        getTargets(e);

        if(Snowstorm.INSTANCE.getModuleManager().getModuleByName("Scaffold").isEnabled() || mc.thePlayer.isSpectator()) return;

        if(mc.thePlayer.isDead) {
            targets.clear();
            target = null;
            shouldAttack = false;
        }

        if(!targets.isEmpty()) {
            if(e.getType() == EventType.PRE) {
                target = targets.get(0);

                if (target.isDead || target.getHealth() <= 0) {
                    targets.clear();
                    target = null;
                    shouldAttack = false;
                }

                if (target == null) {
                    RotationUtils.resetCustomPitch();
                    RotationUtils.resetCustomYaw();
                    return;
                }

                shouldAttack = rotation(e, rotations);
            }

            if(!mc.thePlayer.isDead && target.getHealth() > 0.0) {
                if(!shouldAttack) return;
                if(timer.stopAfter((long) (1000 / ThreadLocalRandom.current().nextDouble(minCps.getValueAsDouble(), maxCps.getValueAsDouble())), true)) {

                    if(rayCast(rangeNum.getValueAsDouble(), rotations[0], rotations[1]) != target) return;

                    for (int i = 0; i < 1; i++) {
                        mc.playerController.attackEntity(mc.thePlayer, target);

                        if(!noSwing.isEnabled()) {
                            mc.thePlayer.swingItem();
                        } else {
                            mc.getNetHandler().getNetworkManager().sendPacket(new C0APacketAnimation());
                        }
                    }
                }
            }
        }

        if(targets.isEmpty()) {
            RotationUtils.resetCustomYaw();
            RotationUtils.resetCustomPitch();
            shouldAttack = false;
        }

    };

    private TimerUtil theByPai = new TimerUtil();

    private boolean rotation(UpdateEvent e, float[] rotations) {
        if(theByPai.stopAfter(10, true)) {
            e.setYaw(rotations[0]);
            e.setPitch(rotations[1]);

            RotationUtils.setCustomYaw(e.getRotYaw());
            RotationUtils.setCustomPitch(e.getRotPitch());

            if (devRotations.isEnabled()) {
                mc.thePlayer.rotationYaw = e.getRotYaw();
                mc.thePlayer.rotationPitch = e.getRotPitch();
            }

            return true;
        }

        return false;
    }

    public float interpolateRotation(float start, float end, float t) {
        // Calculate the difference between the start and end values
        float diff = end - start;

        // Interpolate the value using the provided t value
        return (start + diff * t) / getSensitivityMultiplier() * getSensitivityMultiplier();
    }


    public EntityLivingBase rayCast(double reachDistance, float yaw, float pitch) {
        if (mc.theWorld != null && mc.thePlayer != null) {
            Vec3 pos = mc.thePlayer.getPositionEyes(mc.timer.renderPartialTicks);
            Vec3 lookVec = mc.thePlayer.getVectorForRotation(pitch, yaw);
            Entity ent = null;

            for (Entity currentEntity : mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().addCoord(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance,
                    lookVec.zCoord * reachDistance).expand(1, 1, 1))) {
                if (currentEntity.canBeCollidedWith() && !currentEntity.isEntityEqual(mc.thePlayer)) {
                    MovingObjectPosition objPosition = currentEntity.getEntityBoundingBox().expand(currentEntity.getCollisionBorderSize(), currentEntity.getCollisionBorderSize(), currentEntity.getCollisionBorderSize())
                            .contract(0.1, 0.1, 0.1).calculateIntercept(pos, pos.addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance));
                    if (objPosition != null) {
                        double distance = pos.distanceTo(objPosition.hitVec);
                        if (distance < reachDistance) {
                            if (currentEntity == mc.thePlayer.ridingEntity && !(Reflector.ForgeEntity_canRiderInteract.exists() && Reflector.callBoolean(currentEntity,
                                    Reflector.ForgeEntity_canRiderInteract)) && reachDistance == 0.0D) {
                                ent = currentEntity;
                            } else {
                                ent = currentEntity;
                                reachDistance = distance;
                            }
                        }
                    }
                }
            }

            if (ent != null && !ent.isEntityEqual(mc.thePlayer) && (ent instanceof EntityLivingBase)) {
                return (EntityLivingBase) ent;
            }
        }

        return null;
    }

    public static float getSensitivityMultiplier() {
        float SENS = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (SENS * SENS * SENS * 8.0F) * 0.15F;
    }

    private TimerUtil rotTime = new TimerUtil();

    private float doneOut1 = 0, doneOut2 = 0;

    private float[] getEntRots(Entity ent) {
        double diffX = ent.posX - mc.thePlayer.posX;
        double diffY = (ent.posY + ent.height) - (mc.thePlayer.posY + mc.thePlayer.height) - 0.5;
        double diffZ = ent.posZ - mc.thePlayer.posZ;

        float rotYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0f;
        float rotPitch = (float) (Math.atan2(diffY, mc.thePlayer.getDistanceToEntity(ent)) * 180.0D
                / Math.PI);
        float finishedYaw = mc.thePlayer.rotationYaw
                + MathHelper.wrapAngleTo180_float(rotYaw - mc.thePlayer.rotationYaw);
        float finishedPitch = mc.thePlayer.rotationPitch
                + MathHelper.wrapAngleTo180_float(rotPitch - mc.thePlayer.rotationPitch);

        if(rotTime.stopAfter(60, false)) {
            doneOut1 = finishedYaw + (float) ThreadLocalRandom.current().nextDouble(-2, 2);
            doneOut2 = finishedPitch + (float) ThreadLocalRandom.current().nextDouble(-6, 6);
            doneOut1 = RotationUtils.smoothRotation(finishedYaw, doneOut1, 360);
            doneOut2 = RotationUtils.smoothRotation(finishedPitch, doneOut2, 90);
            rotTime.reset();
            return new float[]{doneOut1, -MathHelper.clamp_float(doneOut2, -90, 90)};
        } else {
            if(rotTime.stopAfter(100, false)) return new float[]{doneOut1, -MathHelper.clamp_float(doneOut2, -90, 90)};;
            doneOut1 = finishedYaw + (float) ThreadLocalRandom.current().nextDouble(-3, 6);
            doneOut1 = RotationUtils.smoothRotation(finishedYaw, doneOut1, 360);
            return new float[]{doneOut1, -MathHelper.clamp_float(doneOut2, -90, 90)};
        }
    }

}
