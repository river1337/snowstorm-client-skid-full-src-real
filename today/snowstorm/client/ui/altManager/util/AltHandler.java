package today.snowstorm.client.ui.altManager.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import today.snowstorm.client.Snowstorm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AltHandler {
    public CopyOnWriteArrayList<Alt> altList = new CopyOnWriteArrayList<>();

    public void addAlt(String username, String password, String email, boolean valid) {
        JSONArray jsonArray = null;
        try (FileReader reader = new FileReader(Snowstorm.INSTANCE.getFileManager().altsDir)) {
            jsonArray = new JSONArray(new JSONTokener(reader));
        } catch (JSONException e) {
            // If the file is empty or doesn't exist, create a new JSON array
            jsonArray = new JSONArray();
        } catch (Exception e) {

        }

        JSONObject user = new JSONObject();
        user.put("username", username);
        user.put("password", password);
        user.put("email", email);

        if(jsonArray == null) return;

        jsonArray.put(user);

        try (FileWriter file = new FileWriter(Snowstorm.INSTANCE.getFileManager().altsDir)) {
            file.write(jsonArray.toString(4));
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAlts() {
        altList.clear();
        FileReader read = null;
        try {
            read = new FileReader(Snowstorm.INSTANCE.getFileManager().altsDir.getAbsoluteFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(read == null) return;

        Type listType = new TypeToken<List<AltJson>>() {}.getType();
        List<AltJson> altJsons = new Gson().fromJson(read, listType);

        for (int i = altJsons.size() - 1; i >= 0; i--) {
            AltJson altJson2 = altJsons.get(i);
            altList.add(new Alt(altJson2.username, altJson2.password, altJson2.email));
        }

    }

    public static class AltJson {
        String username;
        String password;
        String email;

        boolean valid;
    }

}