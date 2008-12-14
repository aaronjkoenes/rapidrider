package rapidrider;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * RapidRiderMidlet is the main midlet class for the RapidRider application.
 */
public class RapidRiderMidlet extends MIDlet implements CommandListener {

	// The command buttons for the GUI.
	private Command cmdExit, cmdPause, cmdRestart;

	// An object that allows us to communicate with the GPS
	private GPSDevice device;

	// I don't need to comment all of this..
	private DisplayScreen screen;

	private Command cmdGetRoute;

	private DirectionsFetcher fetcher;

	/**
	 * Constructs a RapidRider midlet.
	 */
	public RapidRiderMidlet() {
		// Setup the screen.
		screen = new DisplayScreen();
		cmdExit = new Command("Exit", Command.EXIT, 1);
		cmdPause = new Command("Pause", Command.ITEM, 1);
		cmdRestart = new Command("Restart", Command.ITEM, 1);
		cmdGetRoute = new Command("Get Route", Command.OK, 1);

		// Initialize objects for communicating with the world.
		device = new GPSDevice(screen);
		fetcher = new DirectionsFetcher(screen);

		// Add commands to the screen.
		screen.addCommand(cmdExit);
		screen.addCommand(cmdPause);
		screen.addCommand(cmdGetRoute);
		screen.setCommandListener(this);
	}

	/**
	 * Starts the midlet initially.
	 */
	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(screen);
		screen.start();
		device.start();
	}

	/**
	 * Pauses the midlet.
	 */
	protected void pauseApp() {
		screen.stop();
		device.stop();
		screen.setStatus("Paused...");
		screen.removeCommand(cmdPause);
		screen.removeCommand(cmdGetRoute);
		screen.addCommand(cmdRestart);
	}

	/**
	 * Restarts the midlet after it has been paused.
	 */
	protected void restartApp() {
		screen.start();
		device.start();
		screen.setStatus("Connecting...");
		screen.removeCommand(cmdRestart);
		screen.addCommand(cmdPause);
		screen.addCommand(cmdGetRoute);
	}

	/**
	 * Destroys the midlet when it is exited.
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		screen.stop();
		device.stop();
		screen = null;
		System.gc(); // Garbage collect.
	}

	/**
	 * Responds to button taps.
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == cmdExit) {
			try {
				// Do MIDLet resource cleanup (see above).
				destroyApp(false);
				// Tell the JVM that the user wants to terminate this midlet.
				notifyDestroyed();
			} catch (MIDletStateChangeException e) {
			}
		} else if (c == cmdPause) {
			pauseApp();
		} else if (c == cmdGetRoute) {
			screen.setDirections("Contacting server...");
			fetcher.fetchDirections();
		} else if (c == cmdRestart) {
			restartApp();
		}
	}
}
