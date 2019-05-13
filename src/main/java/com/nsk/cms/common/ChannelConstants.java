package com.nsk.cms.common;

import java.util.Set;
import java.util.TreeSet;

public class ChannelConstants {
	public static final int ch0 = 0;
	public static final int ch1 = 1;
	public static final int ch2 = 2;
	public static final int ch3 = 3;
	public static final int ch4 = 4;
	public static final int ch5 = 5;
	public static final int ch6 = 6;
	public static final int ch7 = 7;
	public static final int ch8 = 8;
	public static final int ch9 = 9;
	public static final int ch10 = 10;
	public static final int ch11 = 11;
	public static final int ch12 = 12;
	public static final int ch13 = 13;
	public static final int ch14 = 14;
	public static final int ch15 = 15;
	public static final int ch16 = 16;
	public static final int ch17 = 17;
	public static final int ch18 = 18;
	public static final int ch19 = 19;
	public static final int ch20 = 20;
	public static final int ch21 = 21;
	public static final int ch22 = 22;
	

	public static Set<Integer> allChannels = new TreeSet<>();
	
	public static Set<Integer> vibrationDataChannels = new TreeSet<>();
	
	public static Set<Integer> temperatureDataChannels = new TreeSet<>();
	
	public static Set<Integer> vibrationWaveChannels = new TreeSet<>();
	
	public static Set<Integer> loadDataChannels = new TreeSet<>();
	
	public static Set<Integer> speedDataChannels = new TreeSet<>();
	
	public static Set<Integer> dataChannels = new TreeSet<>();
	
	static
	{
		temperatureDataChannels.add(ch0);
		temperatureDataChannels.add(ch1);
		temperatureDataChannels.add(ch2);
		temperatureDataChannels.add(ch3);
		temperatureDataChannels.add(ch4);
		temperatureDataChannels.add(ch5);
		temperatureDataChannels.add(ch6);
		temperatureDataChannels.add(ch7);
		temperatureDataChannels.add(ch8);
		temperatureDataChannels.add(ch9);
		temperatureDataChannels.add(ch10);
		temperatureDataChannels.add(ch11);
		temperatureDataChannels.add(ch12);
		
		speedDataChannels.add(ch13);
		speedDataChannels.add(ch14);
		
		loadDataChannels.add(ch15);
		loadDataChannels.add(ch16);
		loadDataChannels.add(ch17);
		loadDataChannels.add(ch18);
		
		vibrationDataChannels.add(ch19);
		vibrationDataChannels.add(ch20);
		
		vibrationWaveChannels.add(ch21);
		vibrationWaveChannels.add(ch22);
		
		dataChannels.add(ch0);
		dataChannels.add(ch1);
		dataChannels.add(ch2);
		dataChannels.add(ch3);
		dataChannels.add(ch4);
		dataChannels.add(ch5);
		dataChannels.add(ch6);
		dataChannels.add(ch7);
		dataChannels.add(ch8);
		dataChannels.add(ch9);
		dataChannels.add(ch10);
		dataChannels.add(ch11);
		dataChannels.add(ch12);
		dataChannels.add(ch13);
		dataChannels.add(ch14);
		dataChannels.add(ch15);
		dataChannels.add(ch16);
		dataChannels.add(ch17);
		dataChannels.add(ch18);
		dataChannels.add(ch19);
		dataChannels.add(ch20);
		
		allChannels.add(ch0);
		allChannels.add(ch1);
		allChannels.add(ch2);
		allChannels.add(ch3);
		allChannels.add(ch4);
		allChannels.add(ch5);
		allChannels.add(ch6);
		allChannels.add(ch7);
		allChannels.add(ch8);
		allChannels.add(ch9);
		allChannels.add(ch10);
		allChannels.add(ch11);
		allChannels.add(ch12);
		allChannels.add(ch13);
		allChannels.add(ch14);
		allChannels.add(ch15);
		allChannels.add(ch16);
		allChannels.add(ch17);
		allChannels.add(ch18);
		allChannels.add(ch19);
		allChannels.add(ch20);
		allChannels.add(ch21);
		allChannels.add(ch22);
	}
	
//	range[0]:+/- 5 V
	public static final int mode_PM5 = 0;
//	range[1]:+/- 2.5 V
	public static final int mode_PM2DOT5 = 1;
//	range[2]:+/- 1.25 V
	public static final int mode_PM1DOT25 = 2;
//	range[3]:+/- 625 mV
	public static final int mode_PM0DOT625 = 3;
//	range[4]:+/- 10 V
	public static final int mode_PM10 = 4;
//	range[5]:0 ~ 10 V
	public static final int mode_0TO10 = 5;
//	range[6]:0 ~ 5 V
	public static final int mode_0TO5 = 6;
//	range[7]:0 ~ 2.5 V
	public static final int mode_0TO2DOT5 = 7;
//	range[8]:0 ~ 1.25 V
	public static final int mode_0TO1DOT25 = 7;
}
