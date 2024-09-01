package today.snowstorm.client.ui.altManager.util;

public class Alt extends AltHandler.AltJson {

    private String username, password, email;

    public Alt(String username, String password, String email) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
