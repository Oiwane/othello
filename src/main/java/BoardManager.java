import state.Black;
import state.TurnState;
import state.White;

import java.util.ArrayList;
import java.util.List;

public class BoardManager {
  private TurnState state;
  private Board currentBoard;
  private List<Board> oldBoardList;

  BoardManager() {
    state = Black.getInstance();
    currentBoard = new Board();
    oldBoardList = new ArrayList<>();

    for(int i = 0; i < Board.LOG_SIZE; i++){
      oldBoardList.add(new Board());
    }
  }

  public Board getCurrentBoard() {
    return currentBoard;
  }

  public List<Board> getOldBoardList() {
    return oldBoardList;
  }

  public TurnState getState() {
    return state;
  }

  public void changeTurn() {
    state = (state.equals(Black.getInstance())) ? White.getInstance() : Black.getInstance();
  }
}
