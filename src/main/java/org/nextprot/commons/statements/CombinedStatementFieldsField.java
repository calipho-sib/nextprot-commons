package org.nextprot.commons.statements;

import java.util.HashSet;
import java.util.Set;

/**
 * A field that is the combination of other fields
 *
 * The main use-case is when we need to add more informations via a supplementary column in the nxflat db
 * that correspond to a combo of multiple fields where the value will be in the form of a map indexed by field name
 */
public class CombinedStatementFieldsField implements StatementField {

	private final String name;
	private final Set<StatementField> fields;

	public CombinedStatementFieldsField(String name, Set<StatementField> fields) {

		this.name = name;
		this.fields = new HashSet<>(fields);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean isPartOfUnicityKey() {

		return true;
	}
}
