package games;

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

    private static final int PARS_TOTAL_COUNT = Par.values().length;

    private static final int CARDS_TOTAL_COUNT = PARS_TOTAL_COUNT * Suit.values().length;

    private static Suit getSuit(final int cardNumber) {
        return Suit.values()[cardNumber / PARS_TOTAL_COUNT];
    }

    private static Par getPar(int cardNumber) {
        return Par.values()[cardNumber % PARS_TOTAL_COUNT];
    }

    private static String toString(int cardNumber) {
        return getPar(cardNumber) + " " + getSuit(cardNumber);
    }

    public static void main(final String... __) {
        final int[] pack = IntStream.rangeClosed(0, CARDS_TOTAL_COUNT - 1).toArray();
        MathArrays.shuffle(pack);
        for (int c: pack) {
            System.out.println(toString(c));
        }
    }
}
