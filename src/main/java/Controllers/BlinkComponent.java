package Controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import enums.Variables;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.util.Duration;

public class BlinkComponent extends Component {
    private int deathTime;

    public void blink(){
        deathTime = (int) FXGL.getGameTimer().getNow();
        entity.getComponent(CollidableComponent.class).setValue(false);
        FXGL.getGameTimer().runOnceAfter(()->{entity.getComponent(CollidableComponent.class).setValue(true);}, Duration.seconds(Variables.BLINK_DURATION));
        for (int i =0; i< 3; i++){
            FXGL.getGameTimer().runOnceAfter(()->{entity.setVisible(false);}, Duration.millis(i*300));
            FXGL.getGameTimer().runOnceAfter(()->{entity.setVisible(true);}, Duration.millis(i*300+30));
        }


    }
}
