import java.io.IOException;
import java.io.PrintWriter;
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

	Connection conn; // holds database connection

	Statement stmt; // holds SQL statement
	
	// TODO: Add documentation?
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		if (req.getQueryString() != null) {				// HERE IS WHERE WE DECIED IF WE ARE GOING OLD / NEW SCHOOL
			String queryString = req.getQueryString();	// new school requests include a Query String ...vlet/RapidRider?Field=Value
			//if (queryString.substring(0, 4) == "Curr")// We need to decied what method to invoce based on the 
				out.println(/*"Congradulations, you are:"+*/queryString/*(.substring(11)*/);
		} else {										//old school simply omits the query stirng
		//String reqTypes = req.getHeaderNames().toString();
		//String reqType = req.getHeader("reqType");
		out.println("<?xml version=\"1.0\"?>");
		out.println("<rapidrider>");
		//out.println(reqTypes);
		//out.println(reqType);
		try {
			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection("jdbc:postgresql:rapidrider",
					"postgres", "bjarne");
			stmt = conn.createStatement();
			ResultSet res = stmt
					.executeQuery("SELECT stopid, stop_name, latitude, longitude FROM busstops");

			if (res != null)
				while (res.next()) {
					String stopID = res.getString(1);
					String stopName = res.getString(2);
					String latitude = res.getString(3);
					String longitude = res.getString(4);
					
					String revisedStopName = replaceEscapeCharacters(stopName);

					out.println("<busstop>");
					out.println("<stopID>" + stopID + "</stopID>");
					out.println("<stopName>" + revisedStopName + "</stopName>");
					out.println("<latitude>" + longitude + "</latitude>");
					out.println("<longitude>" + latitude + "</longitude>");
					out.println("</busstop>");
				}
			res.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			out.println("error in JDBC access: " + e.getClass() + " : "
					+ e.getMessage());
		}
		out.println("</rapidrider>"); }
	}/*
	public void getDestinationStop(stdestString) {
		try {
			URL site = new URL("http://tinygeocoder.com/create-api.php?q=");
			HttpURLConnection connection = (HttpURLConnection) site.openConnection(); //Connector.open(site + destinationAddress);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; JVM) ");
			connection.setRequestProperty("Pragma", "no-cache");
			connection.connect();
			
			BufferedReader reader = new BufferedReader( new InputStreamReader(connection.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null ) {
				System.out.println(line);
			}
*/
	
	
	
	/*
	 * This portion of the code protects the XML. Ampersands (&) break XML
	 * documents. This searches for ampersands and replaces them with the HTML
	 * equivalent (&amp;)
	 */
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
