package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandGroup implements Command {

    private EditorInterface editor;
    private ClipGroup group;
    private List<Clip> oldClips;

    public CommandGroup(EditorInterface editor, List<Clip> clips) {
        this.editor = editor;
        this.oldClips = new ArrayList<>(clips);
        this.group = new ClipGroup();
        for (Clip c : clips) {
            group.addClip(c);
        }
    }

    @Override
    public void execute() {
        for (Clip c : oldClips) {
            editor.getBoard().removeClip(c);
        }
        editor.getBoard().addClip(group);
    }

    @Override
    public void undo() {
        editor.getBoard().removeClip(group);
        for (Clip c : oldClips) {
            editor.getBoard().addClip(c);
        }
    }
}
