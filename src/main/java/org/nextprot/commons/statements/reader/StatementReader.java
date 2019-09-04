package org.nextprot.commons.statements.reader;

import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface StatementReader extends Closeable {

	StatementSpecifications getSpecifications();

	/**
	 * Read Statements and return the list.
	 * @return the list of statements.
	 * @throws IOException if an I/O error occurs or if the stream is closed
	 */
	List<Statement> readStatements() throws IOException;

	/**
	 * Read Statements into the specified buffer.
	 *
	 * @param buffer the buffer to read Statements into
	 * @return The number of {@code Statement} values added to the buffer
	 * @throws IOException if an I/O error occurs or if the stream is closed
	 * @throws NullPointerException if buffer is null
	 */
	int readStatements(List<Statement> buffer) throws IOException;
}
