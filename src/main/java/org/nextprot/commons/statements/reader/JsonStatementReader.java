package org.nextprot.commons.statements.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.nextprot.commons.statements.specs.CustomStatementField;
import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.StatementBuilder;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reads all statements from an URL Json resource once and then close the stream.
 */
public class JsonStatementReader extends AbstractJsonStatementReader {

	private final Reader reader;
	private final ObjectMapper mapper;
	private boolean isClosed = false;

	public JsonStatementReader(String content, StatementSpecifications specifications) {

		this(new StringReader(content), specifications);
	}

	public JsonStatementReader(Reader reader, StatementSpecifications specifications) {

		super(specifications);

		/*
		System.out.println("JsonStatementReader() specifications");
		if (specifications == null) {
			System.out.println("NO SPECS in reader");
		} else {
			specifications.getFields().stream()
			.forEach(f->System.out.println("Specs in reader: " + f.getClass() + " - " + f.getName()));
			System.out.println();
		}
		*/		
		
		SimpleModule module = new SimpleModule();
		module.addKeyDeserializer(StatementField.class, new StatementFieldDeserializer(specifications));

		mapper = new ObjectMapper();
		mapper.registerModule(module);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		this.reader = reader;
	}

	@Override
	public List<Statement> readStatements() throws IOException {

		if (isClosed) {
			throw new IOException("Stream closed");
		}

		List<Statement> statements = mapper.readValue(reader, new TypeReference<List<Statement>>() { });
		//System.out.println("JsonStatementReader.readStatements() STEP-1");
		//statements.get(0).keySet()
		//	.forEach(k -> System.out.println("statement key: " + k.getClass() + " - " + k.getName()));
		List<Statement> list = statements.stream()
				.map(statement -> new StatementBuilder(statement).build())
				.collect(Collectors.toList());

		//System.out.println("JsonStatementReader.readStatements() STEP-2");
		//list.get(0).keySet()
		//	.forEach(k -> System.out.println("statement key: " + k.getClass() + " - " + k.getName()));
		isClosed = true;

		return list;
	}


	/**
	 * Instanciate StatementField from key string
	 */
	private static class StatementFieldDeserializer extends KeyDeserializer {

		private final StatementSpecifications specifications;

		public StatementFieldDeserializer(StatementSpecifications specifications) {
			this.specifications = specifications;
		}

		@Override
		public StatementField deserializeKey(String key, DeserializationContext ctxt) {

			if (specifications.hasField(key)) {
				return specifications.getField(key);
			}
			return new CustomStatementField(key);
		}
	}

	public static Map<String, String> readStringMap(String jsonContent) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(jsonContent, new TypeReference<Map<String, String>>() { });
	}

	@Override
	public void close() throws IOException {

		isClosed = true;
	}
}
