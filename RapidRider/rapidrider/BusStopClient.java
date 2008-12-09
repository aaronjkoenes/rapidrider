package rapidrider;
// Sample code for CS 262
// Keith Vander Linden, Calvin College
// Fall 2006
// Based on code from Naveen Balani (creator of KXML)

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;



public class BusStopClient extends MIDlet implements CommandListener {

	private Form mainForm;

	/*
	 * URL of XML data.
	 * I'm going to allow it to keep using the Monopoly URL.  The data has been changed
	 * to allow for Bus Stop data instead, but I fear changing the name or making another
	 * servlet in the same way that I fear throwing nuclear bombs into a volcano:  SOMETHING
	 * is going to blow up.
	 */
	private static final String URL = "http://localhost:8080/monopolyServlet/Monopoly"; 
	private Vector busStopVector = new Vector();
	private StringItem resultItem;
	private Command cmdXMLRead, cmdExit;

	public BusStopClient() {
		mainForm = new Form("Bus Stop Client");
		resultItem = new StringItem("", "");
		mainForm.append(resultItem);
		cmdExit = new Command("Exit", Command.EXIT, 1);
		mainForm.addCommand(cmdExit);
		cmdXMLRead = new Command("Get XML Data", Command.OK, 1);
		mainForm.addCommand(cmdXMLRead);
		mainForm.setCommandListener(this);
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
		BusStop thisStop = new BusStop();
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			parser.require(XmlPullParser.START_TAG, null, null);
			String name = parser.getName();
			String text = parser.nextText();
			if (name.equals("stopID"))
				thisStop.setId(text);
			else if (name.equals("stopName"))
				thisStop.setName(text);
			else if (name.equals("latitude"))
				thisStop.setLatitude(text);
			else if (name.equals("longitude"))
				thisStop.setLongitude(text);

			parser.require(XmlPullParser.END_TAG, null, name);
			
		}
		busStopVector.addElement(thisStop);
		parser.require(XmlPullParser.END_TAG, null, "busstop");
	}

	public void startApp() {
		Display.getDisplay(this).setCurrent(mainForm);
		new ReadXML().start();
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	public void commandAction(Command c, Displayable d) {
		StringBuffer sb = new StringBuffer();
		BusStop stop;
		if (c == cmdExit) {
			destroyApp(false);
			notifyDestroyed();
		} else if (c == cmdXMLRead) {
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
		}

	}

}

