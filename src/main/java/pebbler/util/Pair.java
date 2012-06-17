package pebbler.util;

public class Pair<T, U> {

  private T _1;
  private U _2;

  public Pair(T _1, U _2) {
    this._1 = _1;
    this._2 = _2;
  }

  public T first() {
    return _1;
  }

  public U second() {
    return _2;
  }

  public static <T, U> Pair<T, U> of(T _1, U _2) {
    return new Pair<T, U>(_1, _2);
  }
}
