
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

//represents a state of the game
public class OthelloState implements Cloneable {

	private String playerToMove = "Black"; // player who is about to move
	private String[][] OthelloBoard;
	private double utility; //value depending on the winner(1:win for black,0:win for white,0.5:draw,-1:non terminal state)
	
	public OthelloState(){
	  utility = -1; 
	  OthelloBoard  = new String[8][8];
	  for (int i=0;i<8;i++){
		for(int j=0;j<8;j++){
		  OthelloBoard[i][j] = "Empty";
		 }
	  }
	  OthelloBoard[3][3] = "White";
	  OthelloBoard[3][4] = "Black";
	  OthelloBoard[4][3] = "Black";
	  OthelloBoard[4][4] = "White";
	}
	
	//returns utility of the state
	public double getUtility(){
	  return utility;
	}
    
	//returns the player who is about to move
	public String getPlayerToMove() {
	  return playerToMove;
	}
	
	//change the player of the state
	public void setPlayerToMove(String player){
	  playerToMove = player;
	}
	
	//checks if the square in r,c position is empty
	public boolean isEmptySquare(int r,int c){
	  return OthelloBoard[r][c].compareTo("Empty") == 0;
	}
	
	//returns the value of the square in r,c position
	public String getSquareValue(int r, int c) {
	  return OthelloBoard[r][c];
	}
	
	//change the value of the square in r,c position
	public void setSquareValue(int r,int c,String player) {
	  OthelloBoard[r][c] = player;
	}
	
	//returns all empty squares
	public List<Action> getEmptySquares(){
	  List<Action> list = new ArrayList<Action>();
	  for(int i=0;i<8;i++){
		for(int j=0;j<8;j++){
		  if (isEmptySquare(i,j)){
		    list.add(new Action(i,j));
		  }
		 }
	    }
		return  list;
	}
	
	//creates a copy of the state
	public OthelloState clone(){
	  OthelloState cp = null;
	  try {
		cp = (OthelloState) super.clone();
		cp.OthelloBoard = new String[8][8];
		for(int i=0;i<cp.OthelloBoard.length;i++){
		  for(int j=0;j<cp.OthelloBoard[i].length;j++){
		    cp.OthelloBoard[i][j] = OthelloBoard[i][j];
		   }
		  }	
	   } catch (CloneNotSupportedException e){
		   e.printStackTrace();
		 }
	   return cp;
	}
	
	public void putDisc(Action action){
		putDisc(action.getX(),action.getY());
	}
	
	// puts a disc in the square in r,c position
	public void putDisc(int r,int c){
	  if(utility == -1 && !canPlay(playerToMove)){  // if current player is unable to play
	    JOptionPane.showMessageDialog(null,playerToMove+" can't play!Next Player's Turn!","Player cannot play",JOptionPane.INFORMATION_MESSAGE);
		playerToMove = (playerToMove.compareTo("Black") == 0 ? "White" : "Black");
	   }
	  else{ // if current player has moves to play
		 if (utility == -1 && isEmptySquare(r, c)) {
		   OthelloBoard[r][c] = playerToMove; //play the move
		   boolean check = checkForFlip(r,c);
		   if (check){ // if there is at least one flip
			 analyzeUtility(); //update utility
			 playerToMove = (playerToMove.compareTo("Black") == 0 ? "White" : "Black"); // next player's turn
			}
			else { // if there are no flips
			  OthelloBoard[r][c] = "Empty";
			  JOptionPane.showMessageDialog(null,"Invalid Move!Try Again!","Invalid Move",JOptionPane.ERROR_MESSAGE);
			  analyzeUtility();
			}  
		   }
	   }
	}
	
	//checks diagonal,vertical and horizontal if there is at least one disc for flipping and flips it
	private boolean checkForFlip(int r,int c){
		boolean flag = false;
		for (Action d :DiagonalMatches(r,c)) {
			if (getSquareValue(d.getX(),d.getY()).compareTo(getSquareValue(r,c)) != 0){
				flip(d.getX(),d.getY());
				flag = true;
			}
		}
		for (Action d :RowMatches(r,c)) {
			if (getSquareValue(d.getX(),d.getY()).compareTo(getSquareValue(r,c)) != 0){
				flip(d.getX(),d.getY());
				flag = true;
			}
		}
		for (Action d :ColumnMatches(r,c)) {
			if (getSquareValue(d.getX(),d.getY()).compareTo(getSquareValue(r,c)) != 0){
				flip(d.getX(),d.getY());
				flag = true;
			}
		}
		return flag;
	}
	
