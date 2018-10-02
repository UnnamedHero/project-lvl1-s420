package games;

import java.io.IOException;

public class Choice {
    public static void main(final String... __) throws IOException {
        System.out.println("Выберите игру\n1 - \'Однорукий бандит\'\n2 - \'Пьяница\'");
        switch (System.in.read()) {
            case '1': Slot.main(); break;
            case '2': Drunkard.main(); break;
            default:
                System.out.println("Игры с таким номером нет");
        }
    }
}
