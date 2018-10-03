package games;

import java.io.IOException;
import org.slf4j.Logger;

public class Choice {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Choice.class);

    static char getCharacterFromUser() throws IOException {
        byte[] input = new byte[1 + LINE_SEPARATOR.length()];
        if (System.in.read(input) != input.length) {
            return getCharacterFromUser();
        }
    return (char) input[0];
    }

    public static void main(final String... __) throws IOException {
        log.info("Выберите игру:");
        log.info("1 - Казино (единый баланс для всех игр");
        log.info("2 - \'Однорукий бандит\'");
        log.info("3 - \'Пьяница\'");
        log.info("4 - \'Очко\'");
        switch (getCharacterFromUser()) {
            case '1': Casino.main(); break;
            case '2': Slot.main(); break;
            case '3': Drunkard.main(); break;
            case '4': BlackJack.main(); break;
            default:
                log.info("Игры с таким номером нет");
        }
    }
}
