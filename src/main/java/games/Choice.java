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
        System.out.println("Выберите игру\n1 - \'Однорукий бандит\'\n2 - \'Пьяница\'\n3 - \'Очко\'");
        switch (getCharacterFromUser()) {
            case '1': Slot.main(); break;
            case '2': Drunkard.main(); break;
            case '3': BlackJack.main(); break;
            default:
                System.out.println("Игры с таким номером нет");
        }
    }
}
