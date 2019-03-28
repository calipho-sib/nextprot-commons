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
 * It consists of mandatory columns (string types)
 * and an extra column composed of more fields (as a json column of type map).
 */
public class NXFlatTableSchema implements StatementSpecifications {

	private static final String EXTRAS = "EXTRAS";

	private final MutableStatementSpecifications statementSpecifications;

	public NXFlatTableSchema() {

		this.statementSpecifications = new MutableStatementSpecifications();

		for (StatementField field : CoreStatementField.values()) {

			statementSpecifications.specifyField(field);
		}
	}

	private NXFlatTableSchema(Set<String> standardFields, Set<String> fieldsContributingToUnicityKey) {

		this();

		if (standardFields.isEmpty() && fieldsContributingToUnicityKey.isEmpty()) {
			throw new IllegalArgumentException("missing extra fields");
		}

		List<StatementField> newFields = new ArrayList<>();
		specifyExtraFields(standardFields, false, newFields);
		specifyExtraFields(fieldsContributingToUnicityKey, true, newFields);

		statementSpecifications.specifyField(new CompositeField(EXTRAS, newFields));

	}

	/**
	 * Add an supplementary column of mixed type
	 */
	public static NXFlatTableSchema withExtraColumn(List<String> extraFields) {

		return new NXFlatTableSchema(new HashSet<>(extraFields), new HashSet<>());
	}

	public static NXFlatTableSchema withExtraColumn(List<String> standardFields,
	                                                List<String> fieldsContributingToUnicityKey) {

		return new NXFlatTableSchema(new HashSet<>(standardFields), new HashSet<>(fieldsContributingToUnicityKey));
	}

	public static NXFlatTableSchema fromResultSetMetaData(ResultSetMetaData rsmd) throws SQLException {

		int columnCount = rsmd.getColumnCount();

		List<String> extraFields = new ArrayList<>();

		for (int i = 1; i <= columnCount; i++) {
			String columnName = rsmd.getColumnName(i);

			if (!CoreStatementField.hasKey(columnName)) {
				extraFields.add(columnName);
			}
		}

		if (extraFields.isEmpty()) {
			return new NXFlatTableSchema();
		}

		return NXFlatTableSchema.withExtraColumn(extraFields);
	}

	private void specifyExtraFields(Set<String> extraFields, boolean partOfUnicityKey, List<StatementField> customFields) {

		customFields.addAll(extraFields.stream()
				.map(fieldName -> new CustomStatementField(fieldName, partOfUnicityKey))
				.peek(statementSpecifications::specifyField)
				.collect(Collectors.toList()));
	}

	@Override
	public boolean hasField(String columnName) {

		return statementSpecifications.hasField(columnName);
	}

	public boolean hasExtrasField() {

		return statementSpecifications.hasField(EXTRAS);
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

	public CompositeField getExtrasField() {

		if (!statementSpecifications.hasField(EXTRAS)) {
			return null;
		}

		return (CompositeField) statementSpecifications.getField(EXTRAS);
	}

	/** @return the sql to create the table schema for nxflat.{tableName} */
	public String generateCreateTableInSQL(String tableName) {

		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS nxflat.").append(tableName).append(";\n");
		sb.append("CREATE TABLE nxflat.").append(tableName).append(" (\n");

		sb.append(getFields().stream()
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
