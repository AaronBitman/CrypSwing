package crypSwing;

import java.util.HashMap;
import java.util.List;

/**
 * This class maps each letter in the ciphertext alphabet to every possible plaintext letter to which it can translate.
 * @author Bitman
 * @version 2.0 01/20/21
 */
public class CrypAlphabetMapping {

	static final int SIZE_OF_ALPHABET = 26; // Do we really need this? I mean... would it kill anyone to hardcode a 26 in many places?
	static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	HashMap<Character, CrypLetterMapping> translations = new HashMap<>(SIZE_OF_ALPHABET);

	/**
	 * This constructor builds the map based on whether the user chose like-exclusion.
	 * @param likeExclusion Whether the user chose the "like-exclusion" option.
	 */
	public CrypAlphabetMapping(boolean likeExclusion) {
		int index;
		
		for (index=0; index < SIZE_OF_ALPHABET; index++) {
			CrypLetterMapping temp = new CrypLetterMapping(ALPHABET);
			if (likeExclusion)
				temp.eliminateLetters(ALPHABET.substring(index, index+1));
			translations.put(Character.valueOf(ALPHABET.charAt(index)), temp);
		}
	}

	/**
	 * This method eliminates any number of letters from the possible translations of a ciphertext letter.
	 * @param ciphertext         The ciphertext letter
	 * @param lettersToEliminate A String consisting of plaintext letters to eliminate.
	 */
	public void eliminateLetters(Character ciphertext, String lettersToEliminate) {
		// First of all, if the character is an apostrophe, none of this applies.
		if (ciphertext == '\'') return;

		// Now, how many plaintext letters are there, initially? Find out for later.
		int initialPlaintextLetterCount = translations.get(ciphertext).size();

		// Narrow down the plaintext letters, obviously.
		translations.get(ciphertext).eliminateLetters(lettersToEliminate);

		// But we're not done yet! If there's just one translation - and there was more
		// than one before - we need to eliminate it from all the OTHER letter mappings!
		// Yes, this method is potentially recursive.
		if (initialPlaintextLetterCount > 1 && translations.get(ciphertext).size() == 1)
			for (HashMap.Entry<Character, CrypLetterMapping> entry : translations.entrySet())
				if (entry.getKey() != ciphertext)
					entry.getValue().eliminateLetters(translations.get(ciphertext).possiblePlaintext());
	}

	/**
	 * Narrow down the list of possible plaintext translations of a
	 * given ciphertext letter by intersecting it with a new list.
	 * @param ciphertext The ciphertext letter
	 * @param newList    The new list of proposed translations of the above letter
	 */
	void narrowDownLetters(Character ciphertext, List<Character> newList) {
		// First of all, if the character is an apostrophe, none of this applies.
		if (ciphertext == '\'') return;

		// Now, how many plaintext letters are there, initially? Find out for later.
		int initialPlaintextLetterCount = translations.get(ciphertext).size();

		// Narrow down the plaintext letters, obviously.
		translations.get(ciphertext).narrowDownLetters(newList);

		// But we're not done yet! If there's just one translation (and there was more
		// than one before) we need to eliminate it from all the OTHER letter mappings!
		if (initialPlaintextLetterCount > 1 && newList.size() == 1)
			for (HashMap.Entry<Character, CrypLetterMapping> entry : translations.entrySet())
				if (entry.getKey() != ciphertext)
					entry.getValue().eliminateLetters(newList.get(0).toString());
	}

	/**
	 * Determine if the current mapping for a given ciphertext letter allows the given plaintext letter.
	 * @param ciphertext The ciphertext letter
	 * @param plaintext The plaintext letter
	 * @return true if the mapping allows the plaintext letter, false otherwise
	 */
	public boolean allows(char ciphertext, String plaintext) {
		if (ciphertext == '\'') return true;
		return translations.get(ciphertext).allows(plaintext);
	}

	/**
	 * Determine the size of the mapping as measured by the total of all possibilities
	 * for all of the ciphertext letters added up. The smaller the number, the better.
	 * @return The size as described above.
	 */
	public int size() {
		int temp = 0;
		for (HashMap.Entry<Character, CrypLetterMapping> entry : translations.entrySet())
			temp += entry.getValue().size();
		
		return temp;
	}

}
