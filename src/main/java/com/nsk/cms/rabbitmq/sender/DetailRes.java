package com.nsk.cms.rabbitmq.sender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailRes {
	
	boolean isSuccess;
	
   String errMsg;
}
