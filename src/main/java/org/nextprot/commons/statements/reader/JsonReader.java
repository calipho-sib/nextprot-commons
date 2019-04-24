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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonReader extends StatementJsonReader {

	private final SimpleModule module;

	public JsonReader(StatementSpecifications specifications) {

		super(specifications);

		module = new SimpleModule();
		module.addKeyDeserializer(StatementField.class, new StatementFieldDeserializer(specifications));
	}

	public Map<StatementField, String> readMap(String content) throws IOException {

		if (content == null) {
			return new HashMap<>();
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		return mapper.readValue(content, new TypeReference<Map<StatementField, String>>() { });
	}

	public Statement readStatement(String content) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		return new StatementBuilder(mapper.readValue(content, new TypeReference<Statement>() { }))
				.build();
	}

	public List<Statement> readStatements(String content) throws IOException {

		return readStatements(new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8"))));
	}

	public List<Statement> readStatements(InputStream content) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		List<Statement> statements = mapper.readValue(content, new TypeReference<List<Statement>>() { });
		return statements.stream()
				.map(statement -> new StatementBuilder(statement).build())
				.collect(Collectors.toList());
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
}
