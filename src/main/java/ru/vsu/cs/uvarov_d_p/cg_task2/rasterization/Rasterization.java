package ru.vsu.cs.uvarov_d_p.cg_task2.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawTriangle(
            final GraphicsContext graphicsContext,
            final int x1, final int y1,
            final int x2, final int y2,
            final int x3, final int y3,
            Color strokeColor)
    {
        drawLine(graphicsContext, x1, y1, x2, y2, strokeColor);
        drawLine(graphicsContext, x2, y2, x3, y3, strokeColor);
        drawLine(graphicsContext, x3, y3, x1, y1, strokeColor);
    }

    public static void drawLine(
            GraphicsContext graphicsContext,
            int x1, int y1, int x2, int y2,
            Color color) {
        PixelWriter pixelWriter = graphicsContext.getPixelWriter();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            pixelWriter.setColor(x1, y1, color);

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }
}
