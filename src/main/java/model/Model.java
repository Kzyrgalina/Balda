package model;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Model {
    private char[][] field;
    private Point selectedCell;
    boolean charWasSet;
    int howManyLetters;
    private ArrayList<Point> path;
    private boolean currentPlayer;
    private Player playerTrue;
    private Player playerFalse;
    private char emptyChar;

    public Model(){
        field = new char[5][5];
        path = new ArrayList<Point>();
        playerTrue = new Player();
        playerFalse = new Player();
        emptyChar = '\u0000';
        start();
    }

    public void start(){
        currentPlayer = true;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                field[i][j] = emptyChar;
            }
        }
        path.clear();
        writeFirstWord(randomWord());
        selectedCell = new Point(-1, -1);
        playerTrue.restart();
        playerFalse.restart();
        charWasSet = false;
        howManyLetters = 0;
    }

    //добавление стартового слова в таблицу
    public void writeFirstWord(String word){
        for (int i = 0; i < field.length; i++) {
            field[i][2] = word.charAt(i);
        }
    }

    //поиск случайного слова из списка
    public String randomWord(){
        ArrayList<String> wordList = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("src\\main\\resources\\randomWord"));
            String word;
            while ((word = br.readLine()) != null) wordList.add(word);
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int a = 0;
        int b = wordList.size() - 1;
        int random = a + (int) (Math.random() * b);
        return wordList.get(random);
    }

    //выбор клетки для установки новой буквы
    public boolean setSelectedCell(int x, int y) {
        if (isGameOver()) return false;
        if (!charWasSet) {
            if (hasChar(x, y)) {
                return false;
            }
            if (hasChar(x + 1, y) || hasChar(x - 1, y)
                    || hasChar(x, y + 1) || hasChar(x, y - 1)) {

                selectedCell.x = x;
                selectedCell.y = y;
                return true;
            }
        }
        return false;
    }

    //содержит ли клетка букву
    public boolean hasChar(int x, int y){
        if (x >= 5 || y >= 5 || x < 0 || y < 0) return false;
        return field[x][y] != emptyChar;
    }

    //установка буквы
    public void setChar(char c){
        if (!charWasSet && selectedCell.x >= 0 && selectedCell.y >= 0) {
            field[selectedCell.x][selectedCell.y] = c;
            charWasSet = true;
        }
    }

    //следующая клетка пути должна быть рядом с предыдущей
    public boolean isNeighbours(Point pNew, Point pLast){
        boolean left = pNew.x + 1 == pLast.x && pNew.y == pLast.y;
        boolean right = pNew.x == pLast.x + 1 && pNew.y == pLast.y;
        boolean up = pNew.x == pLast.x && pNew.y - 1 == pLast.y;
        boolean down = pNew.x == pLast.x && pNew.y + 1 == pLast.y;
        return left || right || up || down;
    }

    //сбор пути по клеткам
    public int selectPath(Point point){
        if (path.isEmpty()) {
            path.add(point);
            return 0;
        } else {
            Point lastPoint= path.get(path.size() - 1);
            if (isNeighbours(point, lastPoint) && !path.contains(point)){
                path.add(point);
                return 0;
            }
            if (point.x == lastPoint.x && point.y == lastPoint.y){
                path.remove(point);
                return 1;
            }
        }
        return 2;
    }

    //содержит ли путь новую букву
    public boolean isSelectedCharInPath(){
        if (isGameOver()) return false;
        for (int i = 0; i < path.size(); i++) {
            if (selectedCell != null && selectedCell.equals(path.get(i))) return true;
        }
        return false;
    }

    //построение слова из букв
    public String buildWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            Point point = path.get(i);
            sb.append(field[point.x][point.y]);
        }
        return sb.toString();
    }

    //добавление слова в список для каждого игрока
    public boolean addWord(String word){
        if (playerTrue.notContainWord(word) && playerFalse.notContainWord(word)){
            if (currentPlayer){
                playerTrue.addWord(word);
                if (!word.equals("---")) {
                    playerTrue.setLoseCount(0);
                    playerFalse.setLoseCount(0);
                }
            } else {
                playerFalse.addWord(word);
                if (!word.equals("---")) {
                    playerTrue.setLoseCount(0);
                    playerFalse.setLoseCount(0);
                }
            }
            currentPlayer = !currentPlayer;
            if (!word.equalsIgnoreCase("---"))howManyLetters++;
            return true;
        }
        return false;
    }

    //пропуск хода
    public void loseTurn(){
        if(!isGameOver()){
            addWord("---");
            if (selectedCell.x > 0 && selectedCell.y > 0){
                field[selectedCell.x][selectedCell.y] = emptyChar;
            }
        }

    }

    //очистить путь и сбросить выбор клетки
    public void clearPath(){
        path.clear();
        charWasSet = false;
        selectedCell = new Point(-1,-1);
    }

    public boolean isGameOver(){
        return (playerTrue.getLoseCount() > 1 && playerFalse.getLoseCount() > 1 || howManyLetters > 19);
    }

    public GameState getWinner(){
        if (isGameOver()){
            if ((playerTrue.getLoseCount() == 2 && playerFalse.getLoseCount() == 2) ||
                (playerTrue.getFullScore() == playerFalse.getFullScore())) return GameState.DRAW;
            if (playerTrue.getFullScore() > playerFalse.getFullScore()) return GameState.FIRST;
            else return GameState.SECOND;
        } else return GameState.PLAY;
    }

    public int getPlayerSize(boolean player){
        return getPlayer(player).wordsListSize();
    }

    public int getScore(boolean player){
        return getPlayer(player).getFullScore();
    }

    public int getLoseCount(boolean player){
        return getPlayer(player).getLoseCount();
    }

    public ArrayList<Point> getPath() {
        return path;
    }

    public String getLastWord(boolean player){
        return getPlayer(player).getLastWord();
    }

    public boolean getCurrentPlayer(){
        return currentPlayer;
    }
    public Player getPlayer(boolean player){
        return player ? playerTrue : playerFalse;
    }
    public boolean isCharWasSet() {
        return charWasSet;
    }

    public char[][] getField() {
        return field;
    }

    public Point getSelectedCell() {
        return selectedCell;
    }

    public boolean setSelectedCell(Point point) {
        return setSelectedCell(point.x, point.y);
    }

    public HashMap<String, Integer> getWords(boolean player){
        return getPlayer(player).getWords();
    }
}
