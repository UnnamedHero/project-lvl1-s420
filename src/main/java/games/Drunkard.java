package games;

import java.util.Arrays;
import java.util.stream.IntStream;
import org.apache.commons.math3.util.MathArrays;

public class Drunkard {

    enum Suit {
        SPADES, // пики
        HEARTS, // червы
        CLUBS, // трефы
        DIAMONDS // бубны
    }

    enum Par {
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK, // Валет
        QUEEN, // Дама
        KING, // Король
        ACE // Туз
    }

    private int playersCount;
    private int rounds;

    private static final int PARS_TOTAL_COUNT = Par.values().length;

    private static final int CARDS_TOTAL_COUNT = PARS_TOTAL_COUNT * Suit.values().length;

    private int[] playersCardsBeginCursor;
    private int[] playersCardsEndCursor;
    private int[] playersCardsCount;

    private int[][] playersPacks;

    /**
     * @param players players count
     * @param maxRounds max rounds to play
     */
    public Drunkard(final int players, final int maxRounds) {
        this.playersCount = players;
        this.rounds = maxRounds;
        this.playersCardsBeginCursor = new int[players];
        this.playersCardsEndCursor = new int[players];
        this.playersCardsCount = new int[players];
        this.playersPacks = new int[players][CARDS_TOTAL_COUNT];
    }

    private static int comparePars(final Par card1, final Par card2) {
        if (card1 == Par.SIX && card2 == Par.ACE) {
            return 1;
        }

        if (card1 == Par.ACE && card2 == Par.SIX) {
            return -1;
        }
        return Integer.compare(card1.ordinal(), card2.ordinal());
    }

    private static Suit getSuit(final int cardNumber) {
        return Suit.values()[cardNumber / PARS_TOTAL_COUNT];
    }

    private static Par getPar(final int cardNumber) {
        return Par.values()[cardNumber % PARS_TOTAL_COUNT];
    }

    private static String toString(final int cardNumber) {
        return getPar(cardNumber) + " " + getSuit(cardNumber);
    }

    private static int[] makePack() {
        int[] pack = IntStream.rangeClosed(0, CARDS_TOTAL_COUNT - 1).toArray();
        MathArrays.shuffle(pack);
        return pack;
    }

    private int getNextPlayerId(final int currentPlayerId) {
        return (currentPlayerId + 1) % playersCount;
    }

    private void increasePlayerCardsCount(final int playerId) {
        playersCardsCount[playerId] = playersCardsCount[playerId] + 1;
    }

    private void degreasePlayerCardsCount(final int playerId) {
        playersCardsCount[playerId] = playersCardsCount[playerId] - 1;
    }

    private void handOutPack() {
        final int[] pack = makePack();
        int currentPlayerId = 0;

        for (int playerId = 0; playerId < playersCount; playerId++) {
            playersCardsBeginCursor[playerId] = 0;
        }

        for (int i = 0; i < CARDS_TOTAL_COUNT; i++) {
            int currentIndex = i / playersCount;
            playersPacks[currentPlayerId][currentIndex] = pack[i];
            increasePlayerCardsCount(currentPlayerId);
            playersCardsEndCursor[currentPlayerId] = currentIndex;
            currentPlayerId = getNextPlayerId(currentPlayerId);
        }
    }

    private int getPlayerCardsCount(final int playerId) {
        return playersCardsCount[playerId];
    }

    private static int compareCards(final int[] cards) {
        int strongestCardIndex = 0;
        int tieCount = 0;
        final int maxTieCount = cards.length - 1;
        for (int i = 1; i < cards.length; i++) {
            final Par strongest = getPar(cards[strongestCardIndex]);
            final Par current = getPar(cards[i]);
            switch (comparePars(current, strongest)) {
                case -1: break;
                case 0: tieCount = tieCount + 1; break;
                case 1: strongestCardIndex = i; break;
                default: break;
            }
        }
        return tieCount == maxTieCount ? -1 : strongestCardIndex;
    }

