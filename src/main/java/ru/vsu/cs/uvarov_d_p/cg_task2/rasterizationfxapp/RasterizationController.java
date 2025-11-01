package ru.vsu.cs.uvarov_d_p.cg_task2.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ru.vsu.cs.uvarov_d_p.cg_task2.rasterization.*;

public class RasterizationController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            drawContent();
        });
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            drawContent();
        });

        javafx.application.Platform.runLater(this::drawContent);
    }

    private void drawContent() {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Rasterization.drawTriangle(canvas.getGraphicsContext2D(), 50, 50, 150, 50, 100, 150, Color.RED);
        Rasterization.drawTriangle(canvas.getGraphicsContext2D(), 200, 100, 300, 100, 250, 200, Color.BLUE);
        Rasterization.drawTriangle(canvas.getGraphicsContext2D(), 350, 150, 450, 150, 400, 250, Color.GREEN);
    }
}