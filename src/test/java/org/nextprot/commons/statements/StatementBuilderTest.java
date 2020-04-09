package org.nextprot.commons.statements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOTATION_CATEGORY;
import static org.nextprot.commons.statements.specs.CoreStatementField.ENTRY_ACCESSION;
import static org.nextprot.commons.statements.specs.CoreStatementField.EVIDENCE_QUALITY;
import static org.nextprot.commons.statements.specs.CoreStatementField.LOCATION_BEGIN;
import static org.nextprot.commons.statements.specs.CoreStatementField.NEXTPROT_ACCESSION;
import static org.nextprot.commons.statements.specs.CoreStatementField.STATEMENT_ID;

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
import org.nextprot.commons.statements.reader.JsonStatementReader;
import org.nextprot.commons.statements.specs.CoreStatementField;
import org.nextprot.commons.statements.specs.CustomStatementField;
import org.nextprot.commons.statements.specs.MutableStatementSpecifications;
import org.nextprot.commons.statements.specs.Specifications;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;

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

		Assert.assertEquals("AAA", rs.getEntryAccession());
		Assert.assertEquals("CCC", rs.getAnnotationCategory());
		Assert.assertEquals("GOLD", rs.getValue(EVIDENCE_QUALITY));
		Assert.assertNotNull(rs.getSpecifications());
		Assert.assertEquals(4, rs.getSpecifications().size());
		Assert.assertTrue(rs.hasField(ENTRY_ACCESSION.getName()));
		Assert.assertTrue(rs.getSpecifications().hasField(ANNOTATION_CATEGORY.getName()));
		Assert.assertTrue(rs.getSpecifications().hasField(EVIDENCE_QUALITY.getName()));
		Assert.assertTrue(rs.getSpecifications().hasField(STATEMENT_ID.getName()));
	}


	@Test
	public void shouldBePossibleToAssignValueToCompositeField2() {

		CustomStatementField field1 = new CustomStatementField("f1");
		CustomStatementField field2 = new CustomStatementField("f2");

		Statement s = new StatementBuilder()
				.withSpecifications(new MutableStatementSpecifications()
				.specifyFields(field1, field2))
				.addField(field1, "1")
				.addField(field2, "217610")
				.build();

		Assert.assertTrue(s.getSpecifications().hasField("f1"));
		Assert.assertTrue(s.getSpecifications().hasField("f2"));
		Assert.assertEquals("1", s.getValue(field1));
		Assert.assertEquals("217610", s.getValue(field2));
	}

	
	@Test
	public void shouldHaveSameStatementIdAndAnnotationId() {
		
		CoreStatementField f1 = CoreStatementField.ANNOT_CV_TERM_ACCESSION;
		CoreStatementField f2 = CoreStatementField.ANNOT_CV_TERM_NAME;
		CustomStatementField f3 = new CustomStatementField("f3", true); // true: part of unique key for annotation id
		CustomStatementField f4 = new CustomStatementField("f4");
		StatementSpecifications specs;
		Statement stmt;
		
		specs = new MutableStatementSpecifications().specifyFields(f1, f2, f3, f4);
		stmt = new StatementBuilder().withSpecifications(specs).withAnnotationHash()
				.addField(f1, "valf1 ")
				.addField(f2, "valf2 ")
				.addField(f3, "valf3 ")
				.addField(f4, "valf4 ")
				.build();
		System.out.println("annot id: " + stmt.getAnnotationId());
		System.out.println("stmt  id: " + stmt.getStatementId());
		Assert.assertEquals("5d8961f64e671c6de9ee42915c8df815", stmt.getAnnotationId());
		Assert.assertEquals("9b09f5a63f9fb3237835db33bf1dcb28", stmt.getStatementId());

		String annotId = stmt.getAnnotationId();
		String stmtId = stmt.getStatementId();
		
		// check that we get same ids after adding field in another order
		stmt = new StatementBuilder().withSpecifications(specs).withAnnotationHash()
				.addField(f4, "valf4 ")
				.addField(f3, "valf3 ")
				.addField(f2, "valf2 ")
				.addField(f1, "valf1 ")
				.build();
		Assert.assertEquals(annotId, stmt.getAnnotationId());
		Assert.assertEquals(stmtId, stmt.getStatementId());
		
		// check we get the same ids on after declaing field in alternative order in specifications
		specs = new MutableStatementSpecifications().specifyFields(f2, f3, f4, f1);
		stmt = new StatementBuilder().withSpecifications(specs).withAnnotationHash()
				.addField(f1, "valf1 ")
				.addField(f3, "valf3 ")
				.addField(f2, "valf2 ")
				.addField(f4, "valf4 ")
				.build();
		Assert.assertEquals(annotId, stmt.getAnnotationId());
		Assert.assertEquals(stmtId, stmt.getStatementId());
		
	}
	
	
	@Test
	public void shouldHaveSameStatementId() {

		CoreStatementField field1 = CoreStatementField.ANNOT_CV_TERM_ACCESSION;
		CoreStatementField field2 = CoreStatementField.ANNOT_CV_TERM_NAME;
		CustomStatementField field3 = new CustomStatementField("f3");
		CustomStatementField field4 = new CustomStatementField("f4");

		// NOTE: during the building of the statement key, values are added on a controlled order by the builder !!!
		Statement s1 = new StatementBuilder()
				.withSpecifications(new MutableStatementSpecifications()
				.specifyFields(field1, field2))
				.addField(field1, "1")
				.addField(field2, "217610").build();

		// NOTE: during the building of the statement key, values are added on a controlled order by the builder !!!
		Statement s2 = new StatementBuilder()
				.withSpecifications(new MutableStatementSpecifications()
				.specifyFields(field3, field4))
				.addField(field3, "1")
				.addField(field4, "217610").build();

		Assert.assertEquals(s1.getStatementId(), s2.getStatementId());
		Assert.assertEquals("5ab4289a88bf7f86288c82f538c3e09b", s1.getStatementId());

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
	public void testPam1()  throws IOException {

		String subjectOne = "{\n" +
				"  \"ANNOTATION_CATEGORY\" : \"variant\",\n" +
				"  \"ANNOTATION_NAME\" : \"MSH6-p.Phe1088Leufs*5\",\n" +
				"  \"ANNOT_SOURCE_ACCESSION\" : \"\",\n" +
				"  \"ASSIGMENT_METHOD\" : \"curated\",\n" +
				"  \"ASSIGNED_BY\" : \"NextProt\",\n" +
			//	"  \"DEBUG_NOTE\" : \"Publication not found for CAVA-VD012367nullremoving one allele for multiple genes CAVA-VP014556 set: Set(MSH6-p.Phe1088Leufs*5, MSH6-p.Phe1088Serfs*2, PMS2-p.Arg315*) filtered set Set(MSH6-p.Phe1088Leufs*5, MSH6-p.Phe1088Serfs*2) subject: MSH6-p.Phe1088Serfs*2 gene getName msh6\\n\",\n" +
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
			//	"  \"DEBUG_NOTE\" : \"Publication not found for CAVA-VD012367\",\n" +
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
		vp1Hash = StatementBuilder.extractUniqueFieldValues(vp1, UniqueKey.STATEMENT);
		vp2Hash = StatementBuilder.extractUniqueFieldValues(vp2, UniqueKey.STATEMENT);
		Assert.assertEquals(vp1Hash, vp2Hash);
	}	
	
	
	@Test
	public void testWithCustomFields() {

		StatementField sf = new CustomStatementField("DBSNP_ID");
		Statement rs1 = new StatementBuilder()
				.addField(sf, "rs745905374")
				.build();
		Assert.assertTrue(rs1.containsKey(CoreStatementField.STATEMENT_ID));
		Assert.assertTrue(rs1.containsKey(sf));
	}

	@Test
	public void testWithCustomFields2() {

		StatementField sf = new CustomStatementField("PDIMI_ID");
		Statement rs1 = new StatementBuilder()
				.addField(sf, "my:psimi")
				.build();
		Assert.assertTrue(rs1.containsKey(CoreStatementField.STATEMENT_ID));
		Assert.assertTrue(rs1.containsKey(sf));
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
		Assert.assertFalse(stmt.getOptionalValue("ROUDOUDOU").isPresent());
	}

	@Test
	public void testGnomADStatementsUniqueEntryKey() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSON()))
				.withSpecifications(newGnomADSpecifications())
				.withAnnotationHash()
				.build();

		String key = StatementBuilder.extractUniqueFieldValues(stmt, UniqueKey.ENTRY);
		
		// check that DBSNP_ID custom field is part of unique key
		Assert.assertTrue(key.contains("rs745905374")); 

		// check that CANONICAL custom field is NOT part of unique key
		Assert.assertTrue(! key.contains("YES")); 
		// check that ALLELE_SAMPLED custom field is NOT part of unique key
		Assert.assertTrue(! key.contains("217610")); 
		
		
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
	public void testGetCustomFieldsValue() throws IOException {

		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getGnomADStatementAsJSONWith4CustomFields()))
				.withSpecifications(newGnomADSpecifications())
				.build();
		
		Assert.assertEquals("1", stmt.getOptionalValue("ALLELE_COUNT").get());
		Assert.assertEquals("YES", stmt.getOptionalValue("CANONICAL").get());
		Assert.assertEquals("217610", stmt.getOptionalValue("ALLELE_SAMPLED").get());
		Assert.assertEquals("rs745905374", stmt.getOptionalValue("DBSNP_ID").get());
		
	}

	@Test
	public void testCannotFindFieldValueFromNonExistingCustomField() throws IOException {

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

	@Test
	public void testSpecificationAfterCopy() throws IOException {

		Statement stmt = new StatementBuilder()
				.addField(NEXTPROT_ACCESSION, "NX_Q6S545-2")
				.addField(LOCATION_BEGIN, "2")
				.build();

		StatementSpecifications specs = stmt.getSpecifications();

		Assert.assertEquals(3, specs.size());
		Assert.assertTrue(specs.hasField(NEXTPROT_ACCESSION.getName()));
		Assert.assertTrue(specs.hasField(LOCATION_BEGIN.getName()));
		Assert.assertTrue(specs.hasField(STATEMENT_ID.getName()));

		Statement copy = new StatementBuilder(stmt)
				.addField(ENTRY_ACCESSION, "NX_Q6S545")
				.build();

		StatementSpecifications copySpecs = copy.getSpecifications();

		Assert.assertEquals(4, copySpecs.size());
		Assert.assertTrue(copySpecs.hasField(NEXTPROT_ACCESSION.getName()));
		Assert.assertTrue(copySpecs.hasField(LOCATION_BEGIN.getName()));
		Assert.assertTrue(copySpecs.hasField(STATEMENT_ID.getName()));
		Assert.assertTrue(copySpecs.hasField(ENTRY_ACCESSION.getName()));
	}

	@Test
	public void testToJsonString() {

		Statement stmt = new StatementBuilder()
				.addField(NEXTPROT_ACCESSION, "NX_Q6S545-2")
				.addField(LOCATION_BEGIN, "2")
				.build();

		Assert.assertEquals("{\"LOCATION_BEGIN\": \"2\",\"NEXTPROT_ACCESSION\": \"NX_Q6S545-2\",\"STATEMENT_ID\": \"424a11008e28b7cf044bd01cd5ac8e53\"}", stmt.toJsonString());
	}

	@Test
	public void testToJsonStringFromList() {

		Statement stmt = new StatementBuilder()
				.addField(NEXTPROT_ACCESSION, "NX_Q6S545-2")
				.addField(LOCATION_BEGIN, "2")
				.build();

		Statement copy = new StatementBuilder(stmt)
				.addField(ENTRY_ACCESSION, "NX_Q6S545")
				.build();

		Assert.assertEquals("[{\"LOCATION_BEGIN\": \"2\",\"NEXTPROT_ACCESSION\": \"NX_Q6S545-2\",\"STATEMENT_ID\": \"424a11008e28b7cf044bd01cd5ac8e53\"},{\"ENTRY_ACCESSION\": \"NX_Q6S545\",\"LOCATION_BEGIN\": \"2\",\"NEXTPROT_ACCESSION\": \"NX_Q6S545-2\",\"STATEMENT_ID\": \"386a83d2f78106516b688f8f39e11f11\"}]", Statement.toJsonString(Arrays.asList(stmt, copy)));
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

	private String getGnomADStatementAsJSONWith4CustomFields() {

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
				"\"ALLELE_COUNT\":\"1\",\n" +
				"\"ALLELE_SAMPLED\":\"217610\",\n" +
				"\"DBSNP_ID\":\"rs745905374\",\n" +
				"\"CANONICAL\":\"YES\",\n" +
				"\"SOURCE\": \"gnomAD\",\n" +
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
				"\"ALLELE_COUNT\":\"1\",\n" +
				"\"ALLELE_SAMPLED\":\"217610\",\n" +
				"\"DBSNP_ID\":\"rs745905374\",\n" +
				"\"SOURCE\": \"gnomAD\",\n" +
				"\"STATEMENT_ID\": \"792d509b2d452da2cf4a74faa2773c15\",\n" +
				"\"VARIANT_ORIGINAL_AMINO_ACID\": \"W\",\n" +
				"\"VARIANT_VARIATION_AMINO_ACID\": \"*\"\n" +
				"}";
	}

	private Statement buildStatementFromJsonString(String content) throws IOException {

		return new JsonStatementReader(content, new Specifications.Builder().build()).readStatements().get(0);
	}

	private static StatementSpecifications newGnomADSpecifications() {

		return new Specifications.Builder()
				.withExtraFields(Arrays.asList("CANONICAL", "ALLELE_COUNT", "ALLELE_SAMPLED"))
				.withExtraFieldsContributingToUnicityKey(Collections.singletonList("DBSNP_ID"))
				.build();
	}
	
	private String getBioEditorGoStmt() {
		return "{\n" +
				"\"ANNOT_CV_TERM_TERMINOLOGY\": \"Gene Ontology\",\n" + 
				"\"REFERENCE_ACCESSION\": \"11485994\",\n" + 
				"\"STATEMENT_ID\": \"cf819dd2e3731c2ab7f1ef52b203b6ab\",\n" + 
				"\"ENTRY_ACCESSION\": \"NX_P36896\",\n" + 
				"\"ANNOT_CV_TERM_ACCESSION\": \"GO:0038092\",\n" + 
				"\"ASSIGMENT_METHOD\": \"curated\",\n" + 
				"\"ASSIGNED_BY\": \"NextProt\",\n" + 
				"\"EVIDENCE_CODE\": \"ECO:0000250\",\n" + 
				"\"NEXTPROT_ACCESSION\": \"NX_P36896\",\n" + 
				"\"ANNOTATION_CATEGORY\": \"go-biological-process\",\n" + 
				"\"SOURCE\": \"BioEditor\",\n" + 
				"\"ANNOT_SOURCE_ACCESSION\": \"KKPA-BP005170-ev1\",\n" + 
				"\"RESOURCE_TYPE\": \"publication\",\n" + 
				"\"ANNOT_CV_TERM_NAME\":\"nodal signaling pathway\",\n" + 
				"\"EVIDENCE_QUALITY\":\"GOLD\",\n" + 
				"\"REFERENCE_DATABASE\": \"PubMed\",\n" + 
				"\"GENE_NAME\": \"ACVR1B\"" +
		"}";
	}

	private String getBioEditorVariantStmt() {
		return "{\n" + 
				"\"ANNOTATION_NAME\" : \"APC-p.Arg1114*\",\n" + 
				"\"REFERENCE_ACCESSION\" : \"17176113\",\n" + 
				"\"LOCATION_END\" : \"1114\",\n" + 
				"\"STATEMENT_ID\" : \"d69ae38281a300a9dd7e01a476e78225\",\n" + 
				"\"ENTRY_ACCESSION\" : \"NX_P25054\",\n" + 
				"\"ASSIGNED_BY\" : \"NextProt\",\n" + 
				"\"ASSIGMENT_METHOD\" : \"curated\",\n" + 
				"\"VARIANT_ORIGINAL_AMINO_ACID\" : \"R\",\n" + 
				"\"EVIDENCE_CODE\" : \"ECO:0000219\",\n" + 
				"\"LOCATION_BEGIN\" : \"1114\",\n" + 
				"\"NEXTPROT_ACCESSION\" : \"NX_P25054\",\n" + 
				"\"ANNOTATION_CATEGORY\" : \"variant\",\n" + 
				"\"SOURCE\" : \"BioEditor\",\n" + 
				"\"ANNOT_SOURCE_ACCESSION\" : \"N/A\",\n" + 
				"\"VARIANT_VARIATION_AMINO_ACID\" : \"*\",\n" + 
				"\"RESOURCE_TYPE\" : \"publication\",\n" + 
				"\"EVIDENCE_QUALITY\" : \"GOLD\",\n" + 
				"\"REFERENCE_DATABASE\" : \"PubMed\",\n" + 
				"\"GENE_NAME\" : \"APC\"\n" + 
				"}";
	}
	
	private String getBioEditorPhenotypicVariationStmt1() {
		return "{" + 
				"\"ANNOT_CV_TERM_ACCESSION\" : \"ME:0000004\",\n" + 
				"\"ASSIGMENT_METHOD\" : \"curated\",\n" + 
				"\"ANNOT_SOURCE_ACCESSION\" : \"CAVA-VP013526-ev1\",\n" + 
				"\"ENTRY_ACCESSION\" : \"NX_P25054\",\n" + 
				"\"ANNOTATION_CATEGORY\" : \"phenotypic-variation\",\n" + 
				"\"ASSIGNED_BY\" : \"NextProt\",\n" + 
				"\"SOURCE\" : \"BioEditor\",\n" + 
				"\"ANNOT_DESCRIPTION\" : \"(APC-p.Thr1430*) decreases binding to FEN1\",\n" + 
				"\"ANNOTATION_SUBJECT_SPECIES\" : \"Homo sapiens\",\n" + 
				"\"ANNOT_CV_TERM_NAME\" : \"decrease\",\n" + 
				"\"SUBJECT_STATEMENT_IDS\" : \"9282cdd86d9e997a1cb372f6961621d2,d69ae38281a300a9dd7e01a476e78225\",\n" + 
				"\"REFERENCE_ACCESSION\" : \"17176113\",\n" + 
				"\"EVIDENCE_INTENSITY\" : \"Severe\",\n" + 
				"\"ANNOT_CV_TERM_TERMINOLOGY\" : \"modification-effect-cv\",\n" + 
				"\"OBJECT_STATEMENT_IDS\" : \"b151a59550053b05a74f02ea19330d56\",\n" + 
				"\"STATEMENT_ID\" : \"eaf3e05113a408260237e7e8fb649736\",\n" + 
				"\"EVIDENCE_CODE\" : \"ECO:0000006\",\n" + 
				"\"ANNOTATION_OBJECT_SPECIES\" : \"Homo sapiens\",\n" + 
				"\"EVIDENCE_NOTE\" : \"Additional experimental evidence:natural variation mutant evidence used in manual assertion, immunoprecipitation evidence used in manual assertion\\n\",\n" + 
				"\"GENE_NAME\" : \"APC\",\n" + 
				"\"EVIDENCE_QUALITY\" : \"GOLD\",\n" + 
				"\"REFERENCE_DATABASE\" : \"PubMed\",\n" + 
				"\"RESOURCE_TYPE\" : \"publication\" }";
	}

	private String getBioEditorPhenotypicVariationStmt2() {
		return "{ " +
				"\"ANNOT_CV_TERM_ACCESSION\": \"ME:0000004\",\n" + 
				"\"ASSIGMENT_METHOD\": \"curated\",\n" + 
				"\"ANNOT_SOURCE_ACCESSION\": \"CAVA-VP012675-ev1\",\n" + 
				"\"ENTRY_ACCESSION\": \"NX_P25054\",\n" + 
				"\"ANNOTATION_CATEGORY\": \"phenotypic-variation\",\n" + 
				"\"ASSIGNED_BY\": \"NextProt\",\n" + 
				"\"SOURCE\": \"BioEditor\",\n" + 
				"\"ANNOT_DESCRIPTION\": \"(APC-p.Ser1392Ala) decreases binding to AXIN1\",\n" + 
				"\"ANNOTATION_SUBJECT_SPECIES\": \"Homo sapiens\",\n" + 
				"\"ANNOT_CV_TERM_NAME\": \"decrease\",\n" + 
				"\"SUBJECT_STATEMENT_IDS\": \"e4d8d16593b479fd044a87a4ef9d5ba1\",\n" + 
				"\"REFERENCE_ACCESSION\": \"11487578\",\n" + 
				"\"EVIDENCE_INTENSITY\": \"Moderate\",\n" + 
				"\"ANNOT_CV_TERM_TERMINOLOGY\": \"modification-effect-cv\",\n" + 
				"\"OBJECT_STATEMENT_IDS\": \"8d3ab70e4080bab8eaf376bb87008f78\",\n" + 
				"\"STATEMENT_ID\": \"1148f534e8a162a838a89d12734b8777\",\n" + 
				"\"EVIDENCE_CODE\": \"ECO:0000006\",\n" + 
				"\"ANNOTATION_OBJECT_SPECIES\": \"Homo sapiens\",\n" + 
				"\"EVIDENCE_NOTE\": \"Additional experimental evidence:point mutation evidence used in manual assertion, co-immunoprecipitation evidence\\n\",\n" + 
				"\"GENE_NAME\": \"APC\",\n" + 
				"\"EVIDENCE_QUALITY\": \"SILVER\",\n" + 
				"\"REFERENCE_DATABASE\": \"PubMed\",\n" + 
				"\"RESOURCE_TYPE\": \"publication\" }";
	}

	@Test
	public void test1() {
		System.out.println("aaa\nbbb");
	}
	
	@Test
	public void testStatementIdValueComputation_1() throws IOException {
		// statement created before the refactoring of nextprot-commons
		// picked from http://kant:9001/bioeditor/2020-01-13/ACVR1B%40strauss.json 
		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getBioEditorGoStmt())).build();
		Assert.assertEquals("cf819dd2e3731c2ab7f1ef52b203b6ab", stmt.getStatementId());
	}

	@Test
	public void testStatementIdValueComputation_2() throws IOException {
		// statement created before the refactoring of nextprot-commons
		// picked from http://kant:9001/bioeditor/2020-01-13/APC%40gauss.json 
		Statement stmt = new StatementBuilder(buildStatementFromJsonString(getBioEditorVariantStmt())).build();
		Assert.assertEquals("d69ae38281a300a9dd7e01a476e78225", stmt.getStatementId());
	}

	@Test
	public void testStatementIdValueComputation_3() throws IOException {
		// statement created before the refactoring of nextprot-commons
		// picked from http://kant:9001/bioeditor/2020-01-13/APC%40gauss.json 
		
		Statement stmt;
		// Stmt1 
		stmt = new StatementBuilder(buildStatementFromJsonString(getBioEditorPhenotypicVariationStmt1())).build();
		Assert.assertEquals("eaf3e05113a408260237e7e8fb649736", stmt.getStatementId());

		// Stmt2
		stmt = new StatementBuilder(buildStatementFromJsonString(getBioEditorPhenotypicVariationStmt2())).build();
		Assert.assertEquals("1148f534e8a162a838a89d12734b8777", stmt.getStatementId());
		//
	}

	
	
}
