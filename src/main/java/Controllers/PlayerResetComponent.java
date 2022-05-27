package Controllers;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Component;
import enums.Variables;

public class PlayerResetComponent extends Component {
    public void reset(int damage){
        entity.getComponent(HealthIntComponent.class).damage(damage);
        entity.setX(Variables.WIDTH/10);
        entity.setY(Variables.HEIGHT/2);
        entity.getComponent(BlinkComponent.class).blink();
    }
}
