import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GameWindow {
	private JFrame frame;
	public GameWindow(Chessboard board) {
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.add(board);
		this.frame.setTitle("Chess");
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
		ImageIcon image = new ImageIcon("res/chessGameIcon.png");
		this.frame.setIconImage(image.getImage());
	}
}
