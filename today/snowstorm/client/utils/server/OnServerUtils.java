package today.snowstorm.client.utils.server;

import net.minecraft.client.Minecraft;

public class OnServerUtils {

    protected static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isOnHypixel() {
        try {

            if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net")) {
                return true;
            }
        } catch (Exception e) {

        }

        return false;
    }

}
