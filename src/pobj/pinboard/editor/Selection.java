package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;

public class Selection {

    private List<Clip> contents = new ArrayList<>();

    public void clear() {
        contents.clear();
    }

    @SuppressWarnings("exports")
	public List<Clip> getContents() {
        return contents;
    }

    /** Sélection simple */
    public void select(@SuppressWarnings("exports") Board board, double x, double y) {
        clear();

        for (Clip c : board.getContents()) {
            if (c.isSelected(x, y)) {
                contents.add(c);
                return; // seulement le premier clip
            }
        }
    }

    /** Sélection multiple (toggle) */
    public void toogleSelect(@SuppressWarnings("exports") Board board, double x, double y) {

        for (Clip c : board.getContents()) {
            if (c.isSelected(x, y)) {
                if (contents.contains(c)) {
                    contents.remove(c);
                } else {
                    contents.add(c);
                }
                return; // seulement le premier clip
            }
        }

        // Si aucun clip ne contient (x,y) → toggle ne fait rien
    }

    /** Dessin du feedback : rectangle englobant */
    public void drawFeedback(GraphicsContext gc) {
        if (contents.isEmpty()) return;

        double left = Double.MAX_VALUE;
        double top = Double.MAX_VALUE;
        double right = Double.MIN_VALUE;
        double bottom = Double.MIN_VALUE;

        for (Clip c : contents) {
            if (c.getLeft() < left) left = c.getLeft();
            if (c.getTop() < top) top = c.getTop();
            if (c.getRight() > right) right = c.getRight();
            if (c.getBottom() > bottom) bottom = c.getBottom();
        }

        gc.setStroke(Color.BLUE);
        gc.strokeRect(left, top, right - left, bottom - top);
    }
    
    public void draw(GraphicsContext gc) {
        if (getContents().isEmpty()) return;

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1.5);

        for (Clip c : getContents()) {
            gc.strokeRect(
                c.getLeft(),
                c.getTop(),
                c.getRight() - c.getLeft(),
                c.getBottom() - c.getTop()
            );
        }
    }

}
