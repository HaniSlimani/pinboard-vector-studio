package pobj.pinboard.document;

import java.io.Serializable;

public abstract class AbstractClip implements Clip, Serializable {
    private static final long serialVersionUID = 1L;

    private double left, top, right, bottom;
    private MyColor color; // notre classe sérialisable

    public AbstractClip(double left, double top, double right, double bottom, MyColor color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = color;
    }

    public double getLeft() { return left; }
    public double getTop() { return top; }
    public double getRight() { return right; }
    public double getBottom() { return bottom; }

    public void setGeometry(double left, double top, double right, double bottom) {
        this.left = left; this.top = top; this.right = right; this.bottom = bottom;
    }

    public void move(double dx, double dy) {
        left += dx; right += dx; top += dy; bottom += dy;
    }

    public MyColor getMyColor() { return color; }
    public void setMyColor(MyColor color) { this.color = color; }
}

