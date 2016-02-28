package poe_skills;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * contains some global variables, intialises the CLReader and lets it run
 * @author linc
 *
 */
public class SkillMain extends Application{

	public static final int MAX_NUMBER_OF_SKILLS = 260;
	public static final String POE_WIKI_ADRESS = "http://pathofexile.gamepedia.com/";
	public static final String[] tags = {"Dex", "Str", "Int",
		"Chaos", "Cold", "Fire", "Lightning",
		"Attack", "Cast", "Aura", "Curse", "Spell", "Warcry",
		"Bow", "Movement", "Projectile","Melee", "AoE", 
		"Mine", "Golem", "Minion", "Totem", "Trap",
		"Duration", "Trigger", "Support", "Vaal", "Chaining",
		"Frenzy", "Power", "Endurance",
		"Sword", "Axe", "Claw", "Dagger", "Staff", "Mace", "Unarmed", "Two Handed", "Dual Wield", "Wand", "Any"};
	private final String[] GROUP_DESCRIPTIONS = {"Attribute", "Element", "Modifier1", "Modifier2", "Minion", "Modifier3", "Charges", "Weapons"};
	private final int[] END_INDEX_OF_CHECKBOX_ROWS = {0, 3, 7, 14, 18, 23, 28, 31, 38, 42};
	private final int NUMBER_OF_CHECKBOX_ROWS = END_INDEX_OF_CHECKBOX_ROWS.length - 1;
	private final int CHECKBOXES_START_COL = 2;
	private final int NUMBER_NORMAL_DESCRIPTIONS = 6;
	private final int OFFSET_CUSTOM_TAGS = 1;
	private final int NUMBER_OF_COLUMNS = 9;
	private final int COLUMN_WIDTH = 100;
	private final int COLUMN_SEPARATOR_WIDTH = 20;
	private final int H_GAP = 10;
	private final int V_GAP = 10;
	private final int PADDING_ALL = 25;
	private final int ACCORDION_ROW = END_INDEX_OF_CHECKBOX_ROWS.length + 2;
	private final int ACCORDION_START_COL = 0;
	private final int ACCORDION_COL_SPAN = 10;
	private final int ACCORDION_ROW_SPAN = 1;
	private final int SKILLLIST_SCROLLPANE_PREF_HEIGHT = 800;
	private final int SKILLLIST_SCROLLPANE_PREF_WIDTH = 1200;
	private final int SEPARATOR_COL_SPAN = 10;
	private final int SEPARATOR_ROW_SPAN = 1;
	private final int SEPARATOR_COL_START = 0;
	private final int SEPARATOR_TAG_BUTTON_ROW = 6;
	private final int SEPARATOR_CUSTOM_TAG_BUTTON_ROW = END_INDEX_OF_CHECKBOX_ROWS.length + 1;
	private final int SEPARATOR_VERTICAL_COL_SPAN = 1;
	private final int SEPARATOR_VERTICAL_ROW_SPAN = END_INDEX_OF_CHECKBOX_ROWS.length + 1;
	private final int SEPARATOR_VERTICAL_COL = 1;
	private final int SEPARATOR_VERTICAL_START_ROW = 0;
	private final int WINDOW_HEIGHT = 600;
	private final int WINDOW_WIDTH = 1200;
	private TitledPane[] skillPanes;
	private ScrollPane skillListScrollPane;
	private Accordion skillAccordion;
	private CheckBox[] tagCheckBoxes;
	//private CheckBox[] disabledCheckBoxes;
	
