package learn;

import java.util.Objects;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I in);

    default <V> Processor<I, V> andThen(Processor<O, V> after) {
        Objects.requireNonNull(after);
        return (I in) -> after.process(process(in));
    }
}