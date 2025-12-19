package pobj.pinboard.editor;

import java.util.Stack;
import pobj.pinboard.editor.commands.Command;

public class CommandStack {

    private Stack<Command> undo = new Stack<>();
    private Stack<Command> redo = new Stack<>();

    /** Ajoute une commande et l’exécute */
    @SuppressWarnings("exports")
	public void addCommand(Command cmd) {
        cmd.execute();
        undo.push(cmd);
        redo.clear(); // toute nouvelle commande invalide le redo
    }

    /** Annule la dernière commande */
    public void undo() {
        if (!undo.isEmpty()) {
            Command cmd = undo.pop();
            cmd.undo();
            redo.push(cmd);
        }
    }

    /** Refait la dernière commande annulée */
    public void redo() {
        if (!redo.isEmpty()) {
            Command cmd = redo.pop();
            cmd.execute();
            undo.push(cmd);
        }
    }

    public boolean isUndoEmpty() {
        return undo.isEmpty();
    }

    public boolean isRedoEmpty() {
        return redo.isEmpty();
    }
}
