package today.snowstorm.api.utils.files;

import java.io.File;
import java.io.IOException;

public class FileManager {

    public File mainDir = new File("Snowstorm"),
            altsDir = new File(mainDir, "alts.json");

    public void init() {
        if(!mainDir.exists()) {
            mainDir.mkdirs();
        }

        if(!altsDir.exists()) {
            try {
                altsDir.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
