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

public class RRMidlet extends MIDlet implements CommandListener {

	private Command cmdExit, cmdRoute, cmdRestart, cmdPause;

	private Interface face;
	
	private GPSDevice gps;
	
	private StringItem resultItem;
	
	private Vector stopsVector = new Vector();
	
	private static final String URL = "http://192.168.1.100:8080/RapidRiderServlet/RapidRider"; // URL of XML data

	public RRMidlet() {
		face = new Interface();
		cmdExit = new Command("Exit", Command.EXIT, 1);
		cmdRoute = new Command("Get Route", Command.ITEM, 1);
		cmdRestart = new Command("Restart", Command.ITEM, 1);
		cmdPause = new Command("Pause", Command.ITEM, 1);
		face.addCommand(cmdExit);
		face.addCommand(cmdRoute);
		face.setCommandListener(this);
		gps = new GPSDevice(face);
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
		parser.require(XmlPullParser.START_TAG, null, "stop");
		BusStop stop = new BusStop();
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			parser.require(XmlPullParser.START_TAG, null, null);
			String name = parser.getName();
			String text = parser.nextText();
			if (name.equals("id"))
				stop.setId(text);
			else if (name.equals("emailaddress"))
				stop.setEmailAddress(text);
			else if (name.equals("name"))
				stop.setName(text);
			parser.require(XmlPullParser.END_TAG, null, name);
		}
		stopsVector.addElement(stop);
		parser.require(XmlPullParser.END_TAG, null, "stop");
	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(face);
		face.start();
		gps.start();
	}

	protected void pauseApp() {
		face.stop();
		gps.stop();
		face.removeCommand(cmdPause);
		face.addCommand(cmdRestart);
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		face.stop();
		gps.stop();
		face = null;
		gps = null;
		System.gc();
	}
	
	protected void getRoute() throws Exception {

	}

	public void commandAction(Command c, Displayable d) {
		if (c == cmdExit) {
			try {
				destroyApp(false); // Do MIDLet resource cleanup (see above).
				notifyDestroyed(); // Tell the JVM that the user wants to terminate this midlet.
			} catch (MIDletStateChangeException e) {
			}
		} else if (c == cmdRoute) {
			try {
				getRoute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (c == cmdRestart) {
			face.start();
			face.removeCommand(cmdRestart);
			face.addCommand(cmdRoute);
		}
	}

}
