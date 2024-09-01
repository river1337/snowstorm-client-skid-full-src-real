package today.snowstorm.client.ui.altManager.pages;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import today.snowstorm.client.ui.altManager.AltManagerGUI;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.ui.altManager.components.AltButton;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.Sys.getClipboard;

public class DirectLogin extends GuiScreen {

    private GuiTextField username;
    private GuiTextField password;

    private int inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight;

    private AltButton pasteClipBoard, loginButton, backButton;

    private float contentContainerX, contentContainerY, contentContainerWidth, contentContainerHeight;
    private float buttonX, buttonY, buttonWidth, buttonHeight;

    private CopyOnWriteArrayList<AltButton> buttonList = new CopyOnWriteArrayList();
    @Override
    public void initGui() {

        /////////
        contentContainerHeight = (height / 1.4f);
        contentContainerWidth = (width / 1.3f);
        contentContainerX = (width / 2) - (contentContainerWidth / 2);
        contentContainerY = (height / 2) - (contentContainerHeight / 2);

        ////////
        inputBoxWidth = 100;
        inputBoxHeight = 12;
        inputBoxX = (width / 2) - (inputBoxWidth / 2);
        inputBoxY = (height / 3);

        username = new GuiTextField(0, mc.fontRendererObj, inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight);
        password = new GuiTextField(1, mc.fontRendererObj, inputBoxX, inputBoxY + 32, inputBoxWidth, inputBoxHeight);

        ///////////////
        buttonWidth = 100;
        buttonHeight = 20;
        buttonX = (width / 2) - (buttonWidth / 2);
        buttonY = (contentContainerY * 3.5f);

        pasteClipBoard = new AltButton("Paste Clipboard", buttonX - 105, buttonY, buttonWidth, buttonHeight, new Color(10, 10, 10));
        loginButton = new AltButton("Login", buttonX, buttonY, buttonWidth, buttonHeight, new Color(10, 10, 10));
        backButton = new AltButton("Back", buttonX + 105, buttonY, buttonWidth, buttonHeight, new Color(10, 10, 10));

        // Add to list for clickevent
        buttonList.addAll(Arrays.asList(pasteClipBoard, loginButton, backButton));
    }

    private static String status = "";

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw BG
        RenderUtils.drawRect(0, 0, width, height, new Color(10, 10, 10).getRGB());

        // draw rounded content bg
        RenderUtils.drawRoundedRect(contentContainerX, contentContainerY, contentContainerWidth, contentContainerHeight, 15f, new Color(19,  19,  19));

        String heroTitle = "Direct Login";
        double heroLength = FontUtil.poppinsFat72.getStringWidth(heroTitle);
        FontUtil.poppinsFat72.drawString(heroTitle, (width / 2) - (heroLength / 2), (contentContainerHeight / 4) + 20, -1);


        double statusLength = FontUtil.poppinsNormal24.getStringWidth(status);
        if(!status.isEmpty()) {
            FontUtil.poppinsNormal24.drawString(status, (width / 2) - (statusLength / 2), (contentContainerHeight / 4) + 50, -1);
        }

        String labelUser = "Email:";
        double userLength = FontUtil.poppinsNormal18.getStringWidth(labelUser);

        FontUtil.poppinsNormal18.drawString(labelUser, (width / 2) - (userLength / 2), (height / 3) - 15, -1);
        this.username.drawTextBox();

        String labelPass = "Password:";
        double passLength = FontUtil.poppinsNormal18.getStringWidth(labelPass);

        FontUtil.poppinsNormal18.drawString(labelPass, (width / 2) - (passLength / 2), (height / 3) + 18, -1);
        this.password.drawTextBox();

        if(this.username.isFocused()) {
            this.username.updateCursorCounter();
        } else if(this.password.isFocused()) {
            this.password.updateCursorCounter();
        }

        // Paste Clipboard
        pasteClipBoard.action = new Runnable() {
            @Override
            public void run() {
                String cont = getClipboard();
                if (cont == null || cont.length() < 3) return;
                String[] splice = cont.split(":", 2);
                if (splice.length < 2) return;
                username.setText(splice[0]);
                password.setText(splice[1]);
            }
        };
        pasteClipBoard.render(mouseX, mouseY);

        loginButton.action = new Runnable() {
            @Override
            public void run() {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                try {
                    MicrosoftAuthResult result = authenticator.loginWithCredentials(username.getText(), password.getText());
                    MinecraftProfile profile = result.getProfile();
                    AltManagerGUI.auth = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");

                    status = "Logged In As: " + AltManagerGUI.auth.getUsername();

                    username.setText("");
                    password.setText("");

                } catch (MicrosoftAuthenticationException e) {
                    //e.printStackTrace();
                    if(e.getMessage().contains("Invalid credentials or tokens")) {
                        status = "Invalid credentials!";
                        //return;

                    } else {
                        status = "Failed to log in. Slow down and try again later.";
                        //return;
                    }
                }

                if(AltManagerGUI.auth != null) {
                    mc.session = AltManagerGUI.auth;
                }
            }
        };
        loginButton.render(mouseX, mouseY);

        backButton.action = () -> mc.displayGuiScreen(new AltManagerGUI());
        backButton.render(mouseX, mouseY);
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

        if(HoverUtils.isHovering(this.inputBoxX, this.inputBoxY, this.inputBoxWidth, this.inputBoxHeight, mouseX, mouseY)) {
            if (button == 0) {
                this.username.setFocused(true);
                this.password.setFocused(false);
            }
        }

        if(HoverUtils.isHovering(this.inputBoxX, this.inputBoxY + 32, this.inputBoxWidth, this.inputBoxHeight, mouseX, mouseY)) {
            if (button == 0) {
                this.username.setFocused(false);
                this.password.setFocused(true);
            }
        }
    }

    @Override
    protected void keyTyped(char cha, int id) {
        if(id == 1) {
            mc.displayGuiScreen(new AltManagerGUI());
        }

        if(username.isFocused()) {
            this.username.textboxKeyTyped(cha, id);
        }

        else if(password.isFocused()) {
            this.password.textboxKeyTyped(cha, id);
        }
    }

}
