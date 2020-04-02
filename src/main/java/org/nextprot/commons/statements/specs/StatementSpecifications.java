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
	Collection<StatementField> getCoreFields();
	Collection<StatementField> getCustomFields();

	/** @return the number of fields */
	int size();

}
