package Controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.util.Duration;

public class AnimationComponent extends Component {

    private int speed = 0;

    private AnimatedTexture texture;
    private AnimationChannel animFly;

    public AnimationComponent(String fileName, int framesPerRow, int frameWidth, int frameHeight, Duration duration, int startFrame, int endFrame) {
        animFly = new AnimationChannel(FXGL.image(fileName), framesPerRow, frameWidth, frameHeight, duration, startFrame, endFrame);
        texture = new AnimatedTexture(animFly);
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        texture.loopAnimationChannel(animFly);
    }
}