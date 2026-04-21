package pobj.pinboard.editor.commands.test;

import javafx.scene.paint.Color;
import pobj.pinboard.document.Board;
import pobj.pinboard.editor.CommandStack;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.Selection;

// Helper class for all Command tests

public class CommandTest {

	public class MockEditor implements EditorInterface {
		private Board board = new Board();
		private Selection selection = new Selection();
		private CommandStack command = new CommandStack();
		public Board getBoard() { return board; }
		public Selection getSelection() { return selection; }
		public CommandStack getUndoStack() { return command; }
		public Color getCurrentColor() { return Color.RED; }
	};
	
}
