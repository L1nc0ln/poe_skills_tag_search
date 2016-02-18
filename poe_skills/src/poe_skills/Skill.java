package poe_skills;

/**
 * Class for storing the information about a certain Path of Exile skill
 * @author linc
 *
 */
public class Skill {

	private String skillName;
	private String[] attributes;
	private int primeTotal;
	private String skillDescription;
	
	/**
	 *  Initialises the variables of this class
	 * @param skillName the name ofthe skill
	 * @param attributes the list of tags that the skill has
	 * @param primeTotal the total of all the primes that go with the tags of this skill multiplied with each other
	 * @param skillDescription the description of the skill, taken from http://pathofexile.gamepedia.com/
	 */
	public Skill(final String skillName, final String[] attributes, final int primeTotal,
			final String skillDescription){
		this.skillName = skillName;
		this.attributes = new String[10];
		this.attributes = attributes;
		this.primeTotal = primeTotal;
		this.skillDescription = skillDescription;
	}
	
	/**
	 * 
	 * @return the name of this skill
	 */
	public String getSkillName() {
		return skillName;
	}

	/**
	 * 
	 * @return Array of String with all the tags/attributes that belong to this skill
	 */
	public String[] getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @return the Multiplicative of all the primes that go with an attribute of this skill
	 */
	public int getPrimeTotal() {
		return primeTotal;
	}

	/**
	 * 
	 * @return the description of this skill as taken from http://pathofexile.gamepedia.com/
	 */
	public String getSkillDescription() {
		return skillDescription;
	}

	@Override
	/**
	 * compares this object to another. Deciding factors for false are
	 * a) is the object null?
	 * b) is the object not an object from this class?
	 * c) is the skillName different? SkillName is used since it is unique for each skill
	 * d) if all of the above do not apply the result is true, else false
	 */
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		} else if(!obj.getClass().equals(Skill.class)){
			return false;
		} else if(!((Skill)obj).getSkillName().equals(this.skillName)){
			return false;
		} else return true;
	}
	
	
}
