package edit_distance;

import java.util.HashMap;
import java.util.Map;

public class DPTableRow {
	
	private static DPTableRow root = null;
	protected Map<Character, DPTableRow> children = new HashMap<>();
	protected Character character = null;
	protected int index = 0;
	protected DPTableCell[] columns;
	protected static int size = 0;
	protected boolean complete = false;
	
	// allows recreation of root for use in experiments
	public static void reset() {
		root = null;
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
		if (root != null)
			throw new IllegalArgumentException("Must be called prior to first getRoot() call.");
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

}
