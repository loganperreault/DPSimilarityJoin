package edit_distance;

public class DPTableTree {
	
	DPTableRow root;
	
	public DPTableTree() {
		
		root = DPTableRow.getRoot();
		
		root.addChild("a").addChild("x");
		DPTableRow b = root.addChild("b");
		b.addChild("y");
		b.addChild("z");
		
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
