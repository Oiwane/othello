class Board{
	public int[][] board = new int[10][10];
	public int black = 2;
	public int white = 2;

	//コンストラクタ
	public Board(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(i == 0 || i == 9 || j == 0 || j == 9){
					this.board[i][j] = Constance.OUTSIDE;
				}else if((i == 4 && j == 4) || (i == 5 && j == 5)){
					this.board[i][j] = Constance.BLACK;
				}else if((i == 4 && j == 5) || (i == 5 && j == 4)){
					this.board[i][j] = Constance.WHITE;
				}else{
					this.board[i][j] = Constance.VOID;
				}
			}
		}
	}

	//盤の表示
	public void print_board(){
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

		return;
	}

	//置けるかどうかを確認
	public int judge_turn_over(Player player, int r, int c, int dr, int dc){
		int count = 0;
		int row = r + dr;
		int column = c + dc;
		int opponent = Constance.BLACK;

		if(player.color == Constance.BLACK) opponent = Constance.WHITE;

		while(this.board[row][column] != player.color){
			if(this.board[row][column] != opponent){
				count = 0;
				break;
			}
			row += dr;
			column += dc;
			count++;
		}

		return count;
	}

	//置ける場所の確認
	public int check_turn_over(Player player, int r, int c){
		int count = 0;

		if(this.board[r][c] != Constance.VOID){
			return 0;
		}

		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				count += this.judge_turn_over(player, r, c, i, j);
			}
		}
	
		return count;
	}

	//盤の情報を移行
	public static void shift_board(Board overwritten_board, Board overwrite_board){
		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				overwritten_board.board[i][j] = overwrite_board.board[i][j];
			}
		}

		overwritten_board.black = overwrite_board.black;
		overwritten_board.white = overwrite_board.white;
	}

	/* どのプレイヤーの番かの判定 */
	public int judge_player(int log, Player player1, Player player2){

		if(log%2 == 1){  //白の番

			if(this.is_pass(player2)){
				System.out.println("コマを置けません");
				System.out.println("白の番を飛ばします");
				return -1;
			}
			System.out.println("白の番です");

		}else{  //黒の番

			if(this.is_pass(player1)){
				System.out.println("コマを置けません");
				System.out.println("黒の番を飛ばします");
				return -1;
			}
			System.out.println("黒の番です");

		}

		return 0;

	}

	/* パスかどうかの判定 */
	public boolean is_pass(Player player){
		int status = 0;

		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				status = this.check_turn_over(player, i, j);
				if(Constance.ToBool(status)){
					return false;
				}
			}
		}

		return true;
	}

}