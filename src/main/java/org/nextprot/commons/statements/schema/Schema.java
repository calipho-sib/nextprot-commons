package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.GenericStatementField;
import org.nextprot.commons.statements.StatementField;

import java.util.Collection;
import java.util.stream.Collectors;

public interface Schema {

	/** @return the statement field named fieldName */
	StatementField getField(String fieldName);

	/** @return true schema has a field named fieldName */
	boolean hasField(String fieldName);

	/** @return the collection of statement fields */
	Collection<StatementField> getFields();

	/** @return the number of fields */
	int size();

	/** @return the composite field that contain the given field or null if absent */
	CompositeField searchCompositeFieldOrNull(StatementField field);

	/** @return the sql to create the table schema for nxflat.{tableName} */
	default String generateNXFlatTable(String tableName) {

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
				.append(GenericStatementField.ENTRY_ACCESSION.name())
				.append(" );\n");

		sb.append("CREATE INDEX ").append(tableName, 0, 10)
				.append("_ANNOT_ID_IDX ON nxflat.").append(tableName)
				.append(" ( ")
				.append(GenericStatementField.ANNOTATION_ID.name())
				.append(" );\n");
		sb.append("\n");

		return sb.toString();
	}
}
