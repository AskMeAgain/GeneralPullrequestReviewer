package io.github.askmeagain.pullrequest;

@FunctionalInterface
public interface TriConsumer<I1, I2, I3> {

  void consume(I1 a, I2 b, I3 c);
}
