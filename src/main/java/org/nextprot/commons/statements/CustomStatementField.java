package org.nextprot.commons.statements;


public class CustomStatementField implements StatementField {

	private String name;
	private boolean isPartOfUnicityKey;

	public CustomStatementField(String name) {

		this(name, true);
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
	public boolean isPartOfUnicityKey() {
		return isPartOfUnicityKey;
	}

	@Override
	public String toString() {

		return name;
	}
}
