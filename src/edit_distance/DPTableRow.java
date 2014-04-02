package edit_distance;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DPTableRow {
	
	private static DPTableRow root = null;
	protected Map<Character, DPTableRow> children = new HashMap<>();
	protected Character character = null;
	protected int index = 0;
	protected DPTableCell[] columns;
	protected static int size = 0;
	protected int calculated = 1;
	protected static String str = null;
	
	// allows recreation of root for use in experiments
	public static void reset() {
		root = null;
		str = null;
		size = 0;
	}
	
	public static DPTableRow getRoot() {
		if (size == 0)
			throw new IllegalArgumentException("Must call setSize() first.");
		if (root == null)
			root = new DPTableRow(size);
		return root;
	}
	
	private DPTableRow(int numCols) {
		character = null;
		columns = new DPTableCell[numCols];
		for (int i = 0; i < numCols; i++)
			columns[i] = new DPTableCell(i);
	}
	
	public static void setSize(int sz) {
//		if (root != null)
//			throw new IllegalArgumentException("Must be called prior to first getRoot() call.");
		size = sz;
	}
	
	private DPTableRow(char value, int index, int size) {
		this.index = index;
		this.size = size;
		columns = new DPTableCell[size];
		this.character = value;
		columns[0] = new DPTableCell(index);
	}
	
	protected DPTableRow addChild(char value) {
		// or add the new child
		DPTableRow child = new DPTableRow(value, ++index, size);
		children.put(child.character, child);
		return child;
	}
	
	protected DPTableRow getChild(char value) {
		// look to see if the child already exists
		DPTableRow child = children.get(value);
		return child;
	}
	
	public String toString() {
		return String.valueOf(character);
	}
	
	public String getRowString(int[] columnWidths) {
		String row = "";
		for (int i = 0; i < columns.length; i++) {
			String val;
			if (columns[i] != null)
				val = String.valueOf(columns[i]);
			else
				val = "XX";
			row += String.format("%1$" + columnWidths[i] + "s ", val) + "|";
		}
		return row;
	}
	
	public static void swapWord(String word) {
		if (str == null) {
			setWord(word);
		} else {
			setSize(word.length() + 1);
			DPTableCell[] newColumn = new DPTableCell[word.length() + 1];
			for (int i = 0; i < newColumn.length; i++)
				newColumn[i] = new DPTableCell(i);
			root.columns = newColumn;
			for (Entry<Character, DPTableRow> childRow : getRoot().children.entrySet()) {
				transferRow(childRow.getValue(), word);
			}
			str = word;
		}
	}
	
	private static void transferRow(DPTableRow row, String word) {
		
		DPTableCell[] newColumns = new DPTableCell[word.length() + 1];
		int i;
		newColumns[0] = row.columns[0];
		for (i = 0; i < Math.min(str.length(), word.length()); i++) {
			if (str.charAt(i) == word.charAt(i))
				newColumns[i+1] = row.columns[i+1];
			else
				break;
		}
		
		row.columns = newColumns;
		row.calculated = i;
		
		for (Entry<Character, DPTableRow> childRow : row.children.entrySet())
			transferRow(childRow.getValue(), word);
	}
	
	public static void setWord(String word) {
		reset();
		str = word;
		setSize(word.length() + 1);
	}

}
