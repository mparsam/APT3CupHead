package Models;
import enums.GameHardness;

import java.time.LocalDateTime;
public class GameRecord {
    private int score;
    private LocalDateTime endTime;
    private GameHardness gameHardness;

    public GameRecord(int score, LocalDateTime endTime, GameHardness gameHardness) {
        this.score = score;
        this.endTime = endTime;
        this.gameHardness = gameHardness;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public GameHardness getGameHardness() {
        return gameHardness;
    }

}
