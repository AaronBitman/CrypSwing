package crypSwing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * This class does the real work: the code-breaking.
 * 
 * @author Aaron Bitman
 * @version 1.0 03/20/19
 */

public class CrypCodeBreaker {
	/**
	 * Determines if two words are compatible; that is,
	 * if one can be the plaintext for the other in a monoalphabetic substitution cipher.
	 * Note that this method assumes that both words are the same length.
	 * @param cipherText    The ciphertext word
	 * @param plainText     The possible (candidate) plaintext word
	 * @param likeExclusion Whether the user selected like-exclusion
	 * @param crypKey       The known plaintext letters
	 * @return <code>true</code> if the <code>cipherText</code> and <code>plainText</code> are compatible, or
	 *         <code>false</code> if the <code>cipherText</code> and <code>plainText</code> are not compatible
	 */
	private static boolean compatible(String cipherText, String plainText, boolean likeExclusion, CrypKey crypKey) {
		
		int index1, index2, wordLength = cipherText.length();
		char char1, char2;
		
		// First of all, where one has a hyphen, the other must, and the same goes for apostrophes.
		for (index1 = 0; index1 < wordLength; index1++) {
			char1 = cipherText.charAt(index1);
			char2 =  plainText.charAt(index1);
			if ((char1 == '-'  && char2 != '-')  ||
				(char2 == '-'  && char1 != '-')  ||
				(char1 == '\'' && char2 != '\'') ||
				(char2 == '\'' && char1 != '\'') )
					return false;
		}
		
		// If the user requested like-exclusion, check for it.
		if (likeExclusion)
			for (index1 = 0; index1 < wordLength; index1++)
				if ((cipherText.charAt(index1) == plainText.charAt(index1)) &&
						cipherText.charAt(index1) != '-' &&
						 plainText.charAt(index1) != '\'')
					return false;
		
		// Check for the key - that is, the known letters.
		if (!crypKey.conforms(plainText))
			return false;
		
		// Then do the REAL checking.
		for (index1 = 0; index1 < wordLength - 1; index1++) {
			for (index2 = index1 + 1; index2 < wordLength; index2++) {
				if ((cipherText.charAt(index1) == cipherText.charAt(index2)) !=
					(plainText.charAt(index1) == plainText.charAt(index2)))
						return false;
			}
		}
		
		return true;
	}

	/**
	 * Breaks a one-word monoalphabetic substitution ciphertext.
	 * @param cipherTextWord The ciphertext word
	 * @param likeExclusion  Whether the user selected the like-exclusion option
	 * @param crypKey        The known letters and their plaintext
	 * @return An array of Strings, each String a candidate plaintext translation
	 * @throws FileNotFoundException If the program can't find the right dictionary file
	 */
	public static String[] breakWord(String cipherTextWord, boolean likeExclusion, CrypKey crypKey)
	{
		String plainTextWord;
		ArrayList<String> temp = new ArrayList<String>();
		int length = cipherTextWord.length();
		int total = 0;
		int index;
		
		try {
			// Open the input file.
			Scanner inFile = new Scanner(new File ("words" + length));

			while (inFile.hasNext()) {
				//Get the next plaintext word.
				plainTextWord = inFile.next();
				
				// Determine if the words are compatible.
				if (compatible (cipherTextWord, plainTextWord, likeExclusion, crypKey)) {
					temp.add(plainTextWord);
					total++;
				}
			}

		// Close the file.
		inFile.close();
		if (total==0) {
			total=1;
			temp.add("No results found.");
		}
		String[] results = new String[total];
		for (index=0; index < total; index++)
				results[index] = temp.get(index).toString();
		return results;
		}
		catch (FileNotFoundException f1) {
			String[] error = {"Error: The input file was not found."};
			return error;
		}
	}
}
