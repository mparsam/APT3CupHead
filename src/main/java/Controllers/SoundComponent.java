package Controllers;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class SoundComponent extends Component {
    private String soundName;
    public SoundComponent(String name) {
        soundName = name;
    }

    @Override
    public void onAdded() {
        FXGL.play(soundName);
    }
}
