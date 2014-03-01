package edit_distance;

public class DPTable {
	
	String string1, string2;
	int numRows, numCols;
	DPTableRow[] rows;
	int[] columnWidths;
	DPTableRow root;
	
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
		DPTableRow root = DPTableRow.getRoot();
		rows[0] = root;
	}
	
	public void calculate() {
		
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
