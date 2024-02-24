package com.example.projectakhirasd;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    MazeSolverDFS mazeSolverDFS = new MazeSolverDFS(new int[][]{
            {0, 1, 0, 0, 0, 1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 1, 0, 1, 1, 1, 1, 0, 1},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {0, 0, 1, 0, 1, 1, 1, 0, 0, 1},
            {0, 1, 1, 0, 0, 0, 1, 0, 0, 1},
            {0, 0, 1, 0, 1, 0, 1, 1, 1, 1}
    });

    private static final int ROWS = 10;
    private static final int COLS = 10;
    private int currentStepRow = 0;
    private int currentStepCol = 0;
    private final int endRow = 6;
    private final int endCol = 5;
    private final String[] paths = {
            "/Users/fakhry/IdeaProjects/ProjectAkhirASD/src/main/java/com/example/projectakhirasd/images/people.png",
            "/Users/fakhry/IdeaProjects/ProjectAkhirASD/src/main/java/com/example/projectakhirasd/images/win.png"
    };

    @Override
    public void start(Stage stage) throws IOException {

        Text title = new Text("Maze Solver");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        Text result = new Text("");
        result.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        HBox top = new HBox(10);
        top.getChildren().addAll(title, result);
        top.setAlignment(Pos.CENTER);

        GridPane grid = createMazeGrid();

        HBox midComp = new HBox(10);
        midComp.getChildren().add(grid);
        midComp.setAlignment(Pos.CENTER);
        midComp.setPadding(new Insets(40, 0, 20, 0));

        Button btnSolve = new Button("Find Solution");
        btnSolve.setPrefWidth(200);

        btnSolve.setOnAction(event -> {
            setCurrentStepPosition(0, 0);

            mazeSolverDFS.solveMaze(0, 0, endRow, endCol);

            animatePeople(grid, mazeSolverDFS.getPath());

            if (mazeSolverDFS.result) {
                result.setText("Maze Solved Horrayy!!!");
                result.setFill(Color.GREEN);
            } else {
                result.setText("There Is No Solution For This Maze :(((((");
                result.setFill(Color.RED);
            }
        });

        HBox bot = new HBox(5);
        bot.getChildren().add(btnSolve);
        bot.setAlignment(Pos.CENTER);
        bot.setPadding(new Insets(0, 0, 80, 0));

        BorderPane pane = new BorderPane();
        pane.setTop(top);
        pane.setCenter(midComp);
        pane.setBottom(bot);

        Scene scene = new Scene(pane, 800, 600);

        stage.setTitle("Maze Visualization");
        stage.setScene(scene);
        stage.show();
    }

    private int setCurrentStepPosition(int row, int col) {
        int ind;

        currentStepRow = row;
        currentStepCol = col;

        if(currentStepRow == mazeSolverDFS.endRow && currentStepCol == mazeSolverDFS.endCol) {
            ind = 1;
        } else {
            ind = 0;
        }

        return ind;
    }

    private GridPane createMazeGrid() throws IOException{
        GridPane grid = new GridPane();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++){
                int curr = mazeSolverDFS.maze[row][col];
                Rectangle cell = new Rectangle(40, 40);

                cell.setStroke(Color.BLACK);

                if (curr == 1) {
                    cell.setFill(Color.BLACK);
                } else if (curr == 0){
                    cell.setFill(Color.WHITE);
                }

                if (row == endRow && col == endCol) {
                    cell.setFill(Color.GREEN);
                }

                grid.add(cell, col, row);

                if (row == currentStepRow && col == currentStepCol) {

                    Image peopleImage = new Image(new FileInputStream(paths[0]));
                    ImageView peopleImageView = new ImageView();
                    peopleImageView.setImage(peopleImage);
                    peopleImageView.setFitWidth(35);
                    peopleImageView.setFitHeight(35);
                    cell.setFill(Color.RED);

                    grid.add(peopleImageView, col, row);
                }


            }
        }

        return grid;
    }

    private void animatePeople(GridPane grid, List<Point> path) {
        Timeline timeline = new Timeline();
        int step = 0;
        for(Point point : path) {
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(0.5 * (step + 1)),
                    event -> {
                        // Hapus gambar tikus dari posisi sebelumnya
//                        grid.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == currentStepCol && GridPane.getRowIndex(node) == currentStepRow);

                        Rectangle previousCell = (Rectangle) grid.getChildren().stream()
                                .filter(node -> GridPane.getColumnIndex(node) == currentStepCol && GridPane.getRowIndex(node) == currentStepRow)
                                .findFirst()
                                .orElse(null);

                        // Jika sel ditemukan, ubah warna isinya
                        if (previousCell != null && !(currentStepRow == 0 && currentStepCol == 0)) {
                            previousCell.setFill(Color.YELLOW);
                        }

                        // Update posisi + Tambahkan gambar
                        String pathImage = paths[setCurrentStepPosition(point.getX(), point.getY())];

                        Rectangle newCell = (Rectangle) grid.getChildren().stream()
                                .filter(node -> GridPane.getColumnIndex(node) == currentStepCol && GridPane.getRowIndex(node) == currentStepRow)
                                .findFirst()
                                .orElse(null);

                        // Jika sel ditemukan, ubah warna isinya
                        if (newCell != null && !(currentStepRow == 0 && currentStepCol == 0)) {
                            newCell.setFill(Color.GREEN);
                        }

                        try {
                            Image peopleImage = new Image(new FileInputStream(pathImage));
                            ImageView peopleImageView = new ImageView();
                            peopleImageView.setImage(peopleImage);
                            peopleImageView.setFitWidth(35);
                            peopleImageView.setFitHeight(35);
                            grid.add(peopleImageView, currentStepCol, currentStepRow);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
            step++;
        }
        timeline.play();
    }


    public static void main(String[] args) {
        launch();
    }
}