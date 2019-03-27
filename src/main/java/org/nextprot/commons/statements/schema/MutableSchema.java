package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.StatementField;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MutableSchema implements Schema {

	private final Map<String, StatementField> statementFields = new TreeMap<>();

	public MutableSchema() {}

	public MutableSchema(Schema schema) {
		schema.getFields().forEach(this::registerField);
	}

	public final void registerField(StatementField field) {

		statementFields.put(field.getName(), field);
	}

	public final void registerFields(StatementField... fields) {

		for (StatementField field : fields) {
			registerField(field);
		}
	}

	@Override
	public int size() {
		return statementFields.size();
	}

	@Override
	public final boolean hasField(String field) {

		return statementFields.containsKey(field);
	}

	@Override
	public Collection<StatementField> getFields() {
		return Collections.unmodifiableCollection(statementFields.values());
	}

	@Override
	public CompositeField searchCompositeFieldOrNull(StatementField field) {

		List<CompositeField> fields = statementFields.values().stream()
				.filter(statementField -> statementField instanceof CompositeField)
				.map(statementField -> (CompositeField) statementField)
				.filter(statementField -> statementField.getFields().contains(field))
				.collect(Collectors.toList());

		if (fields.isEmpty()) {
			return null;
		}
		else if (fields.size() == 1) {
			return fields.get(0);
		}
		throw new IllegalStateException("invalid schema: the field "+field.getName()+ " belongs to multiple composite fields "+fields);
	}

	@Override
	public StatementField getField(String field) {

		if (hasField(field)) {
			return statementFields.get(field);
		}
		throw new IllegalStateException("field "+ field + " is not valid (schema="+statementFields+")");
	}
}