ics4u-finalproject-
====================

Final Project for ICS4U

## BuildScreen.java (GUI + Circuit Logic)

- **Purpose**: Main window where the user builds and analyzes circuits on a grid.
- **Grid system**:
  - Custom `GridPanel` draws a grid and blue dots at intersections.
  - Clicks on dots are snapped to the nearest grid intersection and stored in `pos1` and `pos2`.
  - Components are always placed between two selected intersections at their midpoint.
- **Collections used**:
  - `List<Point> componentIntersections`: stores all intersections that currently have components attached.
  - `Map<JButton, Point[]> componentMap`: links each button on the grid to its two intersection points.
  - `Map<JButton, Component> buttonComponentMap`: links each button to its logical `Component` object (e.g., `Resistor`, `PowerSupply`).
  - `Map<JButton, String[]> buttonFieldTextMap`: stores the text values shown in each component’s popup.
  - `ComponentNode` linked list: manually implemented singly linked list that stores components in the order they were placed.
- **Placement & interaction**:
  - `placeBtnActionPerformed`:
    - Validates two distinct grid intersections (no diagonals, not the same point).
    - Computes the midpoint, creates the correct `Component` subtype, and adds a JButton at that position.
    - Updates all maps and the linked list with the new component.
  - Each component button has an ActionListener that opens a popup:
    - Shows component‑specific labels (from `Component.field1` / `field2`) and editable text fields.
    - Includes an **Edit** button to update resistance or voltage, and a **Trash** button to delete the component.
- **Circuit analysis**:
  - `checkCircuitType()`:
    - Finds the left‑most component by X coordinate.
    - Uses that component’s smaller‑X intersection as the starting point.
    - Checks if that intersection is reused by any other component to decide if the circuit is **closed**.
    - Counts how many components are vertical (different Y values) to distinguish:
      - **Closed, Series** vs **Closed, Parallel**.
    - Writes the result into `circuitCheckField`.
  - `updateCircuitTotals()`:
    - Reads `circuitCheckField` to decide whether to treat the circuit as **series** or **parallel**.
    - Calls `Calculations.calculateTotals(...)` to compute total voltage, resistance, current, and power.
    - Updates the labels: `voltLabel`, `resLabel`, `curntLabel`, `powLabel`.
  - Voltage drop in popup:
    - When the circuit is closed and the clicked component is a **Resistor**, the popup shows:
      - `Voltage Drop: X.XX V`, computed via `Calculations.calculateVoltageDrop(...)`.
- **Graph algorithms (Depth-First Search)**:
  - The circuit is treated as an **undirected graph**:
    - **Nodes** = grid intersections (`Point` objects)
    - **Edges** = components between two intersections
  - `buildComponentGraph()`:
    - Builds an **adjacency list**: `Map<Point, Set<Point>>`
    - For every component, adds connections both ways between its two endpoints
  - `dfsPathExists(graph, current, target, visited)`:
    - Runs **Depth-First Search (DFS)** to see if you can get from one intersection to another
    - Uses a `Set<Point> visited` so the search does not loop forever on cycles
    - Recursively walks through neighbors until it either finds the target or runs out of options
  - `buildCircuitPath(start, end)` and `buildPath(...)`:
    - Uses DFS to **build the actual path** between two intersections
    - Keeps a `List<Point> path` with the order of intersections visited
    - Uses **backtracking**: removes the last point from the list when a branch is a dead end
    - Returns the full path if one exists, or `null` if no path is possible
  - **Why DFS is used**:
    - Checks circuit connectivity (whether a continuous path exists)
    - Gives a clear example of a **graph traversal algorithm** in a real project
- **Advanced concepts demonstrated**:
  - Custom painting with `paintComponent` in `GridPanel`.
  - Use of multiple collections (`List`, `Map`, and a custom linked list) to model circuit structure.
  - **Graph algorithms**: Depth-First Search (DFS) for path finding and circuit connectivity analysis.
  - Separation of UI (Swing) from calculation logic (delegated to `Calculations`).

## Calculations.java (Circuit Math)

- **Purpose**: Central place for all circuit‑level calculations.
- **Key types**:
  - `enum CircuitType { SERIES, PARALLEL }`: indicates how to treat the resistor network.
  - `static class Result`: bundles **totalVoltage**, **totalResistance**, **totalCurrent**, **totalPower**.
- **Methods**:
  - `calculateTotals(Collection<Component> components)`:
    - Default entry point; assumes a series circuit.
  - `calculateTotals(Collection<Component> components, CircuitType type)`:
    - Sums all `PowerSource` components’ `getVoltageOut()` to get **totalVoltage**.
    - For each `Resistor`:
      - Adds its resistance to **seriesResistance**.
      - Adds `1/R` to a reciprocal sum for parallel calculations.
    - If `type == PARALLEL`:
      - Uses \( R_\text{eq} = 1 / \sum (1/R_i) \) for **totalResistance**.
    - Otherwise (series):
      - Uses \( R_\text{eq} = \sum R_i \).
    - Computes current and power with Ohm’s Law:
      - \( I = V / R \) and \( P = V \times I \).
  - `calculateVoltageDrop(Component component, Collection<Component> allComponents, CircuitType type)`:
    - Recomputes **totalVoltage** and **totalResistance** from the collection.
    - For a **parallel** circuit:
      - Returns **totalVoltage** as the drop across each branch.
    - For a **series** circuit:
      - Uses the ratio \( (R_\text{component} / R_\text{total}) \times V_\text{total} \) to get that component’s voltage drop.
- **How it’s used in the app**:
  - `BuildScreen.updateCircuitTotals()` calls `calculateTotals(...)` every time the user:
    - Places a component.
    - Edits a component’s resistance/voltage.
    - Deletes a component.
  - The component popup calls `calculateVoltageDrop(...)` to show the drop across a selected resistor once the circuit is closed.
- **Advanced concepts demonstrated**:
  - Encapsulation of all math in a **pure utility class** (no UI code).
  - Use of **enums** to switch behavior cleanly between series and parallel.
  - Reuse of calculation logic in multiple places (totals vs per‑component voltage drop).

## Enums (Type-Safe Constants)

This project uses **Java enums** to store small, fixed sets of values in a clear and safe way.

### CircuitType Enum (in `Calculations.java`)

```java
public enum CircuitType {
    SERIES,
    PARALLEL
}
```

- **Purpose**: Tells the code which formulas to use for circuit calculations.
- **Why it matters**:
  - **Series circuits**: Resistance adds directly → \( R_\text{total} = R_1 + R_2 + R_3 + \ldots \)
  - **Parallel circuits**: Resistance uses the reciprocal sum → \( \frac{1}{R_\text{total}} = \frac{1}{R_1} + \frac{1}{R_2} + \frac{1}{R_3} + \ldots \)
- **Usage**:
  - `BuildScreen` figures out if the circuit is series or parallel and passes `CircuitType.SERIES` or `CircuitType.PARALLEL` to `Calculations.calculateTotals(...)`.
  - `Calculations` checks `if (type == CircuitType.PARALLEL)` to switch between the two calculation styles.
  - `calculateVoltageDrop(...)` also uses `CircuitType` (parallel: each branch gets the full voltage, series: voltage splits across resistors).

### ConnectionType Enum (in `ConnectionType.java`)

```java
public enum ConnectionType {
    RESISTOR, POWERSOURCE, WIRE, MODULE, SWITCH;
}
```

- **Purpose**: Labels what kind of component something is.
- **Usage**: Each component class passes its type into the base `Component` class:
  - `Resistor` → `ConnectionType.RESISTOR`
  - `PowerSupply`, `VoltageRegulator`, `Relay` → `ConnectionType.POWERSOURCE`
  - `Wire` → `ConnectionType.WIRE`
  - `LED`, `Motor` → `ConnectionType.MODULE`
  - `Switch`, `Transistor` → `ConnectionType.SWITCH`
- **Benefits**: Makes it easy to group and filter by type (for example, “find all resistors” or “sum all power sources”).

### Why Enums Instead of Strings or Integers?

1. **Type Safety**: The compiler blocks invalid values (for example, `CircuitType.SERIE` will not compile).
2. **IDE Support**: Enums work well with autocomplete and refactoring tools.
3. **Performance**: Enum comparisons with `==` are fast.
4. **Readability**: `CircuitType.SERIES` is much clearer than `"series"` or a magic number like `1`.
5. **Maintainability**: You define the enum in one place, and all uses stay in sync.

### Advanced Programming Concepts Demonstrated

- **Enum types**: Using enums instead of plain strings or numbers.
- **Type-safe design**: Catching problems at compile time instead of at runtime.
- **Polymorphism**: Using enums to choose different behavior (series vs parallel calculations).

