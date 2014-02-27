package edit_distance;

import java.util.HashMap;
import java.util.Map;

public class DPTableRow {
	
	private static DPTableRow root = null;
	protected DPTableRow parent = null;
	protected Map<String, DPTableRow> children = new HashMap<>();
	protected String value = null;
	
	public static DPTableRow getRoot() {
		if (root == null)
			root = new DPTableRow();
		return root;
	}
	
	private DPTableRow() {
		value = "ROOT";
	}
	
	public DPTableRow(DPTableRow parent, String value) {
		if (value.length() != 1)
			throw new IllegalArgumentException("Value must be a single character.");
		if (parent == null)
			throw new IllegalArgumentException("Parent cannot be null.");
		this.parent = parent;
		this.value = value;
		parent.addChild(this);
	}
	
	protected DPTableRow addChild(String value) {
		DPTableRow child = new DPTableRow(this, value);
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

}
