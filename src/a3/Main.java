package a3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Segment trees solution following explanation from
 * http://discuss.codechef.com/questions/36507/a3-the-guessing-game-editorial-explanation
 * 
 * @author diegofdez
 *
 */
public class Main {
	
	private static final int TOTAL_MIN = 1;
	private static final int TOTAL_MAX = (int) 1e9;
	
	public class Range {
		private int minValue;
		private int maxValue;
		
		Range(int minValue, int maxValue) {
			this.minValue = minValue;
			this.maxValue = maxValue;
		}

		public int getMinValue() {
			return minValue;
		}
		public void setMinValue(int minValue) {
			this.minValue = minValue;
		}
		public int getMaxValue() {
			return maxValue;
		}
		public void setMaxValue(int maxValue) {
			this.maxValue = maxValue;
		}

		@Override
		public String toString() {
			return "Range [minValue=" + minValue + ", maxValue=" + maxValue
					+ "]";
		}
	}
	
	public class Hint {
		private String operator;
		private int value;
		private String logicalValue;
		private List<Range> rangeList;
		
		Hint(String hint) {
			//parse hint
			String[] hintPieces = hint.split(" ");
			this.operator = hintPieces[0];
			this.value = Integer.parseInt(hintPieces[1]);
			this.logicalValue = hintPieces[2];
		}
		
		Hint(String operator, int value, String logicalValue) {
			this.operator = operator;
			this.value = value;
			this.logicalValue = logicalValue;
		}
		
		public List<Range> getRangeList() {
			if (rangeList == null) {
				rangeList = new ArrayList<Range>();
				if ("<".equals(operator) && "Yes".equals(logicalValue)){
					Range lessThanRange = new Range(TOTAL_MIN, this.value - 1);
					rangeList.add(lessThanRange);
				}
				else if ("<".equals(operator) && "No".equals(logicalValue)){
					Range moreOrEqualThanRange = new Range(this.value, TOTAL_MAX);
					rangeList.add(moreOrEqualThanRange);
				}
				else if (">".equals(operator) && "Yes".equals(logicalValue)){
					Range moreThanRange = new Range(this.value + 1, TOTAL_MAX);
					rangeList.add(moreThanRange);
				}
				else if (">".equals(operator) && "No".equals(logicalValue)){
					Range lessOrEqualThanRange = new Range(TOTAL_MIN, this.value);
					rangeList.add(lessOrEqualThanRange);
				}
				else if ("=".equals(operator) && "Yes".equals(logicalValue)){
					Range equalRange = new Range(this.value, this.value);
					rangeList.add(equalRange);
				}
				else if ("=".equals(operator) && "No".equals(logicalValue)){
					Range lessThanRange = new Range(TOTAL_MIN, this.value - 1);
					Range moreThanRange = new Range(this.value + 1, TOTAL_MAX);
					rangeList.add(lessThanRange);
					rangeList.add(moreThanRange);
				}
			}
			return rangeList;
		}

		public List<Range> getIntersection(Hint other) {
			return this.getIntersection(other.getRangeList());
		}
		
		public List<Range> getIntersection(List<Range> otherRangeList) {
			List<Range> result = new ArrayList<Range>();
			for (Range range : this.getRangeList()) {
				for (Range otherRange : otherRangeList) {
					if ((range.getMinValue() > otherRange.getMaxValue()) ||
							(range.getMaxValue() < otherRange.getMinValue())){
						// This ranges don't have an intersection
					}
					else if ((range.getMinValue() >= otherRange.getMinValue()) 
							&& (range.getMaxValue() <= otherRange.getMaxValue())){
						// Range is included in Other Range
						result.add(new Range(range.getMinValue(), range.getMaxValue()));
					}
					else if ((otherRange.getMinValue() >= range.getMinValue()) 
							&& (otherRange.getMaxValue() <= range.getMaxValue())) {
						// Other Range is included in range
						result.add(new Range(otherRange.getMinValue(), otherRange.getMaxValue()));
					}
					else {
						result.add(
								new Range(Math.max(range.getMinValue(), otherRange.getMinValue()), 
										Math.min(range.getMaxValue(), otherRange.getMaxValue())));
					}
				}
			}
			
			return result;
		}
		
		@Override
		public String toString() {
			return "Hint [operator=" + operator + ", value=" + value
					+ ", logicalValue=" + logicalValue + ", rangeList="
					+ rangeList + "]";
		}
	}
	
	public int processTestCase(Hint[] hintArray){
		int maxCompatibleHints = 0;
		// Compare first hint with the other hints
		for (int i=0; i<hintArray.length; i++) {
			List<Range> intersection = hintArray[i].getRangeList();
			int compatibleHints = 1; // Already one hint in the list
			for (int j=i+1; j<hintArray.length; j++){
				List<Range> intersectionCandidate = hintArray[j].getIntersection(intersection);
				if (intersectionCandidate.size() > 0) {
					intersection = intersectionCandidate;
					compatibleHints++;
				}
			}
			
			maxCompatibleHints = Math.max(maxCompatibleHints, compatibleHints);
			if (maxCompatibleHints > hintArray.length - i) break;
		}
		
		return hintArray.length - maxCompatibleHints;
	}
	
	public static void main(String[] args){
		Main theGame = new Main();
		
		BufferedReader br = 
                new BufferedReader(new InputStreamReader(System.in));

		try {
			String numberOfTestCasesString = br.readLine();
			Integer numberOfTestCases = Integer.parseInt(numberOfTestCasesString);
			
			for (int i=0; i<numberOfTestCases; i++) {
				String numberOfHintsString = br.readLine();
				Integer numberOfHints = Integer.parseInt(numberOfHintsString);
				
				Hint[] hintArray = new Hint[numberOfHints];
				for (int j=0; j<numberOfHints; j++) {
					Hint hint = theGame.new Hint(br.readLine());
					hint.getRangeList();
					hintArray[j] = hint;
				}
				System.out.println(theGame.processTestCase(hintArray));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
