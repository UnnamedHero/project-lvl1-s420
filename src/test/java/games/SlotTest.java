package games;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static common.TestUtils.fromSystemOutPrintln;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SlotTest {
//    @Test
    @DisplayName("\"main\" method works correctly")
    void testMain() {
        assertThat(fromSystemOutPrintln(games.Slot::main))
                .isEqualTo("Hello, World!");
    }
}
