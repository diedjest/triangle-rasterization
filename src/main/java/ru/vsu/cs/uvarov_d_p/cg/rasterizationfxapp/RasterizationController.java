package ru.vsu.cs.uvarov_d_p.cg.rasterizationfxapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import ru.vsu.cs.uvarov_d_p.cg.rasterization.Rasterization;
import ru.vsu.cs.uvarov_d_p.cg.util.Triangle;

import java.util.ArrayList;
import java.util.List;

public class RasterizationController {

    @FXML private AnchorPane anchorPane;
    @FXML private Canvas canvas;
    @FXML private Button fillTriangleButton;
    @FXML public Button deleteTriangleButton;

    private final List<Triangle> triangles = new ArrayList<>();
    private Triangle hoveredTriangle = null;
    private Triangle selectedTriangle = null;
    private int selectedVertex = -1;
    private int hoveredVertex = -1;
    private double dragOffsetX, dragOffsetY;
    private boolean dragging = false;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, o, n) -> {
            canvas.setWidth(n.doubleValue());
            drawContent();
        });
        anchorPane.prefHeightProperty().addListener((ov, o, n) -> {
            canvas.setHeight(n.doubleValue());
            drawContent();
        });

        fillTriangleButton.setOnAction(e -> addTriangle());
        deleteTriangleButton.setOnAction(e -> deleteTriangle());

        canvas.setOnMouseMoved(this::onMouseMoved);
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);
        canvas.setOnMouseClicked(this::onMouseClicked);

        Platform.runLater(() -> {
            canvas.requestFocus();
            drawContent();
        });
    }

    private void addTriangle() {
        int[] x = {150, 250, 200};
        int[] y = {150, 150, 250};
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        triangles.add(new Triangle(x, y, colors));
        drawContent();
    }

    private void deleteTriangle() {
        triangles.remove(selectedTriangle);
        selectedTriangle = null;
        drawContent();
    }

    private void onMouseMoved(MouseEvent e) {
        double mx = e.getX(), my = e.getY();
        hoveredTriangle = null;
        hoveredVertex = -1;

        // Проверяем вершины всех треугольников при наведении
        for (Triangle t : triangles) {
            for (int i = 0; i < 3; i++) {
                if (distance(mx, my, t.getX()[i], t.getY()[i]) < 15) {
                    hoveredVertex = i;
                    hoveredTriangle = t;
                    break;
                }
            }
            if (hoveredTriangle != null) break;
        }

        // Если не нашли вершину, проверяем наведение на треугольники
        if (hoveredTriangle == null) {
            for (int i = triangles.size() - 1; i >= 0; i--) {
                Triangle t = triangles.get(i);
                if (t.contains(mx, my)) {
                    hoveredTriangle = t;
                    break;
                }
            }
        }

        drawContent();
    }

    private void onMouseClicked(MouseEvent e) {
        double mx = e.getX(), my = e.getY();

        if (e.getButton() == MouseButton.PRIMARY && !dragging) {
            Triangle clickedTriangle = null;

            for (int i = triangles.size() - 1; i >= 0; i--) {
                Triangle t = triangles.get(i);
                if (t.contains(mx, my)) {
                    clickedTriangle = t;
                    break;
                }
            }

            if (clickedTriangle != null) {
                selectedTriangle = clickedTriangle;
                triangles.remove(clickedTriangle);
                triangles.add(clickedTriangle);
            } else {
                selectedTriangle = null;
            }

            drawContent();
        }
    }

    private void onMousePressed(MouseEvent e) {
        double mx = e.getX(), my = e.getY();
        dragging = false;

        if (e.getButton() == MouseButton.SECONDARY) {
            for (Triangle t : triangles) {
                for (int i = 0; i < 3; i++) {
                    if (distance(mx, my, t.getX()[i], t.getY()[i]) < 15) {
                        showColorPickerPopup(t, i, e);
                        return;
                    }
                }
            }
        }

        if (e.getButton() == MouseButton.PRIMARY) {
            // Проверяем вершины всех треугольников при нажатии
            for (Triangle t : triangles) {
                for (int i = 0; i < 3; i++) {
                    if (distance(mx, my, t.getX()[i], t.getY()[i]) < 15) {
                        selectedTriangle = t;
                        selectedVertex = i;
                        dragOffsetX = mx - t.getX()[i];
                        dragOffsetY = my - t.getY()[i];
                        triangles.remove(t);
                        triangles.add(t);
                        return;
                    }
                }
            }

            for (int i = triangles.size() - 1; i >= 0; i--) {
                Triangle t = triangles.get(i);
                if (t.contains(mx, my)) {
                    selectedTriangle = t;
                    dragOffsetX = mx;
                    dragOffsetY = my;
                    selectedVertex = -1;
                    triangles.remove(t);
                    triangles.add(t);
                    return;
                }
            }

            selectedTriangle = null;
            selectedVertex = -1;
        }
    }

    private void onMouseDragged(MouseEvent e) {

        if (selectedTriangle == null) return;
        dragging = true;

        double mx = e.getX(), my = e.getY();

        if (selectedVertex >= 0) {
            selectedTriangle.setVertex(selectedVertex, (int) mx, (int) my);
        } else {
            double dx = mx - dragOffsetX;
            double dy = my - dragOffsetY;
            for (int i = 0; i < 3; i++) {
                selectedTriangle.setVertex(
                        i,
                        (int) (selectedTriangle.getX()[i] + dx),
                        (int) (selectedTriangle.getY()[i] + dy)
                );
            }
            dragOffsetX = mx;
            dragOffsetY = my;
        }

        drawContent();
    }

    private void onMouseReleased(MouseEvent e) {
        selectedVertex = -1;
        drawContent();
    }

    private void drawContent() {
        var gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Triangle t : triangles) {
            Rasterization.fillTriangleInterpolated(
                    gc,
                    t.getX()[0], t.getY()[0], t.getColors()[0],
                    t.getX()[1], t.getY()[1], t.getColors()[1],
                    t.getX()[2], t.getY()[2], t.getColors()[2]
            );
        }

        if (hoveredTriangle != null && hoveredVertex != -1) {
            drawVertexCircle(gc, hoveredTriangle, hoveredVertex);
        }
    }

    private void drawVertexCircle(javafx.scene.canvas.GraphicsContext gc, Triangle triangle, int vertexIndex) {
        double x = triangle.getX()[vertexIndex];
        double y = triangle.getY()[vertexIndex];

        gc.setFill(Color.WHITE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        double radius = 8;
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    private void showColorPickerPopup(Triangle triangle, int vertexIndex, MouseEvent e) {
        ColorPicker picker = new ColorPicker(triangle.getColors()[vertexIndex]);
        Popup popup = new Popup();
        picker.setOnAction(event -> {
            triangle.setColor(vertexIndex, picker.getValue());
            popup.hide();
            drawContent();
        });
        popup.getContent().add(picker);
        popup.show(canvas.getScene().getWindow(), e.getScreenX(), e.getScreenY());
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }
}