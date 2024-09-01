package today.snowstorm.client.ui.altManager.pages;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import today.snowstorm.client.ui.altManager.AltManagerGUI;
import today.snowstorm.client.ui.altManager.components.AltBox;
import today.snowstorm.client.utils.other.HoverUtils;
import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.ui.altManager.components.AltButton;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.Sys.getClipboard;

public class AddPremiumAlt extends GuiScreen {

    private GuiTextField username;
    private GuiTextField password;

    private int inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight;

    private AltButton pasteClipBoard, addButton, backButton;

    private float contentContainerX, contentContainerY, contentContainerWidth, contentContainerHeight;
    private float buttonX, buttonY, buttonWidth, buttonHeight;

    private CopyOnWriteArrayList<AltButton> buttonList = new CopyOnWriteArrayList();


    public static CopyOnWriteArrayList<AltBox> boxList = new CopyOnWriteArrayList<>();

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
        inputBoxY = (int) (contentContainerY * 2.3f) + 15;

        username = new GuiTextField(0, mc.fontRendererObj, inputBoxX, inputBoxY, inputBoxWidth, inputBoxHeight);
        password = new GuiTextField(1, mc.fontRendererObj, inputBoxX, (int) (contentContainerY * 3.2f) + 15, inputBoxWidth, inputBoxHeight);

        ///////////////
        buttonWidth = 100;
        buttonHeight = 20;
        buttonX = contentContainerX + (contentContainerWidth / 2) - (buttonWidth / 2);
        buttonY = (contentContainerY * 4.5f);

        pasteClipBoard = new AltButton("Paste Clipboard", buttonX - 105, buttonY, buttonWidth, buttonHeight, new Color(10, 10, 10));
        addButton = new AltButton("Add Account & Login", buttonX, buttonY, buttonWidth, buttonHeight, new Color(10, 10, 10));
        backButton = new AltButton("Back", buttonX + 105, buttonY, buttonWidth, buttonHeight, new Color(10, 10, 10));

        // Add to list for clickevent
        buttonList.addAll(Arrays.asList(pasteClipBoard, addButton, backButton));
    }

    private static String status = "";

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw BG
        RenderUtils.drawRect(0, 0, width, height, new Color(10, 10, 10).getRGB());

        // draw rounded content bg
        RenderUtils.drawRoundedRect(contentContainerX, contentContainerY, contentContainerWidth, contentContainerHeight, 15f, new Color(19,  19,  19));

        String heroTitle = "Add Premium Account";
        double heroLength = FontUtil.poppinsFat72.getStringWidth(heroTitle);
        FontUtil.poppinsFat72.drawString(heroTitle, contentContainerX + (contentContainerWidth / 2) - (heroLength / 2), contentContainerY  + 10, -1);


        double statusLength = FontUtil.poppinsNormal24.getStringWidth(status);
        if(!status.isEmpty()) {
            FontUtil.poppinsNormal24.drawString(status, (width / 2) - (statusLength / 2), contentContainerY + 50, -1);
        }

        String labelUser = "Email:";
        double userLength = FontUtil.poppinsNormal18.getStringWidth(labelUser);

        FontUtil.poppinsNormal18.drawString(labelUser, contentContainerX + (contentContainerWidth / 2) - (userLength / 2), contentContainerY * 2.3f, -1);
        this.username.drawTextBox();

        String labelPass = "Password:";
        double passLength = FontUtil.poppinsNormal18.getStringWidth(labelPass);

        FontUtil.poppinsNormal18.drawString(labelPass, contentContainerX + (contentContainerWidth / 2) - (passLength / 2), contentContainerY * 3.2f, -1);
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

        addButton.action = new Runnable() {
            @Override
            public void run() {
                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                try {
                    MicrosoftAuthResult result = authenticator.loginWithCredentials(username.getText(), password.getText());
                    MinecraftProfile profile = result.getProfile();
                    AltManagerGUI.auth = new Session(profile.getName(), profile.getId(), result.getAccessToken(), "microsoft");

                    status = "Logged In As: " + AltManagerGUI.auth.getUsername();

                    Snowstorm.INSTANCE.getAltHandler().addAlt(AltManagerGUI.auth.getUsername(), password.getText(), username.getText(), true);

                    mc.displayGuiScreen(new AltManagerGUI());
                } catch (MicrosoftAuthenticationException e) {
                    if(e.getMessage().contains("Invalid credentials or tokens")) {
                        status = "Invalid credentials!";
                    } else {
                        status = "Failed to log in. Slow down and try again later.";
                    }
                }

                if(AltManagerGUI.auth != null) {
                    mc.session = AltManagerGUI.auth;
                }
            }
        };
        addButton.render(mouseX, mouseY);

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
