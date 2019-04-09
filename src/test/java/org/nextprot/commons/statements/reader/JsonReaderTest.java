package org.nextprot.commons.statements.reader;

import org.junit.Assert;
import org.junit.Test;
import org.nextprot.commons.statements.Statement;
import org.nextprot.commons.statements.specs.Specifications;
import org.nextprot.commons.statements.specs.StatementField;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.nextprot.commons.statements.specs.CoreStatementField.*;

public class JsonReaderTest {

	@Test
	public void readStatementAsMap() throws IOException {

		JsonReader reader = new JsonReader(new Specifications.Builder().build());
		Map<StatementField, String> map = reader.readMap(getStatement());

		Assert.assertEquals(13, map.size());
		Assert.assertEquals("variant", map.get(ANNOTATION_CATEGORY));
		Assert.assertEquals("POTEH-p.Trp34Ter", map.get(ANNOTATION_NAME));
		Assert.assertEquals("NextProt", map.get(ASSIGNED_BY));
		Assert.assertEquals("ECO:0000269", map.get(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", map.get(EVIDENCE_QUALITY));
		Assert.assertEquals("POTEH", map.get(GENE_NAME));
		Assert.assertEquals("34", map.get(LOCATION_BEGIN));
		Assert.assertEquals("34", map.get(LOCATION_END));
		Assert.assertEquals("NX_Q6S545", map.get(NEXTPROT_ACCESSION));
		Assert.assertEquals("gnomAD", map.get(SOURCE));
		Assert.assertEquals("792d509b2d452da2cf4a74faa2773c15", map.get(STATEMENT_ID));
		Assert.assertEquals("W", map.get(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("*", map.get(VARIANT_VARIATION_AMINO_ACID));
	}

	@Test
	public void readStatement() throws IOException {

		JsonReader reader = new JsonReader(new Specifications.Builder().build());
		Statement statement = reader.readStatement(getStatement());

		Assert.assertNotNull(statement.getSpecifications());

		Assert.assertEquals(13, statement.size());
		Assert.assertEquals("variant", statement.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("POTEH-p.Trp34Ter", statement.getValue(ANNOTATION_NAME));
		Assert.assertEquals("NextProt", statement.getValue(ASSIGNED_BY));
		Assert.assertEquals("ECO:0000269", statement.getValue(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", statement.getValue(EVIDENCE_QUALITY));
		Assert.assertEquals("POTEH", statement.getValue(GENE_NAME));
		Assert.assertEquals("34", statement.getValue(LOCATION_BEGIN));
		Assert.assertEquals("34", statement.getValue(LOCATION_END));
		Assert.assertEquals("NX_Q6S545", statement.getValue(NEXTPROT_ACCESSION));
		Assert.assertEquals("gnomAD", statement.getValue(SOURCE));
		Assert.assertEquals("5be0a6bfafde6ddfc380bffed6eca40e", statement.getValue(STATEMENT_ID));
		Assert.assertEquals("W", statement.getValue(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("*", statement.getValue(VARIANT_VARIATION_AMINO_ACID));
	}

	@Test
	public void readStatements() throws IOException {

		JsonReader reader = new JsonReader(new Specifications.Builder().build());
		List<Statement> statements = reader.readStatements(getStatements());

		Assert.assertEquals(2, statements.size());

		Statement statement = statements.get(0);

		Assert.assertNotNull(statement.getSpecifications());

		Assert.assertEquals(17, statement.size());
		Assert.assertEquals("variant", statement.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("SCN9A-iso3-p.Phe1449Val", statement.getValue(ANNOTATION_NAME));
		Assert.assertEquals("N/A", statement.getValue(ANNOT_SOURCE_ACCESSION));
		Assert.assertEquals("NextProt", statement.getValue(ASSIGNED_BY));
		Assert.assertEquals("curated", statement.getValue(ASSIGMENT_METHOD));
		Assert.assertEquals("ECO:0000219", statement.getValue(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", statement.getValue(EVIDENCE_QUALITY));
		Assert.assertEquals("SCN9A", statement.getValue(GENE_NAME));
		Assert.assertEquals("1449", statement.getValue(LOCATION_BEGIN));
		Assert.assertEquals("1449", statement.getValue(LOCATION_END));
		Assert.assertEquals("NX_Q15858", statement.getValue(NEXTPROT_ACCESSION));
		Assert.assertEquals("NX_Q15858", statement.getValue(ENTRY_ACCESSION));
		Assert.assertEquals("BioEditor", statement.getValue(SOURCE));
		Assert.assertEquals("publication", statement.getValue(RESOURCE_TYPE));
		Assert.assertEquals("0f8c71813a30bce322053f458efc41fd", statement.getValue(STATEMENT_ID));
		Assert.assertEquals("F", statement.getValue(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("V", statement.getValue(VARIANT_VARIATION_AMINO_ACID));

		statement = statements.get(1);

		Assert.assertNotNull(statement.getSpecifications());

		Assert.assertEquals(24, statement.size());
	}

	private String getStatement() {

		return "{\n" +
				"\"ANNOTATION_CATEGORY\": \"variant\",\n" +
				"\"ANNOTATION_NAME\": \"POTEH-p.Trp34Ter\",\n" +
				"\"ASSIGNED_BY\": \"NextProt\",\n" +
				"\"EVIDENCE_CODE\": \"ECO:0000269\",\n" +
				"\"EVIDENCE_QUALITY\": \"GOLD\",\n" +
				"\"GENE_NAME\": \"POTEH\",\n" +
				"\"LOCATION_BEGIN\": \"34\",\n" +
				"\"LOCATION_END\": \"34\",\n" +
				"\"NEXTPROT_ACCESSION\": \"NX_Q6S545\",\n" +
				"\"SOURCE\": \"gnomAD\",\n" +
				"\"STATEMENT_ID\": \"792d509b2d452da2cf4a74faa2773c15\",\n" +
				"\"VARIANT_ORIGINAL_AMINO_ACID\": \"W\",\n" +
				"\"VARIANT_VARIATION_AMINO_ACID\": \"*\"\n" +
				"}";
	}

	private String getStatements() {

		return "[\n" +
				"{\n" +
				"  \"ANNOTATION_CATEGORY\" : \"variant\",\n" +
				"  \"ANNOTATION_NAME\" : \"SCN9A-iso3-p.Phe1449Val\",\n" +
				"  \"ANNOT_SOURCE_ACCESSION\" : \"N/A\",\n" +
				"  \"ASSIGMENT_METHOD\" : \"curated\",\n" +
				"  \"ASSIGNED_BY\" : \"NextProt\",\n" +
				"  \"ENTRY_ACCESSION\" : \"NX_Q15858\",\n" +
				"  \"EVIDENCE_CODE\" : \"ECO:0000219\",\n" +
				"  \"EVIDENCE_QUALITY\" : \"GOLD\",\n" +
				"  \"GENE_NAME\" : \"SCN9A\",\n" +
				"  \"LOCATION_BEGIN\" : \"1449\",\n" +
				"  \"LOCATION_END\" : \"1449\",\n" +
				"  \"NEXTPROT_ACCESSION\" : \"NX_Q15858\",\n" +
				"  \"RESOURCE_TYPE\" : \"publication\",\n" +
				"  \"SOURCE\" : \"BioEditor\",\n" +
				"  \"VARIANT_ORIGINAL_AMINO_ACID\" : \"F\",\n" +
				"  \"VARIANT_VARIATION_AMINO_ACID\" : \"V\"\n" +
				"}, {\n" +
				"  \"ANNOTATION_CATEGORY\" : \"phenotypic-variation\",\n" +
				"  \"ANNOTATION_OBJECT_SPECIES\" : \"\",\n" +
				"  \"ANNOTATION_SUBJECT_SPECIES\" : \"Homo sapiens\",\n" +
				"  \"ANNOT_CV_TERM_ACCESSION\" : \"ME:0000002\",\n" +
				"  \"ANNOT_CV_TERM_NAME\" : \"impact\",\n" +
				"  \"ANNOT_CV_TERM_TERMINOLOGY\" : \"modification-effect-cv\",\n" +
				"  \"ANNOT_DESCRIPTION\" : \"impacts regulation of action potential firing threshold\",\n" +
				"  \"ANNOT_SOURCE_ACCESSION\" : \"N/A\",\n" +
				"  \"ASSIGMENT_METHOD\" : \"curated\",\n" +
				"  \"ASSIGNED_BY\" : \"NextProt\",\n" +
				"  \"NEXTPROT_ACCESSION\" : \"NX_Q15858-3\",\n" +
				"  \"ENTRY_ACCESSION\" : \"NX_Q15858\",\n" +
				"  \"EVIDENCE_CODE\" : \"ECO:0000006\",\n" +
				"  \"EVIDENCE_INTENSITY\" : \"Moderate\",\n" +
				"  \"EVIDENCE_NOTE\" : \"Additional experimental evidence:over expression analysis, current clamp voltage recording protocol evidence, natural variation mutant evidence used in manual assertion\\n\",\n" +
				"  \"EVIDENCE_QUALITY\" : \"GOLD\",\n" +
				"  \"GENE_NAME\" : \"SCN9A\",\n" +
				"  \"OBJECT_STATEMENT_IDS\" : \"6c8e563c1dd2339b4d164d8cbb24fcf6\",\n" +
				"  \"REFERENCE_ACCESSION\" : \"15958509\",\n" +
				"  \"REFERENCE_DATABASE\" : \"PubMed\",\n" +
				"  \"RESOURCE_TYPE\" : \"publication\",\n" +
				"  \"SOURCE\" : \"BioEditor\",\n" +
				"  \"SUBJECT_STATEMENT_IDS\" : \"618c2674745cea56a67cfd6a9fb5718e\"\n" +
				"}]\n";
	}
}