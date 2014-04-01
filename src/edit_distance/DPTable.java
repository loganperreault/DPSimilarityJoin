package edit_distance;

public class DPTable {
	
	String string1, string2;
	int numRows, numCols;
	int maxEdits;
	DPTableRow[] rows;
	int[] columnWidths;
	DPTableRow root;
	DPTableTree tree;
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
		return calculate(threshold, false);
	}
	
	public Integer calculate(double threshold, boolean acceptedEarlyStopping) {
		if (threshold < 0.0 || threshold > 1.0)
			throw new IllegalArgumentException("Threshold must be between in the range (0.0 , 1.0)");
		int thresholdEdits = (int) ((1.0 - threshold) * maxEdits);
		calculated = true;
		
		rows[0] = root;
		for (int i = 1; i < numRows; i++) {
			rows[i] = rows[i-1].addChild(string2.charAt(i-1));
			int bestOfRow = Integer.MAX_VALUE;
			for (int j = 1; j < numCols; j++) {
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
				bestOfRow = Math.min(best, bestOfRow);
				MultiEditDistance.cellCount++;
			}
			
			// rejectedEarlyStopping
			if (bestOfRow > thresholdEdits)
				break;
			
			// acceptedEarlyStopping
			if (acceptedEarlyStopping) {
				if (bestOfRow + Math.max(numRows, numCols) - i < thresholdEdits) {
					setSolution(thresholdEdits);
					break;
				}
			}
					
		}
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

}
