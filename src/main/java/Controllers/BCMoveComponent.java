package Controllers;

import com.almasb.fxgl.entity.component.Component;
import enums.Variables;

public class BCMoveComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        entity.translateX(-1);
        if (entity.getX()<= -Variables.BACKgROUND_WIDTH) entity.setX(Variables.BACKgROUND_WIDTH);
    }
}
