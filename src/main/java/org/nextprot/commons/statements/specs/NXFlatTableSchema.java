package org.nextprot.commons.statements.specs;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The NXFlat DB schema based on statement specifications.
 *
 * It consists of core fields (string types) and
 * a supplementary column composed of all the custom fields (as a json column of type map keyed with custom field name).
 */
public class NXFlatTableSchema implements StatementSpecifications {

	public static final String EXTRA_FIELDS = "EXTRA_FIELDS";

	private final StatementSpecifications statementSpecifications;

	public static class Builder {

		private final List<StatementField> customFields = new ArrayList<>();
		private final MutableStatementSpecifications specifications = new MutableStatementSpecifications();

		public Builder() {

			for (StatementField field : CoreStatementField.values()) {

				specifications.specifyField(field);
			}
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

		public NXFlatTableSchema build() {

			if (!customFields.isEmpty()) {
				specifications.specifyField(new CompositeField(EXTRA_FIELDS, customFields));
			}
			return new NXFlatTableSchema(this);
		}
	}

	private NXFlatTableSchema(Builder builder) {

		this.statementSpecifications = builder.specifications;
	}

	public static NXFlatTableSchema build() {

		return new NXFlatTableSchema.Builder().build();
	}

	public static NXFlatTableSchema fromResultSetMetaData(ResultSetMetaData rsmd) throws SQLException {

		Builder builder = new Builder();

		int columnCount = rsmd.getColumnCount();

		List<String> customFields = new ArrayList<>();

		for (int i = 1; i <= columnCount; i++) {
			String columnName = rsmd.getColumnName(i);

			if (!CoreStatementField.hasKey(columnName)) {
				customFields.add(columnName);
			}
		}

		if (!customFields.isEmpty()) {
			builder.withExtraFields(new HashSet<>(customFields));
		}

		return builder.build();
	}

	@Override
	public boolean hasField(String columnName) {

		return statementSpecifications.hasField(columnName);
	}

	public boolean hasCustomFields() {

		return statementSpecifications.hasField(EXTRA_FIELDS);
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

	public CompositeField getCustomFields() {

		if (!statementSpecifications.hasField(EXTRA_FIELDS)) {
			return null;
		}

		return (CompositeField) statementSpecifications.getField(EXTRA_FIELDS);
	}

	/** @return the sql to create the table schema for nxflat.{tableName} */
	public String generateCreateTableInSQL(String tableName) {

		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS nxflat.").append(tableName).append(";\n");
		sb.append("CREATE TABLE nxflat.").append(tableName).append(" (\n");

		sb.append(getFields().stream()
				.filter(StatementField::isNXFlatTableColumn)
				.map(field -> "\t" + field.getName() + " VARCHAR(10000)")
				.collect(Collectors.joining(",\n")));

		sb.append(");\n");

		sb.append("CREATE INDEX ").append(tableName, 0, 10)
				.append("_ENTRY_AC_IDX ON nxflat.").append(tableName)
				.append(" ( ")
				.append(CoreStatementField.ENTRY_ACCESSION.name())
				.append(" );\n");

		sb.append("CREATE INDEX ").append(tableName, 0, 10)
				.append("_ANNOT_ID_IDX ON nxflat.").append(tableName)
				.append(" ( ")
				.append(CoreStatementField.ANNOTATION_ID.name())
				.append(" );\n");
		sb.append("\n");

		return sb.toString();
	}
}
