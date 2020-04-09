package org.nextprot.commons.statements.specs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Specifications implements StatementSpecifications {

	public static final String EXTRA_FIELDS = "EXTRA_FIELDS";

	private final StatementSpecifications statementSpecifications;

	private Specifications(Builder builder) {
		this.statementSpecifications = builder.specifications;
	}

	@Override
	public boolean hasField(String columnName) {
		return statementSpecifications.hasField(columnName);
	}

	@Override
	public Collection<StatementField> getFields() {
		return statementSpecifications.getFields();
	}

	@Override
	public int size() {
		return statementSpecifications.size();
	}

	@Override
	public StatementField getField(String field) {
		return statementSpecifications.getField(field);
	}

	public boolean hasCustomFields() {
		return statementSpecifications.getFields().stream().anyMatch(f -> f instanceof CustomStatementField);
	}

	@Override
	public Collection<StatementField> getCoreFields() {
		return statementSpecifications.getFields().stream()
			.filter(f -> f instanceof CoreStatementField).collect(Collectors.toList());
	}

	@Override
	public Collection<StatementField> getCustomFields() {
		return statementSpecifications.getFields().stream()
			.filter(f -> f instanceof CustomStatementField).collect(Collectors.toList());
	}

	public static class Builder {
		
		private final List<StatementField> customFields = new ArrayList<>();
		private final MutableStatementSpecifications specifications = new MutableStatementSpecifications();

		public Builder() {
			for (StatementField field : CoreStatementField.values()) specifications.specifyField(field);
		}

		/** 
		 * Specify custom fields that will be added in specifications
		 **/
		public Builder withExtraFields(List<String> fields) {
			if (fields.isEmpty()) throw new IllegalArgumentException("missing custom fields for extra_fields column");
			specifyCustomFields(new HashSet<>(fields), false);
			return this;
		}

		public Builder withExtraFieldsContributingToUnicityKey(List<String> fields) {
			if (fields.isEmpty()) throw new IllegalArgumentException("missing extra fields");
			specifyCustomFields(new HashSet<>(fields), true);
			return this;
		}

		private void specifyCustomFields(Set<String> extraFields, boolean partOfUnicityKey) {
			customFields.addAll(extraFields.stream()
					.map(fieldName -> new CustomStatementField(fieldName, partOfUnicityKey))
					.peek(specifications::specifyField)
					.collect(Collectors.toList()));
		}

		public Specifications build() {
			// TODO LATER Pam: is next line redundant with withExtraFields...() methods ??? 
			for (StatementField f: customFields) specifications.specifyField(f);
			return new Specifications(this);
		}
	}

}
