package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.StatementField;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class SchemaImpl implements Schema {

	private final Map<String, StatementField> statementFields = new TreeMap<>();

	public SchemaImpl() {}

	public SchemaImpl(Schema schema) {
		schema.getFields().forEach(this::registerField);
	}

	public final void registerField(StatementField field) {

		statementFields.put(field.getName(), field);
	}

	public int size() {
		return statementFields.size();
	}

	public final boolean hasField(String field) {

		return statementFields.containsKey(field);
	}

	public Collection<StatementField> getFields() {
		return Collections.unmodifiableCollection(statementFields.values());
	}

	public StatementField getField(String field) {

		if (hasField(field)) {
			return statementFields.get(field);
		}
		throw new IllegalStateException("field "+ field + " is not valid (schema="+statementFields+")");
	}
}
