package Controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import enums.Variables;

import java.util.Random;

public class BossMoveComponent extends Component {
    private double x = 0;
    private double y = 0;
    private int i =0 ;
    private boolean isActive = false;
    public void rotate(){
        entity.setRotation(0);
    }

    @Override
    public void onUpdate(double tpf) {
        if (isActive){
            i = (i+1) % 5;
            if (i == 1){
                entity.translateY(y);
                if (entity.getX() + x > Variables.WIDTH/2-250){
                    entity.translateX(x);
                }
            }
            if ((int) FXGL.getGameTimer().getNow() % 5 == 1) {
                x = FXGL.random(-2,2);
                y = FXGL.random(-2,2);
            }
        }
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
}
