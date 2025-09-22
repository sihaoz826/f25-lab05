package frogger;

/**
 * Refactor Task 1.
 *
 * @author Zishen Wen (F22), Deyuan Chen (S22)
 */
public class Road {
    private final boolean[] occupied;

    public Road(boolean[] occupied) {
        this.occupied = occupied;
    }

    public boolean[] getOccupied() {
        return this.occupied;
    }
    
    // Add these methods to make Road the information expert
    public boolean isOccupied(int position) {
        if (!isValid(position)) {
            return false; // or throw an exception
        }
        return this.occupied[position];
    }
    
    public boolean isValid(int position) {
        return position >= 0 && position < this.occupied.length;
    }
}
