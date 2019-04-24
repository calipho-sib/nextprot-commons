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


public class JsonStreamingReader {

	private final JsonParser parser;
	private final StatementSpecifications specifications;

	public JsonStreamingReader(Reader content) throws IOException {

		this(content, new Specifications.Builder().build());
	}

	public JsonStreamingReader(Reader content, StatementSpecifications specifications) throws IOException {

		this.specifications = specifications;
		JsonFactory factory = new JsonFactory();

		parser = factory.createParser(content);
	}

	public Optional<Statement> readStatement() throws IOException {

		StatementBuilder statementBuilder = new StatementBuilder();

		JsonToken token;
		while ((token = parser.nextToken()) != JsonToken.END_OBJECT) {

			if (token == JsonToken.END_ARRAY) {
				parser.close();
				return Optional.empty();
			}

			if (token != JsonToken.START_OBJECT && token != JsonToken.START_ARRAY) {
				String key = parser.getCurrentName();
				String value = parser.getValueAsString();
				statementBuilder.addField(getKey(key), value);
			}
		}

		return Optional.ofNullable(statementBuilder.build());
	}

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
