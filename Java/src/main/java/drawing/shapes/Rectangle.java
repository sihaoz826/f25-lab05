package drawing.shapes;

/**
 * Refactor Task 3: (Mis-)Shaped
 *
 * @author Zishen Wen (F22), Deyuan Chen (S22)
 */
public class Rectangle implements Shape {

    private final int[][] points;

    public Rectangle(int[] point1, int[] point2, int[] point3, int[] point4) {
        this.points = new int[][]{
                point1, point2, point3, point4
        };
    }

    @Override
    public Line[] toLines() {
        return new Line[]{
                new Line(this.points[0], this.points[1]),
                new Line(this.points[1], this.points[2]),
                new Line(this.points[2], this.points[3]),
                new Line(this.points[3], this.points[0])
        };
    }
}
