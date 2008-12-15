

import junit.framework.TestCase;

public class RapidRiderServletTest extends TestCase {

	// TODO Figure out how to test methods in the RapidRiderServlet class. I
	// don't know how to create an instance of the RapidRiderServlet class
	// without getting an exception. And making the methods that I want to test
	// static does not help.

	RapidRiderServlet servlet;

	public void setUp() {
		// servlet = new RapidRiderServlet();
	}

	public void testReplaceSpaces() {
		// assertEquals("%20east%20town%20%20Grand%20Rapids,%20MI",
		// RapidRiderServlet.replaceSpaces(" east town Grand Rapids, MI"));
		// assertEquals("ottawa%20high%20school%20Grand%20Rapids,%20MI",
		// RapidRiderServlet
		// .replaceSpaces("ottawa high school Grand Rapids, MI"));
		// assertEquals("woodland%20mall%20Grand%20Rapids,%20MI",
		// RapidRiderServlet
		// .replaceSpaces("woodland mall Grand Rapids, MI"));
	}

	public void testReplaceEscapeCharacters() {
		// assertEquals("Burton &amp; Breton", servlet
		// .replaceEscapeCharacters("Burton & Breton"));
	}

}
