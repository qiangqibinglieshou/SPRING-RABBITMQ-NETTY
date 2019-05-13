package com.nsk.cms.rabbitmq.handler;

import com.nsk.cms.manager.CmsManager;
import com.nsk.cms.common.util.SpringUtil;
import com.nsk.cms.ptotobuf.proto.AcquisitionDataProto;
import com.nsk.cms.ptotobuf.proto.AcquisitionWaveProto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class RabbitMqHandler{
	
	private String ip;
	
	private CmsManager cmsManager;
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public RabbitMqHandler(String ip) {
		super();
		this.ip = ip;
		this.cmsManager = SpringUtil.getBean(CmsManager.class);
	}
	
	public void handleMessage(AcquisitionDataProto.AcquisitionData acquisitionData) {
		try {
			
		}catch (Exception e) {
			log.error("{}",e.getMessage());
		}
	}
	
	public void handleMessage(AcquisitionWaveProto.AcquisitionWave acquisitionWave) {
		
		try {
			
		}catch(Exception e){
			log.error("{}",e.getMessage());
		}
	}

}
