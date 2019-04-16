package org.nextprot.commons.statements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.nextprot.commons.statements.specs.CoreStatementField.ENTRY_ACCESSION;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.nextprot.commons.constants.QualityQualifier;
import org.nextprot.commons.statements.constants.UniqueKey;
import org.nextprot.commons.statements.specs.CompositeField;
import org.nextprot.commons.statements.specs.CoreStatementField;
import org.nextprot.commons.statements.specs.CustomStatementField;
import org.nextprot.commons.statements.specs.MutableStatementSpecifications;
import org.nextprot.commons.statements.specs.Specifications;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import static org.nextprot.commons.statements.specs.CoreStatementField.*;
import static org.nextprot.commons.statements.specs.Specifications.EXTRA_FIELDS;

public class StatementBuilderTest {
	
	private QualityQualifier defaultQuality = QualityQualifier.GOLD;

	@Test(expected = IllegalStateException.class)
	public void shouldNotBuiltEmptyStatement() {
		new StatementBuilder().build();
	}

	@Test
	public void statementsShouldHaveSpecs() {

		Statement rs = new StatementBuilder()
				.addCompulsoryFields("AAA", "?", "CCC", defaultQuality)
				.build();

		Assert.assertEquals("AAA", rs.getValue(ENTRY_ACCESSION));
		Assert.assertEquals("CCC", rs.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("GOLD", rs.getValue(EVIDENCE_QUALITY));
		Assert.assertNotNull(rs.getSpecifications());
		Assert.assertEquals(4, rs.getSpecifications().size());
		Assert.assertTrue(rs.hasField(ENTRY_ACCESSION.getName()));
		Assert.assertTrue(rs.getSpecifications().hasField(ANNOTATION_CATEGORY.getName()));
		Assert.assertTrue(rs.getSpecifications().hasField(EVIDENCE_QUALITY.getName()));
		Assert.assertTrue(rs.getSpecifications().hasField(STATEMENT_ID.getName()));
	}

	@Test
	public void shouldBePossibleToAssignJsonValueToCompositeField() {

		CustomStatementField field1 = new CustomStatementField("f1");
		CustomStatementField field2 = new CustomStatementField("f2");
		CompositeField field3 = new CompositeField("f3", Arrays.asList(field1, field2));

		Statement s = new StatementBuilder()
				.addField(field3, "{\"f1\":\"1\",\"f2\":\"217610\"}")
				.build();

		Assert.assertEquals("1", s.getValue(field1));
		Assert.assertEquals("217610", s.getValue(field2));
		Assert.assertTrue(s.getSpecifications().hasField("f1"));
		Assert.assertTrue(s.getSpecifications().hasField("f2"));
		Assert.assertTrue(s.getSpecifications().hasField("f3"));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotBePossibleToAssignNonJsonValueToCompositeField() {

		CustomStatementField field1 = new CustomStatementField("f1");
		CustomStatementField field2 = new CustomStatementField("f2");
		CompositeField field3 = new CompositeField("f3", Arrays.asList(field1, field2));

		Statement s = new StatementBuilder()
				.addField(field3, "roudoudou")
				.build();

		Assert.assertEquals("1", s.getValue(field1));
		Assert.assertEquals("217610", s.getValue(field2));
	}

	@Test
	public void shouldBePossibleToAssignValueToCompositeField2() {

		CustomStatementField field1 = new CustomStatementField("f1");
		CustomStatementField field2 = new CustomStatementField("f2");
		CompositeField field3 = new CompositeField("f3", Arrays.asList(field1, field2));

		Statement s = new StatementBuilder()
				.withSpecifications(new MutableStatementSpecifications()
						.specifyFields(field1, field2, field3))
				.addField(field1, "1")
				.addField(field2, "217610")
				.build();

		Assert.assertEquals("1", s.getValue(field1));
		Assert.assertEquals("217610", s.getValue(field2));
		Assert.assertTrue(s.getSpecifications().hasField("f1"));
		Assert.assertTrue(s.getSpecifications().hasField("f2"));
		Assert.assertTrue(s.getSpecifications().hasField("f3"));
	}

	@Test
	public void shouldHaveSameStatementId() {

		CustomStatementField field1 = new CustomStatementField("f1");
		CustomStatementField field2 = new CustomStatementField("f2");
		CompositeField field3 = new CompositeField("f3", Arrays.asList(field1, field2));

		Statement s1 = new StatementBuilder()
				.withSpecifications(new MutableStatementSpecifications()
						.specifyFields(field1, field2, field3))
				.addField(field1, "1")
				.addField(field2, "217610")
				.build();

		Statement s2 = new StatementBuilder()
				.addField(field3, "{\"f1\":\"1\",\"f2\":\"217610\"}")
				.build();

		Assert.assertEquals(s1.getStatementId(), s2.getStatementId());
		Assert.assertEquals("d41d8cd98f00b204e9800998ecf8427e", s1.getStatementId());
		Assert.assertEquals("d41d8cd98f00b204e9800998ecf8427e", s2.getStatementId());
	}

	@Test
	public void testRawStatementEquals() {
		Statement rs1 = new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.build();
		Statement rs2 = new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.build();
		assertEquals(rs1, rs2);
	}

	@Test
	public void testDebugInfoIsNotAffectingStatementIdCalculation() {

		Statement rs1 = new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.addDebugInfo("YOYO")
				.build();

		Statement rs2 = new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.build();

		assertNotEquals(rs1, rs2);
		assertEquals(rs1.getStatementId(), rs2.getStatementId());
	}

	@Test
	public void testRawStatementInsertionInSets() {
		Set<Statement> set1 = new HashSet<>();
		set1.add(new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.build());
		set1.add(new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality )
				.build());
		
		assertEquals(set1.size(), 1);
		
		set1.add(new StatementBuilder()
				.addCompulsoryFields("DDD", "BBB", "CCC", defaultQuality)
				.build());
		assertEquals(set1.size(), 2);

	}

	@Test
	public void testAnnotHashUnicity() {

		Statement rs1 = new StatementBuilder()
				.addField(CoreStatementField.NEXTPROT_ACCESSION, "NX_P25054")
				.addField(CoreStatementField.GENE_NAME, "apc")
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
   	    	    .addField(CoreStatementField.TARGET_ISOFORMS, "[]")
				.addSourceInfo("CAVA-VP90999", "BED")
				.withAnnotationHash()
				.build();
		Statement rs2 = new StatementBuilder()
				.addField(CoreStatementField.NEXTPROT_ACCESSION, "NX_P25054")
				.addField(CoreStatementField.GENE_NAME, "apc")
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
   	    	    .addField(CoreStatementField.TARGET_ISOFORMS, "[]")
				.addSourceInfo("XPTO", "Caviar").withAnnotationHash()
				.build();

		assertNotEquals(rs1, rs2); 
		assertEquals(rs1.get(CoreStatementField.ANNOTATION_ID), rs2.get(CoreStatementField.ANNOTATION_ID));
	}

	@Test
	public void testRawStatement2() {

		Statement rs1 = new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.build();
		Statement rs2 = new StatementBuilder(rs1).build();

		assertEquals(rs1, rs2);
	}

	@Test
	public void testStatementEquality() {

		Statement rs1 = new StatementBuilder()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
				.build();
		Statement rs2 = new StatementBuilder(rs1).build();

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

		Statement sub1 = new StatementBuilder(buildStatementFromJsonString(subjectOne)).build();

		Statement sub2 = buildStatementFromJsonString(subjectTwo);

		Statement vp1 = new StatementBuilder()
				.addField(CoreStatementField.ANNOTATION_CATEGORY, "phenotypic")
				.addSubjects(Arrays.asList(sub1, sub2))
				.build();
		Statement vp2 = new StatementBuilder()
				.addField(CoreStatementField.ANNOTATION_CATEGORY, "phenotypic")
				.addSubjects(Arrays.asList(sub2, sub1))
				.build();

		String vp1Hash = StatementBuilder.extractUniqueFieldValues(vp1, UniqueKey.ENTRY);
		String vp2Hash = StatementBuilder.extractUniqueFieldValues(vp2, UniqueKey.ENTRY);

		Assert.assertEquals(vp1Hash, vp2Hash);
	}

	@Test
	public void testWithCustomFields() {

		Statement rs1 = new StatementBuilder()
				.addField(new CustomStatementField("DBSNP_ID"), "rs745905374")
				.build();

		Assert.assertTrue(rs1.containsKey(CoreStatementField.STATEMENT_ID));
	}

	@Test
	public void testGnomADStatements() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withAnnotationHash()
				.build();

		Assert.assertEquals("1", stmt.getOptionalValue("ALLELE_COUNT").get());
		Assert.assertEquals("217610", stmt.getOptionalValue("ALLELE_SAMPLED").get());
		Assert.assertEquals("rs745905374", stmt.getOptionalValue("DBSNP_ID").get());
		Assert.assertEquals("YES", stmt.getOptionalValue("CANONICAL").get());
		Assert.assertFalse(stmt.getOptionalValue("PROPERTIES").isPresent());
		Assert.assertFalse(stmt.getOptionalValue("ROUDOUDOU").isPresent());
	}

