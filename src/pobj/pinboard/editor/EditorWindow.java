package pobj.pinboard.editor;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.commands.*;
import pobj.pinboard.editor.tools.*;

import java.io.*;
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
    private CommandStack undoStack = new CommandStack();

    // Constructeur normal
    public EditorWindow(Stage stage) {
        this.stage = stage;
        this.board = new Board();
        this.currentTool = new ToolRect();
        initUI();
    }

    // Constructeur pour charger un Board existant
    public EditorWindow(Stage stage, Board loadedBoard) {
        this(stage); // appelle le constructeur normal
        this.board = loadedBoard;
        draw(); // redessine immédiatement
    }


    private void initUI() {
        VBox root = new VBox();

        /* ================= MENU FILE ================= */
        Menu menuFile = new Menu("File");
        MenuItem miNew = new MenuItem("New");
        MenuItem miClose = new MenuItem("Close");
        MenuItem miSave = new MenuItem("Save");
        MenuItem miOpen = new MenuItem("Open");

        miNew.setOnAction(e -> new EditorWindow(new Stage()));
        miClose.setOnAction(e -> stage.close());

        // Sauvegarde
        miSave.setOnAction(e -> {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Save Board");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PinBoard files", "*.pb"));
            java.io.File file = fc.showSaveDialog(stage);
            if (file != null) {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                    out.writeObject(board);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Erreur lors de la sauvegarde : " + ex.getMessage()).showAndWait();
                }
            }
        });

        // Chargement
        miOpen.setOnAction(e -> {
            javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
            fc.setTitle("Open Board");
            fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("PinBoard files", "*.pb"));
            java.io.File file = fc.showOpenDialog(stage);
            if (file != null) {
                try (java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(file))) {
                    Board loaded = (Board) in.readObject();
                    new EditorWindow(new Stage(), loaded); // nouveau EditorWindow avec le board chargé
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement : " + ex.getMessage()).showAndWait();
                }
            }
        });
        menuFile.getItems().addAll(miSave, miOpen);

        /* ================= MENU TOOLS ================= */
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

        /* ================= MENU EDIT ================= */
        Menu menuEdit = new Menu("Edit");
        MenuItem miUndo = new MenuItem("Undo");
        MenuItem miRedo = new MenuItem("Redo");
        MenuItem miCopy = new MenuItem("Copy");
        miPaste = new MenuItem("Paste");
        MenuItem miDelete = new MenuItem("Delete");
        MenuItem miGroup = new MenuItem("Group");
        MenuItem miUngroup = new MenuItem("Ungroup");

        miUndo.setOnAction(e -> { undoStack.undo(); draw(); });
        miRedo.setOnAction(e -> { undoStack.redo(); draw(); });

        miCopy.setOnAction(e -> Clipboard.getInstance().copyToClipboard(selection.getContents()));

        miPaste.setOnAction(e -> {
            List<Clip> copied = Clipboard.getInstance().copyFromClipboard();
            for (Clip c : copied) {
                CommandAdd cmd = new CommandAdd(this, c);
                cmd.execute();
                undoStack.addCommand(cmd);
            }
            draw();
        });

        miDelete.setOnAction(e -> {
            for (Clip c : selection.getContents()) {
                CommandDelete cmd = new CommandDelete(this, c);
                cmd.execute();
                undoStack.addCommand(cmd);
            }
            selection.clear();
            draw();
        });

        miGroup.setOnAction(e -> {
            List<Clip> selected = selection.getContents();
            if (selected.size() <= 1) return;
            CommandGroup cmd = new CommandGroup(this, selected);
            cmd.execute();
            undoStack.addCommand(cmd);
            selection.clear();
            draw();
        });

        miUngroup.setOnAction(e -> {
            List<Clip> selected = selection.getContents();
            if (selected.size() != 1) return;
            Clip c = selected.get(0);
            if (!(c instanceof ClipGroup)) return;
            CommandUngroup cmd = new CommandUngroup(this, (ClipGroup)c);
            cmd.execute();
            undoStack.addCommand(cmd);
            selection.clear();
            draw();
        });

        menuEdit.getItems().addAll(miUndo, miRedo, miCopy, miPaste, miDelete, miGroup, miUngroup);

        MenuBar menuBar = new MenuBar(menuFile, menuTools, menuEdit);
        root.getChildren().add(menuBar);

        /* ================= TOOLBAR ================= */
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

        /* ================= PALETTE DE COULEURS ================= */
        ToolBar colorBar = new ToolBar();
        Color[] colors = { Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW };

        for (Color c : colors) {
            Button btn = new Button();
            btn.setStyle("-fx-background-color: " + toRgbString(c) + "; -fx-min-width: 25; -fx-min-height: 25;");
            btn.setOnAction(e -> currentColor = c);
            colorBar.getItems().add(btn);
        }
        root.getChildren().add(colorBar);

        /* ================= CANVAS ================= */
        canvas = new Canvas(800, 600);
        canvas.setOnMousePressed(e -> { currentTool.press(this, e); draw(); });
        canvas.setOnMouseDragged(e -> { currentTool.drag(this, e); draw(); });
        canvas.setOnMouseReleased(e -> { currentTool.release(this, e); draw(); });
        root.getChildren().add(canvas);

        /* ================= STATUS BAR ================= */
        statusLabel = new Label(currentTool.getName(this));
        root.getChildren().add(new Separator());
        root.getChildren().add(statusLabel);

        Clipboard.getInstance().addListener(this);
        clipboardChanged();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("PinBoard Editor");
        stage.show();

        draw();
    }

    private void setTool(Tool tool) {
        currentTool = tool;
        statusLabel.setText(tool.getName(this));
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        board.draw(gc);
        currentTool.drawFeedback(this, gc);
        selection.draw(gc);
    }

    @Override
    public void clipboardChanged() {
        miPaste.setDisable(Clipboard.getInstance().isEmpty());
    }

    private String toRgbString(Color c) {
        return "rgb(" + (int)(c.getRed()*255) + "," + (int)(c.getGreen()*255) + "," + (int)(c.getBlue()*255) + ")";
    }

    @Override public Board getBoard() { return board; }
    @Override public Selection getSelection() { return selection; }
    @Override public CommandStack getUndoStack() { return undoStack; }
    public Color getCurrentColor() { return currentColor; }
}

