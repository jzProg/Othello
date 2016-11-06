
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

// represents a game board square
@SuppressWarnings("serial")
public class Piece extends JButton{
	
	public  boolean isClicked = false;
	private String Player = "None"; // the player who put a disc on the square
	
	// paints a disc on the square
	public void paintComponent(Graphics g){
	  if (!isClicked) super.paintComponent(g);
	  else{
		if (Player.compareToIgnoreCase("Black") == 0){
		  g.setColor(Color.BLACK);
		  g.fillOval(10, 5, 80, 80);
		}	
		else if (Player.compareToIgnoreCase("White") == 0){
		  g.setColor(Color.WHITE);
		  g.fillOval(10, 5, 80, 80);
		}	
	   }
     }
	
	 //change square's player
	 public void setPlayer(String Player){
	   this.Player = Player;
	 }
	 
	 //returns square's player
	 public String getPlayer (){
	   return Player;
	 }
	 
	 //returns square's size
	 public Dimension getPreferredSize(){
	        return (new Dimension(100, 90));
	 }
 }
	
		
	
	

