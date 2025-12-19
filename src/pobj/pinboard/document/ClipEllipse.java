package pobj.pinboard.document;

import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;

public class ClipEllipse extends AbstractClip implements Serializable {
    private static final long serialVersionUID = 1L;

    private MyColor myColor;

    public ClipEllipse(double left, double top, double right, double bottom, MyColor color) {
        super(left, top, right, bottom, color); // appelle le constructeur de AbstractClip
        this.myColor = color; // <-- IMPORTANT pour éviter le NPE
    }

    @Override
    public void draw(GraphicsContext ctx) {
        ctx.setFill(myColor.toFXColor());
        ctx.fillOval(getLeft(), getTop(),
                     getRight() - getLeft(),
                     getBottom() - getTop());
    }

    @Override
    public Clip copy() {
        return new ClipEllipse(getLeft(), getTop(), getRight(), getBottom(), myColor);
    }

    @Override
    public void setColor(javafx.scene.paint.Color c) {
        this.myColor = new MyColor(c);
    }

    @Override
    public javafx.scene.paint.Color getColor() {
        return myColor.toFXColor();
    }

    @Override
    public boolean isSelected(double x, double y) {
        double cx = (getLeft() + getRight()) / 2.0;
        double cy = (getTop() + getBottom()) / 2.0;
        double rx = (getRight() - getLeft()) / 2.0;
        double ry = (getBottom() - getTop()) / 2.0;

        if (rx == 0 || ry == 0) return false;

        double dx = (x - cx) / rx;
        double dy = (y - cy) / ry;

        return dx*dx + dy*dy <= 1.0;
    }

    @Override
    public void move(double dx, double dy) {
        setGeometry(getLeft() + dx, getTop() + dy,
                    getRight() + dx, getBottom() + dy);
    }

    @Override
    public void setGeometry(double left, double top, double right, double bottom) {
        super.setGeometry(left, top, right, bottom);
    }
}

