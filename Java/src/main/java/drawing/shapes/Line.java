package drawing.shapes;

/**
 * Refactor Task 3: (Mis-)Shaped
 *
 * @author Zishen Wen (F22), Deyuan Chen (S22)
 */
public record Line(int[] start, int[] end) {

    public char[] toJPEG() {
        // Pretend this works.
        return new char[0];
    }

    public char[] toPNG() {
        // Pretend this works.
        return new char[0];
    }
}
