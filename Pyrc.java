package crypSwing;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * This self-contained program generates a cryptogram.
 * @author Bitman
 * @version 2.0 01/20/21
 * @version 2.1 01/02/21
 */
public class Pyrc {

	/**
	 * Determine whether the given ciphertext alphabet violates like-exclusion for the given plaintext.
	 * @param alphabetList The key, where the first letter is the ciphertext for A, the second for B, etc.
	 * @param plaintext The plaintext puzzle
	 * @return true if the alphabet violates like-exclusion for the puzzle; false otherwise
	 */
	private static boolean violatesLikeExclusion(List<Character> alphabetList, String plaintext) {
		final boolean likeExclusionApplies = true; // Do we even WANT like-exclusion?
		
		if (!likeExclusionApplies) return false; // If not then forget it.

		for (int index = 0; index < alphabetList.size(); index++) {
			char ciphertextChar = alphabetList.get(index);
			if ((char)(index + 65) == ciphertextChar && plaintext.indexOf(ciphertextChar) >= 0)
				return true;
		}
		return false;
	}
	
	/**
	 * The main method generates a cryptogram.
	 * @param args not used
	 */
	public static void main(String[] args) {
		String plaintext = "My wife's jealousy is getting ridiculous. The other day she "
				+ "looked at my calendar and wanted to know who May was. (Rodney Dangerfield)";
		String temp = "";
		List<Character> alphabetList = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M',
								'N','O','P','Q','R','S','T','U','V','W','X','Y','Z');
		plaintext = plaintext.toUpperCase();
		do {
			Collections.shuffle(alphabetList);
			violatesLikeExclusion(alphabetList, plaintext);
		} while (violatesLikeExclusion(alphabetList, plaintext));
		for (char ch: plaintext.toCharArray())
			if (ch < 'A' || ch > 'Z') temp += ch;
			else temp += alphabetList.get(((int) ch) - 65);
		System.out.println(temp);
	}
}
