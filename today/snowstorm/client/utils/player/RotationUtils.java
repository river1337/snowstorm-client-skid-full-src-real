package today.snowstorm.client.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.*;

public class RotationUtils {

    protected static Minecraft mc = Minecraft.getMinecraft();

    private static float customYaw, customPitch;

    public static boolean useCustomYaw = false, useCustomPitch = false;

    // Set custom yaw + pitch (useful for KA and Scaffold)
    public static void setCustomYaw(float customYaw) {
        RotationUtils.customYaw = customYaw;
        useCustomYaw = true;
        mc.thePlayer.rotationYawHead = customYaw;
    }

    public static void setCustomPitch(float customPitch) {
        RotationUtils.customPitch = customPitch;
        useCustomPitch = true;
    }

    // getters and resetters.
    public static float getCustomYaw() {
        return customYaw;
    }

    public static float getCustomPitch() {
        return customPitch;
    }

    public static void resetCustomYaw() {
        useCustomYaw = false;
    }

    public static void resetCustomPitch() {
        useCustomPitch = false;
    }

    public static Vec3i getBlockFace(final Vec3i pos) {
        // These rotations get the block we should be facing, and then passes it onto the place rotations.
        return new Vec3i(pos.getX(), pos.getY(), pos.getZ());
    }

    public static float[] getRotation(Vec3i vec3, float currentYaw, float currentPitch, float speed, EnumFacing enumFacing) {
        double xdiff = vec3.getX() - mc.thePlayer.posX;
        double zdiff = vec3.getZ() - mc.thePlayer.posZ;
        double y = vec3.getY() - mc.thePlayer.posY;

        //if(enumFacing == EnumFacing.EAST || enumFacing == EnumFacing.WEST) {
        //    zdiff += 0.6f;
        //}

        //if(enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.NORTH) {
        //    xdiff += 0.40f;
        //}

        //if(enumFacing == EnumFacing.NORTH) {
        //    xdiff += 0.1f;
        //}

        //if (enumFacing != EnumFacing.UP && enumFacing != EnumFacing.DOWN) {
        //    y += 1;
        //} else {
        //    //xdiff += 0.3;
        //    //zdiff += 0.3;
        //}
        //if (enumFacing == EnumFacing.EAST) {
        //    zdiff += 0.15;
        //}
        //if (enumFacing == EnumFacing.NORTH) {
        //    xdiff += 0.15;
        //}

        double posy = mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight() * 2 - y + 0.2f;
        double lastdis = MathHelper.sqrt_double(xdiff * xdiff + zdiff * zdiff);
        float calcYaw = (float) (Math.atan2(zdiff, xdiff) * 180.0 / Math.PI) - 90.0f;
        float calcPitch = (float) (Math.atan2(posy, lastdis) * 180.0 / Math.PI);

        if (Float.compare(calcYaw, 0.0f) < 0)
            calcPitch += 360.0f;

        float yaw = smoothRotation(currentYaw, calcYaw, 180);
        float pitch = smoothRotation(currentPitch, calcPitch, 90);

        //return new float[]{yaw % 360, pitch >= 90 ? 90 : pitch <= -90 ? -90 : pitch};
        //return new float[]{MathHelper.wrapAngleTo180_float(yaw) % 360, MathHelper.wrapAngleTo180_float(pitch) % 360};
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }


    // Get sensitivity multiplier (mouse sens)
    public static float getSensitivityMultiplier() {
        float SENS = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (SENS * SENS * SENS * 8.0F) * 0.15F;
    }

    // Smooth rotations from x to y
    public static float smoothRotation(float from, float to, float speed) {
        float f = MathHelper.wrapAngleTo180_float(to - from);

        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return from + f;
    }

}