	// flips the disc in r,c position
	private void flip(int r,int c){
		 if (OthelloBoard[r][c].compareToIgnoreCase("Black") == 0){
			 OthelloBoard[r][c] = "White";
		 }
		 else if (OthelloBoard[r][c].compareToIgnoreCase("White") == 0){
			 OthelloBoard[r][c] = "Black";
		 }
	}
	
	//checks the 2 diagonals of the board and returns a list with all the discs which are about to be flipped
	private List<Action> DiagonalMatches(int r,int c){
		List<Action> list = new ArrayList<Action>();
		List<Action> list1 = new ArrayList<Action>();
		int i;
		int g = r-1;
		if (g >= 0){
		for( i = c+1;i<8;i++){
			if (g < 0){
				break;
			}
			if(OthelloBoard[g][i].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[g][i].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list1.add(new Action(g,i));
			}
			g--;
		}
		if (i == 8 || g == -1) list1.clear();
		if (i != 8 && g != -1 && OthelloBoard[g][i].compareTo("Empty") == 0) list1.clear();
		}
		List<Action> list2 = new ArrayList<Action>();
		int j;
		int g2 = r+1;
		if (g2 <= 8){
		for(j = c-1;j>=0;j--){
			if (g2 >= 8){
				break;
			}
			if(OthelloBoard[g2][j].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[g2][j].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list2.add(new Action(g2,j));
			}
			g2++;
		}
		if (j == -1 || g2 >= 8) list2.clear();
		if (j != -1 && g2 != 8 && OthelloBoard[g2][j].compareTo("Empty") == 0) list2.clear();
		}
		List<Action> list3 = new ArrayList<Action>();
		int k;
		int s = c+1;
		if (s <= 8){
		for( k = r+1;k<8;k++){
			if (s >= 8){
				break;
			}
			if(OthelloBoard[k][s].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[k][s].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list3.add(new Action(k,s));
			}
			s++;
		}
		if (k == 8 || s == 8)  list3.clear();
		if (k != 8 && s != 8 && OthelloBoard[k][s].compareTo("Empty") == 0) list3.clear();
		}
		List<Action> list4 = new ArrayList<Action>();
		int l;
		int s2 = c-1;
		if (s2 >= 0){
		for(l = r-1;l>=0;l--){
			if (s2 < 0){
				break;
			}
			if(OthelloBoard[l][s2].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[l][s2].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list4.add(new Action(l,s2));
			}
			s2--;
		}
		if (l == -1 || s2 ==-1) list4.clear();
		if (l != -1 && s2 != -1 && OthelloBoard[l][s2].compareTo("Empty") == 0) list4.clear();
		
		}
		for (Action a : list1) list.add(a);
		for (Action a : list2) list.add(a);
		for (Action a : list3) list.add(a);
		for (Action a : list4) list.add(a);
		return list;	
	}
	
	//checks the given column of the board and returns a list with all the discs which are about to be flipped
	private List<Action> ColumnMatches(int r,int c){
		List<Action> list = new ArrayList<Action>();
		List<Action> list1 = new ArrayList<Action>();
		int i;
		for( i = r+1;i<8;i++){
			if(OthelloBoard[i][c].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[i][c].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list1.add(new Action(i,c));
			}
		}
		if (i == 8 && OthelloBoard[i-1][c].compareTo(OthelloBoard[r][c]) != 0) list1.clear();
		if (i != 8 && OthelloBoard[i][c].compareTo("Empty") == 0) list1.clear();
		List<Action> list2 = new ArrayList<Action>();
		int j;
		for(j = r-1;j>=0;j--){
			if(OthelloBoard[j][c].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[j][c].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list2.add(new Action(j,c));
			}
		}
		if (j == -1 && OthelloBoard[0][c].compareTo(OthelloBoard[r][c]) != 0) list2.clear();
		if (j != -1 && OthelloBoard[j][c].compareTo("Empty") == 0) list2.clear();
		for (Action a : list1) list.add(a);
		for (Action a : list2) list.add(a);
		return list;	
	}
	
