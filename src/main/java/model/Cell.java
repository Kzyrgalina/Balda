package model;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {
    private Point locate;
    private CellState state;

    public Cell(int x, int y){
        locate = new Point(x, y);
        state = CellState.EMPTY;
    }

    public void setState(CellState state){
        this.state = state;
        switch (state){
            case EMPTY: setBackground(Color.WHITE);
            break;
            case BUSY: setBackground(Color.LIGHT_GRAY);
            break;
            case SELECTED: setBackground(Color.PINK);
            break;
            case PATH: setBackground(Color.YELLOW);
            break;
            case PRE_APPLY:setBackground(Color.MAGENTA);
            break;
            case APPLY_TO_PATH: setBackground(Color.CYAN);
        }
    }

    public CellState getState() {
        return state;
    }

    public Point getLocate() { return locate; }
}
