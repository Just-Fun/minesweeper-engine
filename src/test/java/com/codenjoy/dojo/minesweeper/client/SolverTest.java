package com.codenjoy.dojo.minesweeper.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SolverTest {

    private Dice dice;
    private YourSolver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new YourSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void shouldStartMoveUp() {
        asertAI("☼☼☼☼☼☼☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼☺****☼" +
                "☼☼☼☼☼☼☼", Direction.UP);
    }

    @Test
    public void shouldIfThereAreEmptySpaceGoForward() {
        asertAI("☼☼☼☼☼☼☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼☺****☼" +
                "☼ ****☼" +
                "☼☼☼☼☼☼☼", Direction.RIGHT);
    }


    @Test
    public void shouldDeadLoop1() {
        asertAI("☼☼☼☼☼☼☼☼" +
                "☼******☼" +
                "☼☺13***☼" +
                "☼  1***☼" +
                "☼  1***☼" +
                "☼*  ***☼" +
                "☼  ****☼" +
                "☼☼☼☼☼☼☼☼", Direction.DOWN);
    }

    @Test
    public void shouldDeadLoop2() {
        asertAI("☼☼☼☼☼☼☼☼" +
                "☼******☼" +
                "☼ 13***☼" +
                "☼☺ 1***☼" +
                "☼  1***☼" +
                "☼*  ***☼" +
                "☼  ****☼" +
                "☼☼☼☼☼☼☼☼", Direction.UP);
    }

    @Test
    public void shouldDeadLoopTest() {
        asertAI("☼☼☼☼☼" +
                "☼***☼" +
                "☼ 3*☼" +
                "☼☺1*☼" +
                "☼☼☼☼☼", Direction.UP);
    }

    @Test
    public void resolveBug1() {
        asertAI("☼☼☼☼☼☼" +
                "☼☺ 1*☼" +
                "☼  2*☼" +
                "☼  2*☼" +
                "☼‼ 2*☼" +
                "☼☼☼☼☼☼ ", Direction.UP);
    }

    @Ignore
    @Test
    public void resolveBug() {
        asertAI("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼   ☺    1****☼" +
                "☼        2****☼" +
                "☼     ‼  2****☼" +
                "☼  ‼‼‼ ‼ 2****☼" +
                "☼     ‼ ‼‼****☼" +
                "☼       1*****☼" +
                "☼       2*****☼" +
                "☼‼    ‼13*****☼" +
                "☼     2*******☼" +
                "☼     2*******☼" +
                "☼     2*******☼" +
                "☼    ‼1*******☼" +
                "☼    ‼********☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼ ", Direction.UP);
    }

    @Test
    public void shouldGetAllNeedToBeOpen() {
        ai.setBoard(board(
                "☼☼☼☼☼☼☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼*****☼" +
                "☼☺****☼" +
                "☼ ****☼" +
                "☼☼☼☼☼☼☼"));
        Set<Point> actual = ai.getAllSafeHiddenPoints();
        assertEquals("[[2,4], [2,5]]", actual.toString());
    }

    @Test
    public void shouldGetNear() {
        Board board = board(
                    " ☼☼☼☼☼ " +
                    "☼*****☼" +
                    "☼*****☼" +
                    "☼** **☼" +
                    "☼*****☼" +
                    "☼ **** " +
                    "☼☼☼☼☼☼☼");
        assertEquals("[[1,4], [2,4], [2,5]]", board.getNear(1, 5, Elements.HIDDEN).toString());
        assertEquals("[[2,1], [1,2], [2,2]]", board.getNear(1, 1, Elements.HIDDEN).toString());
        assertEquals("[[4,4], [5,4], [4,5]]", board.getNear(5, 5, Elements.HIDDEN).toString());
        assertEquals("[[4,1], [4,2], [5,2]]", board.getNear(5, 1, Elements.HIDDEN).toString());
        assertEquals("[[2,2], [3,2], [4,2], [2,3], [4,3], [2,4], [3,4], [4,4]]", board.getNear(3, 3, Elements.HIDDEN).toString());
        assertEquals("[]", board.getNear(50, 50, Elements.HIDDEN).toString());
    }





    private void asertAI(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
