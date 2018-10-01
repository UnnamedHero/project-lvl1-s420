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

//    enum Players {
//        PLAYER_1,
//        PLAYER_2,
//    }

    private int playersCount;
    private int rounds;

    private static final int PARS_TOTAL_COUNT = Par.values().length;

    private static final int CARDS_TOTAL_COUNT = PARS_TOTAL_COUNT * Suit.values().length;

    private static int[] playersCardsBeginCursor;
    private static int[] playersCardsEndCursor;

    private static int[][] playersPacks;

    public Drunkard(int players, int maxRounds) {
        this.playersCount = players;
        this.rounds = maxRounds;
        this.playersCardsBeginCursor = new int[players];
        this.playersCardsEndCursor = new int[players];
        this.playersPacks = new int[players][CARDS_TOTAL_COUNT];
    }

    private static int comparePars(Par card1, Par card2) {
        if (card1 == Par.SIX && card2 == Par.ACE) {
            return 1;
        }

        if (card1 == Par.ACE && card2 == Par.SIX) {
            return -1;
        }
        System.out.printf("\n%d vs %d is %d \n", card1.ordinal(), card2.ordinal(), Integer.compare(card1.ordinal(), card2.ordinal()));
        return Integer.compare(card1.ordinal(), card2.ordinal());
    }

    private static Suit getSuit(final int cardNumber) {
        return Suit.values()[cardNumber / PARS_TOTAL_COUNT];
    }

    private static Par getPar(int cardNumber) {
        return Par.values()[cardNumber % PARS_TOTAL_COUNT];
    }

    private static String toString(int cardNumber) {
        return getPar(cardNumber) + " " + getSuit(cardNumber);
    }

    private static int[] makePack() {
        int[] pack = IntStream.rangeClosed(0, CARDS_TOTAL_COUNT - 1).toArray();
        MathArrays.shuffle(pack);
        return pack;
    }

    private int getNextPlayerId(int currentPlayerId) {
        return (currentPlayerId + 1) % playersCount;
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
            playersCardsEndCursor[currentPlayerId] = currentIndex;
            currentPlayerId = getNextPlayerId(currentPlayerId);
        }
    }

    private int getPlayerCardsCount(int playerId) {
        final int playerBeginCursor = playersCardsBeginCursor[playerId];
        final int playerEndCursor = playersCardsEndCursor[playerId];
        if (playerEndCursor <= playerBeginCursor) {
            return playerEndCursor - playerBeginCursor + 1;
        }
        return playerEndCursor + CARDS_TOTAL_COUNT - playerBeginCursor + 1;
    }

    private static int compareCards(int[] cards) {
        int strongestCardIndex = 0;
        boolean isTie = false;
        for(int i = 1; i < cards.length; i++) {
            final Par strongest = getPar(cards[strongestCardIndex]);
            final Par current = getPar(cards[i]);
            System.out.printf("%s vs %s", toString(cards[strongestCardIndex]), toString(cards[i]));
            switch (comparePars(current, strongest)) {
                case -1: isTie = false; break;
                case 0: isTie = true; break;
                case 1: strongestCardIndex = i; isTie = false; break;
            }
        }
        System.out.printf(" strongest: %s\n", toString(cards[strongestCardIndex]));
        return isTie ? -1 : strongestCardIndex;
    }

    private static int getNewCursorPosition(int currentCursorPosition) {
        return (currentCursorPosition + 1) % CARDS_TOTAL_COUNT;
    }

    private void addCardsToPlayer(int playerId, int... cards) {
        int newEndCursorPosition = playersCardsEndCursor[playerId];
        for (int card: cards) {
            newEndCursorPosition = getNewCursorPosition(newEndCursorPosition);
            playersPacks[playerId][newEndCursorPosition] = card;
        }
        playersCardsEndCursor[playerId] = newEndCursorPosition;
    }


    private int[] shiftFirstCards(int[] playersInGame) {
        final int playersInGameCount = playersInGame.length;
        int[] firstCards = new int[playersInGameCount];
        for (int i = 0; i < playersInGameCount; i++) {
            final int currentPlayerId = playersInGame[i];
            final int currentPlayerFirstCardPos = playersCardsBeginCursor[currentPlayerId];
            firstCards[i] = playersPacks[i][currentPlayerFirstCardPos];
            playersCardsBeginCursor[currentPlayerId] = getNewCursorPosition(currentPlayerFirstCardPos);
        }
        return firstCards;
    }

    private static int[] rotatePlayers(int[] playersInGame) {
        final int playersCount = playersInGame.length;
        int[] shiftedPlayersInGame = new int[playersCount];
        System.arraycopy(playersInGame, 1, shiftedPlayersInGame, 0, playersCount - 1);
        shiftedPlayersInGame[playersCount - 1] = playersInGame[0];
        return shiftedPlayersInGame;
    }

    private void processTieResult(int[] playersInGame, int[] cards) {
        for (int i = 0; i < playersInGame.length; i++) {
            addCardsToPlayer(playersInGame[i], cards[i]);
        }
    }

    private void playRound(int[] playersInGame, int currentRound) {

        if (currentRound == this.rounds) {
            return;
        }

        final int playersCount = playersInGame.length;
        if (playersCount == 1) {
            return;
        }

        System.out.println(String.format("Раунд %d", currentRound + 1));

        int[] firstCards = shiftFirstCards(playersInGame);
        for (int i = 0; i < playersCount; i ++) {
            System.out.println(String.format("Игрок %d карта %9s", playersInGame[i] + 1, toString(firstCards[i])));
        }

        final int strongestIndex = compareCards(firstCards);

        if (strongestIndex == -1) {
            System.out.println("Спор - каждый остаётся при своих!");
            processTieResult(playersInGame, firstCards);
        } else {
            System.out.println(String.format("Выиграл игрок %d", playersInGame[strongestIndex] + 1));
            addCardsToPlayer(playersInGame[strongestIndex], firstCards);
        }

        final int[] sortedPlayers = new int[playersCount];
        System.arraycopy(playersInGame, 0, sortedPlayers, 0, playersCount);
        Arrays.sort(sortedPlayers);

        for (int playerId: sortedPlayers) {
            System.out.print(String.format("У игрока №%d %d карт; ", playerId + 1, getPlayerCardsCount(playerId)));
        }

        System.out.println("\n");
        int[] rotatedPlayers = rotatePlayers(playersInGame);
        int[] nextRoundPlayers = Arrays.stream(rotatedPlayers)
                .filter(playerId -> getPlayerCardsCount(playerId) > 0)
                .toArray();

        playRound(nextRoundPlayers, currentRound + 1);
    }

    public void play() {
        handOutPack();
        System.out.printf("cards count %d %d \n", getPlayerCardsCount(0), getPlayerCardsCount(1));
        int[] playersInGame = IntStream.rangeClosed(0, playersCount - 1).toArray();
        playRound(playersInGame, 0);
    }

    public static void main(final String... __) {
        Drunkard game = new Drunkard(3, 1000);
        game.play();
    }
}
