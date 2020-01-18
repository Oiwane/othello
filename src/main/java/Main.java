import state.Black;
import state.White;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    int is_player;
    Game game = new Game();

    while(true){
      System.out.println("0 : 1人で : プレイヤー先手");
      System.out.println("1 : 1人で : コンピューター先手");
      System.out.println("2 : 2人で");
      System.out.print("上記の数字を入力してください : ");
      is_player = Input.input_int();

      if (is_player == 0) {
        game.vsComputer(Black.getInstance());
        return;
      } else if (is_player == 1) {
        game.vsComputer(White.getInstance());
        return;
      } else if (is_player == 2) {
        game.pvp();
        return;
      } else {
        System.out.println("指定された数字を入力してください");
      }
    }
  }
}
