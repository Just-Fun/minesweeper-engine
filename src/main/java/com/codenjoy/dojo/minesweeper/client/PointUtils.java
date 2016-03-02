package com.codenjoy.dojo.minesweeper.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Point;

/**
 * Created by serzh on 3/2/16.
 */
public class PointUtils {

    public static Direction getDirection(Point from, Point to) {
        int dx = (from.getX() - to.getX());
        int dy = (from.getY() - to.getY());
        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                return Direction.LEFT;
            } else {
                return Direction.RIGHT;
            }
        } else {
            if (dy > 0) {
                return Direction.UP;
            } else {
                return Direction.DOWN;
            }
        }
    }
}
