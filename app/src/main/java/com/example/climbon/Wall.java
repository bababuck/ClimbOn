package com.example.climbon;

import java.util.ArrayList;

public class Wall {
    /* Consists of a set on panels.

    Can be indefinitely long.

    Not sure if class needed since really just holds one thing for now
    */

    public ArrayList<Shape> panel_set = new ArrayList<>();

    public ArrayList<Integer> findCumulativeHoldNumbers() {
        /* Find the running total of number of holds.

        Will allow us to locate the holds for a certain panel.
        */
        ArrayList<Integer> cumulativeHolds = new ArrayList<>();
        int running_count = 0;
        for (Shape shape : panel_set) {
            cumulativeHolds.add(running_count);
            running_count += shape.getNumHolds();
        }
        return cumulativeHolds;
    }

    public int findTotalHolds() {
        /* Find the running total of number of holds.

        Will allow us to locate the holds for a certain panel.
        */
        int running_count = 0;
        for (Shape shape : panel_set) {
            running_count += shape.getNumHolds();
        }
        return running_count;
    }

}
