package edit_distance;

import java.util.HashMap;
import java.util.Map;

public class DPTableRow {
	
	private static DPTableRow root = null;
	protected Map<Character, DPTableRow> children = new HashMap<>();
	protected Character value = null;
	protected int index = 0;
	protected Integer[] columns;
	protected static int size = 0;
	
	public static DPTableRow getRoot() {
		if (size == 0)
			throw new IllegalArgumentException("Must call setSize() first.");
		if (root == null)
			root = new DPTableRow(size);
		return root;
	}
	
	private DPTableRow(int numCols) {
		value = null;
		columns = new Integer[numCols];
		for (int i = 0; i < numCols; i++)
			columns[i] = i*MultiEditDistance.insertionCost;
	}
	
	public static void setSize(int sz) {
		if (root != null)
			throw new IllegalArgumentException("Must be called prior to first getRoot() call.");
		size = sz;
	}
	
	private DPTableRow(char value, int index, int size) {
		this.index = index;
		this.size = size;
		columns = new Integer[size];
		this.value = value;
		columns[0] = index*MultiEditDistance.deletionCost;
	}
	
	protected DPTableRow addChild(char value) {
		DPTableRow child = new DPTableRow(value, ++index, size);
		children.put(child.value, child);
		return child;
	}
	
	public String toString() {
		return String.valueOf(value);
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
