import java.io.*;

class Game{
  public static void main(String[] args) throws IOException{
    int is_player;

    while(true){
      System.out.println("0 : 1人で : プレイヤー先手");
      System.out.println("1 : 1人で : コンピューター先手");
      System.out.println("2 : 2人で");
      System.out.print("上記の数字を入力してください : ");
      is_player = Input.input_int();

      if(is_player == 0 || is_player == 1){
        vsComputer(is_player+1);
        return;
      }else if(is_player == 2){
        pvp();
        return;
      }else{
        System.out.println("指定された数字を入力してください");
      }
    }
  }

  public static void pvp() throws IOException{
    int log;
    int putPlace;
    Board currentBoard = new Board();
    Board[] oldBoard = new Board[60];
    Player player1 = new Player(Constance.BLACK);
    Player player2 = new Player(Constance.WHITE);

    //oldBoardの初期化
    for(int i= 0; i < 60; i++){
      oldBoard[i] = new Board();
    }

    for(log = 0; log < 60; log++){

      currentBoard.printBoard();  //盤の表示
      //どちらかのコマが0になったらゲームを終了させる
      if(currentBoard.numOfBlack == 0 || currentBoard.numOfWhite == 0){
        break;
      }
      showNumOfPiece(currentBoard);  //コマの数を表示

      //どちらの番なのかを判定
      //コマを置く場所がない場合、パスする
      if(currentBoard.judgePlayer(log, player1, player2) == -1){
        continue;
      }

      if(log > 0){
        System.out.println("0を入力すると一つ戻ります。");
      }
      System.out.print("コマを置く場所の数字を入力してください : ");
      putPlace = Input.input_int();
      System.out.println();

      if(putPlace == 0){  //一つ戻る

        if(log == 0){
          System.out.println("それ以上戻れません");
          System.out.println();
          log--;
          continue;
        }

        log--;  //1つ前の番を読み込むため

        //current_boardにold_boardの情報を移行
        Board.shiftBoardInfo(currentBoard, oldBoard[log]);
        log--;  //forの最後でlogが+1されるため引いておく

      }else if((11 <= putPlace && putPlace <= 18) || (21 <= putPlace && putPlace <= 28) ||
              (31 <= putPlace && putPlace <= 38) || (41 <= putPlace && putPlace <= 48) ||
              (51 <= putPlace && putPlace <= 58) || (61 <= putPlace && putPlace <= 68) ||
              (71 <= putPlace && putPlace <= 78) || (81 <= putPlace && putPlace <= 88)){

        //コマを置く
        if(log%2 + 1 == Constance.BLACK){  //黒の番の場合

          if(player1.putPiece(currentBoard, putPlace) == -1){
            log--;
            continue;
          }

        }else{  //白の番の場合

          if(player2.putPiece(currentBoard, putPlace) == -1){
            log--;
            continue;
          }

        }

        //old_boardにcurrent_boardの情報を移行
        Board.shiftBoardInfo(oldBoard[log + 1], currentBoard);

      }else{  //盤内以外が指定された時
        System.out.println("入力された数字が違います");
        log--;
      }

    }

    //結果の表示
    showResult(currentBoard);

    return;
  }


  public static void vsComputer(int playerColor) throws IOException{
    int log = 0;
    int put_place = 0;
    int pass_count = 0;
    Board currentBoard = new Board();
    //パスも1手として数えているため、64より多めに確保
    //パスを1手と数えた時の最大手数が分からないため大雑把に100確保
    Board[] oldBoard = new Board[100];
    Player player = new Player(playerColor);
    Computer computer = new Computer(playerColor);

    for(int i= 0; i < 100; i++){
      oldBoard[i] = new Board();
    }

    //下のコメントはデバッグ用
    //for(log = currentBoard.numOfBlack + currentBoard.numOfWhite - 4; log < 100; log++){
    for(log = 0; log < 100; log++){
      currentBoard.printBoard();  //盤の表示
      //どちらかのコマが0になる、またはパスが続く(どちらも置ける場所がなくなる)、または盤がコマで埋まったら
      if(currentBoard.numOfBlack == 0 || currentBoard.numOfWhite == 0 ||
              pass_count == 2 || currentBoard.numOfBlack + currentBoard.numOfWhite == 64){
        break;
      }
      showNumOfPiece(currentBoard);  //コマの数を表示

      //どちらの番なのかを判定
      //コマを置く場所がない場合、パスする
      if(currentBoard.judgePlayer(log, player, computer) == -1){
        pass_count++;
        continue;
      }

      pass_count = 0;

      if(log%2 + 1 == player.color){  //プレイヤーの処理
        if(log > 1){
          System.out.println("0を入力すると一つ戻ります。");
        }
        System.out.print("コマを置く場所の数字を入力してください : ");
        put_place = Input.input_int();
        System.out.println();

        if(put_place == 0){

          if(log < 2){
            System.out.println("入力された数字が違います");
            System.out.println();
            log--;
            continue;
          }

          log -= 2;  //1つ前の自分の番に戻る -> logを2つ戻す

          //current_boardにold_boardの情報を移行
          Board.shiftBoardInfo(currentBoard, oldBoard[log]);
          log--;

        }else if((11 <= put_place && put_place <= 18) || (21 <= put_place && put_place <= 28) ||
                (31 <= put_place && put_place <= 38) || (41 <= put_place && put_place <= 48) ||
                (51 <= put_place && put_place <= 58) || (61 <= put_place && put_place <= 68) ||
                (71 <= put_place && put_place <= 78) || (81 <= put_place && put_place <= 88)){

          if(player.putPiece(currentBoard, put_place) == -1){
            log--;
            continue;
          }

          //old_boardにcurrent_boardの情報を移行
          Board.shiftBoardInfo(oldBoard[log + 1], currentBoard);

        }else{
          System.out.println("入力された数字が違います");
          log--;
        }
      }else{  //コンピュータの処理
        put_place = computer.decidePlace(currentBoard);
        computer.putPiece(currentBoard, put_place);
        //old_boardにcurrent_boardの情報を移行
        Board.shiftBoardInfo(oldBoard[log + 1], currentBoard);

      }

    }

    //結果の表示
    showResult(currentBoard);
  }

  public static void showNumOfPiece(Board board){
    System.out.println("黒 : " + board.numOfBlack);
    System.out.println("白 : " + board.numOfWhite);
    System.out.println();
  }

  public static void showResult(Board board){
    showNumOfPiece(board);

    if(board.numOfBlack > board.numOfWhite){
      System.out.println("黒の勝ち");
    }else if(board.numOfBlack < board.numOfWhite){
      System.out.println("白の勝ち");
    }else{
      System.out.println("引き分け");
    }
  }

}