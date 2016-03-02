package com.codenjoy.dojo.minesweeper.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by serzh on 3/2/16.
 */
public class PointUtilsTest {

    @Test
    public void test() {
//        assertDirection(Direction.DOWN,   0, 0, 0, 0);
        assertDirection(Direction.DOWN,   0, 0, 0, 1);
        assertDirection(Direction.RIGHT, 0, 0, 1, 0);
        assertDirection(Direction.DOWN,   0, 0, 1, 1);
        assertDirection(Direction.UP,0, 1, 0, 0);
//        assertDirection(Direction.DOWN,0, 1, 0, 1);
        assertDirection(Direction.UP,0, 1, 1, 0);
        assertDirection(Direction.RIGHT,0, 1, 1, 1);
        assertDirection(Direction.LEFT, 1, 0, 0, 0);
        assertDirection(Direction.DOWN,   1, 0, 0, 1);
//        assertDirection(Direction.DOWN, 1, 0, 1, 0);
        assertDirection(Direction.DOWN,   1, 0, 1, 1);
        assertDirection(Direction.UP,1, 1, 0, 0);
        assertDirection(Direction.LEFT,1, 1, 0, 1);
        assertDirection(Direction.UP,1, 1, 1, 0);
//        assertDirection(Direction.DOWN,1, 1, 1, 1);
    }

    private void assertDirection(Direction expected, int x1, int y1, int x2, int y2) {
        assertEquals(expected, PointUtils.getDirection(new PointImpl(x1, y1), new PointImpl(x2, y2)));
    }
}