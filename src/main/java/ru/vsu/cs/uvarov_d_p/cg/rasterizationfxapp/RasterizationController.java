package ru.vsu.cs.uvarov_d_p.cg.rasterizationfxapp;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ru.vsu.cs.uvarov_d_p.cg.rasterization.*;

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
        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Rasterization.drawTriangle(gc, 50, 50, 150, 50, 100, 136, Color.RED);
        Rasterization.fillTriangle(gc, 50, 50, 150, 50, 100, 136, Color.rgb(255, 0, 0, 0.4));

        Rasterization.drawTriangle(gc, 200, 50, 300, 50, 250, 150, Color.BLUE);
        Rasterization.fillTriangle(gc, 200, 50, 300, 50, 250, 150, Color.rgb(0, 0, 255, 0.4));

        Rasterization.drawTriangle(gc, 350, 50, 450, 50, 350, 150, Color.GREEN);
        Rasterization.fillTriangle(gc, 350, 50, 450, 50, 350, 150, Color.rgb(0, 255, 0, 0.4));

        Rasterization.drawTriangle(gc, 500, 50, 600, 80, 540, 160, Color.ORANGE);
        Rasterization.fillTriangle(gc, 500, 50, 600, 80, 540, 160, Color.rgb(255, 165, 0, 0.4));

        Rasterization.drawTriangle(gc, 650, 50, 780, 50, 700, 100, Color.PURPLE);
        Rasterization.fillTriangle(gc, 650, 50, 780, 50, 700, 100, Color.rgb(128, 0, 128, 0.4));
    }
}