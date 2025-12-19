package pobj.pinboard.document;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ClipGroup extends AbstractClip implements Composite {

    private static final long serialVersionUID = 1L;
    private List<Clip> clips = new ArrayList<>();

    public ClipGroup() {
        super(0.0, 0.0, 0.0, 0.0, new MyColor(Color.BLACK));
    }

    @Override
    public List<Clip> getClips() {
        return clips;
    }

    @Override
    public void addClip(Clip toAdd) {
        clips.add(toAdd);
        recomputeBounds();
    }

    @Override
    public void removeClip(Clip toRemove) {
        clips.remove(toRemove);
        recomputeBounds();
    }

    private void recomputeBounds() {
        if (clips.isEmpty()) {
            setGeometry(0, 0, 0, 0);
            return;
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Clip c : clips) {
            minX = Math.min(minX, c.getLeft());
            minY = Math.min(minY, c.getTop());
            maxX = Math.max(maxX, c.getRight());
            maxY = Math.max(maxY, c.getBottom());
        }

        setGeometry(minX, minY, maxX, maxY);
    }

    @Override
    public void draw(GraphicsContext gc) {
        for (Clip c : clips) {
            c.draw(gc);
        }
    }

    @Override
    public void move(double dx, double dy) {
        for (Clip c : clips) {
            c.move(dx, dy);
        }
        recomputeBounds();
    }

    @Override
    public Clip copy() {
        ClipGroup g = new ClipGroup();
        for (Clip c : clips) {
            g.addClip(c.copy());
        }
        return g;
    }

    @Override
    public void setColor(Color c) {
        setMyColor(new MyColor(c));
    }

    @Override
    public Color getColor() {
        return getMyColor().toFXColor();
    }

    @Override
    public boolean isSelected(double x, double y) {
        return x >= getLeft() && x <= getRight() &&
               y >= getTop() && y <= getBottom();
    }
}
