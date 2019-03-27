package org.nextprot.commons.statements;


import java.util.Objects;

public class CustomStatementField implements StatementField {

	private String name;
	private boolean isPartOfUnicityKey;

	public CustomStatementField(String name) {

		this(name, false);
	}

	public CustomStatementField(String name, boolean isPartOfUnicityKey) {

		this.name = name;
		this.isPartOfUnicityKey = isPartOfUnicityKey;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public boolean isPartOfAnnotationUnicityKey() {

		return isPartOfUnicityKey;
	}

	@Override
	public boolean isNXFlatTableColumn() {

		return false;
	}

	@Override
	public String toString() {

		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomStatementField field = (CustomStatementField) o;
		return isPartOfUnicityKey == field.isPartOfUnicityKey &&
				Objects.equals(name, field.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, isPartOfUnicityKey);
	}
}
