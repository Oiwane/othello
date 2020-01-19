import state.Black;
import state.TurnState;
import state.White;

import java.io.IOException;

class Game{
  private TurnState state;
  private Board currentBoard;
  private Board[] oldBoard;

  Game() {
    state = Black.getInstance();
    currentBoard = new Board();
    oldBoard = new Board[Board.LOG_SIZE];

    for(int i = 0; i < Board.LOG_SIZE; i++){
      oldBoard[i] = new Board();
    }
  }

  private void changeTurn() {
    state = (state.equals(Black.getInstance())) ? White.getInstance() : Black.getInstance();
  }

  void pvp() throws IOException{
    int putPlace;
    int passCount = 0;

    for(int logIndex = 0; logIndex < Board.LOG_SIZE; logIndex++){
      currentBoard.print();

      if(currentBoard.getNumOfBlack() == 0 || currentBoard.getNumOfWhite() == 0 ||
              passCount >= 2 || currentBoard.getNumOfBlack() + currentBoard.getNumOfWhite() == 64) break;
      currentBoard.showNumOfPiece();

      if (currentBoard.printWhoseTurn(state) == -1) {
        passCount++;
        changeTurn();
        continue;
      }

      passCount = 0;

      if(logIndex > 0) System.out.println("0を入力すると一つ戻ります。");
      System.out.print("コマを置く場所の数字を入力してください : ");
      putPlace = Input.input_int();
      System.out.println();

      if(putPlace == 0){  //一つ戻る

        if(logIndex == 0){
          System.out.println("不正な値が入力されました");
          System.out.println("もう一度入力してください");
          System.out.println();
          logIndex--;
          continue;
        }

        logIndex--;  //1つ前の番を読み込むため

        currentBoard.copy(oldBoard[logIndex]);
        logIndex--;  //forの最後でlogが+1されるため引いておく

      }else if((11 <= putPlace && putPlace <= 18) || (21 <= putPlace && putPlace <= 28) ||
              (31 <= putPlace && putPlace <= 38) || (41 <= putPlace && putPlace <= 48) ||
              (51 <= putPlace && putPlace <= 58) || (61 <= putPlace && putPlace <= 68) ||
              (71 <= putPlace && putPlace <= 78) || (81 <= putPlace && putPlace <= 88)){

        if (currentBoard.putPiece(state, putPlace) == -1) {
          logIndex--;
          continue;
        }

        oldBoard[logIndex + 1].copy(currentBoard);
        changeTurn();

      }else{  //盤内以外が指定された時
        System.out.println("不正な値が入力されました");
        System.out.println("もう一度入力してください");
        logIndex--;
      }
    }

    currentBoard.showResult();
  }

  void vsComputer(TurnState playersTurn) throws IOException{
    int putPlace;
    int passCount = 0;

    for(int logIndex = 0; logIndex < 100; logIndex++){
      currentBoard.print();

      if(currentBoard.getNumOfBlack() == 0 || currentBoard.getNumOfWhite() == 0 ||
              passCount >= 2 || currentBoard.getNumOfBlack() + currentBoard.getNumOfWhite() == 64) break;

      currentBoard.showNumOfPiece();

      if (currentBoard.printWhoseTurn(state) == -1) {
        passCount++;
        changeTurn();
        continue;
      }

      passCount = 0;

      if(state.equals(playersTurn)){
        if(logIndex > 1) System.out.println("0を入力すると一つ戻ります。");
        System.out.print("コマを置く場所の数字を入力してください : ");
        putPlace = Input.input_int();
        System.out.println();

        if(putPlace == 0){

          if(logIndex < 2){
            System.out.println("不正な値が入力されました");
            System.out.println("もう一度入力してください");
            System.out.println();
            logIndex--;
            continue;
          }

          logIndex -= 2;  //1つ前の自分の番に戻る -> logIndexを2つ戻す

          currentBoard.copy(oldBoard[logIndex]);
          logIndex--;

        }else if((11 <= putPlace && putPlace <= 18) || (21 <= putPlace && putPlace <= 28) ||
                (31 <= putPlace && putPlace <= 38) || (41 <= putPlace && putPlace <= 48) ||
                (51 <= putPlace && putPlace <= 58) || (61 <= putPlace && putPlace <= 68) ||
                (71 <= putPlace && putPlace <= 78) || (81 <= putPlace && putPlace <= 88)){

          if (currentBoard.putPiece(state, putPlace) == -1) {
            logIndex--;
            continue;
          }

          oldBoard[logIndex + 1].copy(currentBoard);
          changeTurn();
        }else{
          System.out.println("不正な値が入力されました");
          System.out.println("もう一度入力してください");
          logIndex--;
        }
      }else{  //コンピュータの処理
        putPlace = currentBoard.decidePlace(state);
        currentBoard.putPiece(state, putPlace);
        oldBoard[logIndex + 1].copy(currentBoard);
        changeTurn();
      }

    }

    currentBoard.showResult();
  }

}