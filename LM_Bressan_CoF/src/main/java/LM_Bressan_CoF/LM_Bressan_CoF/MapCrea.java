package LM_Bressan_CoF.LM_Bressan_CoF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by pietro on 03/05/16.
 */
public class MapCrea {

    private final int row;
    private final int collon;
    private final int[][] grid;
    private final MapJPanel panel;


    public MapCrea(int row, int collon) {
        this.row = row;
        this.collon = collon;
        grid = new int[row][collon];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < collon; j++)
                this.grid[i][j] = 0;
        JFrame frame = new JFrame("Select cities");
        this.panel = new MapJPanel();
        Dimension d = new Dimension(800, 600);
        frame.setSize(d);
        frame.setVisible(true);
        frame.toFront();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void cleanGrid(int[][] grid) {
        for (int i = 0; i < this.row; i++)
            for (int j = 0; j < this.collon; j++)
                grid[i][j] = 0;
    }
}