	public static void main(String[] args) {
		
		launch(args);
		//clReader.run();

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SkillFilter skillFilter = new SkillFilter();
		
		Scene scene;
		primaryStage.setTitle("PoE Skills Tag Search");
        
		//Setup of the grid
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(H_GAP);
		grid.setVgap(V_GAP);
		grid.setPadding(new Insets(PADDING_ALL));
		//set the width of all columns to COLUMN_WIDTH, except the second which countains only the separator
		for(int counter = 0; counter < NUMBER_OF_COLUMNS; counter++){
			if(counter != 1){
				grid.getColumnConstraints().add(new ColumnConstraints(COLUMN_WIDTH));
			}
			else{
				grid.getColumnConstraints().add(new ColumnConstraints(COLUMN_SEPARATOR_WIDTH));
			}
		}
		
		//the descriptions at the side
		for(int counter = 0; counter < GROUP_DESCRIPTIONS.length; counter++){
			if(counter < NUMBER_NORMAL_DESCRIPTIONS){
				grid.add(new Label(GROUP_DESCRIPTIONS[counter]), 0, counter);
			} else {
				grid.add(new Label(GROUP_DESCRIPTIONS[counter]), 0, counter + OFFSET_CUSTOM_TAGS);
			}
		}
		
		tagCheckBoxes = new CheckBox[tags.length];
		
		//create and initialiase the Checkboxes
		for(int counter = 0; counter < tags.length; counter++){
			tagCheckBoxes[counter] = new CheckBox(tags[counter]);
		}
		
		//adds the checkboxes to the grid with their handlers. Two for loops since we have a different number
		//of checkboxes in each row
		//the if else is because the costum tags are seperated by a Seperator and hence need to be an additional row further down
		//than the rest
		int colCounter = CHECKBOXES_START_COL;
		for(int rowCounter = 0; rowCounter < NUMBER_OF_CHECKBOX_ROWS; rowCounter ++){
			if(rowCounter < NUMBER_NORMAL_DESCRIPTIONS){
				for(int counter = END_INDEX_OF_CHECKBOX_ROWS[rowCounter]; counter < END_INDEX_OF_CHECKBOX_ROWS[rowCounter + 1]; counter++){
					grid.add(tagCheckBoxes[counter], colCounter, rowCounter);
					addCheckBoxHandler(tags[counter], tagCheckBoxes[counter], skillFilter, grid);
					colCounter++;
				}
			} else{
				for(int counter = END_INDEX_OF_CHECKBOX_ROWS[rowCounter]; counter < END_INDEX_OF_CHECKBOX_ROWS[rowCounter + 1]; counter++){
					grid.add(tagCheckBoxes[counter], colCounter, rowCounter + OFFSET_CUSTOM_TAGS);
					addCheckBoxHandler(tags[counter], tagCheckBoxes[counter], skillFilter, grid);
					colCounter++;
				}
			}
			colCounter = CHECKBOXES_START_COL;
		}
		
		//adds our three separators
		Separator separatorTagsButton = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(separatorTagsButton, SEPARATOR_COL_START, SEPARATOR_TAG_BUTTON_ROW, SEPARATOR_COL_SPAN, SEPARATOR_ROW_SPAN);
		grid.add(separatorTagsButton, SEPARATOR_COL_START, SEPARATOR_TAG_BUTTON_ROW);
		Separator separatorCostumTags = new Separator(Orientation.HORIZONTAL);
		GridPane.setConstraints(separatorCostumTags, SEPARATOR_COL_START, SEPARATOR_CUSTOM_TAG_BUTTON_ROW, SEPARATOR_COL_SPAN, SEPARATOR_ROW_SPAN);
		grid.add(separatorCostumTags, SEPARATOR_COL_START, SEPARATOR_CUSTOM_TAG_BUTTON_ROW);
		Separator separatorVertical = new Separator(Orientation.VERTICAL);
		GridPane.setConstraints(separatorVertical, SEPARATOR_VERTICAL_COL, SEPARATOR_VERTICAL_START_ROW, SEPARATOR_VERTICAL_COL_SPAN, SEPARATOR_VERTICAL_ROW_SPAN);
		grid.add(separatorVertical, SEPARATOR_VERTICAL_COL, SEPARATOR_VERTICAL_START_ROW);
		
		//set scene and show the stage
		scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
		primaryStage.setScene(scene);
		
        primaryStage.show();
		
	}
	
	
	/**
	 * attaches the actions for ticking a checkbox. If the checkbox gets ticket calls the
	 * addCriterium Function to add the corresponding tag to the list, if the checkbox gets
	 * unticked calls the removeCriterium function to remove the tag
	 * @param tagName Name of the tag that belongs to the checkbox
	 * @param checkBox the checkbox that gets ticked/unticked
	 * @param skillFilter the instance of the skillFilter class responsible for storing the list of ticked tags
	 */
	private void addCheckBoxHandler(String tagName, CheckBox checkBox, SkillFilter skillFilter, GridPane grid){
		final String tagNameFinal = tagName;
		final CheckBox checkBoxFinal = checkBox;
		checkBoxFinal.selectedProperty().addListener(new ChangeListener<Boolean>() {
	        public void changed(ObservableValue<? extends Boolean> ov,
	            Boolean old_val, Boolean new_val) {
	        		if(new_val){
	        			skillFilter.addCriterium(tagNameFinal);
	        			//long before = System.nanoTime();
	        			searchSkills(grid, skillFilter);
	        			//long after = System.nanoTime();
	        			//System.out.println((after - before)/1000000);
	        		}
	        		else{
	        			skillFilter.removeCriterium(tagNameFinal);
	        			//long before = System.nanoTime();
	        			searchSkills(grid, skillFilter);
	        			//long after = System.nanoTime();
	        			//System.out.println((after - before)/1000000);
	        		}
	        }
	    });
	}
	
	
	/**
	 * Removes the accordion if there was one before, then gets the list of skills that fit
	 * the checked tags. Next all skills get added to an array with all their information
	 * like description, link to wiki. In the end it adds the array to the (new) accordion
	 * and then puts this accordion on the grid.
	 * @param grid the Grid we put our accordion with the skills on
	 * @param skillFilter the skillFilter with the information which boxes are ticked
	 */
	private void searchSkills(GridPane grid, SkillFilter skillFilter){
		if(skillListScrollPane != null){
			grid.getChildren().remove(skillListScrollPane);
		}
		skillAccordion = new Accordion();
		ArrayList<Skill> matchedSkills = new ArrayList<Skill>();
		matchedSkills = skillFilter.filter();
		disableUnavailableCheckBoxes(matchedSkills, skillFilter);
		skillPanes = new TitledPane[matchedSkills.size()];
		Skill currentSkill;
		for(int counter = 0; counter < matchedSkills.size(); counter++){
			currentSkill = matchedSkills.get(counter);
			skillPanes[counter] = currentSkill.getSkillPane();
		}
		skillAccordion.getPanes().addAll(skillPanes);
		skillListScrollPane = new ScrollPane(skillAccordion);
		skillListScrollPane.setPrefHeight(SKILLLIST_SCROLLPANE_PREF_HEIGHT);
		skillListScrollPane.setPrefWidth(SKILLLIST_SCROLLPANE_PREF_WIDTH);
		grid.add(skillListScrollPane, ACCORDION_START_COL, ACCORDION_ROW, ACCORDION_COL_SPAN, ACCORDION_ROW_SPAN);
	}
	
	
	public void disableUnavailableCheckBoxes(ArrayList<Skill> matchedSkills, SkillFilter skillFilter){
		for(int tagIndex = 0; tagIndex < tags.length; tagIndex++){
			boolean fitsASkill = false;
			for(Skill skill: matchedSkills){
				for(String skillAttribute: skill.getAttributes()){
					if(skillAttribute.equals(tags[tagIndex])){
						fitsASkill = true;
					}
				}
			}
			if(!fitsASkill && !tagCheckBoxes[tagIndex].isDisabled()){
				tagCheckBoxes[tagIndex].setDisable(true);
			} else if(fitsASkill && tagCheckBoxes[tagIndex].isDisabled()){
				tagCheckBoxes[tagIndex].setDisable(false);
			}
		}
	}

}
