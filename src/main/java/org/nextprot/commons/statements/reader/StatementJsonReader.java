package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public abstract class StatementJsonReader {

	private final Reader content;
	private final StatementSpecifications specifications;

	public StatementJsonReader(Reader content, StatementSpecifications specifications) {

		this.content = content;
		this.specifications = specifications;
	}

	protected final Reader getContent() {
		return content;
	}

	protected final StatementSpecifications getSpecifications() {
		return specifications;
	}

	public abstract List<Statement> readStatements() throws IOException;
}
