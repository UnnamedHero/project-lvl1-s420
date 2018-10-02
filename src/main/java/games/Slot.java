package games;

import java.util.Arrays;
import java.util.Random;

/**
 *  Very simple slot machine.
 */
public class Slot implements ICasinoGame {

    /**
     * default reel max number (not included).
     */
    private final int reelSize = 7;

    /**
     * max number from 0 to rollPower to add to a obtain new reel value.
     */
    private final int rollPower = 100;
    /**
     * jackpot startCash prize.
     */
    private final int winDelta = 1000;

    /**
     * default bet size.
     */
    private final int looseDelta = -10;

    /**
     * static win message.
     */
    private final String winMessage = String
            .format("Выигрыш %d$", this.winDelta);

    /**
     * static loose message.
     */
    private final String looseMessage = String
            .format("Проигрыш %d$", Math.abs(this.looseDelta));

    /**
     * game player's startCash.
     */
    private int startCash;

    /**
     * game's max rounds.
     */
    private int maxRounds;

    /**
     * @param initCash start player's startCash
     * @param rounds game's max rounds
     */
    public Slot(final int initCash, final int rounds) {
        this.startCash = initCash;
        this.maxRounds = rounds;
    }

    public Slot(final int rounds) {
        this.maxRounds = rounds;
        this.startCash = 0;
    }

    /**
     * Change reels' values.
     * @param prevReelsState previous reels values
     * @return new reels values
     */
    private int[] rollReels(final int[] prevReelsState) {
        Random r = new Random();
        return Arrays
                .stream(prevReelsState)
                .map(reel -> (reel + r.nextInt(this.rollPower)) % this.reelSize)
                .toArray();
    }

    /**
     * Check if player wins.
     * Win condition - all values are equal.
     * @param reelState a reels' values to check
     * @return true or false
     */
    private boolean isJackPot(final int[] reelState) {
        return reelState[0] == reelState[1] &&
                reelState[1] == reelState[2];
    }

    /**
     * @param round current round.
     * @param reelState previous reels' values
     * @param cash current player's cash amount
     * @return cash amount after all rounds played or when all cash is gone
     */
    private int playSlotRound(
            final int round,
            final int[] reelState,
            final int cash) {
        if ((round >= this.maxRounds) || cash == 0) {
            return cash;
        }

        System.out.println(String.format("У Вас %d$, ставка 10$", cash));

        System.out.println("Крутим барабаны! Розыгрыш принёс следующие результаты:");
        final int[] newReelsState = rollReels(reelState);
        System.out.println(String
                .format("первый барабан - %d, второй - %d, третий - %d",
                        newReelsState[0],
                        newReelsState[1],
                        newReelsState[2]));

        final boolean isPlayerWin = isJackPot(newReelsState);

        final int cashDelta = isPlayerWin ? winDelta : looseDelta;
        final int newCash = cash + cashDelta;

        final String roundMessage = isPlayerWin ? this.winMessage : this.looseMessage;
        System.out.println(String.format("%s, Ваш капитал теперь составляет %d$", roundMessage, newCash));
        return playSlotRound(round + 1, newReelsState, newCash);
    }

    /**
     *  Start a game calling this method.
     */
    public int play() {
        final int[] initReelsState = {0, 0, 0};
        return playSlotRound(0, initReelsState, this.startCash);
    }

    @Override
    public int playSingleRound(final int cash) {
        this.startCash = cash;
        this.maxRounds = 1;
        return play();
    }

    /**
     * Play a default Slot game with 11 rounds and 100$ cash.
     * @param __ param is not used
     */
    public static void main(final String... __) {
        final int cash = 100;
        final int maxRounds = 11;
        Slot slotGame = new Slot(cash, maxRounds);
        slotGame.play();
    }

}
