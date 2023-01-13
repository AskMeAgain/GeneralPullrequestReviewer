package io.github.askmeagain.pullrequest;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<I1, I2, I3> {

  void consume(I1 a, I2 b, I3 c);

  default TriConsumer<I1, I2, I3> andThen(TriConsumer<I1, I2, I3> after) {
    Objects.requireNonNull(after);
    return (a, b, c) -> {
      this.consume(a, b, c);
      after.consume(a, b, c);
    };
  }
}
