package today.snowstorm.api.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.Map;

public class FontUtil {

    public static TFontRenderer poppinsFat72, poppinsFat24, poppinsNormal18, poppinsNormal24, poppinsNormal16;
    public static Font poppinsFat722, poppinsFat242, poppinsNormal182, poppinsNormal242, poppinsNormal162;

    public static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font = null;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("Snowstorm/fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            System.out.println("Error loading font: " + e.getMessage());
        }

        if (font == null) {
            font = new Font("default", Font.PLAIN, +10);
        }
        return font;
    }

}
