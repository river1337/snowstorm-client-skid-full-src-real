package today.snowstorm.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import today.snowstorm.api.events.Event;

public class Render3DEvent extends Event {

    public float renderPartialTicks;
    private int x, y, z, x2, y2, z2;
    private boolean offset;

    public Render3DEvent(float partialTicks, int x, int y, int z) {
        this.renderPartialTicks = partialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
        x2= x;
        y2 = y;
        z2 = z;
    }

    public boolean isOffset() {
        return offset;
    }

    public void offset(int renderOffsets) {
        double x3 = Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y3 = Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z3 = Minecraft.getMinecraft().getRenderManager().renderPosZ;
        if (renderOffsets < 0) {
            GlStateManager.translate(-x3, -y3, -z3);
            x -= x3;
            y -= y3;
            z -= z3;
            offset = true;
        } else if (renderOffsets > 0) {
            GlStateManager.translate(x3, y3, z3);
            x += x3;
            y += y3;
            z += z3;
            offset = true;
        }
    }

    public void reset() {
        x = x2;
        y = y2;
        z = z2;
        offset = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

}
