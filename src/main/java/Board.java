import state.Black;
import state.TurnState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Board{
  private static final int OUTSIDE = -1;
  private static final int EMPTY = 0;
  private static final int BLACK = 1;
  private static final int WHITE = 2;
  static final int LOG_SIZE = 100;

  private int[][] board = new int[10][10];
  private int numOfBlack;
  private int numOfWhite;

  //コンストラクタ
  Board(){
    initPieces();
    numOfBlack = 2;
    numOfWhite = 2;
  }

  private void initPieces() {
    for(int i = 0; i < 10; i++){
      for(int j = 0; j < 10; j++){
        board[i][j] = initPiece(i, j);
      }
    }
  }

  private int initPiece(int row, int column) {
    if(row == 0 || row == 9 || column == 0 || column == 9) return OUTSIDE;
    else if((row == 4 && column == 4) || (row == 5 && column == 5)) return BLACK;
    else if((row == 4 && column == 5) || (row == 5 && column == 4)) return WHITE;
    else return EMPTY;
  }

  int getNumOfBlack() {
    return numOfBlack;
  }

  int getNumOfWhite() {
    return numOfWhite;
  }

  /**
   *  盤の表示をする
   */
  void print(){
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
   * コマの数を出力する
   */
  void showNumOfPiece(){
    System.out.println("黒 : " + numOfBlack);
    System.out.println("白 : " + numOfWhite);
    System.out.println();
  }

  /**
   * リザルトを出力する
   */
  void showResult(){
    this.showNumOfPiece();

    if(numOfBlack > numOfWhite) System.out.println("黒の勝ち");
    else if(numOfBlack < numOfWhite) System.out.println("白の勝ち");
    else System.out.println("引き分け");
  }

  /**
   * ひっくり返るコマの数を反映させる
   * @param state どちらの番か
   * @param numChangedPiece ひっくり返るコマの数
   */
  private void reflectNumChangedPiece(TurnState state, int numChangedPiece) {
    //+1しているのは置いたコマの分
    if (state.equals(Black.getInstance())) {
      numOfBlack += numChangedPiece + 1;
      numOfWhite -= numChangedPiece;
    } else {
      numOfBlack -= numChangedPiece;
      numOfWhite += numChangedPiece + 1;
    }
  }

  /**
   * コンピュータがコマを置く場所を決める
   * @param state どちらの番か
   * @return ひっくり返るコマの数
   */
  int decidePlace(TurnState state){
    List<Integer> putableList = new ArrayList<>();
    Random rand = new Random();

    for(int i = 1; i <= 8; i++){
      for(int j = 1; j <= 8; j++){
        if(checkTurnOver(state, i, j) != 0){
          putableList.add(i * 10 + j);
        }
      }
    }

    return putableList.get(rand.nextInt(putableList.size()));
  }

  /**
   * 置けるかどうかを確認する
   *
   * @param state どちらの番か
   * @param r コマを設置する場所の行
   * @param c コマを設置する場所の列
   * @param dr 東西
   * @param dc 南北
   * @return dr、dcの方角でひっくり返るコマの枚数
   */
  private int judgeTurnOver(TurnState state, int r, int c, int dr, int dc) {
    int count = 0;
    int row = r + dr;
    int column = c + dc;
    int player = (state.equals(Black.getInstance())) ? BLACK : WHITE;
    int opponent = (state.equals(Black.getInstance())) ? WHITE : BLACK;

    while (board[row][column] != player) {
      if (board[row][column] != opponent) {
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
   * @param state どちらの番か
   * @param r コマを設置する場所の行
   * @param c コマを設置する場所の列
   * @return ひっくり返るコマの数
   */
  private int checkTurnOver(TurnState state, int r, int c) {
    int count = 0;

    if (board[r][c] != EMPTY) return 0;

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        count += judgeTurnOver(state, r, c, i, j);
      }
    }

    return count;
  }

  /**
   * 盤の情報を移行する
   *
   * @param originalBoard コピー元のボード
   */
  void copy(Board originalBoard){
    for(int i = 1; i <= 8; i++){
      for(int j = 1; j <= 8; j++){
        board[i][j] = originalBoard.board[i][j];
      }
    }

    numOfBlack = originalBoard.numOfBlack;
    numOfWhite = originalBoard.numOfWhite;
  }

  /**
   * どのプレイヤーの番かを判定する
   *
   * @param state どちらの番か
   * @return どこかに置けるなら0。パスなら-1。
   */
  int printWhoseTurn(TurnState state) {
    return state.startTurn(isPass(state));
  }

  /**
   * 現在、順番のプレイヤーがパスかどうかを判定する
   *
   * @param state どちらの番か
   * @return パスしか無い場合はtrue
   */
  private boolean isPass(TurnState state) {
    for (int i = 1; i <= 8; i++) {
      for (int j = 1; j <= 8; j++) {
        int count = checkTurnOver(state, i, j);
        if (count != 0) return false;
      }
    }

    return true;
  }

  /**
   * コマを置く
   *
   * @param state どちらの番か
   * @param putPlace 置く場所
   * @return ひっくり返るコマの枚数
   */
  int putPiece(TurnState state, int putPlace){
    int row = putPlace / 10;
    int column = putPlace % 10;
    int status;
    int numChangedPiece;

    //コマが置けるかの判定
    status = checkTurnOver(state, row, column);
    if(status == 0){
      System.out.println(putPlace + "には置けません");
      return -1;
    }
    //コマを置いてひっくり返す
    numChangedPiece = turnOver(state, putPlace);

    reflectNumChangedPiece(state, numChangedPiece);

    return 0;
  }

  /**
   * 挟まれたコマをひっくり返す
   *
   * @param state どちらの番か
   * @param putPlace コマを置く場所
   * @return ひっくり返るコマの枚数
   */
  private int turnOver(TurnState state, int putPlace){
    int total = 0;
    int count = 0;
    int putRow = putPlace / 10;
    int putColumn = putPlace % 10;
    int checkRow, checkColumn;
    int playerColor = (state.equals(Black.getInstance())) ? BLACK : WHITE;

    for(int i = -1; i <= 1; i++){
      for(int j = -1; j <= 1; j++){
        if(i == 0 && j == 0) continue;
        checkRow = putRow + i;
        checkColumn = putColumn + j;
        count = judgeTurnOver(state, putRow, putColumn, i, j);
        for(int k = 0; k < count; k++){
          board[checkRow][checkColumn] = playerColor;
          checkRow += i;
          checkColumn += j;
        }
        total += count;
      }
    }
    board[putRow][putColumn] = playerColor;

    return total;
  }
}