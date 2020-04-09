package org.nextprot.commons.statements;

import java.util.Comparator;
import org.nextprot.commons.statements.specs.StatementField;

public class StatementFieldComparator implements Comparator<StatementField> {

	static StatementFieldComparator instance = new StatementFieldComparator();

	public static StatementFieldComparator getInstance() { return instance; }
	
	@Override
	public int compare(StatementField o1, StatementField o2) {
		String name1 = o1==null ? "" : o1.getName();
		String name2 = o2==null ? "" : o2.getName();
		return name1.compareTo(name2);	
	}

}