    private static int getNewCursorPosition(final int currentCursorPosition) {
        return (currentCursorPosition + 1) % CARDS_TOTAL_COUNT;
    }

    private void addCardsToPlayer(final int playerId, final int... cards) {
        int newEndCursorPosition = playersCardsEndCursor[playerId];
        for (int card: cards) {
            newEndCursorPosition = getNewCursorPosition(newEndCursorPosition);
            playersPacks[playerId][newEndCursorPosition] = card;
            increasePlayerCardsCount(playerId);
        }
        playersCardsEndCursor[playerId] = newEndCursorPosition;
    }


    private int[] shiftFirstCards(final int[] playersInGame) {
        final int playersInGameCount = playersInGame.length;
        int[] firstCards = new int[playersInGameCount];
        for (int i = 0; i < playersInGameCount; i++) {
            final int currentPlayerId = playersInGame[i];
            final int currentPlayerFirstCardPos = playersCardsBeginCursor[currentPlayerId];
            firstCards[i] = playersPacks[i][currentPlayerFirstCardPos];
            playersCardsBeginCursor[currentPlayerId] = getNewCursorPosition(currentPlayerFirstCardPos);
            degreasePlayerCardsCount(currentPlayerId);
        }
        return firstCards;
    }

    private static int[] rotatePlayers(final int[] playersInGame) {
        final int playersCount = playersInGame.length;
        int[] shiftedPlayersInGame = new int[playersCount];
        System.arraycopy(playersInGame, 1, shiftedPlayersInGame, 0, playersCount - 1);
        shiftedPlayersInGame[playersCount - 1] = playersInGame[0];
        return shiftedPlayersInGame;
    }

    private void processTieResult(final int[] playersInGame, final int[] cards) {
        for (int i = 0; i < playersInGame.length; i++) {
            addCardsToPlayer(playersInGame[i], cards[i]);
        }
    }

    private int playRound(final int[] playersInGame, final int currentRound) {

        if (currentRound == this.rounds) {
            return 0;
        }

        final int roundPlayersCount = playersInGame.length;
        if (roundPlayersCount == 1) {
            return 0;
        }

        System.out.println(String.format("Раунд %d", currentRound + 1));

        int[] firstCards = shiftFirstCards(playersInGame);
        for (int i = 0; i < roundPlayersCount; i++) {
            System.out.println(String.format("Игрок %d карта %9s", playersInGame[i] + 1, toString(firstCards[i])));
        }

        final int winPlayerIndex = compareCards(firstCards);

        if (winPlayerIndex == -1) {
            System.out.println("Спор - каждый остаётся при своих!");
            processTieResult(playersInGame, firstCards);
        } else {
            System.out.println(String.format("Выиграл игрок %d", playersInGame[winPlayerIndex] + 1));
            addCardsToPlayer(playersInGame[winPlayerIndex], firstCards);
        }

        final int[] sortedPlayers = new int[roundPlayersCount];
        System.arraycopy(playersInGame, 0, sortedPlayers, 0, roundPlayersCount);
        Arrays.sort(sortedPlayers);

        for (int playerId: sortedPlayers) {
            System.out.print(String.format("У игрока №%d %d карт; ", playerId + 1, getPlayerCardsCount(playerId)));
        }

        System.out.println("\n");
        int[] rotatedPlayers = rotatePlayers(playersInGame);
        int[] nextRoundPlayers = Arrays.stream(rotatedPlayers)
                .filter(playerId -> getPlayerCardsCount(playerId) > 0)
                .toArray();

        return playRound(nextRoundPlayers, currentRound + 1);
    }

    /**
     *  call this method to start game.
     */
    public void play() {
        handOutPack();
        int[] playersInGame = IntStream.rangeClosed(0, playersCount - 1).toArray();
        playRound(playersInGame, 0);
    }

    public static void main(final String... __) {
        Drunkard game = new Drunkard(2, 9999);
        game.play();
    }
}
