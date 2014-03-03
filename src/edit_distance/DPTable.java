package edit_distance;

public class DPTable {
	
	String string1, string2;
	int numRows, numCols;
	DPTableRow[] rows;
	int[] columnWidths;
	DPTableRow root;
	DPTableTree tree;
	
	public DPTable(String string1, String string2) {
		this.string1 = string1;
		this.string2 = string2;
		numCols = string1.length() + 1;
		numRows = string2.length() + 1;
		rows = new DPTableRow[numRows];
		columnWidths = new int[numCols];
		
		int indexPaddingSize = String.valueOf(numCols).length() + 1;
		for (int i = 0; i < numCols; i++)
			columnWidths[i] = indexPaddingSize;
		
		DPTableRow.setSize(numCols);
		root = DPTableRow.getRoot();
		tree = new DPTableTree(root);
		
		calculate();
	}
	
	public void calculate() {
		rows[0] = root;
		for (int i = 1; i < numRows; i++) {
			rows[i] = rows[i-1].addChild(string2.charAt(i-1));
			for (int j = 1; j < numCols; j++) {
				int diag = rows[i-1].columns[j-1].value;
				int left = rows[i].columns[j-1].value;
				int top  = rows[i-1].columns[j].value;
				int insert = left + MultiEditDistance.insertionCost;
				int delete = top  + MultiEditDistance.deletionCost;
				int swap   = diag + MultiEditDistance.substitutionCost;
				int match = Integer.MAX_VALUE;
				if (string1.charAt(j-1) == string2.charAt(i-1))
					match  = diag + MultiEditDistance.matchCost;
				int min = Math.min(insert, Math.min(delete, Math.min(swap, match)));
				rows[i].columns[j] = new DPTableCell(min);
				if (min == insert)
					rows[i].columns[j].solutions.add(rows[i-1].columns[j]);
				else if (min == delete)
					rows[i].columns[j].solutions.add(rows[i].columns[j-1]);
				else if (min == swap)
					rows[i].columns[j].solutions.add(rows[i-1].columns[j-1]);
				else if (min == match)
					rows[i].columns[j].solutions.add(rows[i-1].columns[j-1]);
					
			}
			//break;
		}
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
