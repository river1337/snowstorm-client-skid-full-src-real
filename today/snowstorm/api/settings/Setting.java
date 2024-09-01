package today.snowstorm.api.settings;

public abstract class Setting {
    private final String name;
    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Object getValueObject();
}