package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.Statement;

import java.io.IOException;

public interface BufferedStatementReader extends Specifiable {

	/** @return the next statement of null if no more statements */
	Statement nextStatement() throws IOException;

	/** @return true if has more statements */
	boolean hasStatement() throws IOException;
}
