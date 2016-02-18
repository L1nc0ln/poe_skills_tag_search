package poe_skills;

import java.util.ArrayList;
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
	private InitReader initReader;
	private ArrayList<String> activeFilterCriteria = new ArrayList<>();
	private ArrayList<Skill> allSkills;
	
	/**
	 * Initialises the variables of this class, loads all skills from the skills.ini
	 */
	public CLReader(){
		skillFilter = new SkillFilter();
		initReader = new InitReader(skillFilter);
		allSkills = new ArrayList<>(SkillMain.MAX_NUMBER_OF_SKILLS);
		allSkills = initReader.read();
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
				addCriterium(lastCommand.substring(OFFSET_ADD));
			} else if(lastCommand.equals("showSkills")){
				showSkills();
			} else if(lastCommand.startsWith("describe")){
				describeSkill(lastCommand.substring(OFFSET_DESCRIBE));
			} else if(lastCommand.equals("reset")){
				activeFilterCriteria = new ArrayList<>();
			} else if(lastCommand.startsWith("remove")){
				removeCriterium(lastCommand.substring(OFFSET_REMOVE));	
			} else if(lastCommand.equals("showCriteria")){
				showCriteria();
			} else if(lastCommand.equals("help")){
				printHelp();
			}
			lastCommand = scanner.nextLine();
		}
	}
	
	/**
	 * prints all Skills that are in the parameter skillList
	 * Form is skillname: attribute1 attribute2 ...
	 * @param skillList
	 */
	private void printSkillList(ArrayList<Skill> skillList){
		System.out.println("-----------------------------------");
		for(Skill skill: skillList){
			System.out.print(skill.getSkillName() + ": ");
			for(String attribute: skill.getAttributes()){
				System.out.print(attribute + " ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * Prints the information about the skill with the name skillName
	 * Form is skillName: attribute1 attribute1 ...
	 * skillDescription
	 * Link to wiki
	 * @param skillName the name of the skill whose information should be printed
	 */
	private void describeSkill(String skillName){
		for(Skill skill: allSkills){
			if(skillName.equals(skill.getSkillName())){
				System.out.print(skillName + ": ");
				for(String attribute: skill.getAttributes()){
					System.out.print(attribute + " ");
				}
				System.out.println("\n" + skill.getSkillDescription());
				System.out.println(SkillMain.POE_WIKI_ADRESS + skill.getSkillName().replace(" ", "_"));
				break;
			}
		}
	}
	
	/**
	 * adds all criteria contained in the argument part to the
	 * list of active filter criteria
	 * @param argumentPart
	 */
	private void addCriterium(String argumentPart){
		String[] arguments = argumentPart.split(" ");
		for(String argument: arguments){
			activeFilterCriteria.add(argument);
		}
	}
	
	/**
	 * removes all criteria contained in the argument part from
	 * the list of active filter criteria
	 * @param argumentPart
	 */
	private void removeCriterium(String argumentPart){
		String[] arguments = argumentPart.split(" ");
		for(String argument: arguments){
			activeFilterCriteria.remove(argument);
		}
	}
	
	/**
	 * retrieves all skills matching the current active filter criteria and
	 * the prints the list with the printSkillList function
	 */
	private void showSkills(){
		ArrayList<Skill> filteredSkills;
		try {
			filteredSkills = skillFilter.filter(allSkills, activeFilterCriteria);
			if(!filteredSkills.isEmpty()){
				printSkillList(filteredSkills);
			} else{
				System.out.println("No skills matching filtercriteria found. Criteria are: ");
				System.out.println(activeFilterCriteria);
			}
		} catch (NoSuchFilterCriteriumException e) {
			System.out.println("Could not find a criterium named " + e.getMissingCriterium());
		}
	}
	
	/**
	 * prints the list of currently active filter criteria
	 */
	private void showCriteria(){
		System.out.println(activeFilterCriteria);
	}
	
	/**
	 * prints the help dialog
	 */
	private void printHelp(){
		System.out.println(String.format("%-25s %s", "quit", "ends the program"));
		System.out.println(String.format("%-25s %s", "remove xxx yyy zzz", "removes xxx, yyy and zzz from the filter criteria"));
		System.out.println(String.format("%-25s %s", "add xxx yyy zzz",
				"adds xxx, yyy and zzz to the filter criteria"));
		System.out.println(String.format("%-25s %s", "reset", "removes all filter criteria"));
		System.out.println(String.format("%-25s %s", "showSkills", "prints all skills fitting the current criteria"));
		System.out.println(String.format("%-25s %s", "describe skillname", "prints the description of the skill with name skillname"));
	}

}
