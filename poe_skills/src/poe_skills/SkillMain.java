package poe_skills;

/**
 * contains some global variables, intialises the CLReader and lets it run
 * @author linc
 *
 */
public class SkillMain {

	public static final int MAX_NUMBER_OF_SKILLS = 260;
	public static final String POE_WIKI_ADRESS = "http://pathofexile.gamepedia.com/";
	
	public static void main(String[] args) {

		CLReader clReader = new CLReader();
		clReader.run();

	}

}
