package today.snowstorm.client.ui.mainmenu;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import store.snowstorm.UserAccount;
import store.snowstorm.account.AccountInfo;
import store.snowstorm.network.Authentication;
import today.snowstorm.client.ui.altManager.AltManagerGUI;
import today.snowstorm.client.ui.mainmenu.components.MenuButton;
import today.snowstorm.client.ui.mainmenu.components.bubbles.Bubbles;
import today.snowstorm.client.utils.animations.impl.ElasticAnimation;
import today.snowstorm.client.utils.other.TimerUtil;
import today.snowstorm.client.utils.render.RenderUtils;
import today.snowstorm.client.utils.render.shaders.impl.Blur;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.utils.Wrapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MainMenu extends GuiScreen {

    private CopyOnWriteArrayList<MenuButton> buttons = new CopyOnWriteArrayList<>();

    private MenuButton singleplayer, multiplayer, options, quit;

    private int buttonWidth, buttonHeight, posX, posY;

    private static int offY;

    private ElasticAnimation loadAnimation;

    //private BubbleThread bubbleThread;

    private List<Bubbles> bubbleList = new ArrayList<>();
    private List<Bubbles> deadBubs = new ArrayList<>();

    private ScaledResolution sr;

    private boolean whaddafaq = false;

    @Override
    public void initGui() {

        whaddafaq = false;

        //bubbleThread = new BubbleThread(this, 0, 0, width, height, 10);
        sr = new ScaledResolution(mc);

        loadAnimation = new ElasticAnimation(1200, 0.3D, 4f, 0.8f, false);

        buttonWidth = 120;
        buttonHeight = 20;

        offY = 30;

        posX = (int) ((width / 2) - buttonWidth / 2);
        posY = (int) (height / 2) - 40;

        singleplayer = new MenuButton("Solo Play", posX, posY, buttonWidth, buttonHeight, new Color(50, 50, 50));
        singleplayer.action = new Runnable() {
            @Override
            public void run() {
                mc.displayGuiScreen(new GuiSelectWorld(new MainMenu()));
            }
        };

        multiplayer = new MenuButton("Online Play", posX, posY + (offY), buttonWidth, buttonHeight, new Color(50, 50, 50));
        multiplayer.action = new Runnable() {
            @Override
            public void run() {
                mc.displayGuiScreen(new GuiMultiplayer(new MainMenu()));
            }
        };

        options = new MenuButton("Alt Manager", posX, (int) posY + (offY * 2), buttonWidth, buttonHeight, new Color(50, 50, 50));
        options.action = new Runnable() {
            @Override
            public void run() {
                //mc.displayGuiScreen(new GuiOptions(new MainMenu(), mc.gameSettings));
                mc.displayGuiScreen(new AltManagerGUI());
            }
        };

        //options = new MenuButton("Options", posX, (int) posY + (offY * 2), buttonWidth, buttonHeight, new Color(50, 50, 50));
        //options.action = new Runnable() {
        //    @Override
        //    public void run() {
        //        //mc.displayGuiScreen(new GuiOptions(new MainMenu(), mc.gameSettings));
        //        mc.displayGuiScreen(new AltManagerGUI());
        //    }
        //};

        quit = new MenuButton("Quit", posX, (int) posY + (offY * 3), buttonWidth, buttonHeight, new Color(50, 50, 50));
        quit.action = new Runnable() {
            @Override
            public void run() {
                Wrapper.getMc().shutdown();
            }
        };

        buttons.addAll(Arrays.asList(singleplayer, multiplayer, options, quit));
    }

    private int i = 0;
    private TimerUtil timer = new TimerUtil(), timer2 = new TimerUtil();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, this.width, this.height, new Color(10, 10, 10).getRGB());

        GlStateManager.pushMatrix();

        for(Bubbles bubble : bubbleList) {
            bubble.render();
        }

        Blur.blurArea(0, 0, width, height);

        Gui.drawRect(0, 0, this.width, this.height, new Color(10, 10, 10, 40).getRGB());

        String username = /*ChatFormatting.GREEN + */"API Authenticated"/* + ChatFormatting.RESET */+ " " + UserAccount.account.getName() + " | UID: " + UserAccount.account.getUid();
        int textAlign = sr.getScaledWidth() - fontRendererObj.getStringWidth(username) - 2;

        String mcUser = "Alt: " + mc.getSession().getUsername();
        FontUtil.poppinsNormal16.drawSmoothString(mcUser, 3, 3, -1);

        FontUtil.poppinsNormal16.drawSmoothString(username, 3, this.height - 10, -1);

        RenderUtils.scale(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, (float) loadAnimation.getOutput() + .7f, () -> {
            FontUtil.poppinsFat72.drawSmoothString("Snowstorm.WTF", (this.width / 2) - (FontUtil.poppinsFat72.getStringWidth("Snowstorm.WTF") / 2), (height / 2) - 75, -1);


            singleplayer.render(mouseX, mouseY);
            multiplayer.render(mouseX, mouseY);
            options.render(mouseX, mouseY);
            quit.render(mouseX, mouseY);
        });

        int counter = 0;

        if(!whaddafaq) {
            counter = 1;
            whaddafaq = true;
        } else {
            counter = 10;
        }

        if(sr.getScaledWidth() < 600) {
            for (int i = 0; bubbleList.size() < counter; i++) {
                //bubbleList.add(new Bubbles(width, height, 5, new Color(202, 202, 202)));
                if(whaddafaq) {
                    Bubbles bub = new Bubbles(ThreadLocalRandom.current().nextInt(-10, sr.getScaledWidth()), ThreadLocalRandom.current().nextInt(-10, sr.getScaledHeight()), (float) ThreadLocalRandom.current().nextDouble(8, 25), new Color(ThreadLocalRandom.current().nextInt(100, 210), ThreadLocalRandom.current().nextInt(100, 230), ThreadLocalRandom.current().nextInt(220, 250), 205));
                    bubbleList.add(bub);
                } else {
                    Bubbles bub = new Bubbles(ThreadLocalRandom.current().nextInt(-10, sr.getScaledWidth()), ThreadLocalRandom.current().nextInt(-10, sr.getScaledHeight()), (float) ThreadLocalRandom.current().nextDouble(8, 25), new Color(ThreadLocalRandom.current().nextInt(100, 210), ThreadLocalRandom.current().nextInt(100, 230), ThreadLocalRandom.current().nextInt(220, 250), 205));
                    bubbleList.add(bub);
                }
            }
        } else {
            counter = 30;
            for (int i = 0; bubbleList.size() < counter; i++) {
                Bubbles bub = new Bubbles(ThreadLocalRandom.current().nextInt(-10, sr.getScaledWidth()), ThreadLocalRandom.current().nextInt(-10, sr.getScaledHeight()), (float) ThreadLocalRandom.current().nextDouble(8, 25), new Color(ThreadLocalRandom.current().nextInt(100, 210), ThreadLocalRandom.current().nextInt(100, 230), ThreadLocalRandom.current().nextInt(220, 250), 205));
                bubbleList.add(bub);
            }
        }

        if(timer.stopAfter(8, false)) {
            for (Bubbles bubble : bubbleList) {

                if (bubble.radius > 3f) {
                    bubble.update();
                } else {
                    deadBubs.add(bubble);
                }
            }
        }

        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        sr = new ScaledResolution(mc);

        if(timer.stopAfter(10, false)) {
            for (Bubbles bubbles : deadBubs) {
                bubbleList.remove(bubbles);
            }
            timer.reset();
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        for(MenuButton b : buttons) {
            if(isHovering(b.getPosX(), b.getPosY(), b.getWidth(), b.getHeight(), mouseX, mouseY)) {
                if(button == 0) {
                    b.action.run();
                }
            }
        }
    }

    // Security
    private void func_54368adv(AccountInfo accountInfo, String hwid) {
        Authentication var3 = new Authentication();

        try {
            if(!var3.verifHWID(hwid, accountInfo.getUid())) {
                System.out.println("[Snowstorm PROTECTION] We could not verify account ownership. Please contact a client developer.");
                mc.shutdown();
            } else {
                if(!accountInfo.getHwid().equals("hellohwid")) {
                    System.out.println("[Snowstorm PROTECTION] We could not verify account ownership. Please contact a client developer.");
                    mc.shutdown();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isHovering(double x, double y, double width, double height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
