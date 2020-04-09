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

	public final MutableStatementSpecifications specifyField(StatementField field) {
		statementFields.put(field.getName(), field);
		return this;
	}

	public final MutableStatementSpecifications specifyFields(StatementField... fields) {
		for (StatementField field : fields) specifyField(field);
		return this;
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
	public Collection<StatementField> getCoreFields() {
		return statementFields.values().stream()
				.filter(f -> f instanceof CoreStatementField).collect(Collectors.toList());
	}

	@Override
	public Collection<StatementField> getCustomFields() {
		return statementFields.values().stream()
				.filter(f -> f instanceof CustomStatementField).collect(Collectors.toList());
	}
	
	@Override
	public StatementField getField(String field) {

		if (hasField(field)) {
			return statementFields.get(field);
		}
		throw new IllegalStateException("field "+ field + " is not valid (schema="+statementFields+")");
	}

}
