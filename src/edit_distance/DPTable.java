package edit_distance;

import java.util.Map.Entry;

public class DPTable {
	
	String string1, string2;
	int numRows, numCols;
	int maxEdits;
	DPTableRow[] rows;
	int[] columnWidths;
	DPTableRow root;
	DPTableTree tree;
	private int cellCount = 0;
	private Integer solution = null;
	private Double similarity = null;
	private boolean calculated = false;
	private boolean accepted = false;
	
	public DPTable(String string1, String string2) {
		this.string1 = string1.toLowerCase();
		this.string2 = string2.toLowerCase();
		this.maxEdits = Math.max(string1.length(), string2.length());
		numCols = string1.length() + 1;
		numRows = string2.length() + 1;
		rows = new DPTableRow[numRows];
		columnWidths = new int[numCols];
		
		int indexPaddingSize = String.valueOf(numCols).length() + 1;
		for (int i = 0; i < numCols; i++)
			columnWidths[i] = indexPaddingSize;
		
		root = DPTableRow.getRoot();
		tree = new DPTableTree(root);
	}
	
	public Integer calculate() {
		return calculate(0.0);
	}
	
	public Integer calculate(double threshold) {
		return calculate(threshold, false, false, false);
	}
	
	public Integer calculate(double threshold, boolean rejectedEarlyStopping, boolean acceptedEarlyStopping, boolean useTree) {
		if (threshold < 0.0 || threshold > 1.0)
			throw new IllegalArgumentException("Threshold must be between in the range (0.0 , 1.0)");
		int thresholdEdits = (int) ((1.0 - threshold) * maxEdits);
		calculated = true;
		
		cellCount = 0;
		
		rows[0] = root;
		for (int i = 1; i < numRows; i++) {
			if (useTree) {
				rows[i] = rows[i-1].getChild(string2.charAt(i-1));
			}
			if (rows[i] == null)
				rows[i] = rows[i-1].addChild(string2.charAt(i-1));
			
			if (rows[i].calculated < numCols) {
			
				int bestOfRow = Integer.MAX_VALUE;
				int bestColIndex = 0;
				int j;
				for (j = rows[i].calculated; j < numCols; j++) {
				//for (j = 1; j < numCols; j++) {
					int diag = rows[i-1].columns[j-1].value;
					int left = rows[i].columns[j-1].value;
					int top  = rows[i-1].columns[j].value;
					int insert = left + 1;
					int delete = top  + 1;
					int swap   = diag + 1;
					int match = Integer.MAX_VALUE;
					if (string1.charAt(j-1) == string2.charAt(i-1)) {
						match  = diag + 0;
					}
					int best = Math.min(insert, Math.min(delete, Math.min(swap, match)));
					rows[i].columns[j] = new DPTableCell(best);
					if (best == insert)
						rows[i].columns[j].solutions.add(rows[i-1].columns[j]);
					else if (best == delete)
						rows[i].columns[j].solutions.add(rows[i].columns[j-1]);
					else if (best == swap)
						rows[i].columns[j].solutions.add(rows[i-1].columns[j-1]);
					else if (best == match)
						rows[i].columns[j].solutions.add(rows[i-1].columns[j-1]);
					if (best < bestOfRow) {
						bestOfRow = best;
						bestColIndex = j;
					}
					cellCount++;
				}
				
				// rejectedEarlyStopping
				if (rejectedEarlyStopping)
					if (bestOfRow > thresholdEdits)
						break;
				
				// acceptedEarlyStopping
				if (acceptedEarlyStopping) {
					if (bestOfRow + numRows - i <= (maxEdits - thresholdEdits) &&
						bestOfRow + numCols - bestColIndex <= (maxEdits - thresholdEdits)) {
						setSolution(thresholdEdits);
						break;
					}
				}
				
				rows[i].calculated = j;
			
			} else {
				//System.out.println("Reuse: '"+rows[i]+"'");
			}
					
		}
		
		//System.out.println(rows[4]);
		
		if (rows[numRows-1] != null)
			setSolution(rows[numRows-1].columns[numCols-1].value);
		if (solution != null && solution >= thresholdEdits)
			accepted = true;
		return solution;
	}
	
	public Integer setSolution(Integer solution) {
		this.solution = solution;
		this.similarity = 1.0 - ((double) solution / maxEdits);
		return solution;
	}
	
	public Integer getSolution() {
		if (!calculated)
			throw new IllegalArgumentException("Call calculate() prior to retrieving values.");
		return solution;
	}
	
	public Double getSimilarity() {
		if (!calculated)
			throw new IllegalArgumentException("Call calculate() prior to retrieving values.");
		return similarity;
	}
	
	public int getCellCount() {
		return cellCount;
	}
	
	public String toString() {
		
		int colHeaderPadding = 5;
		
		String tbl = String.format("%"+colHeaderPadding+"s", "") + " ";
		for (int i = 0; i < numCols; i++) {
			if (i==0)
				tbl += String.format("%"+(columnWidths[i]+1)+"s", "") + " ";
			else
				tbl += String.format("%1$" + columnWidths[i] + "s ", string1.charAt(i-1)) + " ";
		}
		tbl += "\n";
		tbl += String.format("%"+colHeaderPadding+"s", "");
		tbl += "|";
		for (int i = 0; i < numCols; i++) {
			tbl += String.format("%1$" + columnWidths[i] + "s ", i) + "|";
		}
		tbl += "\n";
		for (int row = 0; row < numRows; row++) {
			if (row == 0)
				tbl += " ";
			else
				tbl += string2.charAt(row-1);
			tbl += String.format("%1$"+(colHeaderPadding-2)+"s ", " "+row) + "|";
			if (rows[row] != null)
				tbl += rows[row].getRowString(columnWidths);
			tbl += "\n";
		}
		return tbl;
	}
	
	public boolean accepted() {
		return accepted;
	}
	
	public static void swapWord(String word) {
		if (DPTableRow.str == null) {
			setWord(word);
		} else {
			System.out.println("SWAPPING "+word+" FOR "+DPTableRow.str);
			DPTableRow.setSize(word.length() + 1);
			for (Entry<Character, DPTableRow> childRow : DPTableRow.getRoot().children.entrySet()) {
				System.out.println("------------------------------");
				transferRow(childRow.getValue(), word);
			}
			DPTableRow.str = word;
		}
	}
	
	private static void transferRow(DPTableRow row, String word) {
		DPTableCell[] newColumns = new DPTableCell[word.length() + 1];
		int i;
		for (i = 0; i < Math.min(DPTableRow.str.length(), word.length()); i++) {
			if (DPTableRow.str.charAt(i) == word.charAt(i))
				newColumns[i] = row.columns[i];
			else
				break;
		}
		
		System.out.print("OLD: ");
		for (DPTableCell cell : row.columns)
			System.out.print(cell+",");
		System.out.println();
		System.out.print("   NEW: ");
		for (DPTableCell cell : newColumns)
			System.out.print(cell+",");
		System.out.println();
		
		row.columns = newColumns;
		row.calculated = i;
		for (Entry<Character, DPTableRow> childRow : row.children.entrySet())
			transferRow(childRow.getValue(), word);
	}
	
	public static void setWord(String word) {
		DPTableRow.str = word;
		DPTableRow.reset();
		DPTableRow.setSize(word.length() + 1);
	}

}
