package RapidRider;

import java.lang.Math;

public class simpleLoc {
	
	private float myLat;
	private float myLon;

	
	public simpleLoc() {
		myLat = Float.NaN;
		myLon = Float.NaN;
	}
	
	public simpleLoc(double lat, double lon) {
		myLat = (float) lat;
		myLon = (float) lon;

	}
	
	public float getLat() {
		return myLat;
	}
	
	public float getLon() {
		return myLon;
	}
	
	public void setLat(double lat) {
		myLat = (float) lat;
	}
	
	public void setLon(double lon) {
		myLon = (float) lon;
	}
	
	public String printLoc() {
		return String.valueOf(myLat) + ", " + String.valueOf(myLon);
	}

	public float DistanceTo(simpleLoc tar) {
		//float xChange, yChange;
/*
		 * =60*180/PI()*ACOS((SIN(RADIANS(Pos1 Lat)) * SIN(RADIANS(Pos2 Lat)))
+ (COS(RADIANS(Pos1 Lat)) * COS(RADIANS(Pos2 Lat)) *
COS(RADIANS(Pos2 Long) - RADIANS(Pos1 Long))))

		double dist =
			60 * 180 / Math.PI * (1/Math.cos((Math.sin(Math.toRadians(myLat)) * Math.sin(Math.toRadians(targetPoint.getLat())))
					+ (Math.cos(Math.toRadians(myLat)) * Math.cos(Math.toRadians(targetPoint.getLat())) *
					Math.cos(Math.toRadians(targetPoint.getLon()) - Math.toRadians(myLon)))) );
							
		System.out.println(dist);
		
//		double x = Math.sqrt( (myLat - targetPoint.getLat())*(myLat - targetPoint.getLat()) + 
//				(myLon - targetPoint.getLon())*(myLon - targetPoint.getLon()));
*/		
		//xChange = Math.abs(myLat) - Math.abs(tar.getLat());
		//yChange = Math.abs(myLon) - Math.abs(tar.getLon());
		
		double dist = Math.sqrt( 
			 ( (Math.abs(myLat) - Math.abs(tar.getLat()) ) * ( Math.abs(myLat) - Math.abs(tar.getLat()) ) ) +
			 ( (Math.abs(myLon) - Math.abs(tar.getLon()) ) * ( Math.abs(myLon) - Math.abs(tar.getLon()) ) )
			 );
		
		return (float) dist;
	}
}
