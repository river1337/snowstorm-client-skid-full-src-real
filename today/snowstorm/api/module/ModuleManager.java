package today.snowstorm.api.module;

import today.snowstorm.api.utils.font.FontUtil;
import today.snowstorm.client.module.combat.AntiBot;
import today.snowstorm.client.module.combat.KillAura;
import today.snowstorm.client.module.combat.Reach;
import today.snowstorm.client.module.ghost.AimAssist;
import today.snowstorm.client.module.misc.Disabler;
import today.snowstorm.client.module.player.AutoArmor;
import today.snowstorm.client.module.player.ChestStealer;
import today.snowstorm.client.module.player.NoFall;
import today.snowstorm.client.module.visuals.*;
import today.snowstorm.client.module.combat.Velocity;
import today.snowstorm.client.module.movement.Fly;
import today.snowstorm.client.module.movement.Scaffold;
import today.snowstorm.client.module.movement.Speed;
import today.snowstorm.client.module.movement.Sprint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    public static final LinkedHashMap<Class<? extends Module>, Module> modules = new LinkedHashMap<>();

    public ModuleManager() {
        System.out.println("Adding Modules.");
        this.addModules();
    }

    private void addModules() {
        // Ghost
        this.modules.put(AimAssist.class, new AimAssist());

        // Combat
        this.modules.put(KillAura.class, new KillAura());
        this.modules.put(AntiBot.class, new AntiBot());
        this.modules.put(Velocity.class, new Velocity());
        this.modules.put(Reach.class, new Reach());
        System.out.println("Adding Combat Modules.");

        // Player
        this.modules.put(AutoArmor.class, new AutoArmor());
        this.modules.put(ChestStealer.class, new ChestStealer());
        this.modules.put(NoFall.class, new NoFall());
        System.out.println("Adding Player Modules.");

        // Movement
        this.modules.put(Sprint.class, new Sprint());
        this.modules.put(Speed.class, new Speed());
        this.modules.put(Scaffold.class, new Scaffold());
        this.modules.put(Fly.class, new Fly());
        System.out.println("Adding Movement Modules.");

        // Misc
        this.modules.put(Disabler.class, new Disabler());

        // Visuals
        this.modules.put(HUD.class, new HUD());
        this.modules.put(ClickGui.class, new ClickGui());
        this.modules.put(TargetHud.class, new TargetHud());
        this.modules.put(ItemPhysics.class, new ItemPhysics());
        System.out.println("Adding Visuals Modules.");

        // World
        this.modules.put(StorageESP.class, new StorageESP());
        System.out.println("Adding World Modules");
    }

    public List<Module> getModules() {
        return new ArrayList<>(this.modules.values());
    }

    // Ported shit from Vergo :D

    public List<Module> getSortedModules() {
        return this.getModules().stream().sorted(Comparator.comparing(module -> FontUtil.poppinsNormal18.getStringWidth(((Module) module).getName())).reversed()).collect(Collectors.toList());
    }

    public Module getModuleByName(String name) {
        return this.getModules().stream().filter(module -> module.getName().equals(name)).findFirst().orElse(null);
    }

    public List<Module> getModulesByCategory(ModuleType category) {
        return this.getModules().stream().filter(module -> module.getModuleType() == category).collect(Collectors.toList());
    }
}
