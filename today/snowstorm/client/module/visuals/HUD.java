package today.snowstorm.client.module.visuals;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import today.snowstorm.client.events.OverlayEvent;
import today.snowstorm.client.events.TickEvent;
import today.snowstorm.client.utils.other.DisplayUtils;
import today.snowstorm.client.utils.other.TimerUtil;
import today.snowstorm.client.utils.player.MoveUtil;
import today.snowstorm.client.utils.render.shaders.impl.Blur;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.utils.Wrapper;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class HUD extends Module {
    Minecraft mc = Minecraft.getMinecraft();

    public HUD() {
        super("HUD", "The overlay for Snowstorm", ModuleType.VISUALS, Keyboard.KEY_H);
        this.setEnabled(true);
        this.setHidden(true);
    }

    @Override
    public void onDisable() {
        this.setEnabled(true);
    }


    private int lastFps = 0, currentFps = 0, fpsfps = 0;
    private TimerUtil timer = new TimerUtil(), timer2 = new TimerUtil(), timer3 = new TimerUtil();

    String fps;

    // Every tick, check timer.
    @EventHandler
    public Listener<TickEvent> onTickMeDaddy = e -> {
        // If has been 800ms then set lastFps
        if(timer.stopAfter(3000, false)) {
            lastFps = Minecraft.getDebugFPS();
            fpsfps = Minecraft.getDebugFPS();
            timer.reset();
        }

        // If has been a second reset the timer.
        if(timer2.stopAfter(2000, false)) {
            currentFps = Minecraft.getDebugFPS();
            timer2.reset();
        }

        // Wowza
        if(timer3.stopAfter(1, true)) {
            doFps(lastFps, currentFps);
        }
    };

    private TimerUtil timer4 = new TimerUtil();

    private void doFps(int start, int end) {
        int increment = (start > end) ? -1 : 1;
        while (start != end) {
            start += increment;
            if(timer4.stopAfter(1, true)) {
                fpsfps += increment;
            }
        }
    }

    @EventHandler
    public final Listener<OverlayEvent> overlayListener = e -> {
        GlStateManager.pushMatrix();

        // Draw watermark
        String snowstorm = "Snowstorm.today ";
        String version = "b1.0 | ";
        String serverName = "Singleplayer" + " | ";
        fps = DisplayUtils.getFps() + " FPS";
        //String fps = Minecraft.getDebugFPS() + " FPS";

        if(!mc.isSingleplayer()) {
            serverName = mc.getCurrentServerData().serverIP + " | ";
        }


        double barWidth = FontUtil.poppinsNormal18.getStringWidth(snowstorm + version + serverName + fps);
        float barHeight = 15;
        int padding = 3; // add 3 pixels of padding on either side

        RenderUtils.drawRect(6 - padding, 4, (float) barWidth + 2 * padding, -1.5f, new Color(10, 85, 150).getRGB());
        // BlurUtil.blurAreaRounded(6 - padding, 4, (float) barWidth + 2 * padding, barHeight, 3);
        RenderUtils.drawRect(6 - padding, 4, (float) barWidth + 2 * padding, barHeight, new Color(30, 30, 30, 140).getRGB());
        Blur.blurArea(6 - padding, 4, (float) barWidth + 2 * padding, barHeight);

        int SnowstormX = (int) (6 + barWidth / 2 - FontUtil.poppinsNormal18.getStringWidth(snowstorm + version + serverName + fps) / 2);
        FontUtil.poppinsNormal18.drawString(snowstorm, SnowstormX, 7, new Color(255, 255, 255).getRGB());

        int versionX = (int) (SnowstormX + FontUtil.poppinsNormal18.getStringWidth(snowstorm));
        FontUtil.poppinsNormal18.drawString(version, versionX, 7, new Color(255, 255, 255).getRGB());

        int serverNameX = (int) (versionX + FontUtil.poppinsNormal18.getStringWidth(version));
        FontUtil.poppinsNormal18.drawString(serverName, serverNameX, 7, new Color(255, 255, 255).getRGB());

        int fpsX = (int) (serverNameX + FontUtil.poppinsNormal18.getStringWidth(serverName));
        //FontUtil.poppinsNormal18.drawString(fps, fpsX, 7, new Color(255, 255, 255).getRGB());

        FontUtil.poppinsNormal18.drawSmoothString(fps, 5 + FontUtil.poppinsNormal18.getStringWidth(snowstorm + version + serverName), 7, new Color(255, 255, 255).getRGB());

        /*
        RenderUtils.drawRoundedRectangle(2, 2, (float) FontUtil.poppinsNormal24.getStringWidth("Snowstorm.WTF b1.0") + 5, FontUtil.poppinsNormal24.getHeight() + 3, 3, new Color(10, 10, 10));

        FontUtil.poppinsNormal24.drawSmoothString("Snowstorm.WTF b1.0", 5, 4, -1);
         */

        // Speed indicator
        if(Wrapper.getMc().currentScreen instanceof GuiChat) {
            FontUtil.poppinsNormal16.drawSmoothString("Speed: " + MoveUtil.getBlocksSpeed(), 3, (int) (e.getScaledResolution().getScaledHeight() - 25), -1);
        } else {
            FontUtil.poppinsNormal16.drawSmoothString("Speed: " + MoveUtil.getBlocksSpeed(), 3, (int) (e.getScaledResolution().getScaledHeight() - 10), -1);
        }

        // Arraylist
        float xOffset = 2;
        AtomicReference<Float> yOffset = new AtomicReference<>(3F);

        Snowstorm.INSTANCE.getModuleManager().getSortedModules().forEach(module -> {
            int x = (int) (e.getScaledResolution().getScaledWidth() - FontUtil.poppinsNormal18.getStringWidth(module.getName()) - xOffset) - 3;
            int y = yOffset.get().intValue() - 3;
            int width = e.getScaledResolution().getScaledWidth();
            int height = (int) (FontUtil.poppinsNormal18.getHeight()) + 3;
            int color = new Color(30, 30, 30, 200).getRGB();  // or any other desired color
            if(module.isEnabled() && !module.isHidden()) {
                RenderUtils.drawRect(x, y, width, height, color);
                FontUtil.poppinsNormal18.drawSmoothString(module.getName(), (int) (e.getScaledResolution().getScaledWidth() - FontUtil.poppinsNormal18.getStringWidth(module.getName()) - xOffset), yOffset.get().intValue() - 1, -1);
                yOffset.updateAndGet(v -> v + FontUtil.poppinsNormal18.getHeight() + 3);
                Blur.blurArea(x, y, width, height);
            }
        });

        GlStateManager.popMatrix();
    };

    public static void animateValue(int start, int end, long duration) throws InterruptedException {
        long startTime = System.nanoTime();
        long endTime = startTime + (duration * 1000);

        while (System.nanoTime() < endTime) {
            long elapsedTime = System.nanoTime() - startTime;
            double t = (double) elapsedTime / (duration * 1000);
            int currentValue = (int) (start * (1 - t) + end * t);
        }
    }

}
