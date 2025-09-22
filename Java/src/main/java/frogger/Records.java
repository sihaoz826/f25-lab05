package frogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Refactor Task 2.
 *
 * @author Zishen Wen (F22), Deyuan Chen (S22)
 */
public class Records {
    private final List<FroggerID> records;

    public Records() {
        this.records = new ArrayList<>();
    }

    /**
     * Adds a frogger's record.
     *
     * @param froggerID the frogger's identity information
     * @return Return false if the record has existed. Else, return true.
     */
    public boolean addRecord(FroggerID froggerID) {
        // FroggerID.equals() automatically handles comparison of all fields
        if (this.records.contains(froggerID)) {
            return false;  // Record already exists
        }
        this.records.add(froggerID);
        return true;
    }
}