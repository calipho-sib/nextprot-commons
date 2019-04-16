package org.nextprot.commons.statements.specs;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.nextprot.commons.statements.reader.JsonReader.readStringMap;


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
	public CompositeField searchCompositeFieldOrNull(StatementField field) {

		return statementSpecifications.searchCompositeFieldOrNull(field);
	}

	@Override
	public StatementField getField(String field) {

		return statementSpecifications.getField(field);
	}

	public boolean hasCustomFields() {

		return statementSpecifications.hasField(EXTRA_FIELDS);
	}

	public CompositeField getCustomFields() {

		if (!statementSpecifications.hasField(EXTRA_FIELDS)) {
			return null;
		}

		return (CompositeField) statementSpecifications.getField(EXTRA_FIELDS);
	}

	public static class Builder {

		private final List<StatementField> customFields = new ArrayList<>();
		private final MutableStatementSpecifications specifications = new MutableStatementSpecifications();

		public Builder() {

			for (StatementField field : CoreStatementField.values()) {

				specifications.specifyField(field);
			}
		}

		public Builder withUndefinedExtraFields() {

			specifications.specifyField(new CustomStatementField(EXTRA_FIELDS));
			return this;
		}

		public Builder withExtraFieldsValue(String extraFieldsJsonValue) {

			try {
				withExtraFields(readStringMap(extraFieldsJsonValue).keySet());
			} catch (IOException e) {

				throw new IllegalStateException("Json value of column "+EXTRA_FIELDS+" should be of " +
						"type map: cannot create StatementSpecifications", e);
			}

			return this;
		}

		/** Specify custom fields that will be added in */
		public Builder withExtraFields(List<String> fields) {

			return withExtraFields(new HashSet<>(fields));
		}

		public Builder withExtraFields(Set<String> fields) {

			if (fields.isEmpty()) {
				throw new IllegalArgumentException("missing extra fields");
			}
			specifyCustomFields(fields, false);
			return this;
		}

		public Builder withExtraFieldsContributingToUnicityKey(List<String> fields) {

			return withExtraFieldsContributingToUnicityKey(new HashSet<>(fields));
		}

		public Builder withExtraFieldsContributingToUnicityKey(Set<String> fields) {

			if (fields.isEmpty()) {
				throw new IllegalArgumentException("missing extra fields");
			}
			specifyCustomFields(fields, true);
			return this;
		}

		private void specifyCustomFields(Set<String> extraFields, boolean partOfUnicityKey) {

			customFields.addAll(extraFields.stream()
					.map(fieldName -> new CustomStatementField(fieldName, partOfUnicityKey))
					.peek(specifications::specifyField)
					.collect(Collectors.toList()));
		}

		public Specifications build() {

			if (!customFields.isEmpty()) {
				if (customFields.size() == 1) {
					specifications.specifyField(customFields.get(0));
				}
				else {
					specifications.specifyField(new CompositeField(EXTRA_FIELDS, customFields));
				}
			}
			return new Specifications(this);
		}
	}
}
