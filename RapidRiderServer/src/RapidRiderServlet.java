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
 * @web.servlet name="Monopoly" display-name="Name for Monopoly" description="Description for Monopoly"
 * @web.servlet-mapping url-pattern="/Monopoly"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
public class RapidRiderServlet extends HttpServlet {

	Connection conn; // holds database connection

	Statement stmt; // holds SQL statement

	String state_code; // holds state code entered by user

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		out.println("<?xml version=\"1.0\"?>");
		out.println("<rapidrider>");
		try {
			Class.forName("org.postgresql.Driver");

			conn = DriverManager.getConnection("jdbc:postgresql:rapidrider", "postgres", "bjarne");
			stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT stopid, stop_name, latitude, longitude FROM busstops");

			if (res != null)
				while (res.next()) {
					String stopID = res.getString(1);
					String stopName = res.getString(2), RStopName = "";		// Original stop name and Revised
					String latitude = res.getString(3);
					String longitude = res.getString(4);
					
					/*
					 * This portion of the code exists to protect the XML. 
					 * If an ampersand (&) is encountered, it breaks the XML document.
					 * This searches for ampersands and replaces them with the 
					 * unicode equivilent (&amp;)
					 */
								
					for( int i = 0; i < stopName.length(); i++ ) {
						char c = stopName.charAt(i);
						if( c == '&') {
							RStopName += "&amp;";
						} else {
							RStopName += c;
						}
					}
					
					out.println("<busstop>");
					out.println("<stopID>" + stopID + "</stopID>");
					out.println("<stopName>" + RStopName + "</stopName>");
					out.println("<latitude>" + longitude + "</latitude>");
					out.println("<longitude>" + latitude + "</longitude>");
					out.println("</busstop>");
				}
			res.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			out.println("error in JDBC access: " + e.getClass() + " : " + e.getMessage());
		}
		out.println("</rapidrider>");
	}

}
