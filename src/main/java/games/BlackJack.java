package games;

import org.slf4j.Logger;

import java.io.IOException;
import java.util.Random;

public class BlackJack implements ICasinoGame {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(BlackJack.class);

    private static final int MAX_VALUE = 21;
    private static final int MAX_CARDS_COUNT = 8;

    private static final int PLAYER = 0;
    private static final int COMPUTER = 1;

    private static int[] pack;
    private static int packCursor;

    private static int[][] playersCards;
    private static int[] playersCursors;

    private static int[] playersMoney = { 0, 0 };

    private static boolean confirm(final String message) throws IOException {
        log.info("{} \"Y\" - Да, {любой другой символ} - нет (Что бы выйти из игры, нажмите Ctrl + C)", message);
        switch (Choice.getCharacterFromUser()) {
            case 'Y':
            case 'y': return true;
            default: return false;
        }
    }

    private static void shiftPackCursor() {
        packCursor += 1;
    }

    private static int getPlayerCursor(final int playerId) {
        return playersCursors[playerId];
    }

    private static void shiftPlayerCursor(final int playerId) {
        playersCursors[playerId] += 1;
    }

    private static void initRound() {
        log.info("\n\nУ Вас {}$, у компьютера - {}$. Начинаем новый раунд!", playersMoney[0], playersMoney[1]);
        pack = CardUtils.makePack();
        packCursor = 0;
        playersCards = new int[2][MAX_CARDS_COUNT];
        playersCursors = new int[]{0, 0};
    }

    private static int getCardFromPack() {
        if (packCursor == CardUtils.CARDS_TOTAL_COUNT) {
            log.info("В колоде не осталось карт. Как вы этого добились?");
            return -1;
        }
        int card = pack[packCursor];
        shiftPackCursor();
        return card;
    }

    private static void addCardToPlayerPack(final int playerId, final int card) {
        final String playerName = playerId == PLAYER ? "Вам" : "Компьютеру";
        final int playersCursor = getPlayerCursor(playerId);
        log.info("{} выпала карта {}", playerName, CardUtils.toString(card));
        playersCards[playerId][playersCursor] = card;
        shiftPlayerCursor(playerId);
    }

    private static int getPlayerCard(final int playerId, final int cursor) {
        return playersCards[playerId][cursor];
    }

    private static int getPlayerCardsSum(final int playerId) {
        final int cursor = getPlayerCursor(playerId);
        int sum = 0;
        for (int i = 0; i < cursor; i++) {
            sum += value(getPlayerCard(playerId, i));
        }
        return sum;
    }

    private static int getGameScore(final int playerId) {
        final int sum = getPlayerCardsSum(playerId);
        return sum <= MAX_VALUE ? sum : 0;
    }

    private static void addCardsToPlayer(final int playerId, final int cardsAmount) {
        for (int i = 0; i < cardsAmount; i++) {
            final int card = getCardFromPack();
            addCardToPlayerPack(playerId, card);
        }
    }

    private static int value(int card) {
        switch (CardUtils.getPar(card)) {
            case JACK: return 2;
            case QUEEN: return 3;
            case KING: return 4;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 9;
            case TEN: return 10;
            case ACE:
            default: return 11;
         }
    }

    private static void makeComputerTurn() {
        Random r = new Random();
        final int safeScore = 14 + r.nextInt(5);
        addCardsToPlayer(COMPUTER, 2);
        while (getPlayerCardsSum(COMPUTER) <= safeScore) {
            log.info("Компьютер решил взять ещё одну карту");
            addCardsToPlayer(COMPUTER, 1);
        }
    }

    private static void take10BucksFrom(final int playerId) {
        playersMoney[playerId] -= 10;
    }

    private static void add10BuckTo(final int playerId) {
        playersMoney[playerId] += 10;
    }

    private static int getPlayerMoney(final int playerId) {
        return playersMoney[playerId];
    }


    private static int playRound(final int round, final int maxRounds) throws IOException {
        if (round == maxRounds) {
            return getPlayerMoney(PLAYER);
        }
        if (getPlayerMoney(PLAYER) == 0) {
            log.info("Вы проиграли все деньги. Прощайте.");
            return 0;
        }

        if (getPlayerMoney(COMPUTER) == 0) {
            log.info("Вы обыграли компьютер и теперь Вам не с кем играть. Пока!");
            return getPlayerMoney(PLAYER);
        }

        initRound();

        addCardsToPlayer(PLAYER, 2);

        while (confirm("Берём ещё?")) {
            addCardsToPlayer(PLAYER, 1);
        }

        makeComputerTurn();

        final int playerScore = getGameScore(PLAYER);
        final int computerScore = getGameScore(COMPUTER);

        log.info("Сумма ваших очков - {}, компьютера - {}", playerScore, computerScore);

        switch (Integer.compare(playerScore, computerScore)) {
            case -1:
                log.info("Вы проиграли раунд и теряете 10$");
                take10BucksFrom(PLAYER);
                add10BuckTo(COMPUTER);
                break;
            case 1:
                log.info("Вы выиграли раунд и получаете 10$");
                take10BucksFrom(COMPUTER);
                add10BuckTo(PLAYER);
                break;
            case 0:
            default:
                log.info("Ничья, каждый остаётся при своих");
                break;
        }
        return playRound(round + 1, maxRounds);
    }



    private static int play(final int maxRounds, final int playersCash) throws IOException {
        playersMoney[PLAYER] = playersCash;
        playersMoney[COMPUTER] = 100;
        return playRound(0, maxRounds);
    }

    public static void main(final String... __) throws IOException {
        final int defaultMaxRounds = 100;
        final int defaultPlayersCash = 100;
        play(defaultMaxRounds, defaultPlayersCash);
    }

    @Override
    public int playSingleRound(int cash) {
        final int rounds = 1;
        try {
            return play(rounds, cash);
        } catch (IOException e) {
            return cash;
        }
    }
}
