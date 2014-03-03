package driver;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edit_distance.DPTableTree;
import edit_distance.MultiEditDistance;

public class Driver {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//DPTableTree tree = new DPTableTree();
		
		//String base = "Kurt Russell";
		Map.Entry<String, Integer> base = new AbstractMap.SimpleEntry<String, Integer>("Kurt Russell", 1);
		//Predicate starring = new Predicate("data/starring.txt");
		List<String> actors = new ArrayList<>();
		actors.add("Kurt BRussle");
		//actors.add("Samuel Jackson");
		//actors.add("Samuel L Jackson");
		//actors.add("Kurt Russel");
		//actors.add("Kurt Russell");
		Predicate starring = new Predicate();
		starring.populate(actors);
		
		MultiEditDistance med = new MultiEditDistance();
		med.findSimilar(starring, base);
		
	}

}
