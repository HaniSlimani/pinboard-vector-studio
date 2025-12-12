package pobj.pinboard.editor;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.editor.tools.*;

import java.util.List;

public class EditorWindow implements EditorInterface, ClipboardListener {

    private Board board;
    private Stage stage;
    private Canvas canvas;
    private Tool currentTool;
    private Label statusLabel;
    private Selection selection = new Selection();
    private MenuItem miPaste;
    private Color currentColor = Color.BLACK;

    public EditorWindow(Stage stage) {
        this.stage = stage;
        this.board = new Board();
        this.currentTool = new ToolRect();  // outil par défaut

        VBox root = new VBox();

        // --- MENU FILE ---
        Menu menuFile = new Menu("File");
        MenuItem miNew = new MenuItem("New");
        MenuItem miClose = new MenuItem("Close");
        miNew.setOnAction(e -> new EditorWindow(new Stage()));
        miClose.setOnAction(e -> stage.close());
        menuFile.getItems().addAll(miNew, miClose);

        // --- MENU TOOLS ---
        Menu menuTools = new Menu("Tools");
        MenuItem miRect = new MenuItem("Rectangle");
        MenuItem miEllipse = new MenuItem("Ellipse");
        MenuItem miImage = new MenuItem("Image");
        MenuItem miSelect = new MenuItem("Select");

        miRect.setOnAction(e -> setTool(new ToolRect()));
        miEllipse.setOnAction(e -> setTool(new ToolEllipse()));
        miImage.setOnAction(e -> setTool(new ToolImage()));
        miSelect.setOnAction(e -> setTool(new ToolSelection()));
        menuTools.getItems().addAll(miRect, miEllipse, miImage, miSelect);

        // --- MENU EDIT ---
        Menu menuEdit = new Menu("Edit");
        MenuItem miCopy = new MenuItem("Copy");
        miPaste = new MenuItem("Paste");
        MenuItem miDelete = new MenuItem("Delete");

        miCopy.setOnAction(e -> Clipboard.getInstance().copyToClipboard(selection.getContents()));
        miPaste.setOnAction(e -> {
            List<Clip> copied = Clipboard.getInstance().copyFromClipboard();
            for (Clip c : copied) board.addClip(c);
            draw();
        });
        miDelete.setOnAction(e -> {
            List<Clip> selected = selection.getContents();
            for (Clip c : selected) board.removeClip(c);
            selection.clear();
            draw();
        });

        menuEdit.getItems().addAll(miCopy, miPaste, miDelete);

        MenuBar menuBar = new MenuBar(menuFile, menuTools, menuEdit);
        root.getChildren().add(menuBar);

        // --- TOOLBAR ---
        ToolBar toolbar = new ToolBar();
        Button btnRect = new Button("Rect");
        Button btnEllipse = new Button("Ellipse");
        Button btnImg = new Button("Img...");
        Button btnSelect = new Button("Select");

        btnRect.setOnAction(e -> setTool(new ToolRect()));
        btnEllipse.setOnAction(e -> setTool(new ToolEllipse()));
        btnImg.setOnAction(e -> setTool(new ToolImage()));
        btnSelect.setOnAction(e -> setTool(new ToolSelection()));
        toolbar.getItems().addAll(btnRect, btnEllipse, btnImg, btnSelect);
        root.getChildren().add(toolbar);

        // --- PALETTE DE COULEURS ---
        ToolBar colorBar = new ToolBar();
        Color[] colors = { Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW };
        for (Color c : colors) {
            Button btn = new Button();
            btn.setStyle("-fx-background-color: " + toRgbString(c) + "; -fx-min-width: 25; -fx-min-height: 25;");
            btn.setOnAction(e -> currentColor = c);
            colorBar.getItems().add(btn);
        }
        root.getChildren().add(colorBar);

        // --- CANVAS ---
        canvas = new Canvas(800, 600);
        canvas.setOnMousePressed(e -> { currentTool.press(this, e); draw(); });
        canvas.setOnMouseDragged(e -> { currentTool.drag(this, e); draw(); });
        canvas.setOnMouseReleased(e -> { currentTool.release(this, e); draw(); });
        root.getChildren().add(canvas);

        // Status bar
        statusLabel = new Label(currentTool.getName(this));
        root.getChildren().add(new Separator());
        root.getChildren().add(statusLabel);

        // Enregistrement comme observateur du Clipboard
        Clipboard.getInstance().addListener(this);
        clipboardChanged(); // état initial du bouton Paste

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("PinBoard Editor");
        stage.show();

        draw();
    }

    private void setTool(Tool tool) {
        this.currentTool = tool;
        statusLabel.setText(tool.getName(this));
    }

    /** Dessin principal */
    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        board.draw(gc);
        if (currentTool != null) currentTool.drawFeedback(this, gc);
        selection.draw(gc);
    }

    /** Observateur Clipboard */
    @Override
    public void clipboardChanged() {
        miPaste.setDisable(Clipboard.getInstance().isEmpty());
    }

    /** Conversion couleur */
    private String toRgbString(Color c) {
        return "rgb(" + (int)(c.getRed()*255) + "," + (int)(c.getGreen()*255) + "," + (int)(c.getBlue()*255) + ")";
    }

    // --- EditorInterface ---
    @Override
    public Board getBoard() { return board; }
    @Override
    public Selection getSelection() { return selection; }
    @Override
    public CommandStack getUndoStack() { return null; }

    // --- Couleur courante pour outils ---
    public Color getCurrentColor() { return currentColor; }
}
