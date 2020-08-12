package crypSwing;

import java.util.Arrays;

/**
 * This class represents a table of ciphertext letters and their frequency in a puzzle.
 * 
 * @author Aaron Bitman
 * @version 2.0 06/21/2019
 */
public class CrypFrequencyTable {
	
	private final int LETTERSINALPHABET = 26;
	private CrypFreqTableEntry frequencyTable[] = new CrypFreqTableEntry[LETTERSINALPHABET];
	private String freqAlpha, freqByPop;

	/**
	 * This constructor populates the table from a puzzle
	 * @param puzzle	The puzzle in String format
	 */
	CrypFrequencyTable(String puzzle) {

		final int CAPTIALLETTEROFFSET = 65;
		int puzzleLength = puzzle.length();
		char currentCharacter;
		int index; //used for several purposes

		for (index = 0; index < LETTERSINALPHABET; index++)
			frequencyTable[index] = new CrypFreqTableEntry((char)(index + CAPTIALLETTEROFFSET));

		for (index = 0; index < puzzleLength; index++) {
			currentCharacter = puzzle.charAt(index);
			if (currentCharacter >= 'A' && currentCharacter <= 'Z')
				frequencyTable[(int) currentCharacter - CAPTIALLETTEROFFSET].increment();
		}
		
		freqAlpha = setFreqAlpha();
		Arrays.sort(frequencyTable, CrypFreqTableEntry.freqTableEntryComparator);
		freqByPop = setFreqByPop();
	}
	
	/**
	 * Sets the frequency table in alphabetical order.
	 * @return a String listing the frequency in alphabetical order.
	 */
	private String setFreqAlpha () {
		String temp = "";
		int index;
		int occurrences;
		
		for (index=0; index < LETTERSINALPHABET; index++) {
			occurrences = frequencyTable[index].getOccurrences();
			if (occurrences > 0) {
				if (!temp.equals(""))
					temp += ", ";
				temp += frequencyTable[index].getLetter() + ": " + occurrences;
			}
		}
		
		return temp;
	}
	
	/**
	 * Sets the frequency table in order from most frequent to least.
	 * @return a String listing the frequency in reverse order of frequency.
	 */
	private String setFreqByPop() {
		String temp = "";
		CrypFreqTableEntry entry;
		
		int index = 0;
		int count;
		int previousCount = Integer.MAX_VALUE;
		
		do {
			entry = frequencyTable[index];
			count = entry.getOccurrences();
			if (count < previousCount) {
				if (count == 0)
					return temp;
				previousCount = count;
				if (temp != "")
					temp += ", ";
				temp += count + ": ";
			}
			temp += entry.getLetter();
			index++;
		 } while (index < LETTERSINALPHABET);
	
		return temp;
	}

	/**
	 * Gets the frequency table in order from most frequent to least.
	 * @return a String listing the frequency in reverse order of frequency.
	 */
	public String getFreqByPop() {
		return freqByPop;
	}
	
	/**
	 * Gets the frequency table in alphabetical order.
	 * @return a String listing the frequency in alphabetical order.
	 */
	
	public String getFreqAlpha() {
		return freqAlpha;
	}

}