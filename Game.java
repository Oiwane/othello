import java.io.*;

class Game{
	public static void main(String[] args) throws IOException{

		interpersonal();

		return;
	}

	public static void interpersonal() throws IOException{
		int log;
		int put_place;
		Board current_board = new Board();
		Board[] old_board = new Board[60];
		Player player1 = new Player(Constance.BLACK);
		Player player2 = new Player(Constance.WHITE);
		
		//old_boardの初期化
		for(int i= 0; i < 60; i++){
			old_board[i] = new Board();
		}
				
		for(log = 0; log < 60; log++){

			current_board.print_board();  //盤の表示
			//どちらかのコマが0になったらゲームを終了させる
			if(current_board.black == 0 || current_board.white == 0){
				break;
			}
			show_number_of_piece(current_board);  //コマの数を表示
			
			//どちらの番なのかを判定
			//コマを置く場所がない場合、パスする
			if(current_board.judge_player(log, player1, player2) == -1){
				continue;
			}

			if(log > 0){
				System.out.println("0を入力すると一つ戻ります。");
			}
			System.out.print("コマを置く場所の数字を入力してください : ");
			put_place = Input.input_int();
			System.out.println();

			if(put_place == 0){  //一つ戻る

				if(log == 0){
					System.out.println("それ以上戻れません");
					System.out.println();
					log--;
					continue;
				}

				log--;  //1つ前の番を読み込むため

				//current_boardにold_boardの情報を移行
				Board.shift_board(current_board, old_board[log]);
				log--;  //forの最後でlogが+1されるため引いておく

			}else if((11 <= put_place && put_place <= 18) || (21 <= put_place && put_place <= 28) ||
					 (31 <= put_place && put_place <= 38) || (41 <= put_place && put_place <= 48) ||
					 (51 <= put_place && put_place <= 58) || (61 <= put_place && put_place <= 68) ||
					 (71 <= put_place && put_place <= 78) || (81 <= put_place && put_place <= 88)){

				//コマを置く
				if(log%2 + 1 == Constance.BLACK){  //黒の番の場合

					if(player1.put_piece(current_board, put_place) == -1){
						log--;
						continue;
					}

				}else{  //白の番の場合

					if(player2.put_piece(current_board, put_place) == -1){
						log--;
						continue;
					}

				}

				//old_boardにcurrent_boardの情報を移行
				Board.shift_board(old_board[log + 1], current_board);

			}else{  //盤内以外が指定された時
				System.out.println("入力された数字が違います");
				log--;
			}

		}
		
		//結果の表示
		show_result(current_board);

		return;
	}

	public static void show_number_of_piece(Board board){
		System.out.println("黒 : " + board.black);
		System.out.println("白 : " + board.white);
	}
	
	public static void show_result(Board board){
		show_number_of_piece(board);

		if(board.black > board.white){
			System.out.println("黒の勝ち");
		}else if(board.black < board.white){
			System.out.println("白の勝ち");
		}else{
			System.out.println("引き分け");
		}

		return;
	}
	
}