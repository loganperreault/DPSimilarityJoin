package driver;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edit_distance.DPTableRow;
import edit_distance.MultiEditDistance;

public class Driver {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO: Setup predicate to predicate matches, not just predicate to entry (join, not search)
		// TODO: look into pruning tree if it gets too expensive to maintain (use DPTableTree class)
		
//		String baseString = "Kurt Russell";
//		DPTableRow.setSize(baseString.length() + 1);
//		Map.Entry<String, Integer> base = new AbstractMap.SimpleEntry<String, Integer>(baseString, 1);
		//Predicate starring = new Predicate("data/starring.txt");
		
		Predicate predicate1 = getToyPredicate1();
		Predicate predicate2 = getToyPredicate2();
		
		// for methods using different parameters for MultiEditDistance
		MultiEditDistance medBase = new MultiEditDistance(false, false, false, false);
		MultiEditDistance medRejected = new MultiEditDistance(true, false, false, false);
		MultiEditDistance medAccepted = new MultiEditDistance(false, true, false, false);
		MultiEditDistance medEarlyStopping = new MultiEditDistance(true, true, false, false);
		MultiEditDistance medTree = new MultiEditDistance(false, false, true, false);
		MultiEditDistance medEarlyStoppingTree = new MultiEditDistance(true, true, true, false);
		MultiEditDistance medEfficientJoin = new MultiEditDistance(true, true, true, true);
		
		// put desired methods in a hashmap for testing
		Map<String, MultiEditDistance> methods = new LinkedHashMap<String, MultiEditDistance>();
		methods.put("Base", medBase);
		methods.put("Early Stopping", medEarlyStopping);
		methods.put("Tree", medTree);
		methods.put("Early Stopping Tree", medEarlyStoppingTree);
		methods.put("Efficient Join", medEfficientJoin);
		
		runExperiments(methods, predicate1, predicate2, false);
		
	}
	
	public static void runExperiments(Map<String, MultiEditDistance> methods, Predicate predicate1, Predicate predicate2, boolean silent) {
		
		// run tests
		for (Map.Entry<String, MultiEditDistance> method : methods.entrySet()) {
			MultiEditDistance med = method.getValue();
			med.mapSimilar(predicate1, predicate2);
			DPTableRow.reset();
		}
		
		// print out the test results
		if (!silent) {
			System.out.print("       ");
			for (Map.Entry<String, MultiEditDistance> method : methods.entrySet()) {
				String name = method.getKey();
				System.out.print("| "+name+" ");
			}
			System.out.println();
			System.out.print("CELLS: ");
			for (Map.Entry<String, MultiEditDistance> method : methods.entrySet()) {
				String name = method.getKey();
				MultiEditDistance med = method.getValue();
				System.out.format("  %"+name.length()+"d ", med.getCellCount());
			}
			System.out.println();
			System.out.print(" TIME: ");
			for (Map.Entry<String, MultiEditDistance> method : methods.entrySet()) {
				String name = method.getKey();
				MultiEditDistance med = method.getValue();
				System.out.format("  %"+name.length()+"d ", med.getRuntime());
			}
			System.out.println();
		}
	}
	
	public static Predicate getToyPredicate1() {
		List<String> strings1 = new ArrayList<>();
		strings1.add("Kurt Russell");
		strings1.add("Kurt Rupert");
		strings1.add("Samuel L Jackson");
		strings1.add("Humphrey Bogart");
		Predicate predicate = new Predicate();
		predicate.populate(strings1);
		return predicate;
	}
	
	public static Predicate getToyPredicate2() {
		List<String> strings2 = new ArrayList<>();
		strings2.add("Henry Russell");
		strings2.add("Samuel Jackson");
		strings2.add("Sam Jackson");
		strings2.add("Kurt Russel abc");
		Predicate predicate = new Predicate();
		predicate.populate(strings2);
		return predicate;
	}

}
