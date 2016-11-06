

//performs minimax search with alpha-beta pruning
public class MinimaxSearch  {

private OthelloGame game;
private int depth; //minimax algorithm's depth
private String computerPlayer; //computer player's color

public MinimaxSearch(OthelloGame game,int depth,String player) {
  this.game = game;
  this.depth = depth; 
  this.computerPlayer = player;
}

//decides which of the computer player's possible moves is the best and returns it
public Action makeDecision(OthelloState state){
  int temp_depth = depth;
  Action result = null;
  double resultValue = Double.NEGATIVE_INFINITY;
  String player = game.getPlayer(state);
  for (Action action : game.getActions(state,player)){ // for every available move
	double value = minValue(game.getResult(state, action),Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	if (value > resultValue) { // find the max of children's values
	  result = action;
	  resultValue = value;
	}
	depth = temp_depth;
   }
   return result;
}

//finds the maximum value of given state's children values
public double maxValue(OthelloState state,double alpha,double beta){ 
  depth--;
  String player = game.getPlayer(state);
  if (game.isTerminal(state) || depth <= 0) return heuristicFunctionValue(state);
  double value = Double.NEGATIVE_INFINITY;
  for (Action  action : game.getActions(state,player)){
	value = Math.max(value,minValue(game.getResult(state, action),alpha,beta));
	if (value >= beta) return value;
	alpha = Math.max(alpha, value);
  }
  return value;
 }

//finds the minimum value of given state's children values
public double minValue(OthelloState state,double alpha,double beta){ 
  depth--;
  String player = game.getPlayer(state);	
  if (game.isTerminal(state) || depth <= 0)	return heuristicFunctionValue(state);
  double value = Double.POSITIVE_INFINITY;
  for (Action action : game.getActions(state,player)){
	value = Math.min(value,maxValue(game.getResult(state, action),alpha,beta));
	if (value <= alpha) return value;
	beta = Math.min(beta, value);
   }
   return value;
 }

//produce a value for the given state depending on various factors such as disc parity and mobility
public double heuristicFunctionValue(OthelloState state){
  double value = 0;
  if (game.isTerminal(state)){
    value = state.getUtility();
	if (value == 1){ // if winner is black player
	  if (computerPlayer.compareTo("Black") == 0) value = 14000;
	  else value = -14000;
	}
	else if (value == 0){ // if winner is white player
	  if (computerPlayer.compareTo("White") == 0) value = 14000;
	  else value = -14000;
	}
	else if (value == 0.5) value = 10; // if draw
   }
   else{
	 //discs' parity function
     String playerColor = (computerPlayer.compareTo("Black") == 0?"White":"Black");
	 double num1 = state.getNumberOfDiscs(computerPlayer);
	 double num2 = state.getNumberOfDiscs(playerColor);
	 value = 100*(num1 - num2)/(num1 + num2);
	 //number of corners captured by players function
	 double corn1 = state.getNumberOfCorners(computerPlayer);
	 double corn2 = state.getNumberOfCorners(playerColor);	
	 value += 80*(25*(corn1-corn2));
	 //number of discs close to corners captured function
	 double clcorn1 = state.getNumberOfDiscsCloseToCorners(computerPlayer);
	 double clcorn2 = state.getNumberOfDiscsCloseToCorners(playerColor);
	 value += 30*(-12.5*(clcorn1 - clcorn2));
	 //discs' mobility function
	 double mob1 = 0;
	 double mob2 = 0;
	 for (Action a : game.getActions(state, computerPlayer)) mob1 ++;
	 for (Action a : game.getActions(state, playerColor)) mob2 ++;
	 value += 8*(100*(mob1 - mob2)/(mob1 + mob2));
	}
    return value;
 }
}


