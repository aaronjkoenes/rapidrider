package test;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import rapidrider.DirectionsFetcher;

public class DirectionsFetcherTest extends TestCase {

	private DirectionsFetcher fetcher;

	public void setUp() {
		// This null is annoying.
		fetcher = new DirectionsFetcher(null);
	}

	public void testParseBusStopTagWithoutLocation() {
		KXmlParser parser = new KXmlParser();
		try {
			parser.setInput(new StringReader("<?xml version=\"1.0\""
					+ " encoding=\"UTF-8\"?>\n" + "<rapidrider>\n<busstop>\n"
					+ "<stopName>Calvin College &amp; Burton</stopName>\n"
					+ "</busstop>\n</rapidrider>"));
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "rapidrider");
			parser.nextTag();
			assertEquals("Calvin College & Burton", fetcher.parseBusStopTag(
					parser).getName());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testReplaceSpaces() {
		assertEquals("%20east%20town%20%20Grand%20Rapids,%20MI", fetcher
				.replaceSpaces(" east town  Grand Rapids, MI"));
		assertEquals("ottawa%20high%20school%20Grand%20Rapids,%20MI", fetcher
				.replaceSpaces("ottawa high school Grand Rapids, MI"));
		assertEquals("woodland%20mall%20Grand%20Rapids,%20MI", fetcher
				.replaceSpaces("woodland mall Grand Rapids, MI"));
	}
}
