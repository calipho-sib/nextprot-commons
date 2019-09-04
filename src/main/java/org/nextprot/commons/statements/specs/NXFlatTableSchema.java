package org.nextprot.commons.statements.specs;


import java.util.stream.Collectors;


/**
 * The NXFlat DB schema based on statement specifications.
 *
 * It consists of core fields (string types) and
 * a supplementary column composed of all the custom fields (as a json column of type map keyed with custom field name).
 */
public class NXFlatTableSchema {

	private final StatementSpecifications statementSpecifications;

	public NXFlatTableSchema() {

		statementSpecifications = new Specifications.Builder()
				.withUndefinedExtraFields().build();
	}

	public StatementSpecifications getSpecifications() {

		return statementSpecifications;
	}

	public int countColumns() {
		return statementSpecifications.size();
	}

	/** @return the sql to create the table schema for nxflat.{tableName} */
	public String generateCreateTableInSQL(String tableName) {

		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE IF EXISTS nxflat.").append(tableName).append(";\n");
		sb.append("CREATE TABLE nxflat.").append(tableName).append(" (\n");

		sb.append(statementSpecifications.getFields().stream()
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
