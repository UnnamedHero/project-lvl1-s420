package games;

import javax.imageio.IIOException;
import java.io.IOException;

public class Casino {
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
            case '1': return new Slot(1);
            case '2': return new Drunkard(2, 999);
            case '3': return new BlackJack(1);
            case '4':
            default: return null;
        }
    }

    public void play() throws IOException {
        System.out.println("Добро пожаловать в казино \'Последняя надежда\'!");
        System.out.println("Ваш начальный баланс равен 100$.\n" +
                "Стандартная ставка в любой игре равна 10$");

        while(getCash() > 0) {
            System.out.printf("%nВаш баланс: %d$. " +
                    "Выберите игру%n" +
                    "1 - \'Однорукий бандит\'%n" +
                    "2 - \'Пьяница\' (вы игрок №1) %n" +
                    "3 - \'Очко\'%n" +
                    "4 - Покинуть казино%n", getCash());
            final ICasinoGame game = getGame();
            if (game == null) {
                System.out.printf("Возвращайтесь ещё! Ваш баланс %d$%n", getCash());
                return;
            }
            final int newCash = game.playSingleRound(getCash());
            setCash(newCash);
        }
        System.out.println("Вы всё проиграли. До свидания.");
    }

    public static void main(String... __) throws IOException {
        Casino game = new Casino(100);
        game.play();
    }
}
