package today.snowstorm.client.settings;

import today.snowstorm.api.settings.Setting;

public class BooleanSetting extends Setting {

    private boolean enabled;

    public BooleanSetting(String name, boolean boo) {
        super(name);
        this.enabled = boo;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getValue() {
        return enabled;
    }

    @Override
    public Object getValueObject() {
        return enabled;
    }

}
