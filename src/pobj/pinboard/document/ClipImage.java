package pobj.pinboard.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ClipImage implements Clip {

    private double left;
    private double top;
    private double right;
    private double bottom;
    private Image img;

    public ClipImage(double left, double top, File filename) throws FileNotFoundException {
        this.left = left;
        this.top = top;
        this.img = new Image(new FileInputStream(filename.getAbsolutePath()));
        this.right = left + img.getWidth();
        this.bottom = top + img.getHeight();
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(img, left, top);
    }

    @Override
    public double getTop() { return top; }

    @Override
    public double getLeft() { return left; }

    @Override
    public double getBottom() { return bottom; }

    @Override
    public double getRight() { return right; }

    @Override
    public void setGeometry(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void move(double x, double y) {
        left += x;
        right += x;
        top += y;
        bottom += y;
    }

    @Override
    public boolean isSelected(double x, double y) {
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    @Override
    public void setColor(javafx.scene.paint.Color c) {
        // Non applicable pour une image
    }

    @Override
    public javafx.scene.paint.Color getColor() {
        return null; // Non applicable
    }

    @Override
    public Clip copy() {
        try {
            return new ClipImage(left, top, new File(img.getUrl().replace("file:/", "")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
