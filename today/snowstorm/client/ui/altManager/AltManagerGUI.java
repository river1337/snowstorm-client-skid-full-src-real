package today.snowstorm.client.ui.altManager;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;
import today.snowstorm.client.ui.altManager.components.AltBox;
import today.snowstorm.client.ui.altManager.components.AltScreen;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.client.utils.other.TimerUtil;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.ui.altManager.components.AltButton;
import today.snowstorm.client.ui.altManager.pages.AddPremiumAlt;
import today.snowstorm.client.ui.altManager.pages.DirectLogin;
import today.snowstorm.client.ui.mainmenu.MainMenu;
import today.snowstorm.client.utils.Wrapper;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class AltManagerGUI extends GuiScreen {
    private int buttonWidth, buttonHeight, buttonPosX, buttonPosY;

    private AltButton premAlt, directLogin, crackedAlt, deleteAlt, checkAlt, backPage;

    private CopyOnWriteArrayList<AltButton> buttonList = new CopyOnWriteArrayList<>();
    private static int xOffset = 0;

    public static Session auth;

    private AltScreen altScreen;

    private boolean altSelected = false;

    private boolean loadedAltScreen = false;


    public static CopyOnWriteArrayList<AltBox> boxList = new CopyOnWriteArrayList<>();

    @Override
    public void initGui() {
        boxList.clear();

        buttonWidth = 100;
        buttonHeight = 20;
        buttonPosX = (width / 2);
        buttonPosY = (height) - (buttonHeight / 2) - 10;

        altScreen = new AltScreen(0, 50, width, height - 58, boxList);

        premAlt = new AltButton("Add Premium Alt", buttonPosX - buttonWidth / 2 - 105, buttonPosY - 30, buttonWidth, buttonHeight, new Color(10, 10, 10));
        directLogin = new AltButton("Direct Login", buttonPosX - buttonWidth / 2 - 105, buttonPosY - 4, buttonWidth, buttonHeight, new Color(10, 10, 10));


        checkAlt = new AltButton("Login", buttonPosX - buttonWidth / 2, buttonPosY - 30, buttonWidth, buttonHeight, new Color(10, 10, 10), false);
        deleteAlt = new AltButton("Delete Alt", buttonPosX - buttonWidth / 2, buttonPosY - 4, buttonWidth, buttonHeight, new Color(10, 10, 10), false);

        crackedAlt = new AltButton("Add Cracked Alt", buttonPosX - buttonWidth / 2 + 105, buttonPosY - 30, buttonWidth, buttonHeight, new Color(10, 10, 10));
        backPage = new AltButton("Go Back", buttonPosX - buttonWidth / 2 + 105, buttonPosY - 4, buttonWidth, buttonHeight, new Color(10, 10, 10));

        buttonList.addAll(Arrays.asList(premAlt, directLogin, checkAlt, deleteAlt, crackedAlt, backPage));

        loadedAltScreen = false;

        Snowstorm.INSTANCE.getAltHandler().getAlts();
    }

    private TimerUtil timer = new TimerUtil();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // background
        RenderUtils.drawRect(0, 0, width, height, new Color(20, 20, 20).getRGB());

        if(timer.stopAfter(10, false)) {
            boxList.clear();
            timer.reset();
        }

        // top text + vertical rule
        RenderUtils.drawRect(0, 0, width, 58, new Color(15, 15, 15).getRGB());
        FontUtil.poppinsFat72.drawString("Alt Manager", (width / 2) - (FontUtil.poppinsFat72.getStringWidth("Alt Manager") / 2), 10, -1);
        FontUtil.poppinsNormal24.drawString("You have " + Snowstorm.INSTANCE.getAltHandler().altList.size() + " alts.", (width / 2) - (FontUtil.poppinsNormal24.getStringWidth("You have x alts.") / 2), 38, -1);

        // Alt text
        String mcUser = "Alt: " + mc.getSession().getUsername();
        FontUtil.poppinsNormal16.drawSmoothString(mcUser, 3, 3, -1);

        RenderUtils.drawRect(0, 58, width, 1, new Color(30, 30, 30).getRGB());

        // Draw Alt Screen
        altScreen.drawScreen(mouseX, mouseY, partialTicks);

        // bottom buttons + vertical rule
        RenderUtils.drawRect(0, height - 58, width, height + 58, new Color(15, 15, 15).getRGB());
        RenderUtils.drawRect(0, height - 58, width, 1, new Color(30, 30, 30).getRGB());

        // render bludclart buttons
        premAlt.action = () -> {
            mc.displayGuiScreen(new AddPremiumAlt());
        };

        premAlt.render(mouseX, mouseY);

        directLogin.action = () -> mc.displayGuiScreen(new DirectLogin());
        directLogin.render(mouseX, mouseY);
        crackedAlt.render(mouseX, mouseY);

        // Additional changes to these buttons
        deleteAlt.render(mouseX, mouseY);
        checkAlt.render(mouseX, mouseY);

        if(altSelected) {
            deleteAlt.setEnabled(true);
            checkAlt.setEnabled(true);
        } else {
            deleteAlt.setEnabled(false);
            checkAlt.setEnabled(false);
        }

        backPage.action = () -> mc.displayGuiScreen(new MainMenu());
        backPage.render(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        for(AltBox altBox : boxList) {
            altBox.updateScreen();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        for (AltButton button1 : buttonList) {
            if(button1.isEnabled()) {
                if(HoverUtils.isHovering(button1.getX(), button1.getY(), button1.getWidth(), button1.getHeight(), mouseX, mouseY)) {
                    button1.action.run();
                }
            }
        }

        if(button == 0) {
            for (AltBox bocks : boxList) {
                if(HoverUtils.isHovering(bocks.getX(), bocks.getY(), bocks.getWidth(), bocks.getHeight(), mouseX, mouseY)) {
                    //prevBox.isSelected = false;

                    bocks.isSelected = true;
                    altSelected = true;

                    checkAlt.action = () -> {
                        attemptAuth(bocks);
                        //prevBox = bocks;
                        altSelected = false;
                    };

                    //if(prevBox != null) {
                    //    System.out.println(prevBox.getUsername());
                    //}

                    break;
                } else {
                    //prevBox = null;
                    bocks.isSelected = false;
                    altSelected = false;
                    checkAlt.action = null;
                }
            }
        }
    }

    private void attemptAuth(AltBox bocks) {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {
            MicrosoftAuthResult result = authenticator.loginWithCredentials(bocks.getEmail(), bocks.getPassword());
            MinecraftProfile profile = result.getProfile();
            AltManagerGUI.auth = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");
        } catch (MicrosoftAuthenticationException e) {
            e.printStackTrace();
            return;
        }
        if(AltManagerGUI.auth != null) {
            Wrapper.getMc().session = AltManagerGUI.auth;
        }
        return;
    }

}
