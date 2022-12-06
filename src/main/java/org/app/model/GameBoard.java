package org.app.model;

import java.time.Instant;
import java.util.Random;

public class GameBoard {
    public Integer firstNumber;
    public Integer secondNumber;
    public static final Character[] operators = {'+', '-', '*', '/', '%'};

    public Character operator;

    public Integer expectedResult;

    public Instant timestamp = Instant.now();

    public GameBoard(Integer firstNumber, Integer secondNumber, Character operator, Integer expectedResult) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.operator = operator;
        this.expectedResult = expectedResult;
        this.timestamp = Instant.now();
    }

    public static GameBoard generateGame() {
        Random random = new Random();

        Character operator = GameBoard.operators[random.nextInt(GameBoard.operators.length)];
        Integer firstNumber = null;
        Integer secondNumber = null;
        Integer expectedResult = null;

        if (operator == '+') {
            firstNumber = random.nextInt(1, 100);
            secondNumber = random.nextInt(1, 100);
            expectedResult = firstNumber + secondNumber;
        } else if (operator == '-') {
            firstNumber = random.nextInt(1, 100);
            secondNumber = random.nextInt(1, 100);
            expectedResult = firstNumber - secondNumber;
        } else if (operator == '*') {
            firstNumber = random.nextInt(1, 100);
            secondNumber = random.nextInt(1, 100);
            expectedResult = firstNumber * secondNumber;
        } else if (operator == '/') {
            secondNumber = random.nextInt(1, 100);
            expectedResult = random.nextInt(1, 10);
            firstNumber = secondNumber * expectedResult;
        } else if (operator == '%') {
            firstNumber = random.nextInt(1, 100);
            secondNumber = random.nextInt(1, 100);
            expectedResult = firstNumber % secondNumber;
        }
        return new GameBoard(firstNumber, secondNumber, operator, expectedResult);
    }
}
