package today.snowstorm.client.events;

import today.snowstorm.api.events.Event;

public class ChatEvent extends Event {

    private final String msg;

    public ChatEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
