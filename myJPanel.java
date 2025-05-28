import javax.swing.*;
import java.awt.event.*;
//import java.lang.annotation.Repeatable;
import java.awt.*;

class myJPanel extends JPanel
{
	final public static int GRAVITY_SPEED = 50; // frames between piece moving down
	final public static int PLACE_DELAY = 30;
	final public static int REPEAT_DELAY = 167;
	boolean pause;
	KeyList KL;
	myJFrame frame;
	int time, timeOnGround;
	Tetris tetris;
	public myJPanel(KeyList KL1, myJFrame frame1)
	{
		KL = KL1;		//now I have the pointer to the Key Listener in the panel
		frame = frame1;	//now I have the pointer to the Key Listener in the frame
		//set Background Color
		setBackground(Color.white);
				
		tetris = new Tetris();
	}
	public void startGame()
	{
		while(true)
		{
			repaint();
			if(!pause)
				time++;
			try{Thread.sleep(10);} // new frame every 10 ms // 100 fps
			catch(Exception e){}
		}
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(Color.white); // 815, 918 // 801, 
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.gray);
		g.fillRect((frame.getWidth()-15)/4, 0, (frame.getWidth()-15)/2, getHeight());
		g.fillRect(frame.getWidth()/32, frame.getWidth()/32, 3*(frame.getWidth()/16), 3*(frame.getWidth()/32));
		
		tetris.draw(frame, g);
		//System.out.println(time);

		// GRAVITY
		if(time%GRAVITY_SPEED == 0)
			tetris.move(2);
		
		// boolean down, left, right, reset, rCW, rCCW, r180, harddrop, hold
		// CONTROLS
		//System.out.println(KL.up + " " + KL.down + " " + KL.left + " " + KL.up);
		// if(KL.up) {
		// 	tetris.move(0);
		// 	KL.up = false; // set to false to prevent immediate repetition
		// }
		if(!pause) {
			if(KL.down && ( (int)System.currentTimeMillis() <= KL.startDown + 17 || (int)System.currentTimeMillis() >= KL.startDown + REPEAT_DELAY ) ) {
				tetris.move(2); // moves piece downwards
			}
			if(KL.left && ( (int)System.currentTimeMillis() <= KL.startLeft + 17 || (int)System.currentTimeMillis() >= KL.startLeft + REPEAT_DELAY ) ) {
				tetris.move(3); // moves piece left
			}
			if(KL.right && ( (int)System.currentTimeMillis() <= KL.startRight + 17 || (int)System.currentTimeMillis() >= KL.startRight + REPEAT_DELAY ) ) {
				tetris.move(1); // moves piece right
			}
			if(KL.reset) {
				tetris = new Tetris(); // clears the board
				KL.reset = false;
			}
			if(KL.rCW) {
				tetris.rotateCW(); // rotates current piece clockwise
				KL.rCW = false;
			}
			if(KL.rCCW) {
				tetris.rotateCCW(); // rotates current piece counterclockwise
				KL.rCCW = false;
			}
			if(KL.r180) {
				tetris.rotate180(); // rotates current piece 180 degrees
				KL.r180 = false;
			}
			if(KL.harddrop) {
				tetris.hardDrop(); // hard drops the piece
				timeOnGround = PLACE_DELAY;
				KL.harddrop = false;
			}
			if(KL.hold) {
				tetris.hold(); // saves a piece to be used later
				KL.hold = false;
			}
		}
		if(KL.pause) { // toggles pause boolean
			if(pause)
				pause = false;
			else
				pause = true;
			KL.pause = false;
		}
		//System.out.println(getWidth() + " " + getHeight());
		// New Piece
		if(tetris.getCurrent().getY() == tetris.getCurrent().lowestFit()) { // if (piece is on ground)
				timeOnGround++; // waits until PLACE_DELAY before placing the piece when it is on the ground
				if(timeOnGround > PLACE_DELAY) {
					if(tetris.getCurrent().getY() <= 2) { // clears the board if a piece goes above the board
						tetris = new Tetris();
					} else {
						tetris.newPiece(); // creates a new piece`
						tetris.getCurrent().removePiece(Tetris.board);
						tetris.clearLines();
						tetris.getCurrent().placePiece(Tetris.board);
					}
				}
		} else {
			timeOnGround = 0; // resets time piece was on ground if it is no longer on the ground
		}

		// DRAW SCORE
		g.setColor(Color.green);
		//Graphcis.setFont(new Font("String Font Name, can be "" ", 1, size))
		g.setFont(new Font("Times New Roman", 1, frame.getWidth()/27));
		
		//Graphics.drawString(String, x, y) ->x,y is bottom left point of text	
		g.drawString("Score: " + tetris.getScore(), frame.getWidth()/24, frame.getWidth()/13);
		
		// DRAW PAUSE SCREEN
		if(pause || time < 300) {
			if(pause) {
				g.setFont(new Font("Times New Roman", 1, frame.getWidth()/10));
				g.drawString("PAUSED", 3*(frame.getWidth()/10), frame.getWidth()/3);
			}
			g.setFont(new Font("Times New Roman", 1, frame.getWidth()/15));
			g.drawString("Arrow keys - move", (frame.getWidth()/4), frame.getWidth()/2);
			g.drawString("Space - hard drop", (frame.getWidth()/4), frame.getWidth()*3/5);
			g.drawString("C - hold", (frame.getWidth()/4), frame.getWidth()*7/10);
			g.drawString("esc - pause", (frame.getWidth()/4), frame.getWidth()*4/5);
		}
	}
	private class MouseList implements MouseMotionListener, MouseListener
	{
		public void mouseMoved(MouseEvent e) //satisfies MouseMotionListener interface
		{
			//System.out.println("Mouse Moved:\t"+"("+e.getX()+", "+e.getY()+")");
		}
		public void mouseDragged(MouseEvent e) //satisfies MouseMotionListener interface 
		{
			//System.out.println("Mouse Dragged:\t"+"("+e.getX()+", "+e.getY()+")");
		}
		public void mousePressed(MouseEvent e) //satisfies MouseListener interface
		{
			//System.out.println("Mouse Pressed:\t"+"("+e.getX()+", "+e.getY()+")\t");
		}
		public void mouseReleased(MouseEvent e) //satisfies MouseListener interface
		{
			//System.out.println("Mouse Released:\t"+"("+e.getX()+", "+e.getY()+")");
		}
		public void mouseClicked(MouseEvent e){}	//satisfies MouseListener interface
		public void mouseEntered(MouseEvent e){}	//satisfies MouseListener interface
		public void mouseExited(MouseEvent e){}		//satisfies MouseListener interface
	}
}	
	
