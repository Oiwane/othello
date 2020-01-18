import java.util.Random;

class Player{
  int color;

  public Player(){}
  public Player(int playerColor){
    this.color = playerColor;
  }

  /**
   * ひっくり返す
   *
   * @param board ボード
   * @param putPlace コマを置く場所
   * @return ひっくり返るコマの枚数
   */
  public int turnOver(Board board, int putPlace){
    int total = 0;
    int count = 0;
    int putRow = putPlace / 10;
    int putColumn = putPlace % 10;
    int checkRow, checkColumn;

    for(int i = -1; i <= 1; i++){
      for(int j = -1; j <= 1; j++){
        if(i == 0 && j == 0) continue;
        checkRow = putRow + i;
        checkColumn = putColumn + j;
        count = board.judgeTurnOver(this, putRow, putColumn, i, j);
        for(int k = 0; k < count; k++){
          board.board[checkRow][checkColumn] = this.color;
          checkRow += i;
          checkColumn += j;
        }
        total += count;
      }
    }
    board.board[putRow][putColumn] = this.color;

    return total;
  }

  /**
   * コマを置く
   *
   * @param board 現在のボード
   * @param putPlace 置く場所
   * @return ひっくり返るコマの枚数
   */
  public int putPiece(Board board, int putPlace){
    int row = putPlace / 10;
    int column = putPlace % 10;
    int status;
    int change_piece;

    //コマが置けるかの判定
    status = board.checkTurnOver(this, row, column);
    if(status == 0){
      System.out.println(putPlace + "には置けません");
      return -1;
    }
    //コマを置いてひっくり返す
    change_piece = this.turnOver(board, putPlace);

    if(this.color == Constance.BLACK){
      board.numOfBlack += change_piece + 1;  //+1しているのは置いたコマの分
      board.numOfWhite -= change_piece;
    }else{
      board.numOfBlack -= change_piece;
      board.numOfWhite += change_piece + 1;
    }

    return 0;
  }

}

class Computer extends Player{
  boolean isFirst;

  public Computer(int playerColor){
    if(playerColor == Constance.BLACK){
      color = Constance.WHITE;
      isFirst = false;
    }else if(playerColor == Constance.WHITE){
      color = Constance.BLACK;
      isFirst = true;
    }
  }

  public int decidePlace(Board currentBoard){
    int putPlace = 0;
    int[] putableList = new int[20];
    int count = 0;
    Random rand = new Random();

    for(int i = 1; i <= 8; i++){
      for(int j = 1; j <= 8; j++){
        if(currentBoard.checkTurnOver(this, i, j) != 0){
          putableList[count] = i * 10 + j;
          count++;
        }
      }
    }

    putPlace = putableList[rand.nextInt(count)];

    return putPlace;
  }
}