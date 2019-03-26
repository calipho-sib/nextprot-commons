package org.nextprot.commons.statements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Test;
import org.nextprot.commons.constants.QualityQualifier;
import org.nextprot.commons.statements.constants.UniqueKey;
import org.nextprot.commons.statements.schema.GenericSchema;
import org.nextprot.commons.statements.schema.Schema;
import org.nextprot.commons.statements.schema.SchemaImpl;

public class StatementBuilderTest {
	
	private QualityQualifier defaultQuality = QualityQualifier.GOLD;
	
	@Test
	public void testRawStatementEquals() {
		Statement rs1 = StatementBuilder.createNew().addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).build();
		Statement rs2 = StatementBuilder.createNew().addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).build();
		assertEquals(rs1, rs2);
	}

	@Test
	public void testRawStatementInsertionInSets() {
		Set<Statement> set1 = new HashSet<>();
		set1.add(StatementBuilder.createNew().addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).build());
		set1.add(StatementBuilder.createNew().addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality ).build());
		
		assertEquals(set1.size(), 1);
		
		set1.add(StatementBuilder.createNew().addCompulsoryFields("DDD", "BBB", "CCC", defaultQuality).build());
		assertEquals(set1.size(), 2);

	}

	@Test
	public void testAnnotHashUnicity() {

		Statement rs1 = StatementBuilder.createNew()
				.addField(GenericStatementField.NEXTPROT_ACCESSION, "NX_P25054")
				.addField(GenericStatementField.GENE_NAME, "apc")
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
   	    	    .addField(GenericStatementField.TARGET_ISOFORMS, "[]")
				.addSourceInfo("CAVA-VP90999", "BED").buildWithAnnotationHash();
		Statement rs2 = StatementBuilder.createNew()
				.addField(GenericStatementField.NEXTPROT_ACCESSION, "NX_P25054")
				.addField(GenericStatementField.GENE_NAME, "apc")
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
   	    	    .addField(GenericStatementField.TARGET_ISOFORMS, "[]")
				.addSourceInfo("XPTO", "Caviar").buildWithAnnotationHash();

		assertNotEquals(rs1, rs2); 
		assertEquals(rs1.get(GenericStatementField.ANNOTATION_ID), rs2.get(GenericStatementField.ANNOTATION_ID));
	}

	@Test
	public void testRawStatement2() {

		Statement rs1 = StatementBuilder.createNew().addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).build();
		Statement rs2 = StatementBuilder.createNew().addMap(rs1).build();

		assertEquals(rs1, rs2);
	}

	@Test
	public void testDebugInfo() {

		Statement rs1 = StatementBuilder.createNew().addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).addDebugInfo("Oh yeah").build();
		Statement rs2 = StatementBuilder.createNew().addMap(rs1).addDebugInfo("oh oh").build();

		assertEquals(rs1.get(GenericStatementField.DEBUG_INFO), "Oh yeah");
		assertEquals(rs2.get(GenericStatementField.DEBUG_INFO), "oh oh");
	}

	@Test
	public void testStatementEquality() {

		Statement rs1 = StatementBuilder.createNew()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).build();
		Statement rs2 = StatementBuilder.createNew().addMap(rs1).build();

		assertEquals(rs1, rs2);
	}

	@Test
	public void testRawStatementHashEquals() throws IOException {

		//Copied directly from kant.isb-sib.ch:9000/bioeditor/gene/msh6/statements

		String subjectOne = "{\n" +
				"  \"ANNOTATION_CATEGORY\" : \"variant\",\n" +
				"  \"ANNOTATION_NAME\" : \"MSH6-p.Phe1088Leufs*5\",\n" +
				"  \"ANNOT_SOURCE_ACCESSION\" : \"\",\n" +
				"  \"ASSIGMENT_METHOD\" : \"curated\",\n" +
				"  \"ASSIGNED_BY\" : \"NextProt\",\n" +
				"  \"DEBUG_NOTE\" : \"Publication not found for CAVA-VD012367nullremoving one allele for multiple genes CAVA-VP014556 set: Set(MSH6-p.Phe1088Leufs*5, MSH6-p.Phe1088Serfs*2, PMS2-p.Arg315*) filtered set Set(MSH6-p.Phe1088Leufs*5, MSH6-p.Phe1088Serfs*2) subject: MSH6-p.Phe1088Serfs*2 gene getName msh6\\n\",\n" +
				"  \"ENTRY_ACCESSION\" : \"NX_P52701\",\n" +
				"  \"EVIDENCE_CODE\" : \"ECO:0000219\",\n" +
				"  \"EVIDENCE_QUALITY\" : \"GOLD\",\n" +
				"  \"GENE_NAME\" : \"MSH6\",\n" +
				"  \"LOCATION_BEGIN\" : \"1088\",\n" +
				"  \"LOCATION_END\" : \"1092\",\n" +
				"  \"NEXTPROT_ACCESSION\" : \"NX_P52701\",\n" +
				"  \"RESOURCE_TYPE\" : \"publication\",\n" +
				"  \"SOURCE\" : \"BioEditor\",\n" +
				"  \"VARIANT_ORIGINAL_AMINO_ACID\" : \"F\",\n" +
				"  \"VARIANT_VARIATION_AMINO_ACID\" : \"L\"\n" +
				"}";

		String subjectTwo = "{\n" +
				"  \"ANNOTATION_CATEGORY\" : \"variant\",\n" +
				"  \"ANNOTATION_NAME\" : \"MSH6-p.Phe1088Leufs*5\",\n" +
				"  \"ANNOT_SOURCE_ACCESSION\" : \"\",\n" +
				"  \"ASSIGMENT_METHOD\" : \"curated\",\n" +
				"  \"ASSIGNED_BY\" : \"NextProt\",\n" +
				"  \"DEBUG_NOTE\" : \"Publication not found for CAVA-VD012367\",\n" +
				"  \"ENTRY_ACCESSION\" : \"NX_P52701\",\n" +
				"  \"EVIDENCE_CODE\" : \"ECO:0000219\",\n" +
				"  \"EVIDENCE_QUALITY\" : \"GOLD\",\n" +
				"  \"GENE_NAME\" : \"MSH6\",\n" +
				"  \"LOCATION_BEGIN\" : \"1088\",\n" +
				"  \"LOCATION_END\" : \"1092\",\n" +
				"  \"NEXTPROT_ACCESSION\" : \"NX_P52701\",\n" +
				"  \"RESOURCE_TYPE\" : \"publication\",\n" +
				"  \"SOURCE\" : \"BioEditor\",\n" +
				"  \"STATEMENT_ID\" : \"c62d75e6d330281361fdf22c7bc55586\",\n" +
				"  \"VARIANT_ORIGINAL_AMINO_ACID\" : \"F\",\n" +
				"  \"VARIANT_VARIATION_AMINO_ACID\" : \"L\"\n" +
				"}";

		Statement sub1 = StatementBuilder.createNew().addMap(buildStatementFromJsonString(subjectOne)).build();

		Statement sub2 = buildStatementFromJsonString(subjectTwo);

		Statement vp1 = StatementBuilder.createNew().addField(GenericStatementField.ANNOTATION_CATEGORY, "phenotypic").addSubjects(Arrays.asList(sub1, sub2)).build();
		Statement vp2 = StatementBuilder.createNew().addField(GenericStatementField.ANNOTATION_CATEGORY, "phenotypic").addSubjects(Arrays.asList(sub2, sub1)).build();

		String vp1Hash = StatementBuilder.computeUniqueKey(vp1, UniqueKey.ENTRY);
		String vp2Hash = StatementBuilder.computeUniqueKey(vp2, UniqueKey.ENTRY);

		Assert.assertEquals(vp1Hash, vp2Hash);
	}

	@Test
	public void testWithCustomFields() {

		Statement rs1 = StatementBuilder.createNew()
				.addField(new CustomStatementField("DBSNP_ID"), "rs745905374")
				.build();

		Assert.assertTrue(rs1.containsKey(GenericStatementField.STATEMENT_ID));
	}

	@Test
	public void testGnomADStatements() throws IOException {

		Statement stmt = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.build();

		Assert.assertEquals("1", stmt.getValueOrNull("ALLELE_COUNT"));
		Assert.assertEquals("217610", stmt.getValueOrNull("ALLELE_SAMPLED"));
		Assert.assertEquals("rs745905374", stmt.getValueOrNull("DBSNP_ID"));
		Assert.assertEquals("YES", stmt.getValueOrNull("CANONICAL"));
		Assert.assertNull(stmt.getValueOrNull("PROPERTIES"));
		Assert.assertNull(stmt.getValueOrNull("ROUDOUDOU"));
	}

	@Test
	public void testGnomADStatementsWithSchema() throws IOException {

		Statement stmt = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSchema(NextProtSource.GnomAD.getSchema())
				.build();

		Assert.assertEquals("1", stmt.getValueOrNull("ALLELE_COUNT"));
		Assert.assertEquals("217610", stmt.getValueOrNull("ALLELE_SAMPLED"));
		Assert.assertEquals("rs745905374", stmt.getValueOrNull("DBSNP_ID"));
		Assert.assertEquals("YES", stmt.getValueOrNull("CANONICAL"));
		Assert.assertNotNull(stmt.getValueOrNull("PROPERTIES"));
		Assert.assertEquals("{\"ALLELE_COUNT\":\"1\",\"ALLELE_SAMPLED\":\"217610\",\"CANONICAL\":\"YES\",\"DBSNP_ID\":\"rs745905374\"}", stmt.getValueOrNull("PROPERTIES"));
		Assert.assertNull(stmt.getValueOrNull("ROUDOUDOU"));
	}

	@Test
	public void testGnomADStatementsUniqueEntryKeys() throws IOException {

		Statement stmt1 = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.buildWithAnnotationHash();

		Statement stmt2 = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSchema(NextProtSource.GnomAD.getSchema())
				.buildWithAnnotationHash();

		String stmt1EntryKey = StatementBuilder.computeUniqueKey(stmt1, UniqueKey.ENTRY);
		String stmt2EntryKey = StatementBuilder.computeUniqueKey(stmt2, UniqueKey.ENTRY);

		Assert.assertEquals(stmt1EntryKey, stmt2EntryKey);
	}

	@Test
	public void testDefaultSchema() throws IOException {

		String json = "{\n" +
				"\"name\":\"roudoudou\",\n" +
				"\"age\":\"23\",\n" +
				"\"location\":\"geneva\"\n" +
				"}";

		Statement stmt = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(json))
				.build();

		Schema defaultSchema = stmt.getSchema();
		Assert.assertEquals(Arrays.asList("age", "location", "name"), defaultSchema.getFields().stream()
				.map(StatementField::getName)
				.filter(statementField -> !new GenericSchema().hasField(statementField))
				.collect(Collectors.toList()));
	}

	@Test
	public void testGetCompositeFieldValue() throws IOException {

		Statement stmt = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(getGnomADStatementAsJSONFromDB()))
				.withSchema(NextProtSource.GnomAD.getSchema())
				.build();

		Assert.assertEquals("1", stmt.getValue(NextProtSource.GnomAD.getSchema().getField("ALLELE_COUNT")));
		Assert.assertEquals("YES", stmt.getValue(NextProtSource.GnomAD.getSchema().getField("CANONICAL")));
		Assert.assertEquals("217610", stmt.getValue(NextProtSource.GnomAD.getSchema().getField("ALLELE_SAMPLED")));
		Assert.assertEquals("rs745905374", stmt.getValue(NextProtSource.GnomAD.getSchema().getField("DBSNP_ID")));
	}

	@Test
	public void testCannotFindFieldFromCompositeFieldValue() throws IOException {

		Statement stmt = StatementBuilder.createFromExistingStatement(buildStatementFromJsonString(getGnomADStatementAsJSONFromDBNoCanonicalField()))
				.withSchema(NextProtSource.GnomAD.getSchema())
				.build();

		Assert.assertNull(stmt.getValue(NextProtSource.GnomAD.getSchema().getField("CANONICAL")));
	}

	private String getGnomADStatementAsJSON() {

		return "{\n" +
				"\"ANNOTATION_NAME\":\"POTEH-p.Trp34Ter\",\n" +
				"\"ANNOTATION_CATEGORY\":\"Variant\",\n" +
				"\"LOCATION_BEGIN\":\"34\",\n" +
				"\"LOCATION_END\":\"34\",\n" +
				"\"VARIANT_ORIGINAL_AMINO_ACID\":\"W\",\n" +
				"\"VARIANT_VARIATION_AMINO_ACID\":\"*\",\n" +
				"\"EVIDENCE_CODE\":\"ECO:0000269\",\n" +
				"\"EVIDENCE_QUALITY\":\"GOLD\",\n" +
				"\"SOURCE\":\"gnomAD\",\n" +
				"\"ASSIGNED_BY\":\"neXtProt\",\n" +
				"\"GENE_NAME\":\"POTEH\",\n" +
				"\"NEXTPROT_ACCESSION\":\"NX_Q6S545\",\n" +
				"\"CANONICAL\":\"YES\",\n" +
				"\"ALLELE_COUNT\":\"1\",\n" +
				"\"ALLELE_SAMPLED\":\"217610\",\n" +
				"\"DBSNP_ID\":\"rs745905374\"\n" +
				"}";
	}

	private String getGnomADStatementAsJSONFromDB() {

		return "{\n" +
				"\"ANNOTATION_CATEGORY\": \"Variant\",\n" +
				"\"ANNOTATION_NAME\": \"POTEH-p.Trp34Ter\",\n" +
				"\"ASSIGNED_BY\": \"neXtProt\",\n" +
				"\"EVIDENCE_CODE\": \"ECO:0000269\",\n" +
				"\"EVIDENCE_QUALITY\": \"GOLD\",\n" +
				"\"GENE_NAME\": \"POTEH\",\n" +
				"\"LOCATION_BEGIN\": \"34\",\n" +
				"\"LOCATION_END\": \"34\",\n" +
				"\"NEXTPROT_ACCESSION\": \"NX_Q6S545\",\n" +
				"\"PROPERTIES\":\"{\\\"ALLELE_COUNT\\\":\\\"1\\\",\\\"ALLELE_SAMPLED\\\":\\\"217610\\\",\\\"CANONICAL\\\":\\\"YES\\\",\\\"DBSNP_ID\\\":\\\"rs745905374\\\"}\",\n" +
				"\"SOURCE\": \"gnomAD\",\n" +
				"\"STATEMENT_ID\": \"792d509b2d452da2cf4a74faa2773c15\",\n" +
				"\"VARIANT_ORIGINAL_AMINO_ACID\": \"W\",\n" +
				"\"VARIANT_VARIATION_AMINO_ACID\": \"*\"\n" +
				"}";
	}

	private String getGnomADStatementAsJSONFromDBNoCanonicalField() {

		return "{\n" +
				"\"ANNOTATION_CATEGORY\": \"Variant\",\n" +
				"\"ANNOTATION_NAME\": \"POTEH-p.Trp34Ter\",\n" +
				"\"ASSIGNED_BY\": \"neXtProt\",\n" +
				"\"EVIDENCE_CODE\": \"ECO:0000269\",\n" +
				"\"EVIDENCE_QUALITY\": \"GOLD\",\n" +
				"\"GENE_NAME\": \"POTEH\",\n" +
				"\"LOCATION_BEGIN\": \"34\",\n" +
				"\"LOCATION_END\": \"34\",\n" +
				"\"NEXTPROT_ACCESSION\": \"NX_Q6S545\",\n" +
				"\"PROPERTIES\":\"{\\\"ALLELE_COUNT\\\":\\\"1\\\",\\\"ALLELE_SAMPLED\\\":\\\"217610\\\",\\\"DBSNP_ID\\\":\\\"rs745905374\\\"}\",\n" +
				"\"SOURCE\": \"gnomAD\",\n" +
				"\"STATEMENT_ID\": \"792d509b2d452da2cf4a74faa2773c15\",\n" +
				"\"VARIANT_ORIGINAL_AMINO_ACID\": \"W\",\n" +
				"\"VARIANT_VARIATION_AMINO_ACID\": \"*\"\n" +
				"}";
	}

	private Statement buildStatementFromJsonString(String content) throws IOException {

		return buildStatementFromJsonString(content, new SchemaImpl(new GenericSchema()));
	}

	private Statement buildStatementFromJsonString(String content, Schema schema) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule();
		module.addKeyDeserializer(StatementField.class, new KeyDeserializer() {
			@Override
			public StatementField deserializeKey(String key, DeserializationContext ctxt) {

				if (schema.hasField(key)) {
					return schema.getField(key);
				}
				return new CustomStatementField(key);
			}
		});
		mapper.registerModule(module);

		return mapper.readValue(content, new TypeReference<Statement>() { });
	}
}
