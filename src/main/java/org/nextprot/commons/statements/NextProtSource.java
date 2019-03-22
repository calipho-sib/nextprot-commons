package org.nextprot.commons.statements;

import org.nextprot.commons.statements.schema.GenericSchema;
import org.nextprot.commons.statements.schema.Schema;
import org.nextprot.commons.statements.schema.SchemaImpl;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public enum NextProtSource {

	BioEditor("neXtProt", "http://kant.sib.swiss:9001/bioeditor", GenericSchema::new),
	GlyConnect("GlyConnect", "http://kant.sib.swiss:9001/glyconnect", GenericSchema::new),
	GnomAD("gnomAD", "http://kant.sib.swiss:9001/gnomad", GnomADSchema::new)
	;

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

	static class GnomADSchema implements Schema {

		private final SchemaImpl schema;

		public GnomADSchema() {

			this.schema = new SchemaImpl(new GenericSchema());

			StatementField cField = new CustomStatementField("CANONICAL");
			StatementField acField = new CustomStatementField("ALLELE_COUNT");
			StatementField asField = new CustomStatementField("ALLELE_SAMPLED");
			StatementField dbsnpField = new CustomStatementField("DBSNP_ID", true);

			schema.registerField(cField);
			schema.registerField(acField);
			schema.registerField(asField);
			schema.registerField(dbsnpField);
			schema.registerField(new CompositeField("PROPERTIES", Arrays.asList(dbsnpField, cField, acField, asField)));
		}

		@Override
		public boolean hasField(String field) {

			return schema.hasField(field);
		}

		@Override
		public Collection<StatementField> getFields() {

			return schema.getFields();
		}

		@Override
		public StatementField getField(String field) {

			return schema.getField(field);
		}

		@Override
		public int size() {

			return schema.size();
		}
	}
}
