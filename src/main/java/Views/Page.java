package Views;

import javafx.scene.text.Text;

import java.awt.*;

public class Page {
    public static void setTextElement(Text text, String message){
        text.setText(message);
        text.setVisible(true);
    }
}
