package webSearchEngine;

import java.util.HashMap;

public class BoyerMoore {
	private static HashMap<Character, Integer> last; // the last position map
	private String pat; // or as a string

	// pattern provided as a string
	public BoyerMoore(String pat) {
		this.pat = pat;
		int len = pat.length();
		// position of rightmost occurrence of c in the pattern
		last = new HashMap <Character, Integer>();
		for (int c = 0; c < len; c++) {
			
			last.put(pat.charAt(c), pat.lastIndexOf(pat.charAt(c)));
		}
	}

	// return offset of first match; N if no match 
	// The modified approach to BoyerMoore allows mismatches in the string if the mismatches are less than 10% at the time of mismatch
	public int search(String txt) {
		int M = this.pat.length();
		int N = txt.length();
		if(M == 0 || N == 0) {
			return 0;
		}
		int i = M-1;
		int j = M-1;
		int matches = 0;
		int mismatches = 0;
		while(i>=0 && i<N && j>=0 && j<M ) {
			if(this.pat.charAt(j) == txt.charAt(i)) {
				matches++;
				if(j==0) {
					return i;
				}
				j--;
				i--;
			}
			else {
				mismatches++;
				if(.9 * matches >= (int)mismatches) {  //At the moment of mismatch if the string matches 90% or more then it is considered as a match
					i--; 
					j--;
				}
				else {
					int l = M;
					if(last.containsKey(txt.charAt(i)))
						l = last.get(txt.charAt(i));
					i = i+M - (int)Math.min(j,1+l);
					j = M-1;
				}
			}
		}
		return N;
	}
}
