package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.util.List;

public abstract class StatementReader implements Specifiable {

	private final StatementSpecifications specifications;

	public StatementReader(StatementSpecifications specifications) {

		this.specifications = specifications;
	}

	@Override
	public final StatementSpecifications getSpecifications() {
		return specifications;
	}

	public abstract List<Statement> readStatements() throws IOException;
}
