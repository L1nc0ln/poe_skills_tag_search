package poe_skills;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * class for transforming the skills in the ini file into a list of Skills
 * that can be used further
 * @author linc
 *
 */
public class InitReader {
	
	private final int normalMaxSkillLevel = 30;
	private final int specialMaxSkillLevel = 10;
	private final int FIRST_ARGUMENT = 0;
	private final int FIRST_ARRAY_INDEX = 0;
	private final int SECOND_ARGUMENT = 1;
	private final int MAKE_LAST_ARRAY_INDEX = 1;
	private final int SPLIT_TWO_PARTS = 2;
	private final int NUMBER_OF_CHARS_BEFORE_NEWLINE = 140;
	private final int TABLE_COL = 0;
	private final int TABLE_ROW = 1;
	private final int TABLE_COL_SPAN = 8;
	private final int TABLE_ROW_SPAN = 1;
	private final int TABLE_PREF_WIDTH = 100;
	

	/**
	 * reads the skill_tables.ini file and transforms it into a list of Skills
	 * @return the list of skills obtained from the skill_tables.ini file
	 */
	public ArrayList<Skill> read(){
		File initFile = new File(getClass().getClassLoader().getResource("").getPath() + "poe_skills/skill_tables.ini");
		ArrayList<Skill> allSkills = new ArrayList<>(SkillMain.MAX_NUMBER_OF_SKILLS);
		int arrayPosition = FIRST_ARRAY_INDEX;
		String currentLine;
		String skillNameLine;
		Skill createSkill;
		try(BufferedReader reader = new BufferedReader(new FileReader(initFile))){
			currentLine = reader.readLine();
			while(currentLine != null){
				skillNameLine = currentLine;
				
				//Seperate all the information contained in the first line and process it
				//Information in the first line: Skillname, Attributes/Tags, Skill Description
				String[] splitParts = skillNameLine.split("\\|", SPLIT_TWO_PARTS);
				String skillName = splitParts[FIRST_ARGUMENT];
				String[] attributes = splitParts[SECOND_ARGUMENT].split("\\|", FIRST_ARGUMENT);
				String[] lastAttributeSplit = attributes[attributes.length - MAKE_LAST_ARRAY_INDEX].split("\\?");
				String skillDescription = lastAttributeSplit[SECOND_ARGUMENT];
				skillDescription = insertNewlines(skillDescription);
				attributes[attributes.length - MAKE_LAST_ARRAY_INDEX] = lastAttributeSplit[FIRST_ARGUMENT];
				ArrayList<String> attributeList = makeArrayList(attributes);
				
				currentLine = reader.readLine();
				
				//Second line: Headers for the table, what each column is about. Columns are seperated by a tab(\t)
				String[] headers = currentLine.split("\t");
				currentLine = reader.readLine();
				String[][] skillTable;
				//seperate case for enlighten, empower and enhance since those skills do not have as many levels as normal skills
				if(skillName.equals("Enlighten") || skillName.equals("Empower") || skillName.equals("Enhance")){
					skillTable = new String[specialMaxSkillLevel][headers.length];
					for(int rowCounter = 0; rowCounter < specialMaxSkillLevel; rowCounter++){
						String[] partsCurrentLine = currentLine.split("\t");
						for(int colCounter = 0; colCounter < headers.length; colCounter++){
							skillTable[rowCounter][colCounter] = partsCurrentLine[colCounter];
						}
						currentLine = reader.readLine();
					}
				} else{
					skillTable = new String[normalMaxSkillLevel][headers.length];
					for(int rowCounter = 0; rowCounter < normalMaxSkillLevel; rowCounter++){
						String[] partsCurrentLine = currentLine.split("\t");
						for(int colCounter = 0; colCounter < headers.length; colCounter++){
							skillTable[rowCounter][colCounter] = partsCurrentLine[colCounter];
						}
						currentLine = reader.readLine();
					}
				}
				
				//create the pane with all information and the table, we store that with each skill so we dont have to create
				//a new pane for each skill each time a box is checked. Should lower the response time but increases RAM usage
				TitledPane skillPane = new TitledPane();
				skillPane.setText(skillName);
				GridPane paneGrid = new GridPane();
				String skillTags = "Tags: ";
				for(String tag: attributeList){
					skillTags = skillTags + tag + "  ";
				}
				skillTags += "\n";
				paneGrid.add(new Text(skillTags + skillDescription), 0, 0);
				
				//since the amount of columns and the column names are different for each skill we cannot use the normal way
				//of creating a table with a Class. This is the only way I found of doing it when both amount and name of columns are unknown.
				//if you read this and know a better way please contact me :D
				ObservableList<String[]> data = FXCollections.observableArrayList();
				data.addAll(Arrays.asList(skillTable));
				TableView<String[]> table = new TableView<>();
				for (int i = 0; i < skillTable[0].length; i++) {
					TableColumn tc = new TableColumn(headers[i]);
					final int colNo = i;
					tc.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
					   return new SimpleStringProperty((p.getValue()[colNo]));
					}
					});
					tc.setPrefWidth(TABLE_PREF_WIDTH);
					table.getColumns().add(tc);
				}
				table.setItems(data);
				paneGrid.add(table, TABLE_COL, TABLE_ROW, TABLE_COL_SPAN, TABLE_ROW_SPAN);
				skillPane.setContent(paneGrid);
				skillPane.setExpanded(false);
				
				//finally create the skill and add it to the list
				createSkill = new Skill(skillName, attributes, skillDescription, skillPane);
				allSkills.add(createSkill);
				arrayPosition++;
			}
		} catch (IOException e) {
			ErrorFileWriter.logError("ERROR while reading line: " + arrayPosition);
			System.exit(0);
		}
		return allSkills;
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
