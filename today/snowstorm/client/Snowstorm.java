package today.snowstorm.client;

import best.azura.eventbus.core.EventBus;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import store.snowstorm.UserAccount;
import today.snowstorm.client.events.KeyEvent;
import today.snowstorm.client.module.visuals.clickgui.ClickUI;
import today.snowstorm.client.ui.altManager.util.AltHandler;
import today.snowstorm.client.utils.other.DisplayUtils;
import today.snowstorm.api.Release;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleManager;
import today.snowstorm.api.utils.discordrpc.DiscordRPC;
import today.snowstorm.api.utils.files.FileManager;

public enum Snowstorm {
    INSTANCE;

    //public String DISTRO = "DEV"; // DEV , PUBLIC , BETA

    // Better way of defining the distro branch
    public Release DISTRO;

    public String name, build;
    private EventBus eventBus;
    private ModuleManager moduleManager;

    private FileManager fileManager;

    private AltHandler altHandler;

    public ClickUI guiClickGui;

    public boolean hasLoaded = false;

    private DiscordRPC discord = new DiscordRPC();

    public final Runnable run = () -> {
        if(!hasLoaded) {
            this.name = "Snowstorm";
            this.build = "b1.0";
            this.DISTRO = Release.DEV;

            // dont remove or client wont load
            UserAccount.USER_AGENT = name + build + DISTRO.name();

            DisplayUtils.setTitle(name + " " + build);

            // Intialize EventBus
            this.eventBus = new EventBus();

            // Initialize ModuleManager
            this.moduleManager = new ModuleManager();

            this.guiClickGui = new ClickUI();

            this.eventBus.register(this);

            // Initialize FileManager
            this.fileManager = new FileManager();
            this.fileManager.init();

            // Initialize alt handler
            this.altHandler = new AltHandler();
            // gets all alts from alts.json file
            altHandler.getAlts();

            discord.start();
        }

        hasLoaded = true;
    };

    // Key-bind EventHandler.
    @EventHandler
    public final Listener<KeyEvent> onKey = e ->
            getModuleManager().getModules().stream().filter
                    (m -> m.getKeybind() == e.getKey()).forEach(Module::toggle);

    // Get Event Bus
    public final EventBus getEventBus() {
        return eventBus;
    }

    // Get File Manager
    public FileManager getFileManager() {
        return fileManager;
    }

    // Get Alt Handler
    public AltHandler getAltHandler() {
        return altHandler;
    }

    // Get Module Manager
    public final ModuleManager getModuleManager() {
        return moduleManager;
    }
}