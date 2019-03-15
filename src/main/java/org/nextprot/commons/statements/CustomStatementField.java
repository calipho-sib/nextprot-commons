package org.nextprot.commons.statements;

public class CustomStatementField implements StatementField {

	private final String name;
	private final boolean isPartOfUnicityKey;

	public CustomStatementField(String name, boolean isPartOfUnicityKey) {

		this.name = name;
		this.isPartOfUnicityKey = isPartOfUnicityKey;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean isPartOfUnicityKey() {
		return isPartOfUnicityKey;
	}
}
