package today.snowstorm.api.ui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.utils.Wrapper;

import java.awt.*;

public class SplashScreen extends GuiScreen {

    private String splashMsg = "Welcome To Snowstorm";
    private String splashMsgTwo = "Please Wait...";
    private int titlePosX;
    private int titlePosY;

    private int subPosX;
    private int subPosY;

    @Override
    public void initGui() {
        titlePosX =  (width / 2) - (Wrapper.getMc().fontRendererObj.getStringWidth(splashMsg) / 2);
        titlePosY = (height / 2) - 4;

        subPosX = (width / 2) - (Wrapper.getMc().fontRendererObj.getStringWidth(splashMsgTwo) / 2);
        subPosY = (height / 2) + 8;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(Snowstorm.INSTANCE.hasLoaded) {
            mc.displayGuiScreen(new LogInScreen());
        }

        // Draw black background
        Gui.drawRect(0, 0, width, height, new Color(0,0,0).getRGB());

        FontUtil.poppinsNormal24.drawSmoothString(splashMsg, titlePosX, titlePosY, -1);

        if(!Snowstorm.INSTANCE.hasLoaded) {
            Snowstorm.INSTANCE.run.run();
        }
        System.out.println("FUCK YOU");
    }

}