	@Test
	public void testGnomADStatementsWithSchema() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSpecifications(newGnomADSpecifications())
				.withAnnotationHash()
				.build();

		Assert.assertEquals("1", stmt.getOptionalValue("ALLELE_COUNT").get());
		Assert.assertEquals("217610", stmt.getOptionalValue("ALLELE_SAMPLED").get());
		Assert.assertEquals("rs745905374", stmt.getOptionalValue("DBSNP_ID").get());
		Assert.assertEquals("YES", stmt.getOptionalValue("CANONICAL").get());
		Assert.assertTrue(stmt.getOptionalValue(EXTRA_FIELDS).isPresent());
		Assert.assertEquals("{\"ALLELE_COUNT\":\"1\",\"ALLELE_SAMPLED\":\"217610\",\"CANONICAL\":\"YES\",\"DBSNP_ID\":\"rs745905374\"}",
				stmt.getOptionalValue(EXTRA_FIELDS).get());
		Assert.assertFalse(stmt.getOptionalValue("ROUDOUDOU").isPresent());
	}

	@Test
	public void testGnomADStatementsUniqueEntryKeys() throws IOException {

		Statement stmt1 = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withAnnotationHash()
				.build();

		Statement stmt2 = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSpecifications(newGnomADSpecifications())
				.withAnnotationHash()
				.build();

		String stmt1EntryKey = StatementBuilder.extractUniqueFieldValues(stmt1, UniqueKey.ENTRY);
		String stmt2EntryKey = StatementBuilder.extractUniqueFieldValues(stmt2, UniqueKey.ENTRY);

		Assert.assertEquals(stmt1EntryKey, stmt2EntryKey);
	}

	@Test
	public void testDefaultSchema() throws IOException {

		String json = "{\n" +
				"\"name\":\"roudoudou\",\n" +
				"\"age\":\"23\",\n" +
				"\"location\":\"geneva\"\n" +
				"}";

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(json))
				.build();

		StatementSpecifications defaultSpecifications = stmt.getSpecifications();
		Assert.assertEquals(Arrays.asList("age", "location", "name"), defaultSpecifications.getFields().stream()
				.map(StatementField::getName)
				.filter(statementField -> !new Specifications.Builder().build().hasField(statementField))
				.collect(Collectors.toList()));
	}

	@Test
	public void testGetCompositeFieldValue() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSONFromDB()))
				.withSpecifications(newGnomADSpecifications())
				.build();

		Assert.assertEquals("1", stmt.getOptionalValue("ALLELE_COUNT").get());
		Assert.assertEquals("YES", stmt.getOptionalValue("CANONICAL").get());
		Assert.assertEquals("217610", stmt.getOptionalValue("ALLELE_SAMPLED").get());
		Assert.assertEquals("rs745905374", stmt.getOptionalValue("DBSNP_ID").get());
	}

	@Test
	public void testCannotFindFieldFromCompositeFieldValue() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSONFromDBNoCanonicalField()))
				.withSpecifications(newGnomADSpecifications())
				.build();

		Assert.assertFalse(stmt.getOptionalValue("CANONICAL").isPresent());
	}

	@Test
	public void testRemoveEnumField() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSpecifications(newGnomADSpecifications())
				.removeField(new CustomStatementField("ANNOTATION_NAME"))
				.build();

		Assert.assertFalse(stmt.hasField("ANNOTATION_NAME"));
	}

	@Test
	public void testRemoveCustomField() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSpecifications(newGnomADSpecifications())
				.removeField(new CustomStatementField("ALLELE_SAMPLED"))
				.build();

		Assert.assertFalse(stmt.hasField("ALLELE_SAMPLED"));
	}

	@Test
	public void testResetField() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSpecifications(newGnomADSpecifications())
				.addField(new CustomStatementField("ANNOTATION_NAME"), "roudoudou")
				.build();

		Assert.assertTrue(stmt.getOptionalValue("ANNOTATION_NAME").isPresent());
		Assert.assertEquals("roudoudou", stmt.getOptionalValue("ANNOTATION_NAME").get());
	}

	@Test
	public void testGetIsoformAccession() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.addField(NEXTPROT_ACCESSION, "NX_Q6S545-2")
				.build();

		Assert.assertTrue(stmt.getOptionalValue("NEXTPROT_ACCESSION").isPresent());
		Assert.assertTrue(stmt.getOptionalIsoformAccession().isPresent());
		Assert.assertEquals("NX_Q6S545-2", stmt.getOptionalIsoformAccession().get());
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
				"\""+EXTRA_FIELDS +"\":\"{\\\"ALLELE_COUNT\\\":\\\"1\\\",\\\"ALLELE_SAMPLED\\\":\\\"217610\\\",\\\"CANONICAL\\\":\\\"YES\\\",\\\"DBSNP_ID\\\":\\\"rs745905374\\\"}\",\n" +
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
				"\""+EXTRA_FIELDS +"\":\"{\\\"ALLELE_COUNT\\\":\\\"1\\\",\\\"ALLELE_SAMPLED\\\":\\\"217610\\\",\\\"DBSNP_ID\\\":\\\"rs745905374\\\"}\",\n" +
				"\"SOURCE\": \"gnomAD\",\n" +
				"\"STATEMENT_ID\": \"792d509b2d452da2cf4a74faa2773c15\",\n" +
				"\"VARIANT_ORIGINAL_AMINO_ACID\": \"W\",\n" +
				"\"VARIANT_VARIATION_AMINO_ACID\": \"*\"\n" +
				"}";
	}

	private Statement buildStatementFromJsonString(String content) throws IOException {

		return new Specifications.Builder().build().jsonReader().readStatement(content);
	}

	private StatementSpecifications newGnomADSpecifications() {

		return new Specifications.Builder()
				.withExtraFields(Arrays.asList("CANONICAL", "ALLELE_COUNT", "ALLELE_SAMPLED"))
				.withExtraFieldsContributingToUnicityKey(Collections.singletonList("DBSNP_ID"))
				.build();
	}
}
