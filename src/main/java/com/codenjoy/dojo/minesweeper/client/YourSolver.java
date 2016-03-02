package com.codenjoy.dojo.minesweeper.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = GmailMy.gmail; // "user@gmail.com";

    private Dice dice;

    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";
        Set<Point> needToBeOpen = getAllSafeHiddenPoints();
        if (needToBeOpen.isEmpty()) {
            return Direction.UP.toString();
        }
        // наиюолее близкую к нам позицию из needToBeOpen
        Point destination = findShortest(needToBeOpen);
        Direction result = getDirection(board.getMe(), destination);
        return result.toString();
    }

    void setBoard(Board board) {
        this.board = board;
    }

    Direction getDirection(Point from, Point to) {
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

    Set<Point> getAllSafeHiddenPoints() {
        // если на поле пустая клеточка вокруг которой не открытые места, то я должен их открыть
        Set<Point> needToBeOpen = new HashSet<>();

        List<Point> points = board.get(Elements.NONE);
        for (Point point : points) {
            // дай все закрытые клетки вокруг себя
            List<Point> hiddenPoints =
                    board.getNear(point.getX(), point.getY(), Elements.HIDDEN);
            needToBeOpen.addAll(hiddenPoints);
        }
        return needToBeOpen;
    }

    private Point findShortest(Set<Point> points) {
        Point me = board.getMe();
        double min = Integer.MAX_VALUE;
        Point result = null;
        for (Point destination : points) {
            int dx = Math.abs(me.getX() - destination.getX());
            int dy = Math.abs(me.getY() - destination.getY());
            double c = Math.sqrt(dx * dx + dy * dy);
            if (c < min) {
                min = c;
                result = destination;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        start(USER_NAME, WebSocketRunner.Host.REMOTE);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new YourSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
