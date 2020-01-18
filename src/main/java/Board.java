class Board{
  public int[][] board = new int[10][10];
  public int numOfBlack;
  public int numOfWhite;

  //コンストラクタ
  public Board(){
    for(int i = 0; i < 10; i++){
      for(int j = 0; j < 10; j++){
        if(i == 0 || i == 9 || j == 0 || j == 9){
          board[i][j] = Constance.OUTSIDE;
        }else if((i == 4 && j == 4) || (i == 5 && j == 5)){
          board[i][j] = Constance.BLACK;
        }else if((i == 4 && j == 5) || (i == 5 && j == 4)){
          board[i][j] = Constance.WHITE;
        }else{
          board[i][j] = Constance.VOID;
        }
      }
    }

    numOfBlack = 2;
    numOfWhite = 2;
  }

  /**
   *  盤の表示をする
   */
  public void printBoard(){
    String[] piece = {" ", "*", "o"};

    System.out.println("    1   2   3   4   5   6   7   8  ");
    System.out.println("  +---+---+---+---+---+---+---+---+");
    for(int i = 1; i <= 8; i++){
      System.out.print(i + " ");
      for(int j = 1; j <= 8; j++){
        System.out.print("| " + piece[this.board[i][j]] + " ");
      }
      System.out.println("|");
      System.out.println("  +---+---+---+---+---+---+---+---+");
    }
    System.out.println();
  }

  /**
   * 置けるかどうかを確認する
   *
   * @param player プレイヤー情報
   * @param r コマを設置する場所の行
   * @param c コマを設置する場所の列
   * @param dr 東西
   * @param dc 南北
   * @return dr、dcの方角でひっくり返るコマの枚数
   */
  public int judgeTurnOver(Player player, int r, int c, int dr, int dc){
    int count = 0;
    int row = r + dr;
    int column = c + dc;
    int opponent = (player.color == Constance.BLACK) ? Constance.WHITE : Constance.BLACK;

    while(board[row][column] != player.color){
      if(board[row][column] != opponent){
        count = 0;
        break;
      }
      row += dr;
      column += dc;
      count++;
    }

    return count;
  }

  /**
   * コマの置ける場所を確認する
   *
   * @param player プレイヤー情報
   * @param r コマを設置する場所の行
   * @param c コマを設置する場所の列
   * @return ひっくり返るコマの数
   */
  public int checkTurnOver(Player player, int r, int c){
    int count = 0;

    if(board[r][c] != Constance.VOID){
      return 0;
    }

    for(int i = -1; i <= 1; i++){
      for(int j = -1; j <= 1; j++){
        count += this.judgeTurnOver(player, r, c, i, j);
      }
    }

    return count;
  }

  /**
   * 盤の情報を移行する
   *
   * @param overwrittenBoard
   * @param overwriteBoard
   */
  public static void shiftBoardInfo(Board overwrittenBoard, Board overwriteBoard){
    for(int i = 1; i <= 8; i++){
      for(int j = 1; j <= 8; j++){
        overwrittenBoard.board[i][j] = overwriteBoard.board[i][j];
      }
    }

    overwrittenBoard.numOfBlack = overwriteBoard.numOfBlack;
    overwrittenBoard.numOfWhite = overwriteBoard.numOfWhite;
  }

  /* どのプレイヤーの番かの判定 */
  public int judgePlayer(int log, Player player1, Player player2){

    if(log%2 == 1){  //白の番

      if(this.isPass(player2)){
        System.out.println("コマを置けません");
        System.out.println("白の番を飛ばします");
        return -1;
      }
      System.out.println("白の番です");
      System.out.println();

    }else{  //黒の番

      if(this.isPass(player1)){
        System.out.println("コマを置けません");
        System.out.println("黒の番を飛ばします");
        return -1;
      }
      System.out.println("黒の番です");
      System.out.println();

    }

    return 0;

  }

  /* プレイヤーの番かコンピューターか判定 */
  public int judgePlayer(int log, Player player, Computer computer){
    Player tmp = new Player(computer.color);

    if(computer.isFirst){  //コンピューターが先手だった場合
      return this.judgePlayer(log, tmp, player);
    }else{
      return this.judgePlayer(log, player, tmp);
    }

  }

  /* パスかどうかの判定 */
  public boolean isPass(Player player){
    int status = 0;

    for(int i = 1; i <= 8; i++){
      for(int j = 1; j <= 8; j++){
        status = this.checkTurnOver(player, i, j);
        if(Constance.ToBool(status)){
          return false;
        }
      }
    }

    return true;
  }

}