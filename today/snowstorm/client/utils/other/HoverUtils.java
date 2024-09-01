package today.snowstorm.client.utils.other;

public class HoverUtils {

    // If mouse is in this area, return true, else return false
    public static boolean isHovering(double x, double y, double width, double height, int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static boolean inBounds(float x, float y, float w, float h, int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= w && mouseY >= y && mouseY <= h);
    }

}
