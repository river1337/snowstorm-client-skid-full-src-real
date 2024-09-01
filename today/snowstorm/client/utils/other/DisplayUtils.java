package today.snowstorm.client.utils.other;

import org.lwjgl.opengl.Display;
import today.snowstorm.client.Snowstorm;

public class DisplayUtils {

    public static void setTitle(String title) {
        Display.setTitle(title);
    }

    // Sets the title (default: "Snowstorm b<>")
    public static void setDefaultTitle() {
        Display.setTitle(Snowstorm.INSTANCE.name() + " " + Snowstorm.INSTANCE.build);
    }




    // Attempting to get the FPS in an all-round better way!
    private static long lastTime = System.currentTimeMillis();
    private static int frames = 0;

    private static int fps = 0;
    public static int getFps() {
        frames++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > 500) {
            fps = (int) (frames * 1000.0 / (currentTime - lastTime));
            lastTime = currentTime;
            frames = 0;
            return fps;
        }

        return fps;
    }

}
