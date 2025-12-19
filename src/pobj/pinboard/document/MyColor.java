package pobj.pinboard.document;

import java.io.Serializable;
import javafx.scene.paint.Color;

public class MyColor implements Serializable {
    private static final long serialVersionUID = 1L;
    private double red, green, blue;

    public MyColor(double r, double g, double b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    public MyColor(Color c) {
        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
    }

    public Color toFXColor() {
        return new Color(red, green, blue, 1.0);
    }
}
