package it.cascino.time;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RunTime{
	private static Logger log;
	
	public static void main(String[] args){
		PropertyConfigurator.configure("logdir/log.properties");
		log = Logger.getLogger(RunTime.class);
		log.info("START");
		
		@SuppressWarnings("unused")
		Time time = new Time(args);
		
		log.info("STOP");
		System.exit(0);
	}
}
