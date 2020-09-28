import model.Cell;
import model.CellState;
import model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.CellState.EMPTY;
import static model.CellState.BUSY;
import static model.CellState.SELECTED;
import static model.CellState.PATH;
import static model.CellState.PRE_APPLY;
import static model.CellState.APPLY_TO_PATH;

public class Controller {
    private View view;
    private Model model;
    private Cell selectedCell = null;
    private char emptyChar = '\u0000';

    public Controller(final View view, final Model model){
        this.view = view;
        this.model = model;
        restart();

        view.listenField(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cell cell = (Cell) e.getSource();
                CellState state;

                //выбор кнопки для установки буквы
                if (!model.isCharWasSet() && model.setSelectedCell(cell.getLocate())) {
                    if (selectedCell != null) selectedCell.setState(EMPTY);
                    selectedCell = cell;
                    selectedCell.setState(SELECTED);
                }

                //отмена выбранной кнопки в пути
                state = cell.getState();
                if (model.isCharWasSet() && (state == APPLY_TO_PATH || state == PATH)){
                    if (model.selectPath(cell.getLocate()) == 1){
                        if (state == APPLY_TO_PATH){
                            cell.setState(PRE_APPLY);
                        } else {
                            cell.setState(BUSY);
                        }
                    }
                } else{
                    //выбор кнопки в путь слова
                    if (model.isCharWasSet() && (state == BUSY || state == PRE_APPLY)) {
                        if (model.selectPath(cell.getLocate()) == 0){
                            if (state == PRE_APPLY){
                                cell.setState(APPLY_TO_PATH);
                            } else {
                                cell.setState(PATH);
                            }
                        }
                    }
                }
            }
        });

        //установка буквы на поле
        view.listenKeyboard(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!model.isCharWasSet()){
                    model.setChar(((JButton) e.getSource()).getText().charAt(0));
                    init();
                    if (selectedCell != null) {
                        selectedCell.setState(CellState.PRE_APPLY);
                        selectedCell = null;
                    }
                }
            }
        });

        //добавить слово
        view.listenApply(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                boolean player = model.getCurrentPlayer();

                if (!model.isSelectedCharInPath()){
                    view.showMessage("Слово не содержит новую букву!");
                    cancel();
                    return;
                }

                String word = model.buildWord();

                if (!model.addWord(word)){
                    view.showMessage("Такое слово уже есть!");
                    cancel();
                    return;
                }

                view.showMessage("");
                view.addNewWord(player, word, model.getPlayerSize(player));
                view.setScore(true, model.getScore(true), model.getLoseCount(true));
                view.setScore(false, model.getScore(false), model.getLoseCount(false));
                view.setPlayer(player);

                for (Point point :
                        model.getPath()) {
                    view.getField()[point.x][point.y].setState(BUSY);
                }

                model.clearPath();
                view.showMessage(gameOver());
            }
        });

        //пропустить ход
        view.listenLose(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            boolean player = model.getCurrentPlayer();
                model.loseTurn();
                view.addNewWord(player, model.getLastWord(player), model.getPlayerSize(player));
                view.setScore(player, model.getScore(player), model.getLoseCount(player));
                view.setPlayer(player);

            for (Point point : model.getPath()) {
                view.getField()[point.x][point.y].setState(BUSY);
            }

            model.clearPath();
            view.setPlayer(player);
            init();
            view.showMessage(gameOver());
            }
        });

        //отмена хода
        view.listenCancel(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

        //новая игра
        view.listenNewGame(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });

        init();
    }

    //отмена действий на поле
    public void cancel(){
        Point cell = model.getSelectedCell();
        if (cell.x != -1 || cell.y != -1){
            model.getField()[cell.x][cell.y] = emptyChar;
            model.clearPath();
            init();
        }
    }

    public String gameOver(){
        if (model.isGameOver()){
            switch (model.getWinner()){
                case DRAW: return "Ничья";
                case FIRST: return "Победил игрок 1";
                case SECOND: return "Победил игрок 2";
            }
        }
        return "";
    }

    public void restart(){
        model.start();
        view.restart();
        init();
    }

    //синхроизация модели поля и внешнего вида
    public void init(){
        Cell[][] field = view.getField();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                Cell cell = field[i][j];
                if (model.getField()[i][j] != emptyChar){
                    cell.setState(BUSY);
                } else cell.setState(CellState.EMPTY);
                cell.setText(model.getField()[i][j] + "");
            }
        }
    }
}
