package Controllers;

import Models.Player;
import enums.EntityType;

public class GameController {
    private static EntityType shootState = EntityType.BULLET;
    private static Player player;
    private static int score;


    public static EntityType getShootState() {
        return shootState;
    }

    public static void setShootState(EntityType shootState) {
        GameController.shootState = shootState;
    }

    public static void changeShootState(){
        if (shootState == EntityType.BOMB) shootState = EntityType.BULLET;
        else shootState = EntityType.BOMB;
    }
}
