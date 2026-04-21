package pobj.pinboard.editor.commands;

import java.util.ArrayList;
import java.util.List;

import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.EditorInterface;

public class CommandUngroup implements Command {

    private EditorInterface editor;
    private ClipGroup group;
    private List<Clip> subClips;

    public CommandUngroup(EditorInterface editor, ClipGroup group) {
        this.editor = editor;
        this.group = group;
        this.subClips = new ArrayList<>(group.getClips());
    }

    @Override
    public void execute() {
        editor.getBoard().removeClip(group);
        for (Clip c : subClips) {
            editor.getBoard().addClip(c);
        }
    }

    @Override
    public void undo() {
        for (Clip c : subClips) {
            editor.getBoard().removeClip(c);
        }
        editor.getBoard().addClip(group);
    }
}
