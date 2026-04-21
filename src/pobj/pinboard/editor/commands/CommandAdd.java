package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.EditorInterface;

public class CommandAdd implements Command {

    private EditorInterface editor;
    private List<Clip> clips;

    public CommandAdd(EditorInterface editor, Clip clip) {
        this.editor = editor;
        this.clips = new ArrayList<>();
        this.clips.add(clip);
    }

    public CommandAdd(EditorInterface editor, List<Clip> clips) {
        this.editor = editor;
        this.clips = new ArrayList<>(clips);
    }

    @Override
    public void execute() {
        editor.getBoard().addClip(clips);
    }

    @Override
    public void undo() {
        editor.getBoard().removeClip(clips);
    }
}
