package edit_distance;

public class DPTableTree {
	
	DPTableRow root;
	
	public DPTableTree(DPTableRow root) {
		
		this.root = root;
		
	}
	
	public void printTree() {
		printSubTree(root, 0);
	}
	
	public void printSubTree(DPTableRow parent, int padding) {
		System.out.println((padding>0?String.format("%"+padding+"s", ""):"")+parent);
		for (DPTableRow child : parent.children.values())
			printSubTree(child, padding+2);
	}

}
