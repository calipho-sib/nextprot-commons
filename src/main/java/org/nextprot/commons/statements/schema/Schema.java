package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.StatementField;

import java.util.Collection;

public interface Schema {

	StatementField getField(String field);
	boolean hasField(String field);
	Collection<StatementField> getFields();
	int size();
}
