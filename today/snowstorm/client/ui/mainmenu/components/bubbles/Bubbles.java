package today.snowstorm.client.ui.mainmenu.components.bubbles;

import today.snowstorm.client.utils.animations.impl.ElasticAnimation;
import today.snowstorm.client.utils.render.RenderUtils;
import today.snowstorm.client.ui.mainmenu.MainMenu;
import today.snowstorm.client.utils.Wrapper;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Bubbles {

    public float x, y, radius;
    private Color color;

    public float opacity = 255.0f;

    private ElasticAnimation ela;

    public Bubbles(float x, float y, float radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;

        ela = new ElasticAnimation(500, 1.0f, 8f, 0.8f, false);
    }

    public void render() {
        //GL11.glEnable(GL_POINT_SMOOTH);
        //GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        if(!(Wrapper.getMc().currentScreen instanceof MainMenu)) return;

        RenderUtils.drawCircle(x, y, (float) (radius * ela.getOutput()), color);
    }

    // update the bubbles pos
    public void update() {
        // change bubble x and y position by random
        if(!(Wrapper.getMc().currentScreen instanceof MainMenu)) return;

        //x = interpolateRotation(x, (float) (ThreadLocalRandom.current().nextDouble(0, 2)), 0.01f);
        //y = interpolateRotation(y, (float) (ThreadLocalRandom.current().nextDouble(0, 2)), 0.01f);
        radius -= ThreadLocalRandom.current().nextDouble(0.08, 0.12);
    }

    public float interpolateRotation(float start, float end, float t) {
        // Calculate the difference between the start and end values
        float diff = end - start;

        // Interpolate the value using the provided t value
        return (start + diff * t);
    }

}
