package today.snowstorm.client.events;

import today.snowstorm.api.events.Event;

public class MouseMoveEvent extends Event {

    public int mouseX, mouseY, button;

    public MouseMoveEvent(int mouseX, int mouseY, int button) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
    }

    public final int getMouseX() {
        return mouseX;
    }

    public final int getMouseY() {
        return mouseY;
    }

    public final int getMouseButton() {
        return button;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public void setMouseButton(int mouseButton) {
        this.button = mouseButton;
    }

}
