package org.nextprot.commons.statements;

public interface StatementField {

	String getName();
	boolean isPartOfAnnotationUnicityKey();

	default String valueAsString(Object value) {

		if (value instanceof String) {
			return (String) value;
		}
		return String.valueOf(value);
	}
}
