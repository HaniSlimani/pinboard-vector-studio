package pobj.pinboard.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ClipImage implements Clip, Serializable {
    private static final long serialVersionUID = 1L;

    private double left;
    private double top;
    private double right;
    private double bottom;
    private String filename;       // sérialisable
    private transient Image img;   // non sérialisable

    public ClipImage(double left, double top, File file) throws FileNotFoundException {
        this.left = left;
        this.top = top;
        this.filename = file.getAbsolutePath();
        this.img = new Image(new FileInputStream(file));
        this.right = left + img.getWidth();
        this.bottom = top + img.getHeight();
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (img == null) {
            try { img = new Image(new FileInputStream(filename)); }
            catch (FileNotFoundException e) { e.printStackTrace(); }
        }
        gc.drawImage(img, left, top);
    }

    @Override
    public double getLeft() { return left; }

    @Override
    public double getTop() { return top; }

    @Override
    public double getRight() { return right; }

    @Override
    public double getBottom() { return bottom; }

    @Override
    public void setGeometry(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void move(double dx, double dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
    }

    @Override
    public boolean isSelected(double x, double y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    @Override
    public void setColor(Color c) {
        // Non applicable pour une image
    }

    @Override
    public Color getColor() {
        return null; // Non applicable pour une image
    }

    @Override
    public Clip copy() {
        try {
            return new ClipImage(left, top, new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
