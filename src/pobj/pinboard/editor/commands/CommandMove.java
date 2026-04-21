package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandMove implements Command {

    private Clip clip;
    private EditorInterface editor;
    private double dx, dy; // déplacement demandé

    public CommandMove(EditorInterface editor, Clip clip, double dx, double dy) {
        this.editor = editor;
        this.clip = clip;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        clip.move(dx, dy);
    }

    @Override
    public void undo() {
        clip.move(-dx, -dy);
    }
}
