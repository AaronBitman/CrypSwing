package crypSwing;

import java.util.*;

/**
 * This class contains one ciphertext word and every plaintext word to which we might guess it maps.
 * @author Bitman
 * @version 2.0 01/20/21
 */
public class CrypCiphertextWordMapping implements Comparable<CrypCiphertextWordMapping> {
	
	private String ciphertext;
	private List<String> plaintextCandidates = new ArrayList<String>();

	/**
	 * This constructor takes a ciphertext word and stores all the initial guesses of the plaintext translations.
	 * @param ciphertextWord The ciphertext word
	 * @param likeExclusion  Whether the user chose the "like-exclusion" option.
	 */
	public CrypCiphertextWordMapping(String ciphertextWord, boolean likeExclusion) {
		ciphertext = ciphertextWord;
		CrypKey dummy = new CrypKey();

		plaintextCandidates = CrypCodeBreaker.breakWord(ciphertext, likeExclusion, dummy);
		// This code assumes that if the code breaker finds nothing, it returns a message with at least one space in it.
		if (plaintextCandidates.get(0).contains(" ")) plaintextCandidates.clear();
	}

	/**
	 * Get the ciphertext
	 * @return The ciphertext
	 */
	String getCiphertext() {
		return ciphertext;
	}

	/**
	 * Return the number of candidate plaintext words for this ciphertext word.
	 * @return The number of candidates
	 */
	int numberOfCandidates() {
		return plaintextCandidates.size();
	}

	/**
	 * Return the candidate at the given index.
	 * @param index The index
	 * @return The candidate plaintext translation
	 */
	String candidate(int index) {
		if (numberOfCandidates() <= index) return "";
		return plaintextCandidates.get(index);
	}

	/**
	 * This method eliminates all the candidate plaintext words that don't conform to a given alphabet mapping.
	 * @param alphabetMap The alphabet mapping
	 */
	public void pareDown(CrypAlphabetMapping alphabetMap) {
		int wordIndex = 0;
		int letterIndex;

		// Loop through all the candidate words.
		wordLoop:
		while (wordIndex < plaintextCandidates.size()) {
			// Given one candidate word, loop through all the characters in the ciphertext word AND candidate plaintext word.
			for (letterIndex = 0; letterIndex < ciphertext.length(); letterIndex++)
				// If the mapping doesn't allow this plaintext character for this ciphertext letter...
				if (!alphabetMap.allows(ciphertext.charAt(letterIndex), plaintextCandidates.get(wordIndex).substring(letterIndex, letterIndex+1))) {
					// ...then eliminate the plaintext translation...
					plaintextCandidates.remove(wordIndex);
					// ...and resume the list with the same index, which should be the next plaintext translation.
					continue wordLoop;
				}
			// If we didn't eliminate the word (that is, the possible plaintext translation), increment to the next word.
			wordIndex++;
		}
	}

	/**
	 * Defines the sorting order of word-mapping entries
	 * @param other The second entry
	 * @return 1 if the second entry should come first, -1 if the first entry should come first
	 */
	@Override
    public int compareTo(CrypCiphertextWordMapping other) {
		// The first criterion is the number of possible plaintexts; the lower number comes first.
		// If that number is tied, the second criterion is length; the higher number comes first.
		// If both of those criteria are tied, it doesn't matter, except that we want consistent
		// results, so make it alphabetical.
		if (this.plaintextCandidates.size() > other.plaintextCandidates.size())
			return 1;
		if (this.plaintextCandidates.size() < other.plaintextCandidates.size())
			return -1;
		if (this.ciphertext.length() < other.ciphertext.length())
			return 1;
		if (this.ciphertext.length() > other.ciphertext.length())
			return -1;
		if (this.ciphertext.compareTo(other.ciphertext) > 0)
			return 1;
		return -1;
	}

	/**
	 * The Comparator
	 */
	public static Comparator<CrypCiphertextWordMapping> CrypCiphertextWordMappingComparator
		= new Comparator<>() {
		/**
		 * @param entry1	The first entry to be compared
		 * @param entry2	The second entry to be compared
		 * @return <code>1</code> if the second entry should come first
		 *         <code>-1</code> if the first entry should come first
		 */
			public int compare(CrypCiphertextWordMapping entry1, CrypCiphertextWordMapping entry2) {
				return entry1.compareTo(entry2);
			}
	};
}
