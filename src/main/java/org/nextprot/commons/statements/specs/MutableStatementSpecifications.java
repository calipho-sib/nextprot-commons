package org.nextprot.commons.statements.specs;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MutableStatementSpecifications implements StatementSpecifications {

	private final Map<String, StatementField> statementFields = new TreeMap<>();

	public MutableStatementSpecifications() {}

	public final void specifyField(StatementField field) {

		statementFields.put(field.getName(), field);
	}

	public final void specifyFields(StatementField... fields) {

		for (StatementField field : fields) {
			specifyField(field);
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
