package games;

import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Random;

/**
 *  Very simple slot machine.
 */
public class Slot implements ICasinoGame {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Slot.class);

    /**
     * default reel max number (not included).
     */
    private static final int REEL_SIZE = 7;

    /**
     * max number from 0 to ROLL_POWER to add to a obtain new reel value.
     */
    private static final int ROLL_POWER = 100;
    /**
     * jackpot startCash prize.
     */
    private static final int WIN_DELTA = 1000;

    /**
     * default bet size.
     */
    private static final int LOOSE_DELTA = -10;

    /**
     * static win message.
     */
    private static final String WIN_MESSAGE = String
            .format("Выигрыш %d$", WIN_DELTA);

    /**
     * static loose message.
     */
    private static final String LOOSE_MESSAGE = String
            .format("Проигрыш %d$", Math.abs(LOOSE_DELTA));

    /**
     * Change reels' values.
     * @param prevReelsState previous reels values
     * @return new reels values
     */
    private static int[] rollReels(final int[] prevReelsState) {
        Random r = new Random();
        return Arrays
                .stream(prevReelsState)
                .map(reel -> (reel + r.nextInt(ROLL_POWER)) % REEL_SIZE)
                .toArray();
    }

    /**
     * Check if player wins.
     * Win condition - all values are equal.
     * @param reelState a reels' values to check
     * @return true or false
     */
    private static boolean isJackPot(final int[] reelState) {
        return reelState[0] == reelState[1] &&
                reelState[1] == reelState[2];
    }

    /**
     * @param round current round.
     * @param reelState previous reels' values
     * @param cash current player's cash amount
     * @return cash amount after all rounds played or when all cash is gone
     */
    private static int playSlotRound(
            final int round,
            final int maxRounds,
            final int[] reelState,
            final int cash) {
        if ((round >= maxRounds) || cash == 0) {
            return cash;
        }

        log.info("У Вас {}$, ставка 10$", cash);

        log.info("Крутим барабаны! Розыгрыш принёс следующие результаты:");
        final int[] newReelsState = rollReels(reelState);
        log.info("первый барабан - {}, второй - {}, третий - {}",
                        newReelsState[0],
                        newReelsState[1],
                        newReelsState[2]);

        final boolean isPlayerWin = isJackPot(newReelsState);

        final int cashDelta = isPlayerWin ? WIN_DELTA : LOOSE_DELTA;
        final int newCash = cash + cashDelta;

        final String roundMessage = isPlayerWin ? WIN_MESSAGE : LOOSE_MESSAGE;
        log.info("{}, Ваш капитал теперь составляет {}$", roundMessage, newCash);
        return playSlotRound(round + 1, maxRounds, newReelsState, newCash);
    }

    /**
     * @param cash cash size
     * @param maxRounds max game rounds
     * @return cash amount after playing @param maxRounds rounds
     */
    private static int play(int cash, int maxRounds) {
        final int[] initReelsState = {0, 0, 0};
        return playSlotRound(0, maxRounds, initReelsState, cash);
    }

    /**
     * Play a single round game with @param cash
     * @param cash cash amount
     * @return new cash amount
     */
    @Override
    public int playSingleRound(final int cash) {
        final int rounds = 1;
        return play(cash, rounds);
    }

    /**
     * Play a default Slot game with 11 rounds and 100$ cash.
     * @param __ param is not used
     */
    public static void main(final String... __) {
        final int defaultCash = 100;
        final int defaultMaxRounds = 11;
        play(defaultCash, defaultMaxRounds);
    }

}
