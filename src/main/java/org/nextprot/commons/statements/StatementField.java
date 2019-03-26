package org.nextprot.commons.statements;


import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.nextprot.commons.statements.schema.Schema;

public interface StatementField {

	/** @return the field name */
	String getName();

	/** @return true if this field is contributed to the unique key calculation */
	boolean isPartOfAnnotationUnicityKey();

	/** @return true if this field is to be created as a DB column in nxflat tables */
	boolean isNXFlatTableColumn();

	/** @return its String representation */
	default String valueAsString(Object value) {

		if (value instanceof String) {
			return (String) value;
		}
		return String.valueOf(value);
	}
}
