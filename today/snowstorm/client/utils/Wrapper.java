package today.snowstorm.client.utils;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import today.snowstorm.client.Snowstorm;
import today.snowstorm.api.Release;

public class Wrapper {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public static Minecraft getMc() {
        return Minecraft.getMinecraft();
    }

    private static String prefix = ChatFormatting.PREFIX_CODE + "bSnowstorm " +
            ChatFormatting.PREFIX_CODE + "r" + ChatFormatting.PREFIX_CODE + "o>> " + ChatFormatting.PREFIX_CODE + "r";

    public static void addChatMessage(Object message, boolean displayRawMessage) {
        if (displayRawMessage) {
            // Only send raw messages (used for debugs, value logging, etc)
            if(Snowstorm.INSTANCE.DISTRO == Release.DEV) {
                addChatMessage(new ChatComponentText(message + ""));
            }
        }else {
            addChatMessage(new ChatComponentText(prefix + message));
        }
    }

    public static void addChatMessage(Object message, ChatStyle style) {
        ChatComponentText component = new ChatComponentText(prefix + message);
        component.setChatStyle(style);
        addChatMessage(component);
    }

    public static void addChatMessage(IChatComponent component) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(component);
    }

    public static boolean isMoving() {
        return getMc().thePlayer.movementInput.moveForward != 0F || getMc().thePlayer.movementInput.moveStrafe != 0F;
    }

}