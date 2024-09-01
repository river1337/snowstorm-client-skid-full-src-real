package today.snowstorm.client.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MoveUtil {

    protected static Minecraft mc = Minecraft.getMinecraft();

    // Is the player moving?
    public static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }

    // Set players motion (and strafe move)
    public static void setMotion(float speed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * speed * 0.85;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed * 0.85;
    }

    public static boolean isStrafing(){
        return mc.thePlayer.moveStrafing != 0;
    }

    // Set speed fast
    public static void setSpeed(double speed) {
        mc.thePlayer.motionX = - Math.sin(getDirection()) * speed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    // Gets speed (Vanilla value (close enough): 0.30f)
    public static float getBlocksSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }

    // Get the players absolute base speed + factor in speed potions.
    public static double getBaseSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
        }

        return baseSpeed;
    }

    // Checks the "height" under the player to see if a block is there.
    // Best way to use: isOnGround(0.001);
    public static boolean isOnGround(double height) {
        if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMovingForward() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward >= 1);
    }

    // Get direction from yaw.
    public static double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if (mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else
        if (mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if (mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

}
