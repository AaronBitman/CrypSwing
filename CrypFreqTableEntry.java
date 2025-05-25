package crypSwing;

import java.util.Comparator;

/**
 * This class represents one entry in a frequency table -
 * one ciphertext letter and the number of times it occurs.
 * 
 * @author Aaron Bitman
 * @version 1.0 03/27/19
 * @version 2.0 01/20/21
 */
class CrypFreqTableEntry implements Comparable<CrypFreqTableEntry> {
	private char letter;
	private int occurrences;

	/**
	 * The constructor records a ciphertext letter.
	 * @param letter	The ciphertext letter.
	 */
	CrypFreqTableEntry (char letter) {
		this.letter = letter;
		// occurrences should automatically default to 0.
	}

	/**
	 * Gets the letter of this entry.
	 * @return	The letter
	 */
	char getLetter() {
		return letter;
	}

	/**
	 * Gets the number of times this letter occurred.
	 * @return	The number of times this letter occurred
	 */
	int getOccurrences () {
		return occurrences;
	}

	/**
	 * Increments the number of occurrences
	 */
	void increment () {
		occurrences++;
	}

	/**
	 * Defines the sorting order of key entries
	 * @param other	The second entry
	 * @return <code>1</code> if the second entry should come first
	 *         <code>-1</code> if the first entry should come first
	 */
	@Override
    public int compareTo(CrypFreqTableEntry other) {
		
		// We want to sort by occurrences descending and then by letter ascending.
		if (this.occurrences > other.occurrences)
			return -1;
		else if (this.letter < other.letter)
			return -1;
		return 1;
    }

	/**
	 * The Comparator
	 */
	public static Comparator<CrypFreqTableEntry> freqTableEntryComparator
		= new Comparator<>() {

		/**
		 * 
		 * @param entry1	The first entry to be compared
		 * @param entry2	The second entry to be compared
		 * @return 1 if the second entry should come first, -1 if the second one should
		 */
			@Override
			public int compare(CrypFreqTableEntry entry1, CrypFreqTableEntry entry2) {
				return entry1.compareTo(entry2);
			}
		};
}
