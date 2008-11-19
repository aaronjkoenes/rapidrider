package rapidrider;



import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;


public class GPSMidlet extends MIDlet implements CommandListener {

	private Command cmdExit, cmdPause, cmdRestart, cmdFindLocation;
	private GPSDevice device;
	private AppController board;

	public GPSMidlet() {
		board = new AppController();
		cmdExit = new Command("Exit", Command.EXIT, 1);
		cmdPause = new Command("Pause", Command.ITEM, 1);
		cmdRestart = new Command("Restart", Command.ITEM, 1);
		device = new GPSDevice(board);
		cmdFindLocation = new Command("Find Nearest", Command.ITEM, 1);
		board.addCommand(cmdExit);
		board.addCommand(cmdPause);
		board.addCommand(cmdFindLocation);
		board.setCommandListener(this);
	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(board);
		board.start();
		device.start();
	}
	
	protected void pauseApp() {
		board.stop();
		device.stop();
		board.removeCommand(cmdPause);
		board.removeCommand(cmdFindLocation);
		board.addCommand(cmdRestart);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		board.stop();
		device.stop();
		board = null;
		System.gc();
	}

	public void commandAction(Command c, Displayable d) {
		if (c == cmdExit) {
			try {
				destroyApp(false); // Do MIDLet resource cleanup (see above).
				notifyDestroyed(); // Tell the JVM that the user wants to terminate this midlet.
			} catch (MIDletStateChangeException e) {
			}
		} else if (c == cmdPause) {
			pauseApp();
		} else if (c == cmdRestart) {
			board.start();
			board.removeCommand(cmdRestart);
			board.addCommand(cmdPause);
			board.addCommand(cmdFindLocation);
			
		} else if (c == cmdFindLocation) {
			board.findNearest();
		}
	}
}
