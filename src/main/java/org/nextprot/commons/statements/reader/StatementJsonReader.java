package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.specs.StatementSpecifications;

public abstract class StatementJsonReader {

	protected final StatementSpecifications specifications;

	public StatementJsonReader(StatementSpecifications specifications) {

		this.specifications = specifications;
	}
}
