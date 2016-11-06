
import java.util.ArrayList;
import java.util.List;

//represents the game 
public class OthelloGame {
	
  OthelloState initialState = new OthelloState(); //creates the initial state of the game
  
  //returns the initial state of the game
  public OthelloState getInitialState(){
    return initialState;
  }

  //returns the player who is about to move
  public String getPlayer(OthelloState state){
    return state.getPlayerToMove();
  }

  //returns player's valid available moves starting from the given state 
  List<Action> getActions(OthelloState state,String player){ 
    List<Action> list = new ArrayList<Action>();
	for(Action a:state.getEmptySquares()){
	  state.setSquareValue(a.getX(), a.getY(),player);
	  if(state.checkForMatches(a.getX(), a.getY())){
		list.add(a);
	  }
	  state.setSquareValue(a.getX(), a.getY(),"Empty");
	}
	return list;
  }

  //returns the state-result after performing the given action on the given state
  public OthelloState getResult(OthelloState state, Action action){
	OthelloState result = state.clone();
	result.putDisc(action);
	return result;
  }
  
  //checks if the given state is a terminal state of the game
  public boolean isTerminal(OthelloState state){
	return state.getUtility() != -1;
  }
	
  //returns 1 if the given player is the winner and 0 if not
  public double getUtility(OthelloState state, String player) {
    double util = state.getUtility();
	if (util != -1) {
	  if (player.compareToIgnoreCase("White") == 0) util = 1 - util;
	} 
	else throw new IllegalArgumentException("State is not terminal!");
    return util;
  }
}
