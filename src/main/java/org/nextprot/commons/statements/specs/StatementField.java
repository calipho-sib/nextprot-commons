package org.nextprot.commons.statements.specs;


public interface StatementField {

	/** @return the field name */
	String getName();

	/** @return true if this field is contributed to the unique key calculation */
	boolean isPartOfAnnotationUnicityKey();

	/** @return its String representation */
	default String valueAsString(Object value) {

		if (value instanceof String) {
			return (String) value;
		}
		return String.valueOf(value);
	}
}
