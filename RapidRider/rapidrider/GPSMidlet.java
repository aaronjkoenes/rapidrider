package rapidrider;



import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

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

//import rapidrider.BusStopClient.ReadXML;


public class GPSMidlet extends MIDlet implements CommandListener {

	private Command cmdExit, cmdPause, /*cmdXMLRead,*/ cmdRestart, cmdFindLocation;
	private GPSDevice device;
	private AppController screen;
	private static final String URL = "http://153.106.117.64:8080/monopolyServlet/Monopoly"; 
	private Vector busStopVector = new Vector();
	private StringItem resultItem;
	private Command cmdXMLRead;

	public GPSMidlet() {
		screen = new AppController();
		cmdExit = new Command("Exit", Command.EXIT, 1);
		cmdPause = new Command("Pause", Command.ITEM, 1);
		cmdRestart = new Command("Restart", Command.ITEM, 1);
		cmdXMLRead = new Command("Get XML Data", Command.OK, 1);
		device = new GPSDevice(screen);
		cmdFindLocation = new Command("Find Nearest", Command.ITEM, 1);
		resultItem = new StringItem("", "");
		//screen.append(resultItem);
		screen.addCommand(cmdExit);
		screen.addCommand(cmdPause);
		screen.addCommand(cmdFindLocation);
		screen.addCommand(cmdXMLRead);
		screen.setCommandListener(this);
	}

	class ReadXML extends Thread {
		public void run() {
			try {
				HttpConnection httpConnection = (HttpConnection) Connector.open(URL);
				KXmlParser parser = new KXmlParser();
				parser.setInput(new InputStreamReader(httpConnection.openInputStream()));
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

	private void readXMLData(KXmlParser parser) throws IOException, XmlPullParserException {
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
		}
		busStopVector.addElement(busStop);
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
		screen.removeCommand(cmdFindLocation);
		screen.addCommand(cmdRestart);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		screen.stop();
		device.stop();
		screen = null;
		System.gc();
	}

	public void commandAction(Command c, Displayable d) {
		StringBuffer sb = new StringBuffer();
		BusStop stop;
		if (c == cmdExit) {
			try {
				destroyApp(false); // Do MIDLet resource cleanup (see above).
				notifyDestroyed(); // Tell the JVM that the user wants to terminate this midlet.
			} catch (MIDletStateChangeException e) {
			}
		} else if (c == cmdPause) {
			pauseApp();
		} else if (c == cmdXMLRead) {
			new ReadXML().start();
			for (int i = 0; i < busStopVector.size(); i++) {
				stop = (BusStop) busStopVector.elementAt(i);
				sb.append("Stop " + stop.getId() + "\n" + 
				stop.getName() + "\n" + 
				stop.getLatitude() + " " + 
				stop.getLongitude() + "\n" +
				"------\n");
			}
			resultItem.setLabel("Stops:\n");
			resultItem.setText(sb.toString());
		} else if (c == cmdRestart) {
			screen.start();
			screen.removeCommand(cmdRestart);
			screen.addCommand(cmdPause);
			screen.addCommand(cmdFindLocation);
			
		} else if (c == cmdFindLocation) {
			screen.findNearest();
		}
	}
}
