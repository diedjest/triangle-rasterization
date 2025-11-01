package ru.vsu.cs.uvarov_d_p.cg.rasterization;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterization {

    public static void drawTriangle(
            GraphicsContext graphicsContext,
            int x1, int y1,
            int x2, int y2,
            int x3, int y3,
            Color strokeColor)
    {
        drawLine(graphicsContext, x1, y1, x2, y2, strokeColor);
        drawLine(graphicsContext, x2, y2, x3, y3, strokeColor);
        drawLine(graphicsContext, x3, y3, x1, y1, strokeColor);
    }

    public static void fillTriangle(GraphicsContext gc,
                                    int x1, int y1,
                                    int x2, int y2,
                                    int x3, int y3,
                                    Color color) {

        PixelWriter pw = gc.getPixelWriter();

        if (y2 < y1) { int t = y1; y1 = y2; y2 = t; t = x1; x1 = x2; x2 = t; }
        if (y3 < y1) { int t = y1; y1 = y3; y3 = t; t = x1; x1 = x3; x3 = t; }
        if (y3 < y2) { int t = y2; y2 = y3; y3 = t; t = x2; x2 = x3; x3 = t; }

        if (y1 == y3) return;

        float invSlope1 = (y2 - y1) != 0 ? (float)(x2 - x1) / (y2 - y1) : 0;
        float invSlope2 = (y3 - y1) != 0 ? (float)(x3 - x1) / (y3 - y1) : 0;
        float invSlope3 = (y3 - y2) != 0 ? (float)(x3 - x2) / (y3 - y2) : 0;

        float sx = x1;
        float ex = x1;

        for (int y = y1; y <= y2; y++) {
            drawHorizontalLine(pw, (int)sx, (int)ex, y, color);
            sx += invSlope1;
            ex += invSlope2;
        }

        sx = x2;
        ex = x1 + (y2 - y1) * invSlope2;

        for (int y = y2; y <= y3; y++) {
            drawHorizontalLine(pw, (int)sx, (int)ex, y, color);
            sx += invSlope3;
            ex += invSlope2;
        }
    }

    private static void drawHorizontalLine(PixelWriter pw, int x1, int x2, int y, Color color) {
        if (y < 0) return;
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        for (int x = x1; x <= x2; x++) {
            pw.setColor(x, y, color);
        }
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
