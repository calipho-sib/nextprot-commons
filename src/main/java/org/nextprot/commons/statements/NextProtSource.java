package org.nextprot.commons.statements;

import org.nextprot.commons.statements.schema.GenericSchema;
import org.nextprot.commons.statements.schema.Schema;
import org.nextprot.commons.statements.schema.MutableSchema;

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

		private final MutableSchema schema;

		private final StatementField canonicalField = new CustomStatementField("CANONICAL");
		private final StatementField alleleCountField = new CustomStatementField("ALLELE_COUNT");
		private final StatementField alleleSampledField = new CustomStatementField("ALLELE_SAMPLED");
		private final StatementField dbsnpIdField = new CustomStatementField("DBSNP_ID", true);
		private final StatementField propertiesField = new CompositeField("PROPERTIES",
				Arrays.asList(dbsnpIdField, canonicalField, alleleCountField, alleleSampledField));

		public GnomADSchema() {

			this.schema = new MutableSchema(new GenericSchema());

			schema.registerFields(canonicalField, alleleCountField, alleleSampledField, dbsnpIdField, propertiesField);
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
		public CompositeField searchCompositeFieldOrNull(StatementField field) {

			return schema.searchCompositeFieldOrNull(field);
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
