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
		if (threshold < 0.0 || threshold > 1.0)
			throw new IllegalArgumentException("Threshold must be between in the range (0.0 , 1.0)");
		int thresholdEdits = (int) ((1.0 - threshold) * maxEdits);
		calculated = true;
		
		int bestCost;
		if (MultiEditDistance.maximizing)
			bestCost = 	Math.max(MultiEditDistance.insertionCost, 
						Math.max(MultiEditDistance.deletionCost, 
						Math.max(MultiEditDistance.substitutionCost, MultiEditDistance.matchCost)));
		else
			bestCost = 	Math.min(MultiEditDistance.insertionCost, 
						Math.min(MultiEditDistance.deletionCost, 
						Math.min(MultiEditDistance.substitutionCost, MultiEditDistance.matchCost)));
		
		rows[0] = root;
		for (int i = 1; i < numRows; i++) {
			rows[i] = rows[i-1].addChild(string2.charAt(i-1));
			int bestOfRow = Integer.MAX_VALUE;
			if (MultiEditDistance.maximizing)
				bestOfRow *= -1;
			for (int j = 1; j < numCols; j++) {
				int diag = rows[i-1].columns[j-1].value;
				int left = rows[i].columns[j-1].value;
				int top  = rows[i-1].columns[j].value;
				int insert = left + MultiEditDistance.insertionCost;
				int delete = top  + MultiEditDistance.deletionCost;
				int swap   = diag + MultiEditDistance.substitutionCost;
				int match = Integer.MAX_VALUE;
				if (MultiEditDistance.maximizing)
					match *= -1;
				if (string1.charAt(j-1) == string2.charAt(i-1)) {
					match  = diag + MultiEditDistance.matchCost;
				}
				int best;
				if (MultiEditDistance.maximizing)
					best = Math.max(insert, Math.max(delete, Math.max(swap, match)));
				else
					best = Math.min(insert, Math.min(delete, Math.min(swap, match)));
				rows[i].columns[j] = new DPTableCell(best);
				if (best == insert)
					rows[i].columns[j].solutions.add(rows[i-1].columns[j]);
				else if (best == delete)
					rows[i].columns[j].solutions.add(rows[i].columns[j-1]);
				else if (best == swap)
					rows[i].columns[j].solutions.add(rows[i-1].columns[j-1]);
				else if (best == match)
					rows[i].columns[j].solutions.add(rows[i-1].columns[j-1]);
				if (MultiEditDistance.maximizing)
					bestOfRow = Math.max(best, bestOfRow);
				else
					bestOfRow = Math.min(best, bestOfRow);
			}
			
			if ((MultiEditDistance.maximizing && 
				(bestOfRow - (numRows-i)*bestCost) < thresholdEdits) ||
				(!MultiEditDistance.maximizing && 
				(bestOfRow + (numRows-i)*bestCost) > thresholdEdits))
					break;
		}
		if (rows[numRows-1] != null)
			setSolution(rows[numRows-1].columns[numCols-1].value);
		return solution;
	}
	
	public void setSolution(Integer solution) {
		this.solution = solution;
		this.similarity = 1.0 - ((double) solution / maxEdits);
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

}
