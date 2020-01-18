package state;

public class Black implements TurnState {
  private static Black singleton = new Black();

  public static TurnState getInstance() {
    return singleton;
  }

  @Override
  public int startTurn(boolean isPass) {
    if (isPass) {
      System.out.println("コマを置けません");
      System.out.println("黒の番を飛ばします");
      return -1;
    }
    System.out.println("黒の番です");
    System.out.println();
    return 0;
  }
}
