package drawing.shapes;

/**
 * Refactor Task 3: (Mis-)Shaped
 *
 * @author Zishen Wen (F22), Deyuan Chen (S22)
 */
public class Triangle implements Shape {

    private final int[][] points;

    public Triangle(int[] point1, int[] point2, int[] point3) {
        this.points = new int[][]{
                point1, point2, point3
        };
    }

    @Override
    public Line[] toLines() {
        return new Line[]{
                new Line(this.points[0], this.points[1]),
                new Line(this.points[1], this.points[2]),
                new Line(this.points[2], this.points[0])
        };
    }
}
