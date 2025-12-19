package pobj.pinboard.document;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;

public class ClipRect extends AbstractClip implements Serializable {
    private static final long serialVersionUID = 1L;

    public ClipRect(double left, double top, double right, double bottom, MyColor color) {
        super(left, top, right, bottom, color); // <-- ici on passe le MyColor au parent
    }

    @Override
    public void draw(GraphicsContext ctx) {
        ctx.setFill(getMyColor().toFXColor());
        ctx.fillRect(getLeft(), getTop(),
                     getRight() - getLeft(),
                     getBottom() - getTop());
    }

    @Override
    public Clip copy() {
        return new ClipRect(getLeft(), getTop(), getRight(), getBottom(), getMyColor());
    }

    @Override
    public void setColor(javafx.scene.paint.Color c) {
        setMyColor(new MyColor(c));
    }

    @Override
    public javafx.scene.paint.Color getColor() {
        return getMyColor().toFXColor();
    }

    @Override
    public boolean isSelected(double x, double y) {
        return x >= getLeft() && x <= getRight() &&
               y >= getTop() && y <= getBottom();
    }
}
