package Views;

import Controllers.PlayerController;
import Models.Player;
import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.FXGLScene;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import enums.Variables;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.*;

public class CupHeadMainMenu extends FXGLMenu {
    private MediaPlayer mediaPlayer;
    private VBox menuBox = new VBox(15);
    private VBox loginBox = new VBox();
    private HBox loginMenuBox = new HBox();
    protected static TextField usernameField = new TextField();
    protected static TextField passwordField = new TextField();
    protected static Text loginMessage = new Text();

    private Effect shadow = new DropShadow(5, Color.BLACK);
    private Effect blur = new BoxBlur(1,1,1);

    @Override
    public void onCreate() {
        FXGL.getAudioPlayer().stopAllSoundsAndMusic();
        FXGL.getAudioPlayer().loopMusic(FXGL.getAssetLoader().loadMusic("menuMusic.wav"));
        super.onCreate();
    }

    @Override
    public void onDestroy() {

        FXGL.getAudioPlayer().stopAllMusic();
        super.onDestroy();
    }

    public CupHeadMainMenu() {

        super(MenuType.MAIN_MENU);

        loadBackground();
        addLogo();
        getContentRoot().getChildren().add(loginMenuBox);
        loginMenuBox.getChildren().addAll(menuBox);
        initMenuBox();
//        initLoginBox();
        getContentRoot().getChildren().add(menuBox);
    }


    private void initMenuBox() {
        menuBox.setTranslateX(50);
        menuBox.setTranslateY(Variables.HEIGHT/2+70);
        addMenuButtons();
        FXGLScene fxglScene = new FXGLScene() {
            @Override
            public void bindSize(@NotNull DoubleProperty scaledWidth, @NotNull DoubleProperty scaledHeight, @NotNull DoubleProperty scaleRatioX, @NotNull DoubleProperty scaleRatioY) {
                super.bindSize(scaledWidth, scaledHeight, scaleRatioX, scaleRatioY);
            }
        };
        FXGLDefaultMenu.MenuContent menuContent = new FXGLDefaultMenu.MenuContent();
        menuContent.setVisible(true);
        fxglScene.activeProperty().setValue(true);
    }

    private void initLoginBox(){
        loginBox.setTranslateX(250);
        loginBox.setFillWidth(true);
        loginBox.setMinWidth(170);
        loginBox.setTranslateY(Variables.HEIGHT/2+70);
        Text username = new Text("username:");
        usernameField.setPromptText("username");
        passwordField.setPromptText("password");
        Text password = new Text("password:");
        Text login = new Text("Login");
        Text register = new Text("Register");
        Text guest = new Text("Guest");
//        login.setOnMouseClicked(e -> MainMenuController.login());
//        register.setOnMouseClicked(e -> MainMenuController.register());
//        guest.setOnMouseClicked(e -> MainMenuController.guestLogin());
        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(login, register, guest);
        buttons.setAlignment(Pos.CENTER);
        loginMessage.setTextAlignment(TextAlignment.LEFT);
        Paint paint = new Color(1,.20,.20,1);
        loginMessage.setFill(paint);
        StackPane stackPane = new StackPane(loginMessage);

        loginMessage.setFont(Font.font(16));
        loginBox.getChildren().addAll(username, usernameField, password, passwordField, new Text(""),
                buttons,new Text(""), stackPane);
    }

    private void newMenu() {
        getContentRoot().getChildren().clear();
    }

    private void addMenuButtons(){
        addMenuItem("NewGame", this::fireNewGame);
        addMenuItem("ScoreBoard", this::showScoreBoard);
        addMenuItem("Exit", this::fireExit);
    }
    private void loadBackground(){
        ImageView imageView;
        imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/textures/MainMenuBg.png")).toExternalForm()));
        imageView.setFitWidth(Variables.WIDTH+50);
        imageView.setFitHeight(Variables.HEIGHT);
        imageView.setX(0);
        getContentRoot().getChildren().addAll(imageView);
    }

    private void addLogo(){
        ImageView imageView;
        imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/textures/logo.png")).toExternalForm()));
        imageView.setFitWidth(500);
        imageView.setFitHeight(230);
        imageView.setX(10);
        imageView.setY(60);
        getContentRoot().getChildren().addAll(imageView);
    }

    private void showScoreBoard(){
        ArrayList<Player> players = PlayerController.getPlayers();
        ArrayList<Text> texts = new ArrayList<>();
        players.sort(Comparator.comparing(Player::getHighScore).thenComparing(Player::getFinishTime));
        VBox vBox = new VBox();
        for (Player player : PlayerController.getPlayers()) {
            vBox.getChildren().add(new HBox(20,FXGL.getUIFactoryService().newText(player.getUsername()),
                    FXGL.getUIFactoryService().newText(player.getHighScore()+""),
                    FXGL.getUIFactoryService().newText(player.getFinishTime()+"")));
        }
        FXGL.getDialogService().showBox("ScoreBoard", vBox);
    }

    private void addMenuItem(String name, Runnable runnable){

        Text text = new Text(name);
        text.setFont(Font.font(24));
        text.setOnMouseClicked(e -> runnable.run());
        text.effectProperty().bind(
                Bindings.when(text.hoverProperty())
                        .then(shadow)
                        .otherwise(blur)
        );
        menuBox.getChildren().add(text);
    }
}
