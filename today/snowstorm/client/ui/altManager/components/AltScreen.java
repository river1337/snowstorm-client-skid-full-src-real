package today.snowstorm.client.ui.altManager.components;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.ui.altManager.util.Alt;
import today.snowstorm.client.utils.render.RenderUtils;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AltScreen extends GuiScreen {

    private float x, y, width, height;

    private CopyOnWriteArrayList<AltBox> boxList;

    public AltScreen(float x, float y, float width, float height, CopyOnWriteArrayList<AltBox> boxList) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.boxList = boxList;
    }

    @Override
    public void updateScreen() {
        x = x;
        y = y;
        width = width;
        height = height;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float part) {
        //System.out.println(mouseWheel + "");

        // Hello draw background you fuck bitch!
        RenderUtils.drawRect(x, y, width, height, new Color(0, 0, 0, 0).getRGB());

        int boxWidth = 180;
        int boxHeight = 60;

        int xOffset = 20;
        int yOffset = 20;
        int maxHeight = (int) height - 100;
        for (int in = 0; in < Snowstorm.INSTANCE.getAltHandler().altList.size(); in++) {
            Alt alt = (Alt) Snowstorm.INSTANCE.getAltHandler().altList.get(in);
            boxList.add(new AltBox(alt, x + xOffset, y + yOffset, boxWidth, boxHeight));
            if (yOffset < maxHeight - 23) {
                yOffset += 70;
            } else {
                yOffset = 20;
                xOffset += 200;
            }
        }

        for (int in2 = 0; in2 < boxList.size(); in2++) {
            AltBox bocks = (AltBox) boxList.get(in2);
            bocks.drawScreen(mouseX, mouseY, part);
        }

        float scroll = 0;
        if(xOffset > width) {
            scroll += Mouse.getDWheel() / 4;

            if(x <= 0) {
                //System.out.println((-780 > -750));

                if(x + scroll >= (width - xOffset - (boxWidth + 30))) {
                    x += scroll;
                } else {
                    x = (width - xOffset - (boxWidth + 30));
                    scroll = 0;
                }

                if(!(x + scroll >= 1)) {
                    x += scroll;
                } else {
                    scroll = 0;
                }

            } else {
                x = 0;
            }

            //System.out.println(xOffset + " " + x + " " + ( x <= (width - xOffset - (boxWidth + 30))) + " " + (width - xOffset - (boxWidth + 30)));
        } else {

        }

    }

}
