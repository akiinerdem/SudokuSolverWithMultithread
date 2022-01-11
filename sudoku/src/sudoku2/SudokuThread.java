package sudoku2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SudokuThread extends Thread {
    public static boolean isItOver = false;
    public long beginning, finish;
    private List<Move> moves;
    private int[][] sudokuBoard;

    public SudokuThread(int[][] sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
        moves = new ArrayList<>();
    }

    /**
     * Running method of Thread
     */
    @Override
    public void run() {//the action after thread started
        beginning = System.currentTimeMillis();
        int i, j;
        if (getName().contains("1")) {//
            i = new Random().nextInt(5);
            j = new Random().nextInt(5);
        } else if (getName().contains("2")) {
            i = new Random().nextInt(5);
            j = new Random().nextInt(5);
        } else {
            i = new Random().nextInt(5);
            j = new Random().nextInt(5);
        }
        if (solve(i, j, sudokuBoard)) {
            printTime();
            printSudoku(sudokuBoard);
        }
    }



    boolean solve(int i, int j, int[][] sudokuToSolve) {
        if (isItOver) {
            if (isAlive()) {
                stop();
                System.out.println("son hali");
                printSudoku(sudokuToSolve);
            }
        }
        else {
            bekle();
        }

        if (i == 9) {
            i = 0;
            if (++j == 9)
                j = 0;
        }

        if (checkFinished(sudokuToSolve)) {
            finish = System.currentTimeMillis();
            return true;
        }

        if (sudokuToSolve[i][j] != 0)
            return solve(i + 1, j, sudokuToSolve);

        for (int val = 1; val <= 9; ++val) {
            if (check(i, j, val, sudokuToSolve)) {
                sudokuToSolve[i][j] = val;
                moves.add(new Move(i, j, val));
                if (solve(i + 1, j, sudokuToSolve))
                    return true;
            }
        }
        sudokuToSolve[i][j] = 0;
        return false;
    }

    /**
     * method that prints the moves after the solution is found
     */
    public void printMoves() {
        System.out.println("#########################################");
        System.out.println("#########################################");
        System.out.println(getName() + " Moves");
        int i = 0;
        for (Move move : moves) {
            if (i % 10 == 0) {
                System.out.println();
            }
            System.out.print(move + "\t");
            i++;
        }
        System.out.println();
        System.out.println("Latest status of sudoku: ");
        printSudoku(sudokuBoard);
        System.out.println("#########################################");
        System.out.println("#########################################");
        printMovesToTxt();
    }


    private void bekle() {
        try {
            int beklemeSuresi = new Random().nextInt(5);
            Thread.sleep(beklemeSuresi);
        } catch (InterruptedException e) {

        }
    }

    /**
     * checks if sudoku is solved
     */
    boolean checkFinished(int[][] sudokuToSolve) {
        for (int[] area : sudokuToSolve) {
            for (int number : area) {
                if (number == 0) {
                    return false;
                }
            }
        }
        isItOver = true;
        return true;
    }

    /**
     * Check here before assigning a value to sudoku
     */
    boolean check(int i, int j, int val, int[][] sudokuToSolve) {
        //checks rows
        for (int k = 0; k < 9; ++k)
            if (val == sudokuToSolve[k][j])
                return false;
        // checks columns
        for (int k = 0; k < 9; ++k)
            if (val == sudokuToSolve[i][k])
                return false;
        /**
         * Controls 3x3 areas
         */
        int rowArea = (i / 3) * 3;
        int columnArea = (j / 3) * 3;
        for (int k = 0; k < 3; ++k)
            for (int m = 0; m < 3; ++m)
                if (val == sudokuToSolve[rowArea + k][columnArea + m])
                    return false;

        return true;
    }

    void printSudoku(int[][] solution) {
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                System.out.println(" -----------------------");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(solution[i][j] == 0
                        ? "*"
                        : Integer.toString(solution[i][j]));

                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }

    public void printTime() {
        System.out.println("Solved by " + getName() + " in " + (finish - beginning) + " millisecond.");
    }

    private void printMovesToTxt() {

      try {
            Run.bw.write("#########################################\n");
            Run.bw.write("#########################################\n");
            Run.bw.write(getName() + " Moves\n");
            int h = 0;
            for (Move move : moves) {
                if (h % 10 == 0) {
                    Run.bw.newLine();
                }
                Run.bw.write(move + "\t");
                h++;
            }
            Run.bw.newLine();
            Run.bw.write("Latest status of sudoku: \n");
            Run.bw.write("#########################################\n");
            Run.bw.write("#########################################\n");
            for (int i = 0; i < 9; ++i) {
                if (i % 3 == 0)
                    Run.bw.write(" -----------------------\n");
                for (int j = 0; j < 9; ++j) {
                    if (j % 3 == 0) Run.bw.write("| ");
                    Run.bw.write(sudokuBoard[i][j] == 0
                            ? "*"
                            : Integer.toString(sudokuBoard[i][j]));

                    Run.bw.write(' ');
                }
                Run.bw.write("|\n");
            }
            Run.bw.write(" -----------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}