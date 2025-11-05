package ru.vsu.cs.uvarov_d_p.cg.util;

import javafx.scene.paint.Color;

public class Triangle {
    private final int[] x = new int[3];
    private final int[] y = new int[3];
    private final Color[] colors = new Color[3];

    public Triangle(int[] x, int[] y, Color[] colors) {
        System.arraycopy(x, 0, this.x, 0, 3);
        System.arraycopy(y, 0, this.y, 0, 3);
        System.arraycopy(colors, 0, this.colors, 0, 3);
    }

    public int[] getX() { return x; }
    public int[] getY() { return y; }
    public Color[] getColors() { return colors; }

    public void setVertex(int i, int newX, int newY) {
        x[i] = newX;
        y[i] = newY;
    }

    public void setColor(int i, Color c) {
        colors[i] = c;
    }

    public boolean contains(double px, double py) {
        double x1 = x[0], y1 = y[0];
        double x2 = x[1], y2 = y[1];
        double x3 = x[2], y3 = y[2];
        double area = Math.abs((x2 - x1)*(y3 - y1) - (x3 - x1)*(y2 - y1));
        double a1 = Math.abs((x1 - px)*(y2 - py) - (x2 - px)*(y1 - py));
        double a2 = Math.abs((x2 - px)*(y3 - py) - (x3 - px)*(y2 - py));
        double a3 = Math.abs((x3 - px)*(y1 - py) - (x1 - px)*(y3 - py));
        return Math.abs((a1 + a2 + a3) - area) < 0.5;
    }
}
