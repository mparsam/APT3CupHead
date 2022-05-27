package Views;


import Controllers.*;
import Models.GameEntityFactory;
import Models.Player;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.core.View;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.IntegerComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.time.TimerAction;
import com.almasb.fxgl.ui.Position;
import com.almasb.fxgl.ui.ProgressBar;
import enums.EntityType;
import enums.Variables;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static enums.Variables.*;

public class BasicGameApp extends GameApplication {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;
    private static final int MOVEMENT_STEP = 3;

    private Entity player;
    private Entity boss;
    private ProgressBar playerHPBar;
    private ProgressBar bossHPBar;
    private Texture texture;
    private int score = 0;
    private Double startTime = Double.valueOf(0);
    private Entity shootIcon;
    private int level =1;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("CupHead");
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setGameMenuEnabled(true);
        settings.setDeveloperMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setUserProfileEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newMainMenu() {
                return new CupHeadMainMenu();
            }
        });
    }


    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("time", getGameTimer().getNow() / 60 + ":" + getGameTimer().getNow() % 60);
        vars.put("score", "score: " + score);
    }

    @Override
    protected void initInput() {
        Input input = getInput();
        FXGL.onKey(KeyCode.D, () -> {
            player.translateX(MOVEMENT_STEP);
        });

        FXGL.onKey(KeyCode.A, () -> {
            player.translateX(-MOVEMENT_STEP);
        });

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-MOVEMENT_STEP); // move up 5 pixels
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(MOVEMENT_STEP); // move down 5 pixels
        });

        UserAction shoot = new UserAction("shoot") {
            private TimerAction timerAction;

            @Override
            protected void onActionBegin() {
                shoot();
                timerAction = getGameTimer().runAtInterval(() -> shoot(), Duration.millis(200));
            }

            @Override
            protected void onActionEnd() {
                timerAction.expire();
            }
        };

        UserAction changeShootState = new UserAction("changeShootState") {
            @Override
            protected void onActionBegin() {
                GameController.changeShootState();
                shootIcon.getViewComponent().clearChildren();
                String iconName = GameController.getShootState().name + "Icon.png";
                shootIcon.getViewComponent().addChild(new ImageView(getAssetLoader().loadImage(iconName)));
            }
        };
        input.addAction(changeShootState, KeyCode.TAB);
        input.addAction(shoot, KeyCode.SPACE);

    }

    private void shoot() {
        if (GameController.getShootState() == EntityType.BULLET)
            FXGL.spawn(EntityType.BULLET.name, player.getRightX(), (player.getY() + player.getBottomY()) / 2);
        else
            FXGL.spawn(EntityType.BOMB.name, player.getRightX(), (player.getY() + player.getBottomY()) / 2);

    }

    @Override
    protected void initGame() {
        level =1;
        startTime = getGameTimer().getNow();
        texture = new Texture(image("img.png"));

        getSettings().setGlobalMusicVolume(80);
        Music music = FXGL.getAssetLoader().loadMusic("background.mp3");
        FXGL.getAudioPlayer().loopMusic(music);
        FXGL.getGameWorld().addEntityFactory(new GameEntityFactory());
        getGameTimer().runAtInterval(() -> showMiniBoss(), Duration.seconds(MINI_BOSS_DELAY));
        getGameTimer().runOnceAfter(() -> getGameTimer().runAtInterval(() ->showMiniBoss(), Duration.seconds(MINI_BOSS_DELAY)),Duration.seconds(MINI_BOSS_DELAY/2));

        FXGL.spawn("bc", 0, 0);
        FXGL.spawn("bc", BACKgROUND_WIDTH, 0);
        boss = FXGL.spawn(EntityType.BOSS.name, WIDTH * 3 / 4 - 60, HEIGHT / 2 - 40);
        System.err.println(boss.getCenter().toString());
        player = FXGL.spawn(EntityType.PLAYER.name, WIDTH / 10, HEIGHT / 2);
        GameController.setShootState(EntityType.BULLET);
        shootIcon = FXGL.spawn(EntityType.SHOOT_ICON.name, 20, HEIGHT-100);

    }

    private void showMiniBoss() {
        int y = random(100, HEIGHT - 100);
        for (int i = 0; i < MINI_BOSS_COUNT; i++) {
            FXGL.spawn(EntityType.MINI_BOSS.name, WIDTH + MINI_BOSS_X_DISTANCE * i, y);
        }
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.MINI_BOSS) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity miniBoss) {
                FXGL.play("collision.wav");
                bullet.removeFromWorld();
                miniBoss.getComponent(HealthIntComponent.class).damage(BULLET_DAMAGE);
                if (miniBoss.getComponent(HealthIntComponent.class).getValue() == 0) miniBoss.removeFromWorld();
                score+=10;
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOMB, EntityType.MINI_BOSS) {
            @Override
            protected void onCollisionBegin(Entity bomb, Entity miniBoss) {
                FXGL.play("collision.wav");

                miniBoss.getComponent(HealthIntComponent.class).damage(BOMB_DAMAGE);
                bomb.removeFromWorld();
                if (miniBoss.getComponent(HealthIntComponent.class).getValue() <= 0) miniBoss.removeFromWorld();
                score+=20;
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.MINI_BOSS, EntityType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity miniBoss, Entity player) {
                FXGL.play("collision.wav");
                miniBoss.removeFromWorld();
                player.getComponent(PlayerResetComponent.class).reset(PLAYER_COLLISION_DAMAGE);

            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOSS, EntityType.BULLET) {
            @Override
            protected void onCollisionBegin(Entity boss, Entity bullet) {
                FXGL.play("collision.wav");

                bullet.removeFromWorld();
                boss.getComponent(HealthIntComponent.class).damage(BULLET_DAMAGE);
                score+=10;
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOSS, EntityType.BOMB) {
            @Override
            protected void onCollisionBegin(Entity boss, Entity bomb) {
                FXGL.play("collision.wav");
                bomb.removeFromWorld();
                boss.getComponent(HealthIntComponent.class).damage(BOMB_DAMAGE);
                score+=20;
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.BOSS, EntityType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity boss, Entity player) {
                FXGL.play("collision.wav");
                player.getComponent(PlayerResetComponent.class).reset(PLAYER_COLLISION_DAMAGE);
                boss.getComponent(HealthIntComponent.class).damage(PLAYER_COLLISION_DAMAGE);
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Egg, EntityType.PLAYER) {
            @Override
            protected void onCollisionBegin(Entity egg, Entity player) {
                FXGL.play("collision.wav");
                player.getComponent(PlayerResetComponent.class).reset(PLAYER_COLLISION_DAMAGE);
                egg.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Egg, EntityType.BOMB) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
                a.removeFromWorld();
                b.removeFromWorld();
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.Egg, EntityType.BULLET) {
            @Override
            protected void onCollisionBegin(Entity a, Entity b) {
                a.removeFromWorld();
                b.removeFromWorld();
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        if(PlayerController.getCurrentPlayer() == null){
            showLoginMenu();
        }

        if (player.getComponent(HealthIntComponent.class).getValue() <= 0) {
            showEndDialouge("YOU LOST!!!!");
        }
        if (boss.getComponent(HealthIntComponent.class).getValue() <= 0) {
            showEndDialouge("YOU WON!!!");
        }

        if (boss.getComponent(HealthIntComponent.class).getValue() <= BOSS_MAX_HEALTH/2 && level ==1){
            boss.removeComponent(AnimationComponent.class);
            level++;
            boss.addComponent(new BossMoveComponent());
            boss.getComponent(BossMoveComponent.class).setActive(true);
            boss.getViewComponent().clearChildren();
            boss.getViewComponent().addChild(new ImageView(getAssetLoader().loadImage("boss2.png")));
        }
        playerHPBar.setCurrentValue(player.getComponent(HealthIntComponent.class).getValue());
        if (playerHPBar.getCurrentValue() < PLAYER_MAX_HEALTH / 3) playerHPBar.setFill(Color.ORANGE);
        bossHPBar.setCurrentValue(boss.getComponent(HealthIntComponent.class).getValue());
        set("time", Objects.requireNonNull(secondsToTimeFormat((int) (getGameTimer().getNow() - startTime))));
        set("score", "Score: " + score);

    }

    private void showLoginMenu(){
        VBox loginBox = new VBox(5);
        loginBox.setAlignment(Pos.CENTER);
        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        Text loginMessage = new Text();
        loginBox.setFillWidth(true);

        Text username = new Text("username:");

        usernameField.setPromptText("username");
        passwordField.setPromptText("password");
        Text password = new Text("password:");
        password.setFont(Font.font(16));
        username.setFont(Font.font(16));
        username.setFill(new Color(1,1,1,1));
        password.setFill(new Color(1,1,1,1));
        Button loginBtn = FXGL.getUIFactoryService().newButton("Login");
        Button registerBtn = FXGL.getUIFactoryService().newButton("Register");
        Button guestBtn = FXGL.getUIFactoryService().newButton("Guest");

        loginBtn.setOnMouseClicked(e ->
        {
            MainMenuController.login(usernameField.getText(), passwordField.getText(), loginMessage);
            if (PlayerController.getCurrentPlayer() != null){
                FXGL.getGameController().startNewGame();
                PlayerController.writePlayersToJson();
            }
        });
        registerBtn.setOnMouseClicked(e ->
        {
            MainMenuController.register(usernameField.getText(), passwordField.getText(), loginMessage);
            if (PlayerController.getCurrentPlayer() != null){
                FXGL.getGameController().startNewGame();
                PlayerController.writePlayersToJson();

            }
        });
        guestBtn.setOnMouseClicked(e ->
        {
            MainMenuController.guestLogin(loginMessage);
            if (PlayerController.getCurrentPlayer() != null){
                FXGL.getGameController().startNewGame();
            }
        });

        VBox buttons = new VBox(5);
        buttons.getChildren().addAll(loginBtn, registerBtn, guestBtn);
        buttons.setAlignment(Pos.CENTER);

        loginMessage.setTextAlignment(TextAlignment.LEFT);
        Paint paint = new Color(1,.20,.20,1);
        loginMessage.setFill(paint);
        StackPane stackPane = new StackPane(loginMessage);
        loginBox.setMaxWidth(150);
        loginMessage.setFont(Font.font(16));
        loginBox.getChildren().addAll(username, usernameField, password, passwordField, new Text(""),
                buttons,new Text(""), stackPane);
        FXGL.getDialogService().showBox("LOGIN BOY!", loginBox);
    }
    private void showEndDialouge(String message) {
        PlayerController.updatePlayerScore(score, (int) (FXGL.getGameTimer().getNow()-startTime));
        FXGL.getAudioPlayer().pauseAllMusic();
        FXGL.play("gameOver.wav");
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        ImageView imageView = new ImageView(getAssetLoader().loadImage("bossEnd.png"));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        Text text = new Text(message + "\n Score: " + score + "\n" +
                secondsToTimeFormat((int) (getGameTimer().getNow() - startTime)));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(16));
        text.setFill(new Color(1, 1, 1, 1));
        Button newGameBtn = FXGL.getUIFactoryService().newButton("NEW GAME");

        Button mainMenuBtn = FXGL.getUIFactoryService().newButton("MAIN MENU");
        newGameBtn.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());
        mainMenuBtn.setOnMouseClicked(e -> FXGL.getGameController().gotoMainMenu());
        HBox btns = new HBox(newGameBtn, mainMenuBtn);
        btns.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(imageView, text, bossHPBar, btns);
        FXGL.getDialogService().showBox("GAME OVER!", vBox);

    }

    private String secondsToTimeFormat(int seconds) {
        String time = "elapsed time:  ";
        int minute = seconds / 60;
        int second = seconds % 60;
        if (minute > 9) time += minute + ":";
        else time += "0" + minute + ":";
        if (second > 9) time += second;
        else time += "0" + second;
        return time;
    }

    @Override
    protected void initUI() {
//        FXGL.getGameScene().setBackgroundRepeat("egg.png");
        addHPBars();
        addVariables();

    }
    private void addHPBars(){
        playerHPBar = new ProgressBar();
        playerHPBar.setMinValue(0);
        playerHPBar.setMaxValue(PLAYER_MAX_HEALTH);
        playerHPBar.setCurrentValue(PLAYER_MAX_HEALTH);
        playerHPBar.setWidth(300);
        playerHPBar.setLabelVisible(true);
        playerHPBar.setLabelPosition(Position.RIGHT);
        playerHPBar.setFill(Color.GREEN);
        playerHPBar.setLayoutY(5);

        bossHPBar = new ProgressBar();
        bossHPBar.setMinValue(0);
        bossHPBar.setMaxValue(BOSS_MAX_HEALTH);
        bossHPBar.setCurrentValue(BOSS_MAX_HEALTH);
        bossHPBar.setWidth(300);
        bossHPBar.setLabelVisible(true);
        bossHPBar.setLabelPosition(Position.LEFT);
        bossHPBar.setFill(Color.RED);
        bossHPBar.setLayoutX(WIDTH - 400);
        bossHPBar.setLayoutY(5);
        getGameScene().addUINodes(bossHPBar);
        getGameScene().addUINodes(playerHPBar);
    }

    private void addVariables(){
        Text textPixel = new Text();
        textPixel.setX((WIDTH / 2) - 40);
        textPixel.setY(15);
        textPixel.textProperty().bind(getWorldProperties().stringProperty("time"));
        textPixel.setFill(Color.WHITE);
        textPixel.setFont(Font.font(14));
        FXGL.getGameScene().addUINode(textPixel);
        Text text2Pixel = new Text();
        text2Pixel.setX((WIDTH / 2) - 40);
        text2Pixel.setY(30);
        text2Pixel.setFill(Color.WHITE);
        text2Pixel.setFont(Font.font(14));
        text2Pixel.textProperty().bind(getWorldProperties().stringProperty("score"));
        FXGL.getGameScene().addUINode(text2Pixel);
    }

}
