package poe_skills;

import java.util.ArrayList;

/**
 * can filter a list of Path of Exile skills based on a list of criteria
 * contains the mapping criteria -> prime numbers
 * for the explanation of how the filtering process works see the filter function
 * @author linc
 *
 */
public class SkillFilter {

	//primes are not in order since the last primes will be used often - try to minimize the total
//	private final int[] primeNumbers = {2, 3, 5, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149,
//			151, 157, 163, 7, 11, 13, 17, 19, 23, 167, 173};
	private ArrayList<String> activeFilterCriteria = new ArrayList<>();
	private ArrayList<Skill> allSkills;


	/**
	 * Filters the list of all skills with the criteria. A little explanation about how this works:
	 * each criterium has an assigned prime number, and the Multiplicative of all the criteria is stored in the
	 * skill class for each skill. If you have a few criteria that you want to match you multiply the primes of the
	 * criteria and then divide the Multiplicative stored in the skill class by the Multiplicative of the
	 * criteria you want to match. If the remainder is 0 all the criteria you searched for are stored in the
	 * skill aswell. This works due to the nature of prime numbers to only be divisible by 1 and themselves with
	 * a remainder of 0.
	 * @param toFilter a list of skills you want to filter
	 * @param criteria the criteria you want to filter the list of skills by
	 * @return a list that contains the skills that matched the criteria
	 */
	public ArrayList<Skill> filter(ArrayList<Skill> toFilter, ArrayList<String> criteria){
		ArrayList<Skill> resultSet = new ArrayList<>();

		for(Skill currentSkill: toFilter){
			boolean containsAllCriteria = true;
			for(String criterium: criteria){
				boolean containsCriterium = false;
				for(String skillAttribute: currentSkill.getAttributes()){
					if(criterium.equals(skillAttribute)){
						containsCriterium = true;
					}
				}
				if(!containsCriterium){
					containsAllCriteria = false;
					break;
				} else{
					containsCriterium = false;
				}
			}
			if(containsAllCriteria){
				resultSet.add(currentSkill);
			} else{
				containsAllCriteria = true;
			}
		}
		return resultSet;
	}
	
	/**
	 * Filters the list of all skills with the criteria stored in the instance of this class. A little explanation about how this works:
	 * each criterium has an assigned prime number, and the Multiplicative of all the criteria is stored in the
	 * skill class for each skill. If you have a few criteria that you want to match you multiply the primes of the
	 * criteria and then divide the Multiplicative stored in the skill class by the Multiplicative of the
	 * criteria you want to match. If the remainder is 0 all the criteria you searched for are stored in the
	 * skill aswell. This works due to the nature of prime numbers to only be divisible by 1 and themselves with
	 * a remainder of 0.
	 * @returna list that contains the skills that matched the criteria stored in the instance of this class
	 */
	public ArrayList<Skill> filter(){
		return filter(allSkills, activeFilterCriteria);
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
	public void describeSkill(String skillName){
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
	public void addCriterium(String argumentPart){
		activeFilterCriteria.add(argumentPart);
	}
	
	/**
	 * removes all criteria contained in the argument part from
	 * the list of active filter criteria
	 * @param argumentPart
	 */
	public void removeCriterium(String argumentPart){
		activeFilterCriteria.remove(argumentPart);
	}
	
	/**
	 * retrieves all skills matching the current active filter criteria and
	 * the prints the list with the printSkillList function
	 */
	public void showSkills(){
		ArrayList<Skill> filteredSkills;
		filteredSkills = filter(allSkills, activeFilterCriteria);
		if(!filteredSkills.isEmpty()){
			printSkillList(filteredSkills);
		} else{
			System.out.println("No skills matching filtercriteria found. Criteria are: ");
			System.out.println(activeFilterCriteria);
		}
	}
	
	/**
	 * resets the filter to contain no active filtercriteria
	 */
	public void resetFilter(){
		activeFilterCriteria = new ArrayList<String>();
	}
	
	/**
	 * prints the list of currently active filter criteria
	 */
	public void showCriteria(){
		System.out.println(activeFilterCriteria);
	}
	
	/**
	 * prints the help dialog
	 */
	public void printHelp(){
		System.out.println(String.format("%-25s %s", "quit", "ends the program"));
		System.out.println(String.format("%-25s %s", "remove xxx yyy zzz", "removes xxx, yyy and zzz from the filter criteria"));
		System.out.println(String.format("%-25s %s", "add xxx yyy zzz",
				"adds xxx, yyy and zzz to the filter criteria"));
		System.out.println(String.format("%-25s %s", "reset", "removes all filter criteria"));
		System.out.println(String.format("%-25s %s", "showSkills", "prints all skills fitting the current criteria"));
		System.out.println(String.format("%-25s %s", "describe skillname", "prints the description of the skill with name skillname"));
	}
	
	/**
	 * fills the attribute_to_prime Map with values
	 */
	public SkillFilter(){
		allSkills = new ArrayList<>(SkillMain.MAX_NUMBER_OF_SKILLS);
		InitReader initReader = new InitReader();
		allSkills = initReader.read();
	}
	
}
