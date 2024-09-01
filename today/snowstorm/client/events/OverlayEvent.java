package today.snowstorm.client.events;

import net.minecraft.client.gui.ScaledResolution;
import today.snowstorm.api.events.Event;

public class OverlayEvent extends Event {
    private final ScaledResolution sr;

    public OverlayEvent(ScaledResolution sr) {
        this.sr = sr;
    }

    public ScaledResolution getScaledResolution() {
        return sr;
    }

}
