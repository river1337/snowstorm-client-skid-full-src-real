package today.snowstorm.client.events;

import today.snowstorm.api.events.Event;

public class DrawScreenEvent extends Event {

    private int mouseX, mouseY;
    private float partialTicks;

    public DrawScreenEvent(int mouseX, int mouseY, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        this.partialTicks = partialTicks;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setMouseX(int newMouseX) {
        mouseX = newMouseX;
    }

    public void setMouseY(int newMouseY) {
        mouseY = newMouseY;
    }

    public void setPartialTicks(float newPartialTicks) {
        partialTicks = newPartialTicks;
    }

}
