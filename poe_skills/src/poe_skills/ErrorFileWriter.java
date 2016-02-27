package poe_skills;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class ErrorFileWriter {
	

	public static void logError(String message){
		File errorFile = new File(ErrorFileWriter.class.getClassLoader().getResource("").getPath() + "errorLogs/errorFile.log");
		if(!errorFile.exists()){
			try {
				new File(ErrorFileWriter.class.getClassLoader().getResource("").getPath() + "errorLogs").mkdirs();
				errorFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try{
			LocalDateTime timeNow = LocalDateTime.now();
			String logMessage = "\n" + "-----------------------------------------------------------------------------------\n" 
					+ "[ ERROR ] " + timeNow.getDayOfMonth() + "." + timeNow.getMonthValue() + "." + timeNow.getYear() + 
					" " + timeNow.getHour() + ":" + timeNow.getMinute() + ":" + timeNow.getSecond() +  " : " + message;
			Files.write(Paths.get(errorFile.getPath()), logMessage.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
