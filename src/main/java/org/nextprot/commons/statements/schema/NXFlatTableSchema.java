package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.CustomStatementField;
import org.nextprot.commons.statements.GenericStatementField;
import org.nextprot.commons.statements.StatementField;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A statement schema that maps the table schema of nxflat
 *
 * it consists of generic fields (as string columns)
 * + a specific composite field composed of extra fields (as a json column)
 */
public class NXFlatTableSchema implements Schema {

	private static final String EXTRA_FIELDS = "EXTRAS";

	private final MutableSchema schema;

	public NXFlatTableSchema() {

		this.schema = new MutableSchema();

		for (StatementField field : GenericStatementField.values()) {

			schema.registerField(field);
		}
	}

	private NXFlatTableSchema(Set<String> extraFields) {

		this();

		if (extraFields.isEmpty()) {
			throw new IllegalArgumentException("missing extra fields");
		}

		registerExtraFields(extraFields);
	}

	public static NXFlatTableSchema withExtraFields(List<String> extraFields) {

		return new NXFlatTableSchema(new HashSet<>(extraFields));
	}

	private void registerExtraFields(Set<String> extraFields) {

		List<StatementField> newFields = extraFields.stream()
				.map(CustomStatementField::new)
				.peek(schema::registerField)
				.collect(Collectors.toList());

		schema.registerField(new CompositeField(EXTRA_FIELDS, newFields));
	}

	@Override
	public boolean hasField(String field) {

		return schema.hasField(field);
	}

	public boolean hasExtrasField() {

		return schema.hasField(EXTRA_FIELDS);
	}

	@Override
	public Collection<StatementField> getFields() {

		return schema.getFields();
	}

	@Override
	public CompositeField searchCompositeFieldOrNull(StatementField field) {

		return schema.searchCompositeFieldOrNull(field);
	}

	@Override
	public StatementField getField(String field) {

		return schema.getField(field);
	}

	public CompositeField getExtrasField() {

		if (!schema.hasField(EXTRA_FIELDS)) {
			return null;
		}

		return (CompositeField) schema.getField(EXTRA_FIELDS);
	}

	@Override
	public int size() {

		return schema.size();
	}

	/** @return the sql to create the table schema for nxflat.{tableName} */
	public String getCreateTableAsSQL(String tableName) {

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
