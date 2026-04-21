package pobj.pinboard.document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class Board implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Clip> contents;

    public Board() {
        contents = new ArrayList<>();
    }

    public List<Clip> getContents() {
        return contents;
    }

    public void addClip(Clip clip) {
        contents.add(clip);
    }

    public void addClip(List<Clip> clips) {
        contents.addAll(clips);
    }

    public void removeClip(Clip clip) {
        contents.remove(clip);
    }

    public void removeClip(List<Clip> clips) {
        contents.removeAll(clips);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (Clip c : contents) {
            c.draw(gc);
        }
    }
}
