package pobj.pinboard.editor.commands;

import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandDelete implements Command {

    private EditorInterface editor;
    private Clip clip;

    public CommandDelete(EditorInterface editor, Clip clip) {
        this.editor = editor;
        this.clip = clip;
    }

    @Override
    public void execute() {
        Board board = editor.getBoard();
        board.getContents().remove(clip);
    }

    @Override
    public void undo() {
        Board board = editor.getBoard();
        board.getContents().add(clip);
    }
}
