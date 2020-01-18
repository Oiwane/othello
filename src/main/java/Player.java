import java.util.Random;

class Player{
    int color;

    public Player(){}
    public Player(int player_color){
        this.color = player_color;
    }

    //ひっくり返す
	public int turn_over(Board board, int put_place){
		int total = 0;
		int count = 0;
		int put_row = put_place / 10;
		int put_column = put_place % 10;
		int check_row, check_column;

		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				if(i == 0 && j == 0) continue;
				check_row = put_row + i;
				check_column = put_column + j;
				count = board.judge_turn_over(this, put_row, put_column, i, j);
				for(int k = 0; k < count; k++){
					board.board[check_row][check_column] = this.color;
					check_row += i;
					check_column += j;
				}
				total += count;
			}
		}
		board.board[put_row][put_column] = this.color;

		return total;
	}

    //コマを置く
	public int put_piece(Board board, int put_place){
		int row = put_place / 10;
		int column = put_place % 10;
		int status;
		int change_piece;

		//コマが置けるかの判定
		status = board.check_turn_over(this, row, column);
		if(status == 0){
			System.out.println(put_place + "には置けません");
			return -1;
		}
		//コマを置いてひっくり返す
		change_piece = this.turn_over(board, put_place);

		if(this.color == Constance.BLACK){
			board.black += change_piece + 1;  //+1しているのは置いたコマの分
			board.white -= change_piece;
		}else{
			board.black -= change_piece;
			board.white += change_piece + 1;
		}

		return 0;
	}

}

class Computer extends Player{
	boolean is_first;

    public Computer(int player_color){
        if(player_color == Constance.BLACK){
			this.color = Constance.WHITE;
			this.is_first = false;
        }else if(player_color == Constance.WHITE){
			this.color = Constance.BLACK;
			this.is_first = true;
        }
    }

    public int decide_place(Board currentBoard){
        int put_place = 0;
		int[] putable_list = new int[20];
		int count = 0;
		Random rand = new Random();

		for(int i = 1; i <= 8; i++){
			for(int j = 1; j <= 8; j++){
				if(currentBoard.check_turn_over(this, i, j) != 0){
					putable_list[count] = i * 10 + j;
					count++;
				}
			}
		}

		put_place = putable_list[rand.nextInt(count)];
		
        return put_place;
    }
}