package com.nsk.cms.manager;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface CmsManager {

	void deviceAcquisitionStartAction(String ip);

	boolean deviceConnectAction(String ip);
	
}
