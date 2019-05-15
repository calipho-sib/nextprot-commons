package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.util.List;

public abstract class StatementJsonReader {

	private final StatementSpecifications specifications;

	public StatementJsonReader(StatementSpecifications specifications) {

		this.specifications = specifications;
	}

	protected final StatementSpecifications getSpecifications() {
		return specifications;
	}

	public abstract List<Statement> readStatements() throws IOException;
}
