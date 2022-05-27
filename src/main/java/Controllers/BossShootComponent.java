package Controllers;


import Views.BasicGameApp;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.util.Duration;

public class BossShootComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
    }

    @Override
    public void onAdded() {
        FXGL.getGameTimer().runOnceAfter(()->{FXGL.getGameTimer().runAtInterval( ()->shoot(), Duration.seconds(3));}, Duration.millis(1400));
    }

    private void shoot() {
        FXGL.spawn("egg", entity.getCenter().getX()-160, entity.getCenter().getY()-45);
    }
}
