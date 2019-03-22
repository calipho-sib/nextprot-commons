package org.nextprot.commons.statements;

import org.nextprot.commons.statements.schema.GenericSchemaSupplier;
import org.nextprot.commons.statements.schema.Schema;
import org.nextprot.commons.statements.schema.SchemaImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum NextProtSource {

	BioEditor("neXtProt", "http://kant.sib.swiss:9001/bioeditor", new GenericSchemaSupplier()),
	GlyConnect("GlyConnect", "http://kant.sib.swiss:9001/glyconnect", new GenericSchemaSupplier()),
	GnomAD("gnomAD", "http://kant.sib.swiss:9001/gnomad", new GnomADSchemaSupplier())
	;

	private static Map<String, StatementField> ALL_FIELDS;

	static {
		ALL_FIELDS = new HashMap<>();
		for (NextProtSource source : NextProtSource.values()) {
			for (StatementField field : source.getSchema().getFields()) {

				if (!ALL_FIELDS.containsKey(field.getName())) {
					ALL_FIELDS.put(field.getName(), field);
				}
				// should not have multiple fields with the same name
				else if (ALL_FIELDS.get(field.getName()).getClass() != field.getClass()) {
					throw new IllegalStateException("conflict with field name "+field.getName());
				}
			}
		}
	}

	private String sourceName;
	private String statementsUrl;
	private Schema schema;

	NextProtSource(String sourceName, String statementsUrl, Supplier<Schema> schemaSupplier) {
		this.sourceName = sourceName;
		this.statementsUrl = statementsUrl;
		this.schema = schemaSupplier.get();
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getStatementsUrl() {
		return statementsUrl;
	}

	public Schema getSchema() {
		return schema;
	}

	/**
	 * @return true if the given field is found in one of the schema
	 */
	public static boolean isStatementFieldExist(String field) {

		return ALL_FIELDS.containsKey(field);
	}

	/**
	 * @return the StatementField corresponding to the given field name
	 */
	public static StatementField getStatementField(String field) {

		if (!isStatementFieldExist(field)) {
			throw new IllegalStateException("field " + field + " is not found in any neXtProt sources");
		}

		return ALL_FIELDS.get(field);
	}

	static class GnomADSchemaSupplier implements Supplier<Schema> {

		@Override
		public Schema get() {

			SchemaImpl schema = new SchemaImpl();

			for (StatementField field : GenericStatementField.values()) {

				schema.registerField(field);
			}

			StatementField cField = new CustomStatementField("CANONICAL");
			StatementField acField = new CustomStatementField("ALLELE_COUNT");
			StatementField asField = new CustomStatementField("ALLELE_SAMPLED");
			StatementField dbsnpField = new CustomStatementField("DBSNP_ID", true);

			schema.registerField(cField);
			schema.registerField(acField);
			schema.registerField(asField);
			schema.registerField(dbsnpField);
			schema.registerField(new CompositeField("PROPERTIES", Arrays.asList(dbsnpField, cField, acField, asField)));

			return schema;
		}
	}
}
