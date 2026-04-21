package pobj.pinboard.editor;

import java.util.ArrayList;
import java.util.List;
import pobj.pinboard.document.Clip;

public class Clipboard {

    private static final Clipboard instance = new Clipboard();
    private List<Clip> contents = new ArrayList<>();
    private List<ClipboardListener> listeners = new ArrayList<>();

    private Clipboard() { }

    public static Clipboard getInstance() {
        return instance;
    }

    public void copyToClipboard(@SuppressWarnings("exports") List<Clip> clips) {
        contents.clear();
        for (Clip c : clips) {
            contents.add(c.copy());
        }
        notifyListeners();
    }

    @SuppressWarnings("exports")
	public List<Clip> copyFromClipboard() {
        List<Clip> result = new ArrayList<>();
        for (Clip c : contents) {
            result.add(c.copy());
        }
        return result;
    }

    public void clear() {
        contents.clear();
        notifyListeners();
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    // --- Observateur ---
    public void addListener(ClipboardListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ClipboardListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ClipboardListener l : listeners) {
            l.clipboardChanged();
        }
    }
}

