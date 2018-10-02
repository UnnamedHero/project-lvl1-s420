package games;

import org.apache.commons.math3.util.MathArrays;

import java.util.stream.IntStream;

public class CardUtils {
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

    public static final int PARS_TOTAL_COUNT = Par.values().length;

    public static final int CARDS_TOTAL_COUNT = PARS_TOTAL_COUNT * Suit.values().length;

    public static Suit getSuit(final int cardNumber) {
        return Suit.values()[cardNumber / PARS_TOTAL_COUNT];
    }

    public static Par getPar(final int cardNumber) {
        return Par.values()[cardNumber % PARS_TOTAL_COUNT];
    }

    public static String toString(final int cardNumber) {
        return getPar(cardNumber) + " " + getSuit(cardNumber);
    }

    public static int[] makePack() {
        int[] pack = IntStream.rangeClosed(0, CardUtils.CARDS_TOTAL_COUNT - 1).toArray();
        MathArrays.shuffle(pack);
        return pack;
    }
}
