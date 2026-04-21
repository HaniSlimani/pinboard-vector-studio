package pobj.pinboard.editor.tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.Selection;
import pobj.pinboard.editor.commands.CommandMove;

public class ToolSelection implements Tool {

    private double lastX, lastY;

    @Override
    public void press(EditorInterface i, MouseEvent e) {
        Selection s = i.getSelection();
        if (!e.isShiftDown()) s.select(i.getBoard(), e.getX(), e.getY());
        else s.toogleSelect(i.getBoard(), e.getX(), e.getY());
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void drag(EditorInterface i, MouseEvent e) {
        double dx = e.getX() - lastX;
        double dy = e.getY() - lastY;
        for (Clip c : i.getSelection().getContents()) {
            new CommandMove(i, c, dx, dy).execute(); // <-- passe par CommandMove
        }
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void release(EditorInterface i, MouseEvent e) { }

    @Override
    public void drawFeedback(EditorInterface i, GraphicsContext gc) {
        i.getSelection().draw(gc);
    }

    @Override
    public String getName(EditorInterface editor) {
        return "Selection Tool";
    }
}
