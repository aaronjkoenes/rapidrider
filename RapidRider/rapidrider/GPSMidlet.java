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

	private Command cmdExit, cmdPause, cmdRestart;  /* cmdXMLRead, */
	private GPSDevice device;
	private AppController screen;

	// private static final String URL =
	// "http://153.106.117.64:8080/monopolyServlet/Monopoly";
	private static String URL = "http://localhost:8080/rapidRiderServlet/RapidRider";
	//private Vector busStopVector = new Vector();
	private StringItem resultItem;
	private Command cmdGetRoute;

//	private List stopNames = new List("Stops: ", List.MULTIPLE);
	private String stopNames = "\n";
	

	public GPSMidlet() {
		screen = new AppController();
		cmdExit = new Command("Exit", Command.EXIT, 1);
		cmdPause = new Command("Pause", Command.ITEM, 1);
		cmdRestart = new Command("Restart", Command.ITEM, 1);
		cmdGetRoute = new Command("Get Route", Command.OK, 1);
		device = new GPSDevice(screen);


		resultItem = new StringItem("", "");
		
		// TODO: This does not look well thought-through.
		screen.append(resultItem);
		
		screen.addCommand(cmdExit);
		screen.addCommand(cmdPause);

		screen.addCommand(cmdGetRoute);

		screen.setCommandListener(this);
		
//		screen.getRoute().addStop(new BusStop(new SimpleLoc(1,1), "test 1"));
//		screen.getRoute().addStop(new BusStop(new SimpleLoc(-2,2), "test 2"));
//		screen.getRoute().addStop(new BusStop(new SimpleLoc(3,3), "test 3"));

		
	}

	// TODO: Consider defining all classes in their own file.
	class ReadXML extends Thread {
		public void run() {
			try {
				String URLtarget = URL + "?address=" + 
				replaceSpaces(screen.getDestinationAddress() + " Grand Rapids, MI" + 
						"&lat=" + screen.getCurrentLocation().getLat() + "&lon=" +
						screen.getCurrentLocation().getLon());
				HttpConnection connection = 
					(HttpConnection) Connector.open(URLtarget);
					System.out.println("URL:  " + URLtarget);
/*				} else {
				connection = 
					(HttpConnection) Connector.open(URL);
				}*/
				KXmlParser parser = new KXmlParser();
				parser.setInput(new InputStreamReader(connection.openInputStream()));
				parser.nextTag();
				parser.require(XmlPullParser.START_TAG, null, "rapidrider");

				while (parser.nextTag() != XmlPullParser.END_TAG)
					readXMLData(parser);

				parser.require(XmlPullParser.END_TAG, null, "rapidrider");
				parser.next();
				parser.require(XmlPullParser.END_DOCUMENT, null, null);
				screen.setNearestLoc(stopNames);
				stopNames = "\n";
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
			

			while (parser.nextTag() != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, null);
				String name = parser.getName();
				String text = parser.nextText();
				stopNames += (text + "\n");
				parser.require(XmlPullParser.END_TAG, null, name);
			}
			parser.require(XmlPullParser.END_TAG, null, "busstop");
	}
	
	public String replaceSpaces (String s) {
		String Result = "";
		for( int i = 0; i < s.length(); i++ ) {
			if( s.charAt(i) == ' ' ) {
				Result += "%20";
			} else {
				Result += s.charAt(i);
			}
		}
		return Result;
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

		screen.removeCommand(cmdGetRoute);

		screen.addCommand(cmdRestart);
	}
	
	protected void restartApp() {
		screen.start();
		
		// TODO: check that we want this line:
		device.start();
		
		screen.removeCommand(cmdRestart);
		screen.addCommand(cmdPause);

		screen.addCommand(cmdGetRoute);

	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		screen.stop();
		device.stop();
		screen = null;
		System.gc();
	}

	public void commandAction(Command c, Displayable d) {

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
		} else if (c == cmdGetRoute) {
			screen.setNearestLoc("Working...");
			new ReadXML().start();
			
		} else if (c == cmdRestart) {
			restartApp();
		}	
	}
}
