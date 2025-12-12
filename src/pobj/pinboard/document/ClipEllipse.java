package pobj.pinboard.document;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipEllipse extends AbstractClip {

    public ClipEllipse(double left, double top, double right, double bottom, Color color) {
        super(left, top, right, bottom, color);
    }

    @Override
    public void draw(GraphicsContext ctx) {
        ctx.setFill(getColor());
        ctx.fillOval(getLeft(), getTop(),
                     getRight() - getLeft(),
                     getBottom() - getTop());
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
    public Clip copy() {
        return new ClipEllipse(getLeft(), getTop(), getRight(), getBottom(), getColor());
    }
}
