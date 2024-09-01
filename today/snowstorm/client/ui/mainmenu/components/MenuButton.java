package today.snowstorm.client.ui.mainmenu.components;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import today.snowstorm.client.utils.render.RenderUtils;
import today.snowstorm.client.utils.Wrapper;

import java.awt.*;

public class MenuButton {

    private int posX;
    private int posY;
    private int width;
    private int height;
    private String label;

    public Color color;

    public Runnable action;

    public MenuButton(String label, int posX, int posY, int width, int height, Color color) {
        this.label = label;

        this.posX = posX;
        this.posY = posY;

        this.width = width;
        this.height = height;

        this.color = color;
    }

    public int getPosX() {
        return posX;
    }
    public int getPosY() {
        return posY;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void render(int mouseX, int mouseY) {
        Color borderColor = new Color(20, 20, 20);
        Color newColor = color;

        if(isHovering(posX, posY, width, height, mouseX, mouseY)) {
            borderColor = color;
            newColor = new Color(20, 20, 20);
        }

        RenderUtils.drawRoundedRect(posX, posY, width, height, 3, newColor);
        Wrapper.getMc().fontRendererObj.drawString(label, (posX + width / 2) - (Wrapper.getMc().fontRendererObj.getStringWidth(label) / 2), (posY + height / 2) - 3, -1);
    }

    public static boolean isHovering(double x, double y, double width, double height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

}
