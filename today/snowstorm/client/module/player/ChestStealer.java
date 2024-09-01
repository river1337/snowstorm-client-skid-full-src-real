package today.snowstorm.client.module.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.BooleanSetting;
import today.snowstorm.client.settings.NumberSetting;
import today.snowstorm.client.utils.other.TimerUtil;

public class ChestStealer extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public NumberSetting grabDelay = new NumberSetting("Delay", 100, 0, 2500, 10);
    public BooleanSetting randomOrder = new BooleanSetting("Random Order", true);

    public BooleanSetting verifyName = new BooleanSetting("Verify Name", true);

    public ChestStealer() {
        super("ChestStealer", "Steal dem chest items WELL", ModuleType.PLAYER, Keyboard.KEY_UP);
        addSetting(randomOrder, verifyName, grabDelay);
    }

    TimerUtil timer = new TimerUtil();

    boolean hasBeenOpened = false;

    @EventHandler
    public Listener<UpdateEvent> onUpdateMyAsshole = e -> {
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            if(!hasBeenOpened) {
                timer.reset();
                hasBeenOpened = true;
            }
            ContainerChest gui = (ContainerChest) mc.thePlayer.openContainer;

            boolean isInvFull = true;

            if(verifyName.isEnabled()) {
                // If the name isn't "Chest" (default) or "LOW" (Watchdog)
                // then don't steal!
                if (!(gui.getLowerChestInventory().getName().contains("Chest") || (!gui.getLowerChestInventory().getName().contains("LOW")))) return;
            }

            // Chest Stealer Logic Goes Here!
            ItemStack[] itemStackArr;
            int invSize = (itemStackArr = mc.thePlayer.inventory.mainInventory).length;
            for(int i = 0; i < invSize; i++) {
                ItemStack stack = gui.getLowerChestInventory().getStackInSlot(i);
                if(stack == null) {
                    isInvFull = false;
                    break;
                }
            }

            boolean isChestEmpty = true;

            if(!isInvFull) {
                for (int in = 0; in < gui.getLowerChestInventory().getSizeInventory(); in++) {
                    ItemStack is = gui.getLowerChestInventory().getStackInSlot(in);
                    if (is != null) {
                        isChestEmpty = false;
                        break;
                    }
                }

                if(!isChestEmpty) {
                    for (int inn = 0; inn < gui.getLowerChestInventory().getSizeInventory(); inn++) {
                        ItemStack item = gui.getLowerChestInventory().getStackInSlot(inn);
                        if(item != null && timer.stopAfter((long) (grabDelay.getValueAsLong() ), false)) {
                            mc.playerController.windowClick(gui.windowId, inn, 0, 1, mc.thePlayer);
                            timer.reset();
                        }
                    }
                } else {
                    mc.thePlayer.closeScreen();
                }
            }

        } else {
            if(hasBeenOpened) {
                hasBeenOpened = false;
            }
            return;
        }
    };

}
