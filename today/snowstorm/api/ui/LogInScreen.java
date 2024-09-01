package today.snowstorm.api.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import store.Snowstorm.UserAccount;
import store.Snowstorm.network.Authentication;
import today.snowstorm.client.ui.mainmenu.MainMenu;
import today.snowstorm.client.utils.Wrapper;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;

public class LogInScreen extends GuiScreen {

    private String loginString = "Enter Your Snowstorm UID";

    private int textString;

    private int uidBoxWidth;

    private GuiTextField worldNameField;

    @Override
    public void initGui() {
        uidBoxWidth = 150;
        int uidBoxX = (width / 2) - (uidBoxWidth / 2);

        textString = (width / 2) - (Wrapper.getMc().fontRendererObj.getStringWidth(loginString) / 2);

        worldNameField = new GuiTextField(1, mc.fontRendererObj, uidBoxX, 50, uidBoxWidth, 10);
        worldNameField.setFocused(true);
        worldNameField.setMaxStringLength(4);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawRect(0, 0, width, height, new Color(10, 10, 10).getRGB());

        mc.fontRendererObj.drawString(loginString, textString, 30, -1);

        worldNameField.drawTextBox();
    }

    @Override
    public void updateScreen() {
        worldNameField.updateCursorCounter();

        if(worldNameField.getText().length() == 4) {
            //if(UserAccount.account != null) return;

            Authentication authenticate = new Authentication();
            try {
                authenticate.UA_STRING[0] = "SnowstormSTORE ";
                authenticate.authenticateUser(worldNameField.getInt(), "hellohwid");

                System.out.println(UserAccount.account.getName());
                mc.displayGuiScreen(new MainMenu());
            } catch (Exception e) {
                e.printStackTrace();
                loginString = "Login Failed. Try again.";
                worldNameField.setText("");
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Override this so nothing happens
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int mouseButton, long ti) {
        // Override this so nothing happens
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        // Override so nothing happens
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(worldNameField.isFocused()) {
            this.worldNameField.textboxKeyTyped(typedChar, keyCode);
        }

        // Override so nothing happens.
    }

}
