package today.snowstorm.client.events;

import today.snowstorm.api.events.Event;

public class KeyEvent extends Event {

    private final int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public final int getKey() {
        return key;
    }

}
