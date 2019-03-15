package org.nextprot.commons.statements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.nextprot.commons.constants.QualityQualifier;

public class RawStatementTest {
	
	QualityQualifier defaultQuality = QualityQualifier.GOLD;
	
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
				.addField(PredefinedStatementField.NEXTPROT_ACCESSION, "NX_P25054")
				.addField(PredefinedStatementField.GENE_NAME, "apc")
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
   	    	    .addField(PredefinedStatementField.TARGET_ISOFORMS, "[]")
				.addSourceInfo("CAVA-VP90999", "BED").buildWithAnnotationHash();
		Statement rs2 = StatementBuilder.createNew()
				.addField(PredefinedStatementField.NEXTPROT_ACCESSION, "NX_P25054")
				.addField(PredefinedStatementField.GENE_NAME, "apc")
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality)
   	    	    .addField(PredefinedStatementField.TARGET_ISOFORMS, "[]")
				.addSourceInfo("XPTO", "Caviar").buildWithAnnotationHash();

		assertNotEquals(rs1, rs2); 
		assertEquals(rs1.getValue(PredefinedStatementField.ANNOTATION_ID), rs2.getValue(PredefinedStatementField.ANNOTATION_ID));
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

		assertEquals(rs1.getValue(PredefinedStatementField.DEBUG_INFO), "Oh yeah");
		assertEquals(rs2.getValue(PredefinedStatementField.DEBUG_INFO), "oh oh");
	}

	@Test
	public void testWithCustomFields() {

		Statement rs1 = StatementBuilder.createNew()
				.addCompulsoryFields("AAA", "BBB", "CCC", defaultQuality).build();
		Statement rs2 = StatementBuilder.createNew().addMap(rs1).build();

		assertEquals(rs1, rs2);
	}
}