	//checks the given row of the board and returns a list with all the discs which are about to be flipped
	private List<Action> RowMatches(int r,int c){
		List<Action> list = new ArrayList<Action>();
		List<Action> list1 = new ArrayList<Action>();
		int i;
		for( i = c+1;i<8;i++){
			if(OthelloBoard[r][i].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[r][i].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list1.add(new Action(r,i));
			}
		}
		if (i == 8 && OthelloBoard[r][i-1].compareTo(OthelloBoard[r][c]) != 0) list1.clear();
		if (i != 8 && OthelloBoard[r][i].compareTo("Empty") == 0) list1.clear();
		List<Action> list2 = new ArrayList<Action>();
		int j;
		for(j = c-1;j>=0;j--){
			if(OthelloBoard[r][j].compareTo("Empty") == 0){
				break;
			}
			if(OthelloBoard[r][j].compareTo(OthelloBoard[r][c]) == 0){
				break;
			}
			else {
				list2.add(new Action(r,j));
			}
		}
		if (j == -1 && OthelloBoard[r][0].compareTo(OthelloBoard[r][c]) != 0) list2.clear();
		if (j != -1 && OthelloBoard[r][j].compareTo("Empty") == 0) list2.clear();
		for (Action a : list1) list.add(a);
		for (Action a : list2) list.add(a);
		return list;		
	}
	
	//updates the utility of the state
	private void analyzeUtility(){
		int player1Num = getNumberOfDiscs("Black");
		int player2Num = getNumberOfDiscs("White");
		if ((player1Num + player2Num == 64) || (!canPlay("Black")) && !canPlay("White")){
			if (player1Num > player2Num){
				utility = 1;
			}
			else if (player1Num < player2Num){
				utility = 0;
			}
			else {
				utility = 0.5;
			}
		}
		
	}
	
	//checks if the given player has moves to play
	public boolean canPlay(String player){
		boolean flag = false;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				if(OthelloBoard[i][j].compareTo("Empty" )== 0){
					OthelloBoard[i][j] = player;
					if(checkForMatches(i,j)){
						flag = true;
						OthelloBoard[i][j] = "Empty";
						break;
					}
					OthelloBoard[i][j] = "Empty";
				}
			}
		}
		return flag;
	}
	
	//checks if a move in r,c position flips at least one disc
	public boolean checkForMatches(int r , int c){
		boolean flag;
		flag = !DiagonalMatches(r,c).isEmpty() || !RowMatches(r,c).isEmpty() || !ColumnMatches(r,c).isEmpty();
		
		return flag;
	}
	
	//returns the number of discs the given player has played
	public int getNumberOfDiscs(String player) {
		int num = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (getSquareValue(i,j).compareTo(player) == 0){
					num++;
				}
			}
		}
		return num;
	}
	
	//returns the number of corners the given player has captured
	public int getNumberOfCorners(String player){
		int num = 0;
		if (OthelloBoard[0][0].compareTo(player) == 0) num++;
		if (OthelloBoard[7][0].compareTo(player) == 0) num++;
		if (OthelloBoard[7][7].compareTo(player) == 0) num++;
		if (OthelloBoard[0][7].compareTo(player) == 0) num++;
		return num;
	}
	
	//returns the number of discs close to corners the given player has played
	public int getNumberOfDiscsCloseToCorners(String player){
		int num = 0;
		if (OthelloBoard[0][0].compareTo("Empty") == 0){
			if (OthelloBoard[0][1].compareTo(player) == 0) num++;
			if (OthelloBoard[1][1].compareTo(player) == 0) num++;
			if (OthelloBoard[1][0].compareTo(player) == 0) num++;
		}
		
		if (OthelloBoard[0][7].compareTo("Empty") == 0){
			if (OthelloBoard[0][6].compareTo(player) == 0) num++;
			if (OthelloBoard[1][6].compareTo(player) == 0) num++;
			if (OthelloBoard[1][7].compareTo(player) == 0) num++;
		}
		if (OthelloBoard[7][7].compareTo("Empty") == 0){
			if (OthelloBoard[6][7].compareTo(player) == 0) num++;
			if (OthelloBoard[6][6].compareTo(player) == 0) num++;
			if (OthelloBoard[7][6].compareTo(player) == 0) num++;
		}
		if (OthelloBoard[7][0].compareTo("Empty") == 0){
			if (OthelloBoard[7][1].compareTo(player) == 0) num++;
			if (OthelloBoard[6][1].compareTo(player) == 0) num++;
			if (OthelloBoard[6][0].compareTo(player) == 0) num++;
		}
		return num;
	}
	
}
