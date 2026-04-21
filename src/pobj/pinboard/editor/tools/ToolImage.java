package pobj.pinboard.editor.tools;

import java.io.File;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import pobj.pinboard.document.ClipImage;
import pobj.pinboard.editor.EditorInterface;
import pobj.pinboard.editor.commands.CommandAdd;

public class ToolImage implements Tool {

    private ClipImage clip;
    private File file;

    @Override
    public void press(EditorInterface i, MouseEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select an image");
        file = chooser.showOpenDialog(null);
        if (file != null) {
            try {
                clip = new ClipImage(e.getX(), e.getY(), file);
            } catch (Exception ex) {
                ex.printStackTrace();
                clip = null;
            }
        }
    }

    @Override
    public void drag(EditorInterface i, MouseEvent e) {
        if (clip != null) {
            clip.setGeometry(e.getX(), e.getY(), e.getX() + (clip.getRight() - clip.getLeft()),
                             e.getY() + (clip.getBottom() - clip.getTop()));
        }
    }

    @Override
    public void release(EditorInterface i, MouseEvent e) {
        if (clip != null) {
            new CommandAdd(i, clip).execute(); // <-- utilisation de la commande
            clip = null;
        }
    }

    @Override
    public void drawFeedback(EditorInterface i, GraphicsContext gc) {
        if (clip != null) clip.draw(gc);
    }

    @Override
    public String getName(EditorInterface editor) {
        return "Image";
    }
}
