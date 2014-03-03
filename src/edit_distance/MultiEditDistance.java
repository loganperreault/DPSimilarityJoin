package edit_distance;

import java.util.Map;

import driver.Predicate;

public class MultiEditDistance {
	
	double threshold = 0.6;
	protected static int insertionCost = 1;
	protected static int deletionCost = 1;
	protected static int substitutionCost = 1;
	protected static int matchCost = 0;
	protected static boolean maximizing = false;
	
	public Map.Entry<String, Integer> findSimilar(Predicate predicate, Map.Entry<String, Integer> word) {
	     
	    // setup
	    Map.Entry<String, Integer> pair2, bestMatch = null;
	    String pair1value = word.getKey();
	    double bestMatchValue = 0.0;
	    
	    // loop through all words in the target predicate
	    int i;
	    for (i = 0, pair2 = predicate.getStart(); pair2 != predicate.getLast(); pair2 = predicate.getNext(), i++) {
	    	
	    	if (pair2.getKey().isEmpty())
	    		continue;
	    	
	        // get the similarity value from the string comparison class
	        //double value = compare.compare(pair1value, pair2.getKey());
	    	double value = 1.0;
	    	
	    	System.out.println(word.getKey()+" vs "+pair2.getKey());
	    	
	    	DPTable table = new DPTable(word.getKey(), pair2.getKey());
	    	table.calculate(threshold);
	    	//System.out.println(table);
	    	System.out.println("SIMILARITY: "+table.getSimilarity());
	    	
	    	// store the most similar word so far in a variable
	        if (value > bestMatchValue) {
                bestMatchValue = value;
                bestMatch = pair2;
	        }
	        
	        //break;
	        
	    }
	    
	    // return the best
	    if (bestMatchValue > threshold)
            return bestMatch;
    	else
            return null;
	    
	}
	 
	public void setThreshold(double threshold) {
		 this.threshold = threshold;
	}

}
