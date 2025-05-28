
import java.awt.event.*;
import java.awt.event.KeyListener;

class KeyList implements KeyListener
{
	final boolean CONTROL_TOGGLE = true;
	boolean down, left, right, reset, rCW, rCCW, r180, harddrop, hold, up, pause;
	int startLeft, startDown, startRight;
	public void keyPressed(KeyEvent e)
	{
		if(CONTROL_TOGGLE) {
			switch(e.getKeyCode())
			{		
				case KeyEvent.VK_ESCAPE:	pause = true;	break;
				//case KeyEvent.VK_E:		System.exit(0);
			
			
				// DEFAULT CONTROLS
				case KeyEvent.VK_LEFT:		if(!left) startLeft = (int)System.currentTimeMillis(); left = true;	break;
				case KeyEvent.VK_RIGHT:		if(!right) startRight = (int)System.currentTimeMillis(); right= true; break;
				case KeyEvent.VK_UP:		rCW = true;		break;
				case KeyEvent.VK_X:			rCW = true;		break;
				case KeyEvent.VK_Z:			rCCW = true;	break;
				case KeyEvent.VK_CONTROL:	rCCW = true;	break;
				case KeyEvent.VK_DOWN:		if(!down) startDown = (int)System.currentTimeMillis(); down = true;	break;
				case KeyEvent.VK_C:			hold = true;	break;
				case KeyEvent.VK_R:			reset = true;	break;
				case KeyEvent.VK_SPACE:		harddrop = true; break;
			}
		} else {
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_ESCAPE:	pause = true;	break;
				//case KeyEvent.VK_E:		System.exit(0);
			
			
				// MY CONTROLS
				case KeyEvent.VK_A:		if(!left) startLeft = (int)System.currentTimeMillis(); left = true;	break;
				case KeyEvent.VK_D:		if(!right) startRight = (int)System.currentTimeMillis(); right= true; break;
				case KeyEvent.VK_UP:		r180 = true;		break;
				case KeyEvent.VK_RIGHT:			rCW = true;		break;
				case KeyEvent.VK_LEFT:			rCCW = true;	break;
				case KeyEvent.VK_CONTROL:	rCCW = true;	break;
				case KeyEvent.VK_S:		if(!down) startDown = (int)System.currentTimeMillis(); down = true;	break;
				case KeyEvent.VK_C:			hold = true;	break;
				case KeyEvent.VK_R:			reset = true;	break;
				case KeyEvent.VK_SPACE:		harddrop = true; break;
			}
		}
	}
	public void keyReleased(KeyEvent e)
	{
		if(CONTROL_TOGGLE) {
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_ESCAPE:	pause = false;	break;
				// DEFAULT CONTROLS
				case KeyEvent.VK_LEFT:		left = false;	break;
				case KeyEvent.VK_RIGHT:		right= false;	break;
				case KeyEvent.VK_UP:		rCW = false;	break;
				case KeyEvent.VK_X:			rCW = false;	break;
				case KeyEvent.VK_Z:			rCCW = false;	break;
				case KeyEvent.VK_CONTROL:	rCCW = false;	break;
				case KeyEvent.VK_DOWN:		down = false;	break;
				case KeyEvent.VK_C:			hold = false;	break;
				case KeyEvent.VK_R:			reset = false;	break;
				case KeyEvent.VK_SPACE:		harddrop = false;	break;
			}
		} else {
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_ESCAPE:	pause = false;	break;
				// MY CONTROLS
				case KeyEvent.VK_A:			left = false;	break;
				case KeyEvent.VK_D:			right= false;	break;
				case KeyEvent.VK_UP:		r180 = false;	break;
				case KeyEvent.VK_RIGHT:		rCW = false;	break;
				case KeyEvent.VK_LEFT:		rCCW = false;	break;
				case KeyEvent.VK_CONTROL:	rCCW = false;	break;
				case KeyEvent.VK_S:			down = false;	break;
				case KeyEvent.VK_C:			hold = false;	break;
				case KeyEvent.VK_R:			reset = false;	break;
				case KeyEvent.VK_SPACE:		harddrop = false;	break;
			}
		}
	}
		
	public void keyTyped(KeyEvent e){}
}
	