package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.Selection;

public class ToolSelection implements Tool {

    private double lastX, lastY;

    @Override
    public void press(EditorInterface i, MouseEvent e) {
        Selection s = i.getSelection();

        // Cas sélection simple
        if (!e.isShiftDown()) {
            s.select(i.getBoard(), e.getX(), e.getY());
        }
        // Cas toggle (sélection multiple)
        else {
            s.toogleSelect(i.getBoard(), e.getX(), e.getY());
        }

        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void drag(EditorInterface i, MouseEvent e) {
        Selection s = i.getSelection();

        double dx = e.getX() - lastX;
        double dy = e.getY() - lastY;

        // Déplace tous les clips sélectionnés
        for (Clip c : s.getContents()) {
            c.move(dx, dy);
        }

        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void release(EditorInterface i, MouseEvent e) {
        // rien à faire
    }

    @Override
    public void drawFeedback(EditorInterface i, GraphicsContext gc) {
        // Le feedback est géré par Selection.draw()
        i.getSelection().draw(gc);
    }

    @Override
    public String getName(EditorInterface editor) {
        return "Selection Tool";
    }
}
