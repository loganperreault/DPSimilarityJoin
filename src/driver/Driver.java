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
		
		String baseString = "Kurt Russell";
		DPTableRow.setSize(baseString.length() + 1);
		Map.Entry<String, Integer> base = new AbstractMap.SimpleEntry<String, Integer>(baseString, 1);
		//Predicate starring = new Predicate("data/starring.txt");
		List<String> actors = new ArrayList<>();
		actors.add("Kurt BRussle");
		actors.add("Henry Russell");
		actors.add("Samuel Jackson");
		actors.add("Samuel L Jackson");
		actors.add("Kurt Russel");
		actors.add("Kurt Russell");
		Predicate starring = new Predicate();
		starring.populate(actors);
		
		// for methods using different parameters for MultiEditDistance
		MultiEditDistance medBase = new MultiEditDistance(false, false, false);
		MultiEditDistance medRejected = new MultiEditDistance(true, false, false);
		MultiEditDistance medAccepted = new MultiEditDistance(false, true, false);
		MultiEditDistance medEarlyStopping = new MultiEditDistance(true, true, false);
		MultiEditDistance medTree = new MultiEditDistance(false, false, true);
		MultiEditDistance medAll = new MultiEditDistance(true, true, true);
		
		// put desired methods in a hashmap for testing
		Map<String, MultiEditDistance> methods = new LinkedHashMap<String, MultiEditDistance>();
		methods.put("Base", medBase);
		methods.put("Early Stopping", medEarlyStopping);
		methods.put("Tree", medTree);
		methods.put("All", medAll);
		
		runExperiments(methods, starring, base);
		
	}
	
	public static void runExperiments(Map<String, MultiEditDistance> methods, Predicate predicate1, Map.Entry<String, Integer> base) {
		// print out the test results
		System.out.print("       ");
		for (Map.Entry<String, MultiEditDistance> method : methods.entrySet()) {
			String name = method.getKey();
			System.out.print("| "+name+" ");
			MultiEditDistance med = method.getValue();
			med.findSimilar(predicate1, base);
			DPTableRow.reset();
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
