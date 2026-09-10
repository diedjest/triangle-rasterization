# Triangle Rasterization

Interactive **JavaFX** demo that rasterizes triangles in software: no GPU fill, only pixels written by hand.

The app is a small computer-graphics study project. It shows how a triangle is drawn on a 2D canvas with **Bresenham’s line algorithm**, **scanline fill**, and **per-vertex color interpolation**.

## Features

- Bresenham line drawing via `PixelWriter`
- Gradient (interpolated) edges between vertex colors
- Scanline fill of a triangle
- Smooth RGB interpolation across the interior
- Interactive editor: add, select, drag, reshape, recolor, and delete triangles

## How it works

1. Vertices are sorted by Y.
2. The triangle is split into an upper and a lower part at the middle vertex.
3. For each scanline, the left and right X bounds are interpolated.
4. Each pixel on the span gets a color interpolated from the endpoints (and those endpoints from the vertices).

Edges use the same Bresenham stepper as solid lines; fill uses horizontal spans of that stepper.

## Controls

| Action | How |
| --- | --- |
| Add a triangle | **Вставить треугольник** |
| Select | Left-click inside a triangle |
| Move | Drag the interior |
| Reshape | Drag a vertex |
| Recolor a vertex | Right-click the vertex, pick a color |
| Delete | Select, then **Удалить треугольник** |

## Requirements

- JDK **21** or newer
- Maven **3.9+** (or the included Maven Wrapper)

## Run

```bash
./mvnw javafx:run
```

On Windows:

```bat
mvnw.cmd javafx:run
```

## Project layout

```
src/main/java/ru/vsu/cs/uvarov_d_p/cg/
  rasterization/Rasterization.java      # algorithms
  rasterizationfxapp/                   # JavaFX UI
  util/Triangle.java                    # triangle model + hit test
```

Core drawing lives in `Rasterization`: `drawLine`, `drawLineInterpolated`, `fillTriangle`, `fillTriangleInterpolated`.
