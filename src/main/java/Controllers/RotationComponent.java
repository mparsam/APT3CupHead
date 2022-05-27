package Controllers;

import com.almasb.fxgl.entity.component.Component;

public class RotationComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        entity.rotateBy(1);
    }
}
