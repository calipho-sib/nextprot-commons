package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.StatementField;

import java.util.Collection;

public interface Schema {

	boolean hasField(String field);
	Collection<StatementField> getFields();
	StatementField getField(String field);
	int size();
}
