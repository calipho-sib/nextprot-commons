package org.nextprot.commons.statements.schema;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.nextprot.commons.statements.CustomStatementField;
import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.StatementField;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonReader {

	private final StatementFieldDeserializer statementFieldDeserializer;

	public JsonReader(Schema schema) {

		statementFieldDeserializer = new StatementFieldDeserializer(schema);
	}

	public Map<StatementField, String> readMap(String content) throws IOException {

		if (content == null) {
			return new HashMap<>();
		}

		SimpleModule module = new SimpleModule();
		module.addKeyDeserializer(StatementField.class, statementFieldDeserializer);

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(module);

		return mapper.readValue(content, new TypeReference<Map<StatementField, String>>() { });
	}

	public Statement readStatement(String content) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addKeyDeserializer(StatementField.class, statementFieldDeserializer);
		mapper.registerModule(module);

		return mapper.readValue(content, new TypeReference<Statement>() { });
	}

	/**
	 * Instanciate StatementField from key string
	 */
	private static class StatementFieldDeserializer extends KeyDeserializer {

		private final Schema schema;

		public StatementFieldDeserializer(Schema schema) {
			this.schema = schema;
		}

		@Override
		public StatementField deserializeKey(String key, DeserializationContext ctxt) {

			if (schema.hasField(key)) {
				return schema.getField(key);
			}
			return new CustomStatementField(key);
		}
	}
}
