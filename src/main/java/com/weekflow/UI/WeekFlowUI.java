package com.weekflow.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WeekFlowUI {

    static final int ROWS = 24;  // 시간 블록 수
    static final int COLS = 7;   // 요일 수

    public void showUI() {

        JFrame frame = new JFrame("WeekFlow Scheduler UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        JPanel grid = new JPanel(new GridLayout(ROWS, COLS, 2, 2));
        JButton[][] blocks = new JButton[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                JButton btn = new JButton();
                btn.setBackground(Color.WHITE);

                btn.addActionListener(e -> {
                    if (btn.getBackground() == Color.WHITE) {
                        btn.setBackground(Color.CYAN);
                    } else {
                        btn.setBackground(Color.WHITE);
                    }
                });

                blocks[r][c] = btn;
                grid.add(btn);
            }
        }

        frame.add(grid);
        frame.setVisible(true);
    }
}
