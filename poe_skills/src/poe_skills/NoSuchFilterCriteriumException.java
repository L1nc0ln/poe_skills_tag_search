package poe_skills;

/**
 * Exception that shows that a criterium could not be found in the list that
 * assings a prime to a criterium
 * @author linc
 *
 */
public class NoSuchFilterCriteriumException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4370232170026376029L;
	private String missingCriterium;
	
	/**
	 * 
	 * @param criterium the criterium that could not be found
	 */
	public NoSuchFilterCriteriumException(String criterium) {
		missingCriterium = criterium;
	}

	@Override
	public String getMessage() {
		return "The following criterium could not be found: " + missingCriterium + "\n" + super.getMessage();
	}

	/**
	 * 
	 * @return the criterium that could not be found
	 */
	public String getMissingCriterium() {
		return missingCriterium;
	}
	
	

}
