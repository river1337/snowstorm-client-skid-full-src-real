package today.snowstorm.api.module;

public enum ModuleType {

    COMBAT("Combat"),
    MOVEMENT("Movement"),
    MISC("Misc"),
    VISUALS("Visuals"),
    WORLD("World"),
    PLAYER("Player"),
    GHOST("GHOST");

    final String name;
    ModuleType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
