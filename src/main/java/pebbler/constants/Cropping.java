package pebbler.constants;

public enum Cropping {
  // First x.y pair is bottom left, 
  // Second x.y pair is the topRight
  //
  // Spacing between lines is 20. 
  //
  // Off sets are classy, so let's do them.
  //                  br.X  br.Y tl.X t.Y
  TopLeft(new int[]   {0, 190, 230, 400}),
  TopRight(new int[]  {170, 190, 400, 400}),
  BotRight(new int[]  {0, 0, 230, 210}),
  BotLeft(new int[]   {170, 210, 400, 400}),
  Top(new int[]       {0, 190, 400, 400}),
  Bot(new int[]       {0, 0, 400, 210}),
  Left(new int[]      {0, 0, 210, 400}),
  Right(new int[]     {210, 0, 400, 400}),
  Full(new int[]      {0, 0, 400, 400});

  private int[] cropbox;

  private Cropping(int[] cropbox) {
    this.cropbox = cropbox;
  }

  public int[] getCropbox() {
    return cropbox;
  }
}
