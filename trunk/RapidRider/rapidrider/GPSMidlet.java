package rapidrider;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class GPSMidlet extends MIDlet implements CommandListener {

	private Command cmdExit, cmdPause, cmdRestart, cmdFindNearest, cmdGetDestinationStop;  /* cmdXMLRead, */
	private GPSDevice device;
	private AppController screen;

	// private static final String URL =
	// "http://153.106.117.64:8080/monopolyServlet/Monopoly";
	private static final String URL = "http://localhost:8080/rapidRiderServlet/RapidRider&Request=";
	//private Vector busStopVector = new Vector();
	private StringItem resultItem;
	private Command cmdXMLRead;

	public GPSMidlet() {
		screen = new AppController();
		cmdExit = new Command("Exit", Command.EXIT, 1);
		cmdPause = new Command("Pause", Command.ITEM, 1);
		cmdRestart = new Command("Restart", Command.ITEM, 1);
		cmdXMLRead = new Command("Get XML Data", Command.OK, 1);
		device = new GPSDevice(screen);
		cmdFindNearest = new Command("Find Nearest", Command.ITEM, 1);
		cmdGetDestinationStop = new Command("Get Dest. Stop", Command.ITEM, 1);
		resultItem = new StringItem("", "");
		
		// TODO: This does not look well thought-through.
		screen.append(resultItem);
		
		screen.addCommand(cmdExit);
		screen.addCommand(cmdPause);
		screen.addCommand(cmdFindNearest);
		screen.addCommand(cmdXMLRead);
		screen.addCommand(cmdGetDestinationStop);
		screen.setCommandListener(this);
	}

	// TODO: Consider defining all classes in their own file.
	class ReadXML extends Thread {
		public void run() {
			try {
				//URL += "rt1"
				HttpConnection connection = (HttpConnection) Connector.open(URL);
				KXmlParser parser = new KXmlParser();
				parser.setInput(new InputStreamReader(connection.openInputStream()));
				parser.nextTag();
				parser.require(XmlPullParser.START_TAG, null, "rapidrider");
				while (parser.nextTag() != XmlPullParser.END_TAG)
					readXMLData(parser);
				parser.require(XmlPullParser.END_TAG, null, "rapidrider");
				parser.next();
				parser.require(XmlPullParser.END_DOCUMENT, null, null);
			} catch (Exception e) {
				e.printStackTrace();
				resultItem.setLabel("Error:");
				resultItem.setText(e.toString());
			}
		}
	}

	private void readXMLData(KXmlParser parser) throws IOException,
			XmlPullParserException {
		
		parser.require(XmlPullParser.START_TAG, null, "busstop");
		BusStop busStop = new BusStop();
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			parser.require(XmlPullParser.START_TAG, null, null);
			String name = parser.getName();
			String text = parser.nextText();
			if (name.equals("stopID"))
				busStop.setId(text);
			else if (name.equals("stopName"))
				busStop.setName(text);
			else if (name.equals("latitude"))
				busStop.setLatitude(text);
			else if (name.equals("longitude"))
				busStop.setLongitude(text);

			parser.require(XmlPullParser.END_TAG, null, name);
			/*
			 * TODO: This currently parses ALL bus stops, and stores them on the palm.
			 * Probably not what we want to do.
			 */
			screen.getRoute().addStop(busStop);
		}
		//busStopVector.addElement(busStop);
		parser.require(XmlPullParser.END_TAG, null, "busstop");
	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(screen);
		screen.start();
		device.start();
	}

	protected void pauseApp() {
		screen.stop();
		device.stop();
		screen.removeCommand(cmdPause);
		screen.removeCommand(cmdFindNearest);
		screen.removeCommand(cmdXMLRead);
		screen.removeCommand(cmdGetDestinationStop);
		screen.addCommand(cmdRestart);
	}
	
	protected void restartApp() {
		screen.start();
		
		// TODO: check that we want this line:
		device.start();
		
		screen.removeCommand(cmdRestart);
		screen.addCommand(cmdPause);
		screen.addCommand(cmdFindNearest);
		screen.addCommand(cmdXMLRead);
		screen.addCommand(cmdGetDestinationStop);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		screen.stop();
		device.stop();
		screen = null;
		System.gc();
	}
	
/*	public void setStops () {
		BusStop stop = new BusStop();
		new ReadXML().start();
		for (int i = 0; i < busStopVector.size(); i++) {
			stop = (BusStop) busStopVector.elementAt(i);
		}
		screen.getRoute().addStop(stop);
	}*/

	public void commandAction(Command c, Displayable d) {
		StringBuffer sb = new StringBuffer();
		//BusStop stop;
		if (c == cmdExit) {
			try {
				destroyApp(false); // Do MIDLet resource cleanup (see above).
				notifyDestroyed(); // Tell the JVM that the user wants to
				// terminate this midlet.
			} catch (MIDletStateChangeException e) {
			}
		} else if (c == cmdPause) {
			pauseApp();
		} else if (c == cmdGetDestinationStop) {
			try {

				screen.getDestinationStop();

			} catch (IOException e) {
				System.out.println("CAUGHT EXCEPTION");
			}
		} else if (c == cmdXMLRead) {
			screen.getRoute().removeAllStops();
			new ReadXML().start();
			
			// TODO: Here we start a thread that grabs the data.  Then we
			// assume that the data is ready and move on.  This is bad.

	/*		for (int i = 0; i < busStopVector.size(); i++) {
				stop = (BusStop) busStopVector.elementAt(i);
				
				sb.append("Stop " + stop.getId() + "\n" + stop.getName() + "\n"
						+ stop.getLatitude() + " " + stop.getLongitude() + "\n"
						+ "------\n");
			}
	*/
			
			sb.append("Stops Received");
			resultItem.setLabel("Stops:\n");
			resultItem.setText(sb.toString());
		} else if (c == cmdRestart) {
			restartApp();
		} else if (c == cmdFindNearest) {
			screen.findNearest();
		}
	}
}
