package org.nextprot.commons.statements;

import java.util.Collection;
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
			for (StatementField field : source.getSchema().getStatementFields()) {

				if (!ALL_FIELDS.containsKey(field.getName())) {
					ALL_FIELDS.put(field.getName(), field);
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

	public static boolean isFieldExist(String field) {

		return ALL_FIELDS.containsKey(field);
	}

	public static StatementField valueOfField(String field) {

		if (!isFieldExist(field)) {
			throw new IllegalStateException("field " + field + " is not found in any neXtProt sources");
		}

		return ALL_FIELDS.get(field);
	}

	static class GenericSchemaSupplier implements Supplier<Schema> {

		@Override
		public Schema get() {

			Schema schema = new Schema();

			for (StatementField field : GenericStatementField.values()) {

				schema.registerField(field);
			}

			return schema;
		}
	}

	static class GnomADSchemaSupplier implements Supplier<Schema> {

		@Override
		public Schema get() {

			Schema schema = new Schema();

			for (StatementField field : GenericStatementField.values()) {

				schema.registerField(field);
			}

			schema.registerField(new CustomStatementField("CANONICAL", false));
			schema.registerField(new CustomStatementField("ALLELE_COUNT", false));
			schema.registerField(new CustomStatementField("ALLELE_SAMPLED", false));
			schema.registerField(new CustomStatementField("DBSNP_ID"));

			return schema;
		}
	}

	static class Schema {

		private final Map<String, StatementField> statementFields = new HashMap<>();

		public final void registerField(StatementField field) {

			statementFields.put(field.getName(), field);
		}

		public final boolean hasField(String field) {

			return (statementFields.containsKey(field));
		}

		public Collection<StatementField> getStatementFields() {
			return statementFields.values();
		}

		// used to deserialize statement keys from json
		public StatementField valueOf(String field) {

			if (hasField(field)) {
				return statementFields.get(field);
			}
			throw new IllegalStateException("field "+ field + " is not valid");
		}
	}
}
