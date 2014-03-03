package edit_distance;

import java.util.ArrayList;
import java.util.List;

public class DPTableCell {
	
	Integer value;
	List<DPTableCell> solutions = new ArrayList<>();

	DPTableCell(Integer value) {
		this.value = value;
	}
	
	public String toString() {
		return String.valueOf(value);
	}

}
