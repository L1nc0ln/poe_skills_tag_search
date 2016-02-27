package poe_skills;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * can filter a list of Path of Exile skills based on a list of criteria
 * contains the mapping criteria -> prime numbers
 * for the explanation of how the filtering process works see the filter function
 * @author linc
 *
 */
public class SkillFilter {

	private final int[] primeNumbers = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113};
	private HashMap<String, Integer> attribute_to_prime;
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
	 * @throws NoSuchFilterCriteriumException if a criterium can not be assigned a prime number
	 */
	public ArrayList<Skill> filter(ArrayList<Skill> toFilter, ArrayList<String> criteria) throws NoSuchFilterCriteriumException{
		int filterValue = getFilterValue(criteria);
		ArrayList<Skill> resultSet = new ArrayList<>();

		for(Skill currentSkill: toFilter){
			if(currentSkill.getPrimeTotal()%filterValue == 0){
				resultSet.add(currentSkill);
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
	 * @throws NoSuchFilterCriteriumException if a criterium can not be assigned a prime number
	 */
	public ArrayList<Skill> filter() throws NoSuchFilterCriteriumException{
		return filter(allSkills, activeFilterCriteria);
	}
	
	/**
	 * calculates the combined value of the criteria in the Array
	 * 
	 * @param criteria the criteria searched for
	 * @return value based on the criteria
	 * @throws NoSuchFilterCriteriumException if a criterium can not be matched to a prime
	 */
	public int getFilterValue(ArrayList<String> criteria) throws NoSuchFilterCriteriumException{
		int result = 1;
		for(String criterium: criteria){
			try{
				result = result * attribute_to_prime.get(criterium);
			} catch(NullPointerException e){
				throw new NoSuchFilterCriteriumException(criterium);
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param criterium the criterium that we want the corresponding prime from
	 * @return the corresponding prime to the criterium
	 * @throws NoSuchFilterCriteriumException
	 */
	public int getPrime(String criterium) throws NoSuchFilterCriteriumException{
		try{
			return attribute_to_prime.get(criterium);
		} catch(NullPointerException e){
			throw new NoSuchFilterCriteriumException(criterium);
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
	public void removeCriterium(String argumentPart){
		String[] arguments = argumentPart.split(" ");
		for(String argument: arguments){
			activeFilterCriteria.remove(argument);
		}
	}
	
	/**
	 * retrieves all skills matching the current active filter criteria and
	 * the prints the list with the printSkillList function
	 */
	public void showSkills(){
		ArrayList<Skill> filteredSkills;
		try {
			filteredSkills = filter(allSkills, activeFilterCriteria);
			if(!filteredSkills.isEmpty()){
				printSkillList(filteredSkills);
			} else{
				System.out.println("No skills matching filtercriteria found. Criteria are: ");
				System.out.println(activeFilterCriteria);
			}
		} catch (NoSuchFilterCriteriumException e) {
			ErrorFileWriter.logError("Could not find a criterium named " + e.getMissingCriterium());
			System.exit(0);
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
		attribute_to_prime = new HashMap<>(SkillMain.tags.length);
		for(int counter = 0; counter < SkillMain.tags.length; counter++){
			attribute_to_prime.put(SkillMain.tags[counter], primeNumbers[counter]);
		}
		allSkills = new ArrayList<>(SkillMain.MAX_NUMBER_OF_SKILLS);
		InitReader initReader = new InitReader(this);
		allSkills = initReader.read();
	}
	
}
