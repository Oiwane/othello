import state.TurnState;

import java.io.IOException;

class Game{
  private BoardManager boardManager;

  Game() {
    boardManager = new BoardManager();
  }

  void pvp() throws IOException{
    int putPlace;
    int passCount = 0;
    Board currentBoard = boardManager.getCurrentBoard();

    for(int logIndex = 0; logIndex < Board.LOG_SIZE; logIndex++){
      currentBoard.print();

      if(currentBoard.getNumOfBlack() == 0 || currentBoard.getNumOfWhite() == 0 ||
              passCount >= 2 || currentBoard.getNumOfBlack() + currentBoard.getNumOfWhite() == 64) break;
      currentBoard.showNumOfPiece();

      if (currentBoard.printWhoseTurn(boardManager.getState()) == -1) {
        passCount++;
        boardManager.changeTurn();
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

        currentBoard.copy(boardManager.getOldBoardList().get(logIndex));
        logIndex--;  //forの最後でlogが+1されるため引いておく

      }else if((11 <= putPlace && putPlace <= 18) || (21 <= putPlace && putPlace <= 28) ||
              (31 <= putPlace && putPlace <= 38) || (41 <= putPlace && putPlace <= 48) ||
              (51 <= putPlace && putPlace <= 58) || (61 <= putPlace && putPlace <= 68) ||
              (71 <= putPlace && putPlace <= 78) || (81 <= putPlace && putPlace <= 88)){

        if (currentBoard.putPiece(boardManager.getState(), putPlace) == -1) {
          logIndex--;
          continue;
        }

        boardManager.getOldBoardList().get(logIndex + 1).copy(currentBoard);
        boardManager.changeTurn();

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
    Board currentBoard = boardManager.getCurrentBoard();

    for(int logIndex = 0; logIndex < Board.LOG_SIZE; logIndex++){
      currentBoard.print();

      if(currentBoard.getNumOfBlack() == 0 || currentBoard.getNumOfWhite() == 0 ||
              passCount >= 2 || currentBoard.getNumOfBlack() + currentBoard.getNumOfWhite() == 64) break;

      currentBoard.showNumOfPiece();

      if (currentBoard.printWhoseTurn(boardManager.getState()) == -1) {
        passCount++;
        boardManager.changeTurn();
        continue;
      }

      passCount = 0;

      if(boardManager.getState().equals(playersTurn)){
        if(logIndex > 1) System.out.println("0を入力すると一つ戻ります。");
        System.out.print("コマを置く場所の数字を入力してください : ");
        putPlace = Input.input_int();
        System.out.println();

        if(putPlace == 0){

          if(logIndex == 0){
            System.out.println("不正な値が入力されました");
            System.out.println("もう一度入力してください");
            System.out.println();
            logIndex--;
            continue;
          }

          logIndex--;  //1つ前の番を読み込むため

          currentBoard.copy(boardManager.getOldBoardList().get(logIndex));
          logIndex--;

        }else if((11 <= putPlace && putPlace <= 18) || (21 <= putPlace && putPlace <= 28) ||
                (31 <= putPlace && putPlace <= 38) || (41 <= putPlace && putPlace <= 48) ||
                (51 <= putPlace && putPlace <= 58) || (61 <= putPlace && putPlace <= 68) ||
                (71 <= putPlace && putPlace <= 78) || (81 <= putPlace && putPlace <= 88)){

          if (currentBoard.putPiece(boardManager.getState(), putPlace) == -1) {
            logIndex--;
            continue;
          }

          boardManager.getOldBoardList().get(logIndex + 1).copy(currentBoard);
          boardManager.changeTurn();
        }else{
          System.out.println("不正な値が入力されました");
          System.out.println("もう一度入力してください");
          logIndex--;
        }
      }else{  //コンピュータの処理
        putPlace = currentBoard.decidePlace(boardManager.getState());
        currentBoard.putPiece(boardManager.getState(), putPlace);
//        boardManager.getOldBoardList().get(logIndex + 1).copy(currentBoard);
        boardManager.changeTurn();
      }

    }

    currentBoard.showResult();
  }

}