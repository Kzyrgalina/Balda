package model;

import javafx.util.Pair;
import java.util.HashMap;

public class Player {
    private int fullScore = 0;
    private int loseCount = 0;
    private int i = 0;
    private HashMap<String, Integer> words;
    private Pair<String,Integer> lastWord;

    public Player(){
        words = new HashMap<String, Integer>();
        lastWord = new Pair<String, Integer>("", 0);
    }

    public void addWord(String word){
        int score = 0;
        if (word.equals("---")){
           loseCount++;
           words.put(i + "---", 0);
           i++;
        } else {
            score = word.length();
            words.put(word, score);
            loseCount = 0;
        }
        fullScore += score;
        lastWord = new Pair<String, Integer>(word, score);
    }

    public void restart(){
        i = 0;
        fullScore = 0;
        words.clear();
        loseCount = 0;
    }

    public boolean notContainWord(String word){
        if (word.equals("---")) return true;
        return !words.containsKey(word);
    }

    public HashMap<String, Integer> getWords() {
        return words;
    }

    public String getLastWord(){
        return lastWord.getKey();
    }

    public int wordsListSize(){
        return words.size();
    }

    public int getFullScore() {
        return fullScore;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(int loseCount) {
        this.loseCount = loseCount;
    }
}
