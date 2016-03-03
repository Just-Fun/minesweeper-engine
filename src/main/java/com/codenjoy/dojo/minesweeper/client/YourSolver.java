package com.codenjoy.dojo.minesweeper.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = GmailMy.gmail; // "user@gmail.com";

    private Dice dice;

    private Board board;
    private List<Direction> path = new LinkedList<>();

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        if (path.isEmpty()) {

            Set<Point> needToBeOpen = getAllSafeHiddenPoints();
            if (needToBeOpen.isEmpty()) {
                return Direction.UP.toString();
            }

            path = new DeikstraFindWay().getShortestWay(board.size(), board.getMe(),
                    new LinkedList<Point>(needToBeOpen),
                    new DeikstraFindWay.Possible() {
                        @Override
                        public boolean possible(Point from, Direction direction) {
                            Elements atFrom = board.getAt(from.getX(), from.getY());
                            if (atFrom == Elements.BORDER) {
                                return false;
                            }

                            Point newPoint = direction.change(from);
                            Elements at = board.getAt(newPoint.getX(), newPoint.getY());
                            if (at == Elements.BORDER) {
                                return false;
                            }

                            if (at != Elements.HIDDEN) {
                                return true;
                            }

                            List<Point> near = board.getNear(newPoint.getX(), newPoint.getY(), Elements.NONE);
                            return  !near.isEmpty();
                        }

                        @Override
                        public boolean possible(Point atWay) {
                            Elements atFrom = board.getAt(atWay.getX(), atWay.getY());
                            if (atFrom == Elements.BORDER) {
                                return false;
                            }
                            return true;
                        }
                    });
        }
        return path.remove(0).toString();
    }

    void setBoard(Board board) {
        this.board = board;
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
