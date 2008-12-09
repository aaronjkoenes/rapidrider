import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet Class
 * 
 * TODO: More documentation should be added.
 * 
 * @web.servlet name="RapidRider" display-name="Name for RapidRider"
 *              description="Description for RapidRider"
 * @web.servlet-mapping url-pattern="/RapidRider"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
public class RapidRiderServlet extends HttpServlet {
	static final long serialVersionUID = 1;

	Connection conn; // holds database connection
	Statement stmt; // holds SQL statement

	double targetLat = 0.0, targetLon = 0.0;
	URL target;
	private BusRoute route = new BusRoute("test Route");
	private SimpleLoc currentLoc;
	
	// TODO: Add documentation?
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\"?>");
		out.println("<rapidrider>");
/*		if (req.getQueryString() != null) {		
*/			String destinationAddress = req.getParameter("address");
			double currentLat = Double.parseDouble(req.getParameter("lat"));
			double currentLon = Double.parseDouble(req.getParameter("lon"));
			currentLoc = new SimpleLoc(currentLat, currentLon);
/*			System.out.println(req.getQueryString());
*/			getDestinationCoords(destinationAddress);
/*
			out.println("<destLoc>");
			out.println("<latitude>" + targetLat + "</latitude>");
			out.println("<longitude>" + targetLon + "</longitude>");
			out.println("</destLoc>");
		} else {*/										
			try {
				Class.forName("org.postgresql.Driver");
	
				conn = DriverManager.getConnection("jdbc:postgresql:rapidrider",
						"postgres", "bjarne");
				stmt = conn.createStatement();
				ResultSet res = stmt
						.executeQuery("SELECT * FROM busstops");
	
				// TODO Question: Is it possible for res to be null?
				if (res != null) {
					while (res.next()) {
						String stopName = res.getString(2);
						double longitude = Double.parseDouble(res.getString(4));
						double latitude = Double.parseDouble(res.getString(5));
						
						String revisedStopName = replaceEscapeCharacters(stopName);
						BusStop temp = new BusStop(new SimpleLoc(latitude, longitude), revisedStopName);
						route.addStop(temp);
//						out.println("<busstop>");
////						temp2 = route.getstop(route.routeLength());
//						out.println("<stopName>" + temp.getName() + " " + temp.getLatitude() + " " + temp.getLongitude() + "</stopName>");
//						out.println("</busstop>");

/*						String latitude = res.getString(3);
						String longitude = res.getString(4);
						out.println("<busstop>");
						out.println("<stopID>" + stopID + "</stopID>");
						out.println("<stopName>" + revisedStopName + "</stopName>");
						out.println("<latitude>" + latitude + "</latitude>");
						out.println("<longitude>" + longitude + "</longitude>");
						out.println("</busstop>");

*/
					}
					out.println("<busstop>");
					out.println("<stopName>" + findNearest(currentLoc).getName() + "</stopName>");
					out.println("</busstop>");
					out.println("<busstop>");
					out.println("<stopName>" + findNearest(new SimpleLoc(targetLat, targetLon)).getName() + "</stopName>");
					out.println("</busstop>");
				}
				res.close();
				stmt.close();
				conn.close();
	
			} catch (Exception e) {
				out.println("error in JDBC access: " + e.getClass() + " : "
						+ e.getMessage());
				e.printStackTrace();
			}
//		}
		out.println("</rapidrider>");
	}
	
/*	public void findNearest() {
		int nearestLocIndex = 0;
		double distance = 0.0;
		double shortest = -1.0;
		System.out.println(route.listStops());
		int length = route.routeLength();
		for (int i = 0; i < length; i++) {
			distance = currentLoc.distanceTo(new SimpleLoc(route.getstop(i).getLatitude(),route.getstop(i).getLongitude()));
			if (shortest > distance || shortest < 0) {
				shortest = distance;
				nearestLocIndex = i;
			}
		}
		// nearestLocation.setText(route.getstop(nearestLocIndex).getName());
	}*/
	
	public BusStop findNearest(SimpleLoc l) {
		int nearestLocIndex = -1;
		double distance = 0.0;
		double shortest = -1.0;
//		System.out.println(route.listStops());
		int length = route.routeLength();
		for (int i = 0; i < length; i++) {
			double locationLat = route.getstop(i).getLatitude();
			double locationLon = route.getstop(i).getLongitude();
			SimpleLoc location = new SimpleLoc(locationLat, locationLon);
			distance = l.distanceTo(location);
//			System.out.print("Distance found: " + distance);
			if (shortest > distance || shortest < 0) {
//				System.out.println(".  Shorter.  Setting to " + route.getstop(i).getName());
				shortest = distance;
				nearestLocIndex = i;
			} else {
//				System.out.println();
			}
		}
		return route.getstop(nearestLocIndex);
		//nearestToDest.setText(route.getstop(nearestLocIndex).getName());
	}

	
	/*
	 * This portion of the code protects the XML. Ampersands (&) break XML
	 * documents. This searches for ampersands and replaces them with the HTML
	 * equivalent (&amp;)
	 */
	public void getDestinationCoords(String s) throws IOException {
		String URLx = "http://tinygeocoder.com/create-api.php?q=" + replaceSpaces(s) ;
		System.out.println(URLx);
		target = new URL(URLx);
		InputStreamReader x = new InputStreamReader(target.openStream());
		BufferedReader in = new BufferedReader(x);
		String result = in.readLine();
		in.close();
		x.close();
		parse(result);
	}


	public void parse(String s ) {
		String[] s2 = s.split(",");
		targetLat = Double.parseDouble(s2[0]);
		targetLon = Double.parseDouble(s2[1]);
	}	
	
	public String replaceSpaces(String s) {
		String Result = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ') {
				Result += "%20";
			} else {
				Result += s.charAt(i);
			}
		}
		return Result;
	}

	private String replaceEscapeCharacters(String stopName) {
		String revisedStopName = "";
		// TODO: Should we use a character array instead of repeatedly adding to
		// immutable string objects?
		for (int i = 0; i < stopName.length(); i++) {
			char c = stopName.charAt(i);
			// TODO: Should other characters be escaped also?
			if (c == '&') {
				revisedStopName += "&amp;";
			} else {
				revisedStopName += c;
			}
		}
		return revisedStopName;
	}

}
