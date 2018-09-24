package common;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Consumer;

public interface TestUtils {

    @NotNull // Метод никогда не вернёт null
    static String fromPrintStream(@NotNull Consumer<PrintStream> printStreamConsumer) { // Передавать в метод null в качестве значения аргумента нельзя - за этим проследит CheckerFramework при компиляции
        val out = new ByteArrayOutputStream();
        @Cleanup val printStream = new PrintStream(out);
        printStreamConsumer.accept(printStream);
        return new String(out.toByteArray()).intern();
    }

    /**
     * Метод выполняет переданный ему код и возвращает строку, которая была записана в исходящий поток вывода
     * {@code System.out} во время его выполнения.
     * @param task
     * @return
     */
    @NotNull
    @SneakyThrows
    @Contract("_ -> new")
    static String fromSystemOutPrint(@NotNull Runnable task) {
        return fromPrintStream(printStream -> {
            PrintStream realOut = System.out;
            System.setOut(printStream);
            task.run();
            System.setOut(realOut);
        });
    }

    String LINE_SEPARATOR = System.getProperty("line.separator");

    static String fromSystemOutPrintln(Runnable runnable) {
        String s = fromSystemOutPrint(runnable);
        return s.endsWith(LINE_SEPARATOR) ?
                s.substring(0, s.length() - LINE_SEPARATOR.length()) :
                s;
    }

    String TEST_RESOURCES_PATH = "./src/test/resources/";

    @NotNull
    @Contract(pure = true)
    static String toTestResourceFilePath(String fileName) {
        return TEST_RESOURCES_PATH + fileName;
    }
}
