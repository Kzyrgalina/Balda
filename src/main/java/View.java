import model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View{

    JFrame frame;
    Cell[][] field;
    JButton[] keyboard;
    JButton btnApply;
    JButton btnCancel;
    JButton btnNewGame;
    JButton btnLose;
    JLabel[] wordsTrue;
    JLabel[] wordsFalse;
    JLabel scoreTrue;
    JLabel scoreFalse;
    JLabel message;
    JLabel loseTrue;
    JLabel loseFalse;
    JLabel nameTrue;
    JLabel nameFalse;
    JLabel currentPlayer;
    JLabel score;
    JLabel lose;

    public View(){
        frame = new JFrame();
        frame.setSize(1000, 670);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);

        //поле для игры
        field = new Cell[5][5];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = new Cell(i, j);
                field[i][j].setBounds(375 + 50*i, 70 + 50*j, 50, 50);
                frame.add(field[i][j]);
            }
        }

        //алфавит
        keyboard = new JButton[32];
        for (int i = 0; i < keyboard.length; i++) {
            keyboard[i] = new JButton("" + (char) ('а' + i));
            keyboard[i].setBounds(300 + 50*(i % 8), 380 + 50*(i / 8), 50, 50);
            keyboard[i].setBackground(Color.WHITE);
            frame.add(keyboard[i]);
        }

        //список слов первого игрока
        wordsTrue = new JLabel[30];
        for (int i = 0; i < wordsTrue.length; i++) {
            wordsTrue[i] = new JLabel("");
            wordsTrue[i].setBounds(80, 80 + i*17, 100, 50);
            frame.add(wordsTrue[i]);
        }

        //список слов второго игрока
        wordsFalse = new JLabel[30];
        for (int i = 0; i < wordsFalse.length; i++) {
            wordsFalse[i] = new JLabel("");
            wordsFalse[i].setBounds(190, 80 + i*17, 100, 50);
            frame.add(wordsFalse[i]);
        }

        btnApply = new JButton("Подтвердить");
        btnApply.setBounds(440,330, 120, 40);
        frame.add(btnApply);

        btnCancel = new JButton("Отменить");
        btnCancel.setBounds(300, 330, 120, 40);
        frame.add(btnCancel);

        btnLose = new JButton("Пропустить");
        btnLose.setBounds(580, 330, 120, 40);
        frame.add(btnLose);

        btnNewGame = new JButton("Новая игра");
        btnNewGame.setBounds(440, 15, 120, 40);
        frame.add(btnNewGame);

        score = new JLabel("Счет:");
        score.setBounds(750, 100, 100, 50);
        frame.add(score);

        scoreTrue = new JLabel("0");
        scoreTrue.setBounds(750, 130, 100, 50);
        frame.add(scoreTrue);

        scoreFalse = new JLabel("0");
        scoreFalse.setBounds(850, 130, 100, 50);
        frame.add(scoreFalse);

        lose = new JLabel("Количество пропусков:");
        lose.setBounds(750, 180, 200, 50);
        frame.add(lose);

        loseTrue = new JLabel("0");
        loseTrue.setBounds(750, 210, 100, 50);
        frame.add(loseTrue);

        loseFalse = new JLabel("0");
        loseFalse.setBounds(850, 210, 100, 50);
        frame.add(loseFalse);

        nameTrue = new JLabel("Игрок 1");
        nameTrue.setBounds(80, 50, 100, 50);
        frame.add(nameTrue);

        nameFalse = new JLabel("Игрок 2");
        nameFalse.setBounds(190, 50, 100, 50);
        frame.add(nameFalse);

        message = new JLabel("");
        message.setBounds(750, 260, 200, 50);
        message.setForeground(Color.RED);
        frame.add(message);

        currentPlayer = new JLabel("Сейчас ходит игрок 1");
        currentPlayer.setBounds(750, 50, 200, 50);
        frame.add(currentPlayer);

        frame.setVisible(true);
    }

    public void restart(){
        for (int i = 0; i < wordsTrue.length; i++) {
            wordsTrue[i].setText("");
            wordsFalse[i].setText("");
        }

        scoreTrue.setText("0");
        scoreFalse.setText("0");
        loseTrue.setText("0");
        loseFalse.setText("0");
        currentPlayer.setText("Сейчас ходит игрок 1");
        frame.repaint();
    }

    public void showMessage(String string){
        message.setText(string);
    }

    //добавить новое слово в рейтинговую таблицу
    public void addNewWord(boolean player, String word, int i){
        if (player){
            wordsTrue[i - 1].setText(word);
        } else {
            wordsFalse[i - 1].setText(word);
        }
    }

    //обновить счет
    public void setScore(boolean player, int score, int loseCount){
        if (player){
            scoreTrue.setText(score + "");
            loseTrue.setText(loseCount + "");
        } else {
            scoreFalse.setText(score + "");
            loseFalse.setText(loseCount + "");
        }
    }

    //кто сейчас ходит
    public void setPlayer(boolean player){
        if (!player){
            currentPlayer.setText("Сейчас ходит игрок 1");
        } else {
            currentPlayer.setText("Сейчас ходит игрок 2");
        }
    }

    public void listenField(ActionListener listener){
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j].addActionListener(listener);
            }
        }
    }

    public void listenKeyboard(ActionListener listener){
        for (int i = 0; i < keyboard.length; i++) {
            keyboard[i].addActionListener(listener);
        }
    }

    public void listenApply(ActionListener listener){
        btnApply.addActionListener(listener);
    }

    public void listenCancel(ActionListener listener){
        btnCancel.addActionListener(listener);
    }

    public void listenLose(ActionListener listener){
        btnLose.addActionListener(listener);
    }

    public void listenNewGame(ActionListener listener){
        btnNewGame.addActionListener(listener);
    }

    public Cell[][] getField() {
        return field;
    }

}
