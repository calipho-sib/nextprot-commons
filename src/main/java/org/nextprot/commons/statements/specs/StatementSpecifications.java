package org.nextprot.commons.statements.specs;

import java.util.Collection;


/**
 * Defines all fields that describe a statement provided by a
 * given external neXtProt source.
 */
public interface StatementSpecifications {

	/** @return the statement field named fieldName */
	StatementField getField(String fieldName);

	/** @return true schema has a field named fieldName */
	boolean hasField(String fieldName);

	/** @return the collection of statement fields */
	Collection<StatementField> getFields();

	/** @return the number of fields */
	int size();

	/** @return the composite field that contain the given field or null if absent */
	CompositeField searchCompositeFieldOrNull(StatementField field);
}
