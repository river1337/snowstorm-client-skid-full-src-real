package today.snowstorm.client.module.visuals;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import today.snowstorm.client.events.Render3DEvent;
import today.snowstorm.client.utils.render.RenderUtils;
import today.snowstorm.api.events.EventType;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.settings.BooleanSetting;

import static org.lwjgl.opengl.GL11.*;

public class StorageESP extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    private BooleanSetting enderChests = new BooleanSetting("Ender Chests", false), colorMatch = new BooleanSetting("Color Match", false);

    public StorageESP() {
        super("StorageESP", "Track Storage Containers", ModuleType.WORLD, Keyboard.KEY_9);
        addSetting(enderChests, colorMatch);
    }

    @EventHandler
    public Listener<Render3DEvent> ellenisadorable = e -> {
        if (e.getType() != EventType.PRE) return;

        for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest) {
                TileEntityLockable storage = (TileEntityLockable) tileEntity;
                BlockPos chestLocation = storage.getPos();

                GlStateManager.pushMatrix();
                GlStateManager.pushAttrib();

                GlStateManager.depthMask(false);

                GL11.glClearStencil(0);
                GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
                GL11.glEnable(GL11.GL_STENCIL_TEST);
                GL11.glStencilFunc(GL11.GL_ALWAYS, 1, -1);
                GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, -1099998.0f);

                // Enable Alias
                glEnable(GL_LINE_SMOOTH);
                glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

                // Horizontals
                if (colorMatch.isEnabled()) {
                    GlStateManager.color(0.5f, 0.3f, 0, 0.5f);
                } else {
                    GlStateManager.color(1, 1, 1, 0.5f);
                }
                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ(), chestLocation.getX(), chestLocation.getY(), chestLocation.getZ());
                RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ(), chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ());

                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX(), chestLocation.getY(), chestLocation.getZ() + 1);
                RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ() + 1);


                // Verticals
                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ() + 1);
                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ(), chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ());

                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ(), chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ());
                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ() + 1);

                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ());
                RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ() + 1, chestLocation.getX(), chestLocation.getY(), chestLocation.getZ());
                RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ());
                RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ());


                //RenderUtils.drawColoredBox(chestLocation.getX() - 0.0001, chestLocation.getY() - 0.0001, chestLocation.getZ() - 0.0001, chestLocation.getX() + 1.0001, chestLocation.getY() + 1.0001, chestLocation.getZ() + 1.0001, new Color(throughWallsRed.getValueAsInt(), throughWallsGreen.getValueAsInt(), throughWallsBlue.getValueAsInt(), throughWallsAlpha.getValueAsInt()).getRGB());
                GL11.glDisable(32823);
                GL11.glPolygonOffset(1.0f, 1099998.0f);

                GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, -1);
                GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                //RenderUtils.drawColoredBox(chestLocation.getX() - 0.0001, chestLocation.getY() - 0.0001, chestLocation.getZ() - 0.0001, chestLocation.getX() + 1.0001, chestLocation.getY() + 1.0001, chestLocation.getZ() + 1.0001, 0x00000000);
                //RenderUtils.drawColoredBox(chestLocation.getX() - 0.0001, chestLocation.getY() - 0.0001, chestLocation.getZ() - 0.0001, chestLocation.getX() + 1.0001, chestLocation.getY() + 1.0001, chestLocation.getZ() + 1.0001, new Color(normalRed.getValueAsInt(), normalGreen.getValueAsInt(), normalBlue.getValueAsInt(), normalAlpha.getValueAsInt()).getRGB());

                // Disable Alias
                glDisable(GL_LINE_SMOOTH);
                glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);

                GlStateManager.depthMask(true);

                GlStateManager.popAttrib();
                GlStateManager.popMatrix();

            }
        }
        if (enderChests.isEnabled()) {
            for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
                if (tileEntity instanceof TileEntityEnderChest) {
                    TileEntityEnderChest storage = (TileEntityEnderChest) tileEntity;
                    BlockPos chestLocation = storage.getPos();

                    GlStateManager.pushMatrix();
                    GlStateManager.pushAttrib();

                    GlStateManager.depthMask(false);

                    GL11.glClearStencil(0);
                    GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
                    GL11.glEnable(GL11.GL_STENCIL_TEST);
                    GL11.glStencilFunc(GL11.GL_ALWAYS, 1, -1);
                    GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
                    GL11.glEnable(32823);
                    GL11.glPolygonOffset(1.0f, -1099998.0f);

                    // Horizontals
                    if (colorMatch.isEnabled()) {
                        GlStateManager.color(0.5f, 0.3f, 0.5f, 0.5f);
                    } else {
                        GlStateManager.color(1, 1, 1, 0.5f);
                    }
                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ(), chestLocation.getX(), chestLocation.getY(), chestLocation.getZ());
                    RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ(), chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ());

                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX(), chestLocation.getY(), chestLocation.getZ() + 1);
                    RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ() + 1);


                    // Verticals
                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ() + 1);
                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ(), chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ());

                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ(), chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ());
                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ() + 1);

                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX(), chestLocation.getY() + 1, chestLocation.getZ());
                    RenderUtils.drawLine(chestLocation.getX(), chestLocation.getY(), chestLocation.getZ() + 1, chestLocation.getX(), chestLocation.getY(), chestLocation.getZ());
                    RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY(), chestLocation.getZ());
                    RenderUtils.drawLine(chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ() + 1, chestLocation.getX() + 1, chestLocation.getY() + 1, chestLocation.getZ());


                    //RenderUtils.drawColoredBox(chestLocation.getX() - 0.0001, chestLocation.getY() - 0.0001, chestLocation.getZ() - 0.0001, chestLocation.getX() + 1.0001, chestLocation.getY() + 1.0001, chestLocation.getZ() + 1.0001, new Color(throughWallsRed.getValueAsInt(), throughWallsGreen.getValueAsInt(), throughWallsBlue.getValueAsInt(), throughWallsAlpha.getValueAsInt()).getRGB());
                    GL11.glDisable(32823);
                    GL11.glPolygonOffset(1.0f, 1099998.0f);

                    GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, -1);
                    GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
                    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                    //RenderUtils.drawColoredBox(chestLocation.getX() - 0.0001, chestLocation.getY() - 0.0001, chestLocation.getZ() - 0.0001, chestLocation.getX() + 1.0001, chestLocation.getY() + 1.0001, chestLocation.getZ() + 1.0001, 0x00000000);
                    //RenderUtils.drawColoredBox(chestLocation.getX() - 0.0001, chestLocation.getY() - 0.0001, chestLocation.getZ() - 0.0001, chestLocation.getX() + 1.0001, chestLocation.getY() + 1.0001, chestLocation.getZ() + 1.0001, new Color(normalRed.getValueAsInt(), normalGreen.getValueAsInt(), normalBlue.getValueAsInt(), normalAlpha.getValueAsInt()).getRGB());

                    GlStateManager.depthMask(true);

                    GlStateManager.popAttrib();
                    GlStateManager.popMatrix();

                }
            }
        }

    };
}
