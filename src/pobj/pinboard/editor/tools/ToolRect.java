package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipRect;
import pobj.pinboard.editor.EditorInterface;

public class ToolRect implements Tool {

    private double startX, startY, endX, endY;

    @Override
    public void press(EditorInterface i, MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        endX = startX;
        endY = startY;
    }

    @Override
    public void drag(EditorInterface i, MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
    }

    @Override
    public void release(EditorInterface i, MouseEvent e) {
        endX = e.getX();
        endY = e.getY();
        Clip c = new ClipRect(
            Math.min(startX, endX), Math.min(startY, endY),
            Math.max(startX, endX), Math.max(startY, endY),
            i.getCurrentColor()  // <-- utilise l'interface, pas de cast
        );
        i.getBoard().addClip(c);
    }

    @Override
    public void drawFeedback(EditorInterface i, GraphicsContext gc) {
        gc.setStroke(i.getCurrentColor());
        gc.strokeRect(Math.min(startX, endX), Math.min(startY, endY),
                      Math.abs(endX - startX), Math.abs(endY - startY));
    }

    @Override
    public String getName(EditorInterface editor) {
        return "Rectangle Tool";
    }
}

