package games;

import java.io.IOException;

public class Choice {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    static char getCharacterFromUser() throws IOException {
        byte[] input = new byte[1 + LINE_SEPARATOR.length()];
        if (System.in.read(input) != input.length) {
            throw new RuntimeException("Пользователь ввёл недостаточное кол-во символов");
        }
    return (char) input[0];
    }

    public static void main(final String... __) throws IOException {
        System.out.printf("Выберите игру%n" +
                "1 - Казино (единый баланс для всех игр)%n" +
                "2 - \'Однорукий бандит\'%n" +
                "3 - \'Пьяница\'%n" +
                "4 - \'Очко\'%n");
        switch (getCharacterFromUser()) {
            case '1': Casino.main(); break;
            case '2': Slot.main(); break;
            case '3': Drunkard.main(); break;
            case '4': BlackJack.main(); break;
            default:
                System.out.println("Игры с таким номером нет");
        }
    }
}
