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
	private SimpleLoc temploc = new SimpleLoc();
	// private static final String URL =
	// "http://153.106.117.64:8080/monopolyServlet/Monopoly";
	private String location = "";
	private static String URL = "http://153.106.117.64:8080/rapidRiderServlet/RapidRider";
	//private Vector busStopVector = new Vector();
	private StringItem resultItem;
	private Command cmdXMLRead;
	private BusStop tempStop;
	
	
	

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
		
//		screen.getRoute().addStop(new BusStop(new SimpleLoc(1,1), "test 1"));
//		screen.getRoute().addStop(new BusStop(new SimpleLoc(-2,2), "test 2"));
//		screen.getRoute().addStop(new BusStop(new SimpleLoc(3,3), "test 3"));

		
	}

	// TODO: Consider defining all classes in their own file.
	class ReadXML extends Thread {
		public void run() {
			try {
				//URL += "rt1"
				HttpConnection connection;
				if( !(screen.getDestinationAddress().equals(""))) {
					connection = 
						(HttpConnection) Connector.open(URL + "?address=" + 
								replaceSpaces(screen.getDestinationAddress() + " Grand Rapids, MI"));
					System.out.println("URL:  " + URL + replaceSpaces(screen.getDestinationAddress() + 
					" Grand Rapids, MI"));
				} else {
				connection = 
					(HttpConnection) Connector.open(URL);
				}
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
		String tempId = "", tempName = "", tempLat = "", tempLon = "";
		if( parser.getName().equals("busstop") ) {
			System.out.println("** " + parser.getName() + " **");
			parser.require(XmlPullParser.START_TAG, null, "busstop");
			
			tempStop = new BusStop();
			while (parser.nextTag() != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, null);
				String name = parser.getName();
				String text = parser.nextText();
				if (name.equals("stopID")) 
					tempId = text.trim();
					//tempStop.setId(text);
				else if (name.equals("stopName"))
					tempName = text.trim();
					//tempStop.setName(text);
				else if (name.equals("latitude"))
					tempLat = text.trim();
					//tempStop.setLatitude(text);
				else if (name.equals("longitude"))
					tempLon = text.trim();
					//tempStop.setLongitude(text);
	
	
				parser.require(XmlPullParser.END_TAG, null, name);
	
				if( tempId != "" && tempName != "" && tempLat != "" && tempLon != "") {
					tempStop.setId(tempId);
					tempStop.setName(tempName);
					tempStop.setLatitude(tempLat);
					tempStop.setLongitude(tempLon);
					screen.getRoute().addStop(tempStop);
					tempId = tempName = tempLat = tempLon = null;
				}
			}
			//busStopVector.addElement(busStop);
			parser.require(XmlPullParser.END_TAG, null, "busstop");
		} else if( parser.getName().equals("destLoc") ) {
			System.out.println("** " + parser.getName() + " **");
			parser.require(XmlPullParser.START_TAG, null, "destLoc");
			while (parser.nextTag() != XmlPullParser.END_TAG) {
				System.out.println(parser.getName());
				parser.require(XmlPullParser.START_TAG, null, null);
				String name = parser.getName();
				String text = parser.nextText();
				if (name.equals("latitude"))
					tempLat = text.trim();
				else //if (name.equals("longitude"))
					tempLon = text.trim();

				parser.require(XmlPullParser.END_TAG, null, name);
				
				if( tempLat != "" && tempLon != "" ) {
					temploc.setLat(Double.parseDouble(tempLat));
					temploc.setLon(Double.parseDouble(tempLon));
					tempLat = tempLon = "";
					System.out.println("loc: " + temploc.printLoc());
				}
				
			}
			//busStopVector.addElement(busStop);
			System.out.println("We got HERE!!!!!!!!!");
			parser.require(XmlPullParser.END_TAG, null, "destLoc");
		}
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
														/*		} else if (c == cmdGetDestinationStop) {
																	try {
														
																		screen.getDestinationStop();
														
																	} catch (IOException e) {
																		System.out.println("CAUGHT EXCEPTION");
																	}*/
		} else if (c == cmdXMLRead) {
			screen.getRoute().removeAllStops();
			new ReadXML().start(); 
			sb.append("Stops Received");
			resultItem.setLabel("Stops:\n");
			resultItem.setText(sb.toString());
		} else if (c == cmdRestart) {
			restartApp();
		} else if (c == cmdFindNearest) {
			screen.findNearest();
			screen.findNearest(temploc);
		}
	}
}
