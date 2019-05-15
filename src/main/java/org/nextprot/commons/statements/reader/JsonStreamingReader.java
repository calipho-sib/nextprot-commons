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

	private static final int DEFAULT_MAX_BUFFER_SIZE = 100;

	private final JsonParser parser;
	private final int maxBufferSize;

	public JsonStreamingReader(Reader url) throws IOException {

		this(url, new Specifications.Builder().build(), DEFAULT_MAX_BUFFER_SIZE);
	}

	public JsonStreamingReader(Reader url, int maxBufferSize) throws IOException {

		this(url, new Specifications.Builder().build(), maxBufferSize);
	}

	public JsonStreamingReader(Reader url, StatementSpecifications specifications, int maxBufferSize) throws IOException {

		super(specifications);
		JsonFactory factory = new JsonFactory();

		parser = factory.createParser(url);

		// consume and test the first token
		JsonToken token = parser.nextToken();
		if (token != JsonToken.START_ARRAY && token != JsonToken.START_OBJECT) {

			throw new IOException("not a valid json content");
		}

		if (maxBufferSize <= 0) {
			throw new IllegalArgumentException("maxBufferSize="+maxBufferSize+": cannot define a negative (or 0) number for the buffer size ");
		}

		this.maxBufferSize = maxBufferSize;
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
	public Optional<Statement> readOneStatement() throws IOException {

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
	 * Each call return at most n statements
	 */
	@Override
	public List<Statement> readStatements() throws IOException {

		List<Statement> statements = new ArrayList<>();

		for (int i = 0; i < maxBufferSize; i++) {

			Optional<Statement> statement = readOneStatement();

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

		if (getSpecifications().hasField(key)) {
			return getSpecifications().getField(key);
		}
		return new CustomStatementField(key);
	}
}
