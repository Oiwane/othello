import java.io.*;

class Game{
	public static void main(String[] args) throws IOException{
		int is_player;

		while(true){
			System.out.println("0 : 1人で : プレイヤー先手 コンピューター後手");
			System.out.println("1 : 1人で : プレイヤー後手 コンピューター先手");
			System.out.println("2 : 2人で");
			System.out.print("上記の数字を入力してください : ");
			is_player = Input.input_int();
		
			if(is_player == 0 || is_player == 1){
				vs_computer(is_player+1);
				break;
			}else if(is_player == 2){
				pvp();
				break;
			}else{
				System.out.println("指定された数字を入力してください");
			}	
		}

		return;
	}

	public static void pvp() throws IOException{
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

	
	public static void vs_computer(int player_color) throws IOException{
		int log = 0;
		int put_place = 0;
		int pass_count = 0;
		Board current_board = new Board();
		//パスも1手として数えているため、64より多めに確保
		//パスを1手と数えた時の最大手数が分からないため大雑把に100確保
		Board[] old_board = new Board[100];
		Player player = new Player(player_color);
		Computer computer = new Computer(player_color);

		for(int i= 0; i < 100; i++){
			old_board[i] = new Board();
		}
	
		//下のコメントはデバッグ用
		//for(log = current_board.black + current_board.white - 4; log < 100; log++){
		for(log = 0; log < 100; log++){
			current_board.print_board();  //盤の表示
			//どちらかのコマが0になる、またはパスが続く(どちらも置ける場所がなくなる)、または盤がコマで埋まったら
			if(current_board.black == 0 || current_board.white == 0 ||
				pass_count == 2 || current_board.black + current_board.white == 64){
				break;
			}
			show_number_of_piece(current_board);  //コマの数を表示

			//どちらの番なのかを判定
			//コマを置く場所がない場合、パスする
			if(current_board.judge_player(log, player, computer) == -1){
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
					Board.shift_board(current_board, old_board[log]);
					log--;

				}else if((11 <= put_place && put_place <= 18) || (21 <= put_place && put_place <= 28) ||
						(31 <= put_place && put_place <= 38) || (41 <= put_place && put_place <= 48) ||
						(51 <= put_place && put_place <= 58) || (61 <= put_place && put_place <= 68) ||
						(71 <= put_place && put_place <= 78) || (81 <= put_place && put_place <= 88)){

					if(player.put_piece(current_board, put_place) == -1){
						log--;
						continue;
					}
		
					//old_boardにcurrent_boardの情報を移行
					Board.shift_board(old_board[log + 1], current_board);

				}else{
					System.out.println("入力された数字が違います");
					log--;	
				}
			}else{  //コンピュータの処理
				put_place = computer.decide_place(current_board);
				computer.put_piece(current_board, put_place);
				//old_boardにcurrent_boardの情報を移行
				Board.shift_board(old_board[log + 1], current_board);

			}

		}

		//結果の表示
		show_result(current_board);

		return;
		
	}

	public static void show_number_of_piece(Board board){
		System.out.println("黒 : " + board.black);
		System.out.println("白 : " + board.white);
		System.out.println();
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