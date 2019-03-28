package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.StatementField;
import org.nextprot.commons.statements.reader.JsonReader;

import java.util.Collection;


/**
 * A schema defines all fields that describe a statement provided by a
 * given external neXtProt source.
 */
public interface Schema {

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

	/** @return a new instance of JsonReader that create Statements from json String */
	default JsonReader jsonReader() {

		return new JsonReader(this);
	}
}
