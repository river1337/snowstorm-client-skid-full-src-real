package today.snowstorm.client.module.player;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import today.snowstorm.api.module.Module;
import today.snowstorm.api.module.ModuleType;
import today.snowstorm.client.events.UpdateEvent;
import today.snowstorm.client.settings.ModeSetting;
import today.snowstorm.client.utils.other.TimerUtil;

public class AutoArmor extends Module {

    protected static Minecraft mc = Minecraft.getMinecraft();

    private ModeSetting timerMode = new ModeSetting("Delay", "Instant", "Instant", "Normal", "Faster");

    private TimerUtil timer = new TimerUtil();

    public AutoArmor() {
        super("AutoArmor", "Automatically suit up with the best!", ModuleType.PLAYER, Keyboard.KEY_COMMA);
        addSetting(timerMode);
    }

    @EventHandler
    public Listener<UpdateEvent> onSuckMyFatKnob = e -> {
        if(mc.currentScreen instanceof GuiInventory) {
            getBestArmor();
        }
    };

    // Utils ported from Vergo
    public void getBestArmor(){
        for(int type = 1; type < 5; type++){
            if(mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()){
                ItemStack item = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if(isBestArmor(item, type)){
                    continue;
                }else{
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 4 + type, 0, 1, mc.thePlayer);
                    return;
                }
            }
            for (int i = 9; i < 45; i++) {
                if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                    if(isBestArmor(is, type) && getProtection(is) > 0){
                        if(timerMode.is("Instant")) {
                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                        } else {
                            if(timerMode.is("Faster")) {
                                if(timer.stopAfter(300, false)) {
                                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                                }
                            } else if (timerMode.is("Normal")) {
                                if(timer.stopAfter(700, false)) {
                                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 1, mc.thePlayer);
                                }
                            }
                        }
                        return;
                    }
                }
            }
        }
    }
    public static boolean isBestArmor(ItemStack stack, int type){
        float prot = getProtection(stack);
        String strType = "";
        if(type == 1){
            strType = "helmet";
        }else if(type == 2){
            strType = "chestplate";
        }else if(type == 3){
            strType = "leggings";
        }else if(type == 4){
            strType = "boots";
        }
        if(!stack.getUnlocalizedName().contains(strType)){
            return false;
        }
        for (int i = 5; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                    return false;
            }
        }
        return true;
    }

    public static float getProtection(ItemStack stack){
        float prot = 0;
        if ((stack.getItem() instanceof ItemArmor)) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)/100d;
//    		if ((stack.getMaxDamage() - stack.getItemDamage()) / stack.getMaxDamage() < 0.01) {
//    			prot -= 0.01;
//    		}
            prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack)/100d;
        }
        return prot;
    }
}
