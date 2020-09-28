package model;

import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    Model model = new Model();

    @Test
    void startAndSetChar() {
        Point truePoint = new Point(2,1);
        Point falsePoint = new Point(2,0);
        model.start();
        //установим букву верно
        model.setSelectedCell(truePoint);
        model.setChar('a');
        //установим букву в недоступной клетке
        assertFalse(model.setSelectedCell(falsePoint));
        model.setChar('b');
        //установилась только первая, а вторая осталась пустой
        assertEquals(model.getField()[2][1], 'a');
        assertNotEquals(model.getField()[2][0], 'b');
    }

    @Test
    void buildPathAndWord(){
        ArrayList<Point> expected = new ArrayList<Point>();
        expected.add(new Point(2, 2));
        expected.add(new Point(1, 2));
        expected.add(new Point(1, 1));

        //построение пути из символов
        model.start();
        model.writeFirstWord("asdfg");
        assertTrue(model.setSelectedCell(new Point(1,1)));
        model.setChar('w');
        assertTrue(model.isCharWasSet()); //буква установлена
        assertEquals(0, model.selectPath(new Point(2, 2)));
        assertEquals(0, model.selectPath(new Point(1, 2)));
        assertFalse(model.isSelectedCharInPath()); //путь не содержит новую букву
        assertEquals(0, model.selectPath(new Point(1, 1)));
        assertTrue(model.isSelectedCharInPath()); //слово содержит новую букву

        assertEquals(expected, model.getPath());

        //построение слова из букв
        assertEquals("dsw", model.buildWord());
    }

    @Test
    void addWord(){
        HashMap<String,Integer> expectedFirst = new HashMap<String, Integer>();
        HashMap<String,Integer> expectedSecond = new HashMap<String, Integer>();
        expectedFirst.put("qwerty", 6);
        expectedFirst.put("zxcvb",5);
        expectedSecond.put("asdfg", 5);

        model.start();
        assertTrue(model.getCurrentPlayer());
        assertTrue(model.addWord("qwerty")); //слово добавлено
        assertFalse(model.getCurrentPlayer()); //сменился игрок
        assertFalse(model.addWord("qwerty")); //повтор, слово не добавлено
        assertFalse(model.getCurrentPlayer()); //игрок остался прежним
        assertTrue(model.addWord("asdfg")); //теперь новое слово добавлено
        assertTrue(model.getCurrentPlayer()); //после добавления игрок снова сменился
        assertTrue(model.addWord("zxcvb"));

        //полученные баллы и крайние слова игроков для рейтинговой таблицы
        assertEquals(11, model.getScore(true));
        assertEquals(5, model.getScore(false));
        assertEquals("zxcvb", model.getLastWord(true));
        assertEquals("asdfg", model.getLastWord(false));

        //равенство списка слов
        assertEquals(expectedFirst, model.getWords(true));
        assertEquals(expectedSecond, model.getWords(false));
    }

    @Test
    void lose(){
        model.start();

        //игроки просто так пропускают ход
        assertTrue(model.addWord("---"));
        assertTrue(model.addWord("1"));
        assertTrue(model.addWord("---"));
        assertTrue(model.addWord("2"));

        //игроки 2 раза подряд пропускают ход
        assertTrue(model.addWord("---"));
        assertTrue(model.addWord("---"));
        assertTrue(model.addWord("---"));
        assertTrue(model.addWord("---"));

        //пропуски содержатся в списке слов
        assertTrue(model.getWords(true).containsKey("0---"));
        assertTrue(model.getWords(true).containsKey("1---"));
        assertTrue(model.getWords(true).containsKey("2---"));
        assertTrue(model.getWords(false).containsKey("0---"));
        assertTrue(model.getWords(false).containsKey("1---"));


        //игра окончена, ничья
        assertTrue(model.isGameOver());
        assertEquals(GameState.DRAW, model.getWinner());

    }

    @Test
    void win(){
        model.start();
        model.addWord("123"); // для первого игрока
        model.addWord("1234"); // для второго игрока

        for (int i = 10; i < 28; i++) {
            model.addWord(i + "");
        }

        //игра окончена, победил второй
        assertTrue(model.isGameOver());
        assertEquals(21, model.getScore(true));
        assertEquals(22, model.getScore(false));
        assertEquals(GameState.SECOND, model.getWinner());
    }
}