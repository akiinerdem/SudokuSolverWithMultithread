package sudoku2;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Run {

    public static BufferedWriter bw = null;// to write moves to file
    public static FileWriter fw = null;

    static {
        try {
            fw = new FileWriter("moves.txt");
            bw = new BufferedWriter(fw);
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        new Run();
    }


    public Run() {
        int[][] sudokuBoard = new int[9][9];
        /**
         * seçilen dosyadan sudokuyu okur ve array haline getirir
         */
        try (Stream<String> stream = Files.lines(Paths.get(askForTxt().getAbsolutePath()))) {//Prompts the user to select a file. Reads the selected file line by line.
            int j = 0;
            for (Object satir : stream.toArray()) {
                for (int i = 0; i < satir.toString().length(); i++) {
                    if (satir.toString().charAt(i) == '*') {
                        sudokuBoard[j][i] = 0;
                    } else {
                        sudokuBoard[j][i] = Integer.parseInt("" + satir.toString().charAt(i));
                    }
                }
                j += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sudoku To Solve:");
        printSudoku(sudokuBoard);


        SudokuThread thread1 = new SudokuThread(copyBoard(sudokuBoard));
        SudokuThread thread2 = new SudokuThread(copyBoard(sudokuBoard));
        SudokuThread thread3 = new SudokuThread(copyBoard(sudokuBoard));
        SudokuThread thread4 = new SudokuThread(copyBoard(sudokuBoard));
        SudokuThread thread5 = new SudokuThread(copyBoard(sudokuBoard));
        thread1.setName("Solver1");
        thread2.setName("Solver2");
        thread3.setName("Solver3");
        thread4.setName("Solver4");
        thread5.setName("Solver5");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread1.interrupt();//threads stop when the job is done
        thread2.interrupt();
        thread3.interrupt();
        thread4.interrupt();
        thread5.interrupt();
        /**
         * After the threads are finished, move control is done.
         */
        boolean thread1Over = false;
        boolean thread2Over = false;
        boolean thread3Over = false;
        boolean thread4Over = false;
        boolean thread5Over = false;

        while (!thread1Over) {
            if (!thread1.isAlive()) {
                thread1Over = true;
                thread1.printMoves();
            }
        }
        while (!thread2Over) {
            if (!thread2.isAlive()) {
                thread2Over = true;
                thread2.printMoves();
            }
        }
        while (!thread3Over) {
            if (!thread3.isAlive()) {
                thread3Over = true;
                thread3.printMoves();
            }
        }
        while (!thread4Over) {
            if (!thread4.isAlive()) {
                thread4Over = true;
                thread4.printMoves();
            }
        }
        while (!thread5Over) {
            if (!thread5.isAlive()) {
                thread5Over = true;
                thread5.printMoves();
            }
        }
        try {
            if (Run.bw != null)//closes the file after typing
                Run.bw.close();
            if (Run.fw != null)//
                Run.fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * opens a file request screen to the user, returns the received file
     */
    private File askForTxt() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        File secilenDosya = null;
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            secilenDosya = jfc.getSelectedFile();
        }
        return secilenDosya;
    }
    /**
     * We copy the board for each thread and give it to the thread to solve it.
     * If we give all threads the same board, the moment someone changes the bits, the whole solution is broken.
     */
    private int[][] copyBoard(int[][] tahta) {
        int[][] copiedBoard = new int[9][9];

        int i = 0, j = 0;
        for (int[] ints : tahta) {
            for (int deger : ints) {
                copiedBoard[i][j] = deger;
                j++;
            }
            i++;
            j = 0;
        }
        return copiedBoard;
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
}