package com.example.climbon;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void genSimpleShape() {
        ArrayList<Float> corners = new ArrayList<Float>(Arrays.<Float>asList((float) 0,(float) 0,(float) 10,(float) 0,(float) 10,(float) 10, (float) 10, (float) 0));
        Shape simple_shape;
        try{
            simple_shape = new Shape(corners, new Coordinate((float) 0.5, (float) 0.5));
            assertEquals(16, simple_shape.getNumHolds());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 //   @Test

}