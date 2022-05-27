package Controllers;

import Models.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import enums.LoginResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PlayerController {
    private static ArrayList<Player> players;
    private static Player currentPlayer;

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    static {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/resources/Players.json")));
            players = new Gson().fromJson(json, new TypeToken<List<Player>>(){}.getType());
            if (players == null) players = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error while loading Players");
            e.printStackTrace();
        }
    }

    public static LoginResponse newPlayer(String username, String password){
        if (playerExists(username)) return LoginResponse.USERNAME_TAKEN;
        players.add(new Player(username, password));
        return LoginResponse.SUCCESSFUL;
    }

    private static boolean isPasswordWeak(String password) {
        return !(password.length()>=8 && password.matches("^\\w+$") &&
                password.matches("^.*[A-Z].*$") && password.matches("^.*[a-z].*$")
                && password.matches("^.*[0-9].*$"));
    }

    public static LoginResponse login(String username, String password){
        if (getPlayerByUsername(username) == null) return LoginResponse.MISMATCH;
        Player player = getPlayerByUsername(username);
        if (!player.getPassword().equals(password)) return LoginResponse.MISMATCH;
        currentPlayer = player;
        return LoginResponse.SUCCESSFUL;
    }

    public static void Logout(){
        currentPlayer = null;
    }

    private static boolean playerExists(String username){
        return (getPlayerByUsername(username) != null);
    }

    private static Player getPlayerByUsername(String username){
        for (Player player : players) {
            if (player.getUsername().equals(username)) return player;
        }
        return null;
    }

    public static LoginResponse changeUsername(String username){
        if (currentPlayer == null) System.err.println("not logged IN");
        if (playerExists(username)) return LoginResponse.USERNAME_TAKEN;
        currentPlayer.setUsername(username);
        return LoginResponse.SUCCESSFUL;
    }

    public static LoginResponse changePassword(String newPassword, String oldPassword) {
        if (currentPlayer == null) System.err.println("not logged in");
        if (!currentPlayer.getPassword().equals(oldPassword)) return LoginResponse.WRONG_PASSWORD;
        currentPlayer.setPassword(newPassword);
        return LoginResponse.SUCCESSFUL;
    }

    // TODO: 5/14/2022 remember to call me after every change and at exit
    public static void writePlayersToJson(){
        try {
            String playersJson = new Gson().toJson(players);
            FileWriter playersJsonFileWriter = new FileWriter("src/main/resources/Players.json");
            playersJsonFileWriter.write(playersJson);
            playersJsonFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loginGuest() {
        login("guest$%", "1");
    }

    public static boolean isLoggedIN(){
        return currentPlayer != null;
    }

    public static void updatePlayerScore(int score, int time){
        if (score>currentPlayer.getHighScore()){
            currentPlayer.setFinishTime(time);
            currentPlayer.setHighScore(score);
        }
        writePlayersToJson();
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }
}
