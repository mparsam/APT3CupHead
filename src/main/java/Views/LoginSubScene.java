package Views;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.scene.SubScene;
import javafx.geometry.Point2D;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.animationBuilder;

public class LoginSubScene extends SubScene {
    public LoginSubScene() {
        Pane pane = new Pane(new TextField());
        getContentRoot().getChildren().add(new Text("HELLO"));
        getContentRoot().getChildren().addAll(new TextField());
        System.err.println("logun subScene gened");
        this.getContentRoot().getChildren().add(pane);
//        animationBuilder()
//                .onFinished(() -> {
//                    animationBuilder()
//                            .onFinished(() -> FXGL.getSceneService().popSubScene())
//                            .delay(Duration.seconds(2.0))
//                            .duration(Duration.seconds(0.5))
//                            .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
//                            .translate(getContentRoot())
//                            .from(new Point2D(0, 0))
//                            .to(new Point2D(1050, 0))
//                            .buildAndPlay(this);
//                })
//                .delay(Duration.seconds(0.3))
//                .duration(Duration.seconds(0.5))
//                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
//                .translate(getContentRoot())
//                .from(new Point2D(-1050, 0))
//                .to(new Point2D(0, 0))
//                .buildAndPlay(this);

    }

    @Override
    protected void onUpdate(double tpf) {
        System.err.println("gggg");
        super.onUpdate(tpf);
    }
}