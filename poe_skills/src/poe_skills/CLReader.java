package poe_skills;

import java.util.Scanner;

/**
 * This class runs a scanner on the command line, waits for input and
 * manages all actions depending on the user input
 * @author linc
 *
 */
public class CLReader {
	
	private final int OFFSET_ADD = 4;
	private final int OFFSET_DESCRIBE = 9;
	private final int OFFSET_REMOVE = 7;
	private Scanner scanner = new Scanner(System.in);
	private String lastCommand;
	private SkillFilter skillFilter;
	
	/**
	 * Initialises the variables of this class, loads all skills from the skills.ini
	 */
	public CLReader(SkillFilter skillFilter){
		this.skillFilter = skillFilter;
		
	}
	
	/**
	 * starts the scanner for the command line and listens to input
	 * for what a specific input does type help in the command line when
	 * the program is running
	 */
	public void run(){
		System.out.println("Welcome to the poe_skills tool, for help and commands type \"help\"");
		lastCommand = scanner.nextLine();
		while(!lastCommand.equals("quit")){
			if(lastCommand.startsWith("add")){
				skillFilter.addCriterium(lastCommand.substring(OFFSET_ADD));
			} else if(lastCommand.equals("showSkills")){
				skillFilter.showSkills();
			} else if(lastCommand.startsWith("describe")){
				skillFilter.describeSkill(lastCommand.substring(OFFSET_DESCRIBE));
			} else if(lastCommand.equals("reset")){
				skillFilter.resetFilter();
			} else if(lastCommand.startsWith("remove")){
				skillFilter.removeCriterium(lastCommand.substring(OFFSET_REMOVE));	
			} else if(lastCommand.equals("showCriteria")){
				skillFilter.showCriteria();
			} else if(lastCommand.equals("help")){
				skillFilter.printHelp();
			}
			lastCommand = scanner.nextLine();
		}
	}

}
