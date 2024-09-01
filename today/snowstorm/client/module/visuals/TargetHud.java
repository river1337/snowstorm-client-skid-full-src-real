package today.snowstorm.client.module.visuals;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import today.snowstorm.client.events.OverlayEvent;
import today.snowstorm.client.module.combat.KillAura;
import today.snowstorm.client.module.visuals.clickgui.ClickUI;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.api.utils.font.TFontRenderer;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.settings.NumberSetting;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class TargetHud extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public ModeSetting modes = new ModeSetting("Style", "Snowstorm", "Snowstorm", "Blocky", "TMI", "Classical");

    public NumberSetting xPos = new NumberSetting("xPos", 100, 0, 1500, 30), yPos = new NumberSetting("yPos", 100, 0, 1000, 30);

    private float x, y, width, height;

    public TargetHud() {
        super("TargetHUD", "Display KillAura's target information.", ModuleType.VISUALS, Keyboard.KEY_Y);
        addSetting(modes, xPos, yPos);
    }

    @EventHandler
    public Listener<OverlayEvent> onOverlayEvent = e -> {
        if(KillAura.target != null && !KillAura.target.isDead && KillAura.target.getHealth() > 0) {
            EntityLivingBase ent = null;
            ent = KillAura.target;

            if(ent == null) {
                if (mc.currentScreen instanceof ClickUI) {
                    ent = mc.thePlayer;
                }
            }

            if(ent != null) {
                GlStateManager.pushMatrix();

                TFontRenderer fr1 = FontUtil.poppinsNormal18;

                String targetName = ent.getName();

                x = xPos.getValueAsFloat();
                y = yPos.getValueAsFloat();
                width = (float) fr1.getStringWidth(targetName) + 100;
                height = 40;

                float skinWidth = 36;
                float skinHeight = 36;

                float rectX = x - (width - skinWidth) / 2;
                float rectY = y - (height - skinHeight) / 2;

                RenderUtils.drawBorderRect((int) rectX, (int) rectY, (int) width, (int) height, new Color(10, 85, 150), new Color(30, 30, 30, 140));

                int textX = (int) (rectX + (width - fr1.getStringWidth(targetName)) / 2);
                int textY = (int) (rectY + (height - fr1.getHeight()) / 2) -7;

                fr1.drawSmoothString(targetName, textX, textY, -1);

                String health = String.format("%.1f", ent.getHealth());
                fr1.drawSmoothString(health, textX + fr1.getStringWidth(targetName) + 5, textY, -1);

                float healthBar = width * (ent.getHealth() / ent.getMaxHealth());

                // RenderUtils.drawGradientRect((int) rectX, (int) rectY + 30, (int) healthBar, 5, new Color(0, 255, 0), new Color(255, 0, 0));

                final int startColor = fadeBetween(new Color(11, 81, 201).getRGB(), new Color(99, 158, 250).getRGB(), 0);
                final int endColor = fadeBetween(new Color(99, 158, 250).getRGB(), new Color(11, 81, 201).getRGB(), 250);

                glDrawSidewaysGradientRect((int) rectX, (int) rectY + 30, (int) healthBar, 5, startColor, endColor);


                GlStateManager.popMatrix();

            }
            //RenderUtils.drawRoundedRectangle();
        }
    };

    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1) progress = 1 - progress % 1;
        return fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int startColour, int endColour, long offset) {
        return fadeBetween(startColour, endColour, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return fadeBetween(startColour, endColour, 0L);
    }


    public static int fadeTo(int startColor, int endColor, double progress) {
        double invert = 1.0 - progress;
        int r = (int) ((startColor >> 16 & 0xFF) * invert +
                (endColor >> 16 & 0xFF) * progress);
        int g = (int) ((startColor >> 8 & 0xFF) * invert +
                (endColor >> 8 & 0xFF) * progress);
        int b = (int) ((startColor & 0xFF) * invert +
                (endColor & 0xFF) * progress);
        int a = (int) ((startColor >> 24 & 0xFF) * invert +
                (endColor >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }

    public static void glDrawSidewaysGradientRect(final double x, final double y, final double width, final double height, final int startColor, final int endColor) {
        final boolean wasEnabled = GL11.glIsEnabled(GL_BLEND);

        if (!wasEnabled) {
            GL11.glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        }

        // notexture
        GL11.glDisable(GL_TEXTURE_2D);
        // smood color
        GL11.glShadeModel(GL_SMOOTH);

        // plan draw rect
        GL11.glBegin(GL_QUADS);
        {
            // start fade
            RenderUtils.setColor(startColor);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x, y + height);
            // end fade
            RenderUtils.setColor(endColor);
            GL11.glVertex2d(x + width, y + height);
            GL11.glVertex2d(x + width, y);
        }
        // draw rect
        GL11.glEnd();

        // china characters
        GL11.glShadeModel(GL_FLAT);
        // chinese text ye
        GL11.glEnable(GL_TEXTURE_2D);

        // off blend
        if(!wasEnabled) {
            GL11.glDisable(GL_BLEND);
        }

        // reset da culer
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

}
