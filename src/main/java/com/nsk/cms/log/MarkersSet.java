package com.nsk.cms.log;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
 
public class MarkersSet {
 
	public static final Marker DB = MarkerFactory.getMarker("dblog");  //dblog就是上面MarkerFilter里的标记
 
}