package Views;

import Controllers.PlayerController;
import enums.LoginResponse;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class MainMenuController {
    public static void login(String username, String password, Text loginMessage) {
        LoginResponse response = PlayerController.login(username, password);
        Page.setTextElement(loginMessage, response.toString());
    }

    public static void register(String username, String password, Text loginMessage) {
        LoginResponse response = PlayerController.newPlayer(username, password);
        Page.setTextElement(loginMessage, response.toString());
    }

    public static void guestLogin(Text loginMessage) {
        PlayerController.loginGuest();
        Page.setTextElement(loginMessage, "entered as guest");
    }
}
