package edit_distance;

import java.util.Map;

import driver.Predicate;

public class MultiEditDistance {
	
	//protected static int insertionCost = 1;
	//protected static int deletionCost = 1;
	//protected static int substitutionCost = 1;
	//protected static int matchCost = 0;
	//protected static boolean maximizing = false;
	
	double threshold = 0.6;
	protected static boolean rejectedEarlyStopping = true;
	protected static boolean acceptedEarlyStopping = true;
	protected static boolean useTree = false;
	protected static int cellCount = 0;
	
	public MultiEditDistance() {
		
	}
	
	public MultiEditDistance(boolean rejectedEarlyStopping, boolean acceptedEarlyStopping, boolean useTree) {
		setOptions(rejectedEarlyStopping, acceptedEarlyStopping, useTree);
	}
	
	public void setOptions(boolean rejectedEarlyStopping, boolean acceptedEarlyStopping, boolean useTree) {
		MultiEditDistance.rejectedEarlyStopping = rejectedEarlyStopping;
		MultiEditDistance.acceptedEarlyStopping = acceptedEarlyStopping;
		MultiEditDistance.useTree = useTree;
	}
	
	public Map.Entry<String, Integer> findSimilar(Predicate predicate, Map.Entry<String, Integer> word) {
	     
	    // setup
	    Map.Entry<String, Integer> pair2, bestMatch = null;
	    String string1 = word.getKey();
	    double bestMatchValue = 0.0;
	    
	    // loop through all words in the target predicate
	    int i;
	    for (i = 0, pair2 = predicate.getStart(); pair2 != predicate.getLast(); pair2 = predicate.getNext(), i++) {
	    	String string2 = pair2.getKey();
	    	
	    	if (string2.isEmpty())
	    		continue;
	    	
	    	DPTable table = new DPTable(word.getKey(), pair2.getKey());
	    	if (rejectedEarlyStopping)
	    		table.calculate(threshold, acceptedEarlyStopping);
	    	else
	    		table.calculate();
	    	
	    	System.out.println(string1+" vs "+string2);
	    	System.out.println(table);
	    	System.out.println("SIMILARITY: "+table.getSimilarity());
	    	System.out.println("ACCEPTED: "+table.accepted());
	        
	        //break;
	        
	    }
	    
	    // return the best
	    if (bestMatchValue > threshold)
            return bestMatch;
    	else
            return null;
	    
	}
	
	public int getCellCount() {
		return cellCount;
	}
	 
	public void setThreshold(double threshold) {
		 this.threshold = threshold;
	}

}
