package state;

public class White implements TurnState {
  private static TurnState singleton = new White();

  public static TurnState getInstance() {
    return singleton;
  }

  @Override
  public int startTurn(boolean isPass) {
    if(isPass){
      System.out.println("コマを置けません");
      System.out.println("白の番を飛ばします");
      return -1;
    }
    System.out.println("白の番です");
    System.out.println();
    return 0;
  }
}
