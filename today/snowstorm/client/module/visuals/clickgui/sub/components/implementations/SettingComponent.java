package today.snowstorm.client.module.visuals.clickgui.sub.components.implementations;


import today.snowstorm.client.module.visuals.clickgui.sub.components.Component;
import today.snowstorm.api.settings.Setting;

public abstract class SettingComponent<Type extends Setting> extends Component {

    protected Type setting;

    public SettingComponent(Type setting, float x, float y, float width, float height) {
        this(setting, x, y, width, height, true);
    }

    public SettingComponent(Type setting, float x, float y, float width, float height, boolean visible) {
        super(x, y, width, height, visible);
        this.setting = setting;
    }

    public Type setting() {
        return setting;
    }
    public void setting(Type setting) {
        this.setting = setting;
    }
}
