package org.nextprot.commons.statements.reader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.StatementBuilder;
import org.nextprot.commons.statements.specs.CustomStatementField;
import org.nextprot.commons.statements.specs.Specifications;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This reader read statements from a json content one by one or
 * n at a time and close it self when all have been red
 */
public class JsonStreamingReader extends StatementJsonReader {

	private final JsonParser parser;

	public JsonStreamingReader(Reader content) throws IOException {

		this(content, new Specifications.Builder().build());
	}

	public JsonStreamingReader(Reader content, StatementSpecifications specifications) throws IOException {

		super(specifications);
		JsonFactory factory = new JsonFactory();

		parser = factory.createParser(content);

		// consume and test the first token
		JsonToken token = parser.nextToken();
		if (token != JsonToken.START_ARRAY && token != JsonToken.START_OBJECT) {

			throw new IOException("not a valid json content");
		}
	}

	/**
	 * @return true if some more statements to read
	 */
	public boolean hasNextStatement() {

		return !parser.isClosed() && parser.getCurrentToken() != null
				&& parser.getCurrentToken() != JsonToken.END_ARRAY;
	}

	/**
	 * Read one statement only
	 * @return a statement or empty if not more statements to read
	 * @throws IOException
	 */
	public Optional<Statement> readStatement() throws IOException {

		if (parser.isClosed()) {
			return Optional.empty();
		}

		StatementBuilder statementBuilder = new StatementBuilder();

		JsonToken token;
		while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {

			if (token != JsonToken.START_OBJECT && token != JsonToken.START_ARRAY) {

				String key = parser.getCurrentName();
				String value = parser.getValueAsString();
				statementBuilder.addField(getKey(key), value);
			}
		}

		// consume next token (should be either null, } or ])
		token = parser.nextToken();

		if (token == null || token == JsonToken.END_ARRAY) {
			parser.close();
		}

		return Optional.ofNullable(statementBuilder.build());
	}

	/**
	 * Read n statements
	 * @return a maximum of n statements
	 * @throws IOException
	 */
	public List<Statement> readStatements(int n) throws IOException {

		if (n <= 0) {
			throw new IllegalArgumentException("n="+n+": cannot read a negative (or 0) number of statements ");
		}

		List<Statement> statements = new ArrayList<>();

		for (int i = 0; i < n; i++) {

			Optional<Statement> statement = readStatement();

			if (statement.isPresent()) {
				statements.add(statement.get());
			}
			else {
				break;
			}
		}

		return statements;
	}

	private StatementField getKey(String key) {

		if (specifications.hasField(key)) {
			return specifications.getField(key);
		}
		return new CustomStatementField(key);
	}
}
