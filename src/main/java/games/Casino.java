package games;

import org.slf4j.Logger;

import java.io.IOException;

public class Casino {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Casino.class);

    private int cash;

    public Casino(final int newCash) {
        this.cash = newCash;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int newCash) {
        this.cash = newCash;
    }

    private  ICasinoGame getGame() throws IOException{
        switch (Choice.getCharacterFromUser()) {
            case '1': return new Slot();
            case '2': return new Drunkard();
            case '3': return new BlackJack();
            case '4':
            default: return null;
        }
    }

    public void play() throws IOException {
        log.info("Добро пожаловать в казино \'Последняя надежда\'!");
        log.info("Ваш начальный баланс равен 100$.\n" +
                "Стандартная ставка в любой игре равна 10$\n");

        while(getCash() > 0) {
            log.info("Ваш баланс: {}$. " +
                    "Выберите игру\n" +
                    "1 - \'Однорукий бандит\'\n" +
                    "2 - \'Пьяница\' (вы игрок №1)\n" +
                    "3 - \'Очко\'\n" +
                    "4 - Покинуть казино", getCash());
            final ICasinoGame game = getGame();
            if (game == null) {
                log.info("Возвращайтесь ещё! Ваш баланс {}$", getCash());
                return;
            }
            final int newCash = game.playSingleRound(getCash());
            setCash(newCash);
        }
        log.info("Вы всё проиграли. До свидания.");
    }

    public static void main(String... __) throws IOException {
        Casino game = new Casino(100);
        game.play();
    }
}
