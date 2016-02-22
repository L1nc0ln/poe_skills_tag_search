package poe_skills;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class for transforming the skills in the ini file into a list of Skills
 * that can be used further
 * @author linc
 *
 */
public class InitReader {
	
	private final int FIRST_ARGUMENT = 0;
	private final int FIRST_ARRAY_INDEX = 0;
	private final int SECOND_ARGUMENT = 1;
	private final int MAKE_LAST_ARRAY_INDEX = 1;
	private final int SPLIT_TWO_PARTS = 2;
	private final int NUMBER_OF_CHARS_BEFORE_NEWLINE = 140;
	private File initFile;
	private SkillFilter skillFilter;

	/**
	 * assings the path to the skills.ini file to the variable, assigns the skillFilter
	 * @param skillFilter the skillFilter to use, needed for knowing which criterium belongs to which prime
	 */
	public InitReader(SkillFilter skillFilter){
		initFile = new File(getClass().getClassLoader().getResource("").getPath() + "poe_skills/skills.ini");
		this.skillFilter = skillFilter;
	}
	
	/**
	 * reads the skills.ini file and transforms it into a list of Skills
	 * @return the list of skills obtained from the skills.ini file
	 */
	public ArrayList<Skill> read(){
		ArrayList<Skill> allSkills = new ArrayList<>(SkillMain.MAX_NUMBER_OF_SKILLS);
		int arrayPosition = FIRST_ARRAY_INDEX;
		Skill createSkill;
		try(BufferedReader reader = new BufferedReader(new FileReader(initFile))){
			String activeLine = reader.readLine();
			while(activeLine != null){
				String[] splitParts = activeLine.split("\\|", SPLIT_TWO_PARTS);
				String skillName = splitParts[FIRST_ARGUMENT];
				String[] attributes = splitParts[SECOND_ARGUMENT].split("\\|", FIRST_ARGUMENT);
				String[] lastAttributeSplit = attributes[attributes.length - MAKE_LAST_ARRAY_INDEX].split("\\?");
				String skillDescription = lastAttributeSplit[SECOND_ARGUMENT];
				skillDescription = insertNewlines(skillDescription);
				attributes[attributes.length - MAKE_LAST_ARRAY_INDEX] = lastAttributeSplit[FIRST_ARGUMENT];
				ArrayList<String> attributeList = makeArrayList(attributes);
				createSkill = new Skill(skillName, attributes, skillFilter.getFilterValue(attributeList),
						skillDescription);
				allSkills.add(createSkill);
				arrayPosition++;
				activeLine = reader.readLine();
			}
			return allSkills;
		} catch (IOException | NoSuchFilterCriteriumException e) {
			System.out.println("ERROR while reading line: " + arrayPosition);
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Adds a newline after a set amount of characters but does so only when there is a
	 * space. So we start at the index from which we consider the string too long and then look
	 * for the next space and insert a newline there. This is done until the rest of the string is no
	 * longer than the set amount of characters in a line
	 * @param skillDescription the string that should get newlines if it is too long
	 * @return the string with newlines if it was too long
	 */
	private String insertNewlines(String skillDescription){
		if(skillDescription.length() > NUMBER_OF_CHARS_BEFORE_NEWLINE){
			int numberOfLines = skillDescription.length()/NUMBER_OF_CHARS_BEFORE_NEWLINE + 1;
			int offset = 1;
			for(int counter = 1; counter < numberOfLines; counter++){
				char charAtCurrentIndex = skillDescription.charAt(NUMBER_OF_CHARS_BEFORE_NEWLINE*counter);
				while(charAtCurrentIndex != ' ' && (NUMBER_OF_CHARS_BEFORE_NEWLINE*counter + offset) < skillDescription.length()){
					charAtCurrentIndex = skillDescription.charAt(NUMBER_OF_CHARS_BEFORE_NEWLINE*counter + offset);
					offset++;
				}
				skillDescription = skillDescription.substring(0, NUMBER_OF_CHARS_BEFORE_NEWLINE*counter + offset) + "\n" 
						+ skillDescription.substring(NUMBER_OF_CHARS_BEFORE_NEWLINE*counter + offset);
				offset = 1;
			}
			return skillDescription;
		} else return skillDescription;
	}
	
	/**
	 * function to make an ArrayList<String> out of a  Stringarray
	 * @param attributes the Stringarray to be converted
	 * @return the converted Stringarray as an ArrayList<String>
	 */
	private ArrayList<String> makeArrayList(String[] attributes){
		ArrayList<String> attributeList = new ArrayList<>();
		for(String attribute: attributes){
			attributeList.add(attribute);
		}
		return attributeList;
	}
	
}
