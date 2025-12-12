package pobj.pinboard.editor;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pobj.pinboard.document.Board;
import pobj.pinboard.editor.tools.*;

@SuppressWarnings("unused")
public class EditorWindow implements EditorInterface {

    private Board board;
    private Stage stage;
    private Canvas canvas;
    private Tool currentTool;
    private Label statusLabel;

    public EditorWindow(Stage stage) {
        this.stage = stage;
        this.board = new Board();
        this.currentTool = new ToolRect(); // Outil par défaut
        this.statusLabel = new Label(currentTool.getName(this));

        VBox root = new VBox();


        // Menu File
        Menu menuFile = new Menu("File");
        MenuItem miNew = new MenuItem("New");
        MenuItem miClose = new MenuItem("Close");

        miNew.setOnAction(e -> new EditorWindow(new Stage()));
        miClose.setOnAction(e -> stage.close());

        menuFile.getItems().addAll(miNew, miClose);
 
        // Menu Tools (optionnel)
        Menu menuTools = new Menu("Tools");
        MenuItem miRect = new MenuItem("Rectangle");
        MenuItem miEllipse = new MenuItem("Ellipse");
        MenuItem miImage = new MenuItem("Image");

        miRect.setOnAction(e -> {
            currentTool = new ToolRect();
            statusLabel.setText(currentTool.getName(this));
        });
        miEllipse.setOnAction(e -> {
            currentTool = new ToolEllipse();
            statusLabel.setText(currentTool.getName(this));
        });
        miImage.setOnAction(e -> {
            currentTool = new ToolImage();
            statusLabel.setText(currentTool.getName(this));
        });

        menuTools.getItems().addAll(miRect, miEllipse, miImage);

        MenuBar menuBar = new MenuBar(menuFile, menuTools);
        root.getChildren().add(menuBar);

        // Toolbar
        ToolBar toolbar = new ToolBar();
        Button btnRect = new Button("Rect");
        Button btnEllipse = new Button("Ellipse");
        Button btnImg = new Button("Img...");

        btnRect.setOnAction(e -> {
            currentTool = new ToolRect();
            statusLabel.setText(currentTool.getName(this));
        });
        btnEllipse.setOnAction(e -> {
            currentTool = new ToolEllipse();
            statusLabel.setText(currentTool.getName(this));
        });
        btnImg.setOnAction(e -> {
            currentTool = new ToolImage();
            statusLabel.setText(currentTool.getName(this));
        });

        toolbar.getItems().addAll(btnRect, btnEllipse, btnImg);
        root.getChildren().add(toolbar);

        // Canvas
        canvas = new Canvas(800, 600);
        @SuppressWarnings("unused")
		GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(e -> {
            currentTool.press(this, e);
            draw();
        });

        canvas.setOnMouseDragged(e -> {
            currentTool.drag(this, e);
            draw();
        });

        canvas.setOnMouseReleased(e -> {
            currentTool.release(this, e);
            draw();
        });

        root.getChildren().add(canvas);
        
        // Barre de statut
        Separator sep = new Separator();
        root.getChildren().addAll(sep, statusLabel);

        // Configuration de la scène
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("PinBoard Editor");
        stage.show();

        // Premier dessin
        draw();
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Effacer le canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dessiner les clips de la planche
        board.draw(gc);

        // Dessiner le feedback de l'outil courant
        if (currentTool != null) {
            currentTool.drawFeedback(this, gc);
        }
    }

    // Implémentation EditorInterface
    @SuppressWarnings("exports")
	@Override
    public Board getBoard() {
        return board;
    }

    @Override
    public Selection getSelection() {
        return null; // pour l'instant vide
    }

    @Override
    public CommandStack getUndoStack() {
        return null; // pour l'instant vide
    }
}
