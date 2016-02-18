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

	private HashMap<String, Integer> attribute_to_prime;
	
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
	 * fills the attribute_to_prime Map with values
	 */
	public SkillFilter(){
		attribute_to_prime = new HashMap<>(30);
		attribute_to_prime.put("AoE", 2);
		attribute_to_prime.put("Attack", 3);
		attribute_to_prime.put("Aura", 5);
		attribute_to_prime.put("Bow", 7);
		attribute_to_prime.put("Cast", 11);
		attribute_to_prime.put("Support", 13);
		attribute_to_prime.put("Chaos", 17);
		attribute_to_prime.put("Cold", 19);
		attribute_to_prime.put("Curse", 23);
		attribute_to_prime.put("Duration", 29);
		attribute_to_prime.put("Fire", 29);
		attribute_to_prime.put("Golem", 31);
		attribute_to_prime.put("Lightning", 37);
		attribute_to_prime.put("Melee", 41);
		attribute_to_prime.put("Mine", 43);
		attribute_to_prime.put("Minion", 47);
		attribute_to_prime.put("Movement", 53);
		attribute_to_prime.put("Projectile", 59);
		attribute_to_prime.put("Spell", 61);
		attribute_to_prime.put("Totem", 67);
		attribute_to_prime.put("Trap", 71);
		attribute_to_prime.put("Trigger", 73);
		attribute_to_prime.put("Vaal", 79);
		attribute_to_prime.put("Warcry", 83);
		attribute_to_prime.put("Dex", 89);
		attribute_to_prime.put("Str", 97);
		attribute_to_prime.put("Int", 101);
		attribute_to_prime.put("Chaining", 103);
		//attribute_to_prime.put("", 107);
		//attribute_to_prime.put("", 109);
		//attribute_to_prime.put("", 113);
	}
	
}
