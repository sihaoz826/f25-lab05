# Lab 5 Deliverables

## Task 1: Frogger Crossing the Road

### Design Problem Identified
**Anti-pattern: Feature Envy / Inappropriate Intimacy**

The main design problem is in the `Frogger` class methods `isOccupied()` and `isValid()`. These methods directly access the internal `occupied` array from the `Road` class:

```java
public boolean isOccupied(int position) {
    boolean[] occupied = this.road.getOccupied();  // Direct access to internal data
    return occupied[position];
}

public boolean isValid(int position) {
    if (position < 0) return false;
    boolean[] occupied = this.road.getOccupied();  // Direct access to internal data
    return position < occupied.length;
}
```

**Problems:**
1. **Feature Envy**: The `Frogger` class is doing work that should belong to the `Road` class
2. **Inappropriate Intimacy**: `Frogger` is directly accessing and manipulating the internal data structure of `Road`
3. **Violation of Information Expert**: The `Road` class should be responsible for knowing whether a position is occupied or valid

### Solution Implementation

**Step 1: Move responsibility to Road class**
Add the methods to `Road.java` to make it the information expert:

```java
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
```

**Step 2: Update Frogger to use Road's methods**
Modify `Frogger.java` to delegate to the Road class:

```java
public class Frogger {
    private final Road road;
    private int position;
    
    // ... other fields and constructor ...

    public boolean move(boolean forward) {
        int nextPosition = this.position + (forward ? 1 : -1);
        if (!road.isValid(nextPosition) || road.isOccupied(nextPosition)) {
            return false;
        }
        this.position = nextPosition;
        return true;
    }

    // Remove isOccupied() and isValid() methods - they now belong to Road class
}
```

### Key Improvements
1. **Information Expert**: The `Road` class now handles all logic related to road positions and occupancy
2. **Reduced Coupling**: `Frogger` no longer needs to know about the internal structure of the road
3. **Better Encapsulation**: The `Road` class properly encapsulates its data and provides appropriate methods
4. **Single Responsibility**: Each class now has a clearer, more focused responsibility

This refactoring follows the principle that the class with the most information about something should be responsible for that thing. The `Road` class knows about the `occupied` array, so it should handle all operations related to position validation and occupancy checking.

## Task 2: Frogger Recording Themselves

### Design Problem Identified
**Anti-pattern: Long Parameter List / Data Clumps**

The main design problems are:

1. **Long Parameter List in Constructor**: The `Frogger` constructor has 9 parameters:
```java
public Frogger(Road road, int position, Records records, String firstName, String lastName, String phoneNumber,
String zipCode, String state, String gender)
```

2. **Data Clumps**: Identity-related fields are scattered throughout the `Frogger` class:
```java
private String firstName, lastName, phoneNumber, zipCode, state, gender;
```

3. **Long Parameter List in Records.addRecord()**: The method has 6 parameters:
```java
public boolean addRecord(String firstName, String lastName, String phoneNumber,
                         String zipCode, String state, String gender)
```

4. **Poor Cohesion**: The `Frogger` class mixes two different responsibilities:
   - Movement behavior (road crossing)
   - Identity management (personal information)

5. **Unused FroggerID**: The code provides a `FroggerID` record class designed to solve this exact problem, but it's not being used!

### Solution Implementation

**Step 1: Update Frogger to use FroggerID**
Replace individual identity fields with a single `FroggerID` object:

```java
public class Frogger {
    private final Road road;
    private int position;
    private final Records records;
    private final FroggerID id;  // Replace individual fields with FroggerID

    public Frogger(Road road, int position, Records records, FroggerID id) {
        this.road = road;
        this.position = position;
        this.records = records;
        this.id = id;
    }

    public boolean move(boolean forward) {
        int nextPosition = this.position + (forward ? 1 : -1);
        if (!road.isValid(nextPosition) || road.isOccupied(nextPosition)) {
            return false;
        }
        this.position = nextPosition;
        return true;
    }

    public boolean recordMyself() {
        return records.addRecord(id);  // Much cleaner!
    }
}
```

**Step 2: Update Records to accept FroggerID**
Modify `Records.java` to use `FroggerID` instead of individual parameters:

```java
public class Records {
    private final List<FroggerID> records;  // Use FroggerID instead of String[]

    public Records() {
        this.records = new ArrayList<>();
    }

    public boolean addRecord(FroggerID froggerID) {
        // FroggerID.equals() automatically handles comparison of all fields
        if (this.records.contains(froggerID)) {
            return false;  // Record already exists
        }
        this.records.add(froggerID);
        return true;
    }
}
```

### Key Improvements
1. **Reduced Parameter Lists**: Constructor goes from 9 parameters to 4
2. **Better Encapsulation**: Identity data is grouped together in `FroggerID`
3. **Improved Cohesion**: Frogger focuses on movement, identity is properly encapsulated
4. **Easier Maintenance**: Changes to identity structure only affect `FroggerID`
5. **Type Safety**: `FroggerID` provides compile-time checking
6. **Immutability**: `FroggerID` is immutable, preventing accidental modifications
7. **Automatic Equality**: `FroggerID.equals()` handles comparison of all fields automatically

