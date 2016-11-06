
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

//creates the game board frame and handles the button events
@SuppressWarnings("serial")
public class GameBoard extends JFrame implements ActionListener{
	
	static private final int ROWS = 8;
	static private final int COLUMNS = 8;
	Piece[][] board;
	OthelloGame game;
	OthelloState curState; //the current state of the game
	JLabel status;   // label with the current player's turn
	JLabel score;    // label with the game score
	JLabel[] numbers; // labels with the discs' numbers
	JLabel[] letters; // labels with the discs' letters
	JButton newGame;   // button for game restart
	String computerColor; //the chosen color of computer player
	int minimaxDepth;  //the user's chosen depth for minimax algorithm
	
	public GameBoard(String color,int dep){
		computerColor = color;
		minimaxDepth = dep;
		this.setTitle("Othello");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		GridLayout layout = new GridLayout(ROWS+1,COLUMNS+1);
		setLayout(new BorderLayout());
		JPanel panel_0 = new JPanel();
		JPanel panel = new JPanel();
		JPanel statusPanel = new JPanel();
		statusPanel.setBackground(new Color(0,125,44));
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEADING,20,5));
		statusPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		newGame = new JButton("New Game");
		newGame.addActionListener(this);
		status = new JLabel("",JLabel.LEFT);
		status.setPreferredSize(new Dimension(200,35));
		status.setFont(new Font("Serif", Font.BOLD, 22));
		score = new JLabel("",JLabel.RIGHT);
		score.setPreferredSize(new Dimension(400,35));
		score.setFont(new Font("Serif", Font.BOLD, 22));
		numbers = new JLabel[8];
		numbers[0] = new JLabel("   1");
		numbers[1] = new JLabel("   2");
		numbers[2] = new JLabel("   3");
		numbers[3] = new JLabel("   4");
		numbers[4] = new JLabel("   5");
		numbers[5] = new JLabel("   6");
		numbers[6] = new JLabel("   7");
		numbers[7] = new JLabel("   8");
		
		letters = new JLabel[9];
		letters[0] = new JLabel("      A");
		letters[1] = new JLabel("      B");
		letters[2] = new JLabel("      C");
		letters[3] = new JLabel("      D");
		letters[4] = new JLabel("      E");
		letters[5] = new JLabel("      F");
		letters[6] = new JLabel("      G");
		letters[7] = new JLabel("      H");
		letters[8] = new JLabel(" ");
		
		for(int i=0;i<8;i++){
			numbers[i].setForeground(Color.BLACK);
			letters[i].setForeground(Color.BLACK);
			numbers[i].setOpaque(true);
			letters[i].setOpaque(true);
			numbers[i].setBackground(Color.GRAY);
			letters[i].setBackground(Color.GRAY);
			numbers[i].setFont(new Font("Serif", Font.BOLD, 25));
			letters[i].setFont(new Font("Serif", Font.BOLD, 25));
		}
		letters[8].setOpaque(true);
		letters[8].setBackground(Color.GRAY);
		
		score.setForeground(Color.WHITE);
		status.setForeground(Color.WHITE);
		statusPanel.add(newGame);
		statusPanel.add(status);
		statusPanel.add(score);
		this.add(statusPanel,BorderLayout.NORTH);
		panel.setLayout(layout);
		panel.setBackground(new Color(0,125,44));
		panel_0.setBorder(new EmptyBorder(20, 20, 20, 20)); 
		panel_0.setBackground(Color.GRAY);
		panel_0.add(panel);
	    this.add(panel_0,BorderLayout.CENTER);
		board = new Piece[ROWS][COLUMNS];
		
		for(int i=0;i<ROWS;i++){
			for(int j=0;j<COLUMNS;j++){
				board[i][j] = new Piece();
				board[i][j].setBackground(new Color(0,125,44));
				board[i][j].setContentAreaFilled(false);
				board[i][j].addActionListener(this);	
			}
		}
		
		for(int i=0;i<ROWS;i++){
			for(int j=0;j<COLUMNS;j++){
				panel.add(board[i][j]);	
			}
			panel.add(numbers[i]);
		}
		
		for(int i=0;i<ROWS+1;i++){
			panel.add(letters[i]);	
	}
		
		this.pack();
		
		actionPerformed(null);
}

	// handles all the events
	public void actionPerformed(ActionEvent e) {
		if (e == null || e.getSource() == newGame) { 
		  if (e != null){ // if restart button was clicked
		    int answer = JOptionPane.showConfirmDialog(this,"Do you want to take the black discs and play first?","Choose Color",JOptionPane.YES_NO_OPTION);
			if(answer == JOptionPane.YES_OPTION){
			  computerColor = "White";
			}
			else {
			  computerColor = "Black";
			}
			String answer2 = JOptionPane.showInputDialog(this,"Insert the depth value of minimax search");
			minimaxDepth = answer2.compareTo("") == 0 ? 0 : Integer.parseInt(answer2);
		  }
		  game = new OthelloGame(); //creates a new game
		  curState = game.getInitialState(); // initialize the game
		  updateBoard();
		  updateStatus();
		  if (computerColor.compareTo("Black") == 0) { // if computer player plays first
		    ComputerMove();
		  }
		} 
		else {                           
		  for (int i = 0; i < 8; i++){              
		    for (int j = 0; j < 8; j++){
			  if (e.getSource() == board[i][j]){
				board[i][j].isClicked = true;
				curState = game.getResult(curState,new Action(i,j));
				updateBoard();
				updateStatus();
				SwingUtilities.invokeLater(new Runnable() {
				  public void run() {
				    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				    if(!(game.isTerminal(curState)) && curState.getPlayerToMove().compareTo(computerColor) == 0){
						ComputerMove();
						while(!(game.isTerminal(curState)) && !(curState.canPlay(curState.getPlayerToMove()))){
							JOptionPane.showMessageDialog(null,curState.getPlayerToMove()+" can't play!Next Player's Turn!","Player cannot play",JOptionPane.INFORMATION_MESSAGE);
							curState.setPlayerToMove(computerColor);
							updateStatus();
							ComputerMove();
						 }
					 }	
				    }
				}); 
			   }
		    }
		  }
		 }
		
	}
	
	// performs the computer player motion using minimax algorithm
	private void ComputerMove(){
	  MinimaxSearch s;
	  Action a;
	  s = new MinimaxSearch(game,minimaxDepth,computerColor); 
	  a = s.makeDecision(curState); // decide which possible move is the best
	  if (a == null){ // if computer player has no moves to play
		JOptionPane.showMessageDialog(this,curState.getPlayerToMove()+" can't play!Next Player's Turn!","Player cannot play",JOptionPane.INFORMATION_MESSAGE);
		curState.setPlayerToMove((curState.getPlayerToMove().compareTo("Black") == 0 ? "White" : "Black"));
		updateStatus();
	  }
	  else{
		curState = game.getResult(curState, a); // play the move
		updateBoard();
	    updateStatus();	
	  }
	}
	
	//updates the buttons' values
	private void updateBoard(){
	  for (int i = 0; i < 8; i++) {
	    for(int j = 0;j<8;j++){
		  String val = curState.getSquareValue(i,j);
		  if (val.compareTo("Empty") != 0){
		    board[i][j].setPlayer(val);
			board[i][j].isClicked = true;
			repaint();
		   }
		   else {
		     board[i][j].isClicked = false;
			 board[i][j].setPlayer("None");
			 repaint();
		   }
		  }				
		}
	 }
	
	//updates the score's label ,the turns' label and announce the winner when the game is over
	private void updateStatus(){
	  String statusText = "";
	  String scoreText;
	  scoreText = "Score: "+"White player vs "+"Black player "+curState.getNumberOfDiscs("White")+"-"+curState.getNumberOfDiscs("Black");
	  score.setText(scoreText);
	  if (game.isTerminal(curState)){
	    String message;
		if (game.getUtility(curState, "Black") == 1) // black player wins
		  message = "Black has won! Score: "+curState.getNumberOfDiscs("Black")+"-"+curState.getNumberOfDiscs("White");
		else if (game.getUtility(curState, "White") == 1) // white player wins
		  message = "White has won! Score: "+curState.getNumberOfDiscs("White")+"-"+curState.getNumberOfDiscs("Black");
		else // draw
		  message = "It's a draw! No winner!";
			
		JOptionPane.showMessageDialog(this,message,"End of Game",JOptionPane.INFORMATION_MESSAGE);
	   }
	   else{
		 statusText = "Turn: " + game.getPlayer(curState)+" player";
	   }
	   status.setText(statusText);  
	}

}
