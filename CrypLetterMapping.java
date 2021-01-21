package crypSwing;
import java.util.*;

/**
 * This class maps one ciphertext letter to every possible plaintext letter to which it can translate.
 * @author Bitman
 * @version 2.0 01/20/21
 */
public class CrypLetterMapping {
	
	private String possiblePlaintextLetters;

	/**
	 * This constructor builds the map based on whether the user chose like-exclusion.
	 * @param initialValue The set of all letters to which we initially guess a ciphertext character might map
	 */
	public CrypLetterMapping(String initialValue) {
		possiblePlaintextLetters = initialValue;
	}

	/**
	 * Eliminate a set of characters from the list of possible plaintext letters.
	 * Note that this method includes some validation, namely making sure that the list isn't narrowed down to zero letters. 
	 * @param lettersToEliminate The characters to eliminate, validation permitting 
	 */
	public void eliminateLetters(String lettersToEliminate) {
		int index;
		for (index=0; index < lettersToEliminate.length(); index++) {
			if (possiblePlaintextLetters.length() <= 1) return;
			possiblePlaintextLetters = possiblePlaintextLetters.replaceFirst(lettersToEliminate.substring(index, index+1), "");
		}
	}

	/**
	 * Get a new list of characters, intersect it with the existing possibilities, and if
	 * there's anything left, set the possible plaintext letters to that intersection.
	 * Note that this method assumes that the list of characters is in alphabetical order.
	 * @param newList The new list of possible plaintext characters
	 */
	void narrowDownLetters(List<Character> newList) {
		String temp = "";
		for (Character c : newList)
			if (possiblePlaintextLetters.contains(c.toString()))
				temp += c;
		if (temp != "") possiblePlaintextLetters = temp;
	}

	/**
	 * Determine whether the current mapping allows a given plaintext character.
	 * @param plaintext The plaintext character
	 * @return true if the mapping allows the character, false otherwise
	 */
	public boolean allows(String plaintext) {
		return possiblePlaintextLetters.contains(plaintext);
	}

	/**
	 * Determine the size of the mapping - that is, how many possible plaintext letters there are.
	 * @return The number of possible plaintext letters.
	 */
	public int size() {
		return possiblePlaintextLetters.length();
	}

	/**
	 * Return what possible plaintext letters there are.
	 * @return The number of possible plaintext letters.
	 */
	public String possiblePlaintext() {
		return possiblePlaintextLetters;
	}

}