This refactoring uses the **Parameter Object** pattern to group related data together, making the code more maintainable and reducing the cognitive load of managing multiple individual parameters.

## Task 3: Drawing System Analysis

### Design Problems Identified

After examining the drawing system, I've identified several design problems that need refactoring:

#### Problem 1: Code Duplication in the `draw()` Method
**Anti-pattern: Duplicate Code / Shotgun Surgery**

The `draw()` method in `Drawing.java` contains significant code duplication:

```java
public void draw(String format, String filename) {
    if (format.equals("jpeg")) {
        try (Writer writer = new JPEGWriter(filename + ".jpeg")) {
            for (Shape shape : this.shapes) {
                Line[] lines = shape.toLines();
                shape.draw(writer, lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else if (format.equals("png")) {
        try (Writer writer = new PNGWriter(filename + ".png")) {  // DUPLICATE CODE
            for (Shape shape : this.shapes) {                     // DUPLICATE CODE
                Line[] lines = shape.toLines();                   // DUPLICATE CODE
                shape.draw(writer, lines);                        // DUPLICATE CODE
            }                                                     // DUPLICATE CODE
        } catch (IOException e) {                                 // DUPLICATE CODE
            e.printStackTrace();                                  // DUPLICATE CODE
        }                                                         // DUPLICATE CODE
    }
}
```

**Problems:**
- The entire drawing logic is duplicated for each file format
- Adding a new file format (e.g., GIF, SVG) requires duplicating the entire block
- Violates DRY (Don't Repeat Yourself) principle
- Makes maintenance difficult - changes need to be made in multiple places

**Refactoring Solution:**
Extract the common drawing logic into a separate method and use a factory pattern for writers:

```java
public void draw(String format, String filename) {
    try (Writer writer = createWriter(format, filename)) {
        drawShapes(writer);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private Writer createWriter(String format, String filename) {
    return switch (format) {
        case "jpeg" -> new JPEGWriter(filename + ".jpeg");
        case "png" -> new PNGWriter(filename + ".png");
        default -> throw new IllegalArgumentException("Unsupported format: " + format);
    };
}

private void drawShapes(Writer writer) {
    for (Shape shape : this.shapes) {
        Line[] lines = shape.toLines();
        shape.draw(writer, lines);
    }
}
```

#### Problem 2: Excessive Instanceof Usage
**Anti-pattern: Excessive Instanceof**

The `Shape.draw()` method uses `instanceof` to determine the writer type:

```java
default void draw(Writer writer, Line[] lines) {
    try {
        for (Line line : lines) {
            if (writer instanceof JPEGWriter) {        // EXCESSIVE INSTANCEOF
                writer.write(line.toJPEG());
            } else if (writer instanceof PNGWriter) {  // EXCESSIVE INSTANCEOF
                writer.write(line.toPNG());
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

**Problems:**
- Violates Open/Closed Principle - adding new formats requires modifying existing code
- Poor encapsulation - the Shape class needs to know about specific writer types
- Hard to extend - each new format requires another `instanceof` check
- Tight coupling between Shape and specific Writer implementations

**Refactoring Solution:**
Use polymorphism and the Strategy pattern:

```java
// Create a Writer interface with format-specific methods
public interface DrawingWriter {
    void writeLine(Line line) throws IOException;
}

// Update Shape interface
public interface Shape {
    Line[] toLines();
    default void draw(DrawingWriter writer) {
        try {
            for (Line line : toLines()) {
                writer.writeLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Implement format-specific writers
public class JPEGDrawingWriter implements DrawingWriter {
    private final Writer writer;
    
    public JPEGDrawingWriter(Writer writer) {
        this.writer = writer;
    }
    
    @Override
    public void writeLine(Line line) throws IOException {
        writer.write(line.toJPEG());
    }
}
```

#### Problem 3: Exposing Internal Implementation Details
**Anti-pattern: Inappropriate Intimacy**

The `Drawing` class directly creates and manipulates `Line[]` arrays:

```java
for (Shape shape : this.shapes) {
    Line[] lines = shape.toLines();  // EXPOSING INTERNAL DETAILS
    shape.draw(writer, lines);       // RELYING ON INTERNAL STRUCTURE
}
```

**Problems:**
- The `Drawing` class shouldn't need to know that shapes are composed of lines
- Violates encapsulation - exposes the internal representation of shapes
- Creates unnecessary coupling between `Drawing` and the line-based representation
- Makes it harder to change how shapes are internally represented

**Refactoring Solution:**
Let shapes handle their own drawing without exposing internal details:

```java
public void draw(String format, String filename) {
    try (DrawingWriter writer = createDrawingWriter(format, filename)) {
        for (Shape shape : this.shapes) {
            shape.draw(writer);  // Shape handles its own drawing internally
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### Summary of Refactoring Benefits

1. **Eliminates Code Duplication**: Single implementation for all file formats
2. **Improves Extensibility**: Easy to add new formats without modifying existing code
3. **Reduces Coupling**: Shapes and writers are loosely coupled through interfaces
4. **Better Encapsulation**: Internal shape representation is hidden from Drawing class
5. **Follows SOLID Principles**: Open/Closed, Single Responsibility, and Dependency Inversion
