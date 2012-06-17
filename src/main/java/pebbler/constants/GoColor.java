package pebbler.constants;

public enum GoColor {
  BLACK,
  WHITE;

  public GoColor opposite() {
    switch(this) {
      case BLACK:   return WHITE;
      case WHITE:   return BLACK;
      default: throw new AssertionError("Unknown color: " + this);
    }
  }
}
