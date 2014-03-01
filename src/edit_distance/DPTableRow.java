package edit_distance;

import java.util.HashMap;
import java.util.Map;

public class DPTableRow {
	
	private static DPTableRow root = null;
	protected DPTableRow parent = null;
	protected Map<String, DPTableRow> children = new HashMap<>();
	protected String value = null;
	protected int index = 0;
	protected int[] columns;
	protected static int size = 0;
	
	public static DPTableRow getRoot() {
		if (size == 0)
			throw new IllegalArgumentException("Must call setSize() first.");
		if (root == null)
			root = new DPTableRow(size);
		return root;
	}
	
	private DPTableRow(int numCols) {
		value = "ROOT";
		columns = new int[numCols];
		for (int i = 0; i < numCols; i++)
			columns[i] = i;
	}
	
	public static void setSize(int sz) {
		if (root != null)
			throw new IllegalArgumentException("Must be called prior to first getRoot() call.");
		size = sz;
	}
	
	public DPTableRow(DPTableRow parent, String value, int index) {
		if (value.length() != 1)
			throw new IllegalArgumentException("Value must be a single character.");
		if (parent == null)
			throw new IllegalArgumentException("Parent cannot be null.");
		this.parent = parent;
		this.value = value;
		parent.addChild(this);
		columns[0] = index*MultiEditDistance.deletionCost;
	}
	
	protected DPTableRow addChild(String value) {
		DPTableRow child = new DPTableRow(this, value, index++);
		children.put(child.value, child);
		return child;
	}
	
	protected DPTableRow addChild(DPTableRow child) {
		children.put(child.value, child);
		return child;
	}
	
	public String toString() {
		return value;
	}
	
	public String getRowString(int[] columnWidths) {
		String row = "";
		for (int i = 0; i < columns.length; i++) {
			row += String.format("%1$" + columnWidths[i] + "s ", columns[i]*MultiEditDistance.insertionCost) + "|";
		}
		return row;
	}

}
