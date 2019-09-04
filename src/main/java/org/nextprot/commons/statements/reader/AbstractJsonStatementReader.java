package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.util.List;


/**
 * Base class to read Statements
 */
public abstract class AbstractJsonStatementReader implements StatementReader {

	private final StatementSpecifications specifications;

	public AbstractJsonStatementReader(StatementSpecifications specifications) {

		this.specifications = specifications;
	}

	@Override
	public final StatementSpecifications getSpecifications() {
		return specifications;
	}

	@Override
	public int readStatements(List<Statement> buffer) throws IOException {

		List<Statement> statements = readStatements();
		buffer.addAll(statements);
		return statements.size();
	}
}
