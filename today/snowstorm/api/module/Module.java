package today.snowstorm.api.module;

import org.lwjgl.input.Keyboard;
import today.snowstorm.api.settings.Setting;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.client.utils.player.RotationUtils;

import java.util.*;

public class Module {
    private final String name;
    private String description;
    private int keybind;
    private boolean enabled, hidden;
    private ModuleType moduleType;
    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, ModuleType type, int defaultKey) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.moduleType = Objects.requireNonNull(type);
        this.keybind = defaultKey;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDesc) {
        description = Objects.requireNonNull(newDesc);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean toggled) {
        this.enabled = toggled;
        if(this.enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public String getBind(){
        return Keyboard.getKeyName(keybind);
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void onEnable() {
        if(!Snowstorm.INSTANCE.getEventBus().isRegistered(this)) {
            Snowstorm.INSTANCE.getEventBus().register(this);
        }

        RotationUtils.resetCustomYaw();
        RotationUtils.resetCustomPitch();
    }

    public void onDisable() {
        if(Snowstorm.INSTANCE.getEventBus().isRegistered(this)) {
            Snowstorm.INSTANCE.getEventBus().unregister(this);
        }

        RotationUtils.resetCustomYaw();
        RotationUtils.resetCustomPitch();
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void addSetting(Setting... setting) {
        settings.addAll(Arrays.asList(setting));
    }

    public List<Setting> getSettings() {
        return Collections.unmodifiableList(settings);
    }

}