package com.nsk.cms.log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.core.Filter;

public class SingleFilterBuilder {

	private static Map<String, MarkerFilter> filterMap = new ConcurrentHashMap<String, MarkerFilter>();

	public static MarkerFilter builderMarkerFilter(String key) {
		MarkerFilter filter = filterMap.get(key);
		if(filter != null) {
			;
		}else {
			filter = MarkerFilter.createFilter(key, Filter.Result.ACCEPT, Filter.Result.DENY);
			filterMap.put(key, filter);
		}
		return filter;
	}
}
