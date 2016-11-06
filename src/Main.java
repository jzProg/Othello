
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args){
		
		int minimaxDepth;
		String computerColor;
		int answer = JOptionPane.showConfirmDialog(null,"Do you want to take the black discs and play first?","Choose Color",JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION){
			computerColor = "White";
		}
		else {
			computerColor = "Black";
		}
		String answer2 = JOptionPane.showInputDialog("Insert the depth value of minimax search");
		minimaxDepth = answer2.compareTo("") == 0 ? 0 : Integer.parseInt(answer2);
		new GameBoard(computerColor,minimaxDepth);			
	}
}
