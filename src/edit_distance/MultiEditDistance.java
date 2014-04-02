package edit_distance;

import java.util.HashMap;
import java.util.Map;

import driver.Predicate;

public class MultiEditDistance {
	
	//protected static int insertionCost = 1;
	//protected static int deletionCost = 1;
	//protected static int substitutionCost = 1;
	//protected static int matchCost = 0;
	//protected static boolean maximizing = false;
	
	double threshold = 0.6;
	protected boolean rejectedEarlyStopping = true;
	protected boolean acceptedEarlyStopping = true;
	protected boolean useTree = true;
	protected boolean efficientJoin = true;
	protected int cellCount = 0;
	protected long runtime = 0;
	protected Map<Map.Entry<String, Integer>, Map.Entry<String, Integer>> mapping = new HashMap<>();
	
	public MultiEditDistance() {
		
	}
	
	public MultiEditDistance(boolean rejectedEarlyStopping, boolean acceptedEarlyStopping, boolean useTree, boolean efficientJoin) {
		setOptions(rejectedEarlyStopping, acceptedEarlyStopping, useTree, efficientJoin);
	}
	
	public void setOptions(boolean rejectedEarlyStopping, boolean acceptedEarlyStopping, boolean useTree, boolean efficientJoin) {
		this.rejectedEarlyStopping = rejectedEarlyStopping;
		this.acceptedEarlyStopping = acceptedEarlyStopping;
		this.useTree = useTree;
		this.efficientJoin = efficientJoin;
	}
	
	public Map<Map.Entry<String, Integer>, Map.Entry<String, Integer>> mapSimilar(Predicate predicate1, Predicate predicate2) {
		
		for (Map.Entry<String, Integer> pair = predicate1.getStart(); pair != predicate1.getLast(); pair = predicate1.getNext()) {
			findSimilar(pair, predicate2);
		}
		
		return mapping;
	}
	
	public Map<Map.Entry<String, Integer>, Map.Entry<String, Integer>> findSimilar(Map.Entry<String, Integer> word, Predicate predicate) {
	     
	    // setup
	    String string1 = word.getKey();
	    
	    DPTableRow.setSize(string1.length() + 1);
		//DPTableRow.reset();
	    
	    // loop through all words in the target predicate
	    int i;
	    Map.Entry<String, Integer> pair = null;
	    for (i = 0, pair = predicate.getStart(); pair != predicate.getLast(); pair = predicate.getNext(), i++) {
	    	String string2 = pair.getKey();
	    	
	    	if (string2.isEmpty())
	    		continue;
	    	
	    	DPTable table = new DPTable(string1, string2);
	    	
	    	if (i == 0) {
	    		if (efficientJoin) {
	    			DPTable.swapWord(word.getKey());
	    		} else {
	    			DPTable.setWord(word.getKey());
	    		}
	    	}
	    	
	    	long startTime = System.currentTimeMillis();
	    	table.calculate(threshold, rejectedEarlyStopping, acceptedEarlyStopping, useTree);
	    	runtime += System.currentTimeMillis() - startTime;
	    	cellCount += table.getCellCount();
	    	
	    	//System.out.println(i+": "+string1+" vs "+string2);
	    	//System.out.println(table);
	    	//System.out.println("SIMILARITY: "+table.getSimilarity());
	    	//System.out.println("ACCEPTED: "+table.accepted());
	        
	        //break;
	        
	    }
	    
	    return mapping;
	    
	}
	
	public int getCellCount() {
		return cellCount;
	}
	 
	public void setThreshold(double threshold) {
		 this.threshold = threshold;
	}
	
	/**
	 * @return	The total computation time in milliseconds
	 */
	public long getRuntime() {
		return runtime;
	}

}
