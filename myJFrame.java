import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


class myJFrame extends JFrame
{
	myJPanel panel;
	public myJFrame()
	{
		setPreferredSize(new Dimension(815, 918));
		pack();
		//dimensions of the frame(pixels)
			//note: top left is (0,0)
			//		right is the pos x axis and down is the POS y axis
			//		The top 20 or so rows is not in the panel

		//KeyListener
		KeyList KL = new KeyList();
		addKeyListener(KL);
		
		//PANELS
		panel = new myJPanel(KL, this);
		Container container = getContentPane();
		container.add(panel);
		
		repaint();
		
	}
	public void startGame()
	{
		panel.startGame();
	}
	private class ExitListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//System.out.println("Exit");
			System.exit(0);
		}
	}
	
}
