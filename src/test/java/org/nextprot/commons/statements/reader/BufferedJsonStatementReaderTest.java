package org.nextprot.commons.statements.reader;

import org.junit.Assert;
import org.junit.Test;
import org.nextprot.commons.statements.Statement;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.nextprot.commons.statements.reader.JsonStatementReaderTest.getStatement;
import static org.nextprot.commons.statements.reader.JsonStatementReaderTest.getStatements;
import static org.nextprot.commons.statements.specs.CoreStatementField.*;

public class BufferedJsonStatementReaderTest {

	@Test
	public void hasNextShouldNotConsumeTokens() throws IOException {

		BufferedJsonStatementReader reader = new BufferedJsonStatementReader(new StringReader(getStatements()));
		Assert.assertTrue(reader.hasStatement());
	}

	@Test
	public void readStatement() throws IOException {

		BufferedJsonStatementReader reader = new BufferedJsonStatementReader(new StringReader(getStatement()));

		Statement s = reader.nextStatement();
		Assert.assertNotNull(s);
		Assert.assertEquals(13, s.size());
		Assert.assertEquals("variant", s.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("POTEH-p.Trp34Ter", s.getValue(ANNOTATION_NAME));
		Assert.assertEquals("NextProt", s.getValue(ASSIGNED_BY));
		Assert.assertEquals("ECO:0000269", s.getValue(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", s.getValue(EVIDENCE_QUALITY));
		Assert.assertEquals("POTEH", s.getValue(GENE_NAME));
		Assert.assertEquals("34", s.getValue(LOCATION_BEGIN));
		Assert.assertEquals("34", s.getValue(LOCATION_END));
		Assert.assertEquals("NX_Q6S545", s.getValue(NEXTPROT_ACCESSION));
		Assert.assertEquals("gnomAD", s.getValue(SOURCE));
		Assert.assertEquals("2dc94938c20a61ea69df3b0434b50e71", s.getValue(STATEMENT_ID));
		Assert.assertEquals("W", s.getValue(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("*", s.getValue(VARIANT_VARIATION_AMINO_ACID));

		Assert.assertFalse(reader.hasStatement());
	}

	@Test
	public void readStatementTwice() throws IOException {

		BufferedJsonStatementReader reader = new BufferedJsonStatementReader(new StringReader(getStatements()));

		Statement s = reader.nextStatement();
		Assert.assertNotNull(s);
		Assert.assertEquals(17, s.size());
		Assert.assertEquals("variant", s.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("SCN9A-iso3-p.Phe1449Val", s.getValue(ANNOTATION_NAME));
		Assert.assertEquals("N/A", s.getValue(ANNOT_SOURCE_ACCESSION));
		Assert.assertEquals("NextProt", s.getValue(ASSIGNED_BY));
		Assert.assertEquals("curated", s.getValue(ASSIGMENT_METHOD));
		Assert.assertEquals("ECO:0000219", s.getValue(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", s.getValue(EVIDENCE_QUALITY));
		Assert.assertEquals("SCN9A", s.getValue(GENE_NAME));
		Assert.assertEquals("1449", s.getValue(LOCATION_BEGIN));
		Assert.assertEquals("1449", s.getValue(LOCATION_END));
		Assert.assertEquals("NX_Q15858", s.getValue(NEXTPROT_ACCESSION));
		Assert.assertEquals("NX_Q15858", s.getValue(ENTRY_ACCESSION));
		Assert.assertEquals("BioEditor", s.getValue(SOURCE));
		Assert.assertEquals("publication", s.getValue(RESOURCE_TYPE));
		Assert.assertEquals("618c2674745cea56a67cfd6a9fb5718e", s.getValue(STATEMENT_ID));
		Assert.assertEquals("F", s.getValue(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("V", s.getValue(VARIANT_VARIATION_AMINO_ACID));

		s = reader.nextStatement();
		Assert.assertNotNull(s);
		Assert.assertEquals(24, s.size());
		Assert.assertEquals("phenotypic-variation", s.getValue(ANNOTATION_CATEGORY));
		s = reader.nextStatement();
		Assert.assertNull(s);

		Assert.assertFalse(reader.hasStatement());
	}

	@Test
	public void readNStatements() throws IOException {

		StringReader sr = new StringReader(getStatements());

		BufferedJsonStatementReader reader = new BufferedJsonStatementReader(sr, 2);

		List<Statement> statements = reader.readStatements();
		Assert.assertFalse(statements.isEmpty());
		Assert.assertEquals(2, statements.size());

		Statement s = statements.get(0);
		Assert.assertEquals(17, s.size());
		Assert.assertEquals("variant", s.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("SCN9A-iso3-p.Phe1449Val", s.getValue(ANNOTATION_NAME));
		Assert.assertEquals("N/A", s.getValue(ANNOT_SOURCE_ACCESSION));
		Assert.assertEquals("NextProt", s.getValue(ASSIGNED_BY));
		Assert.assertEquals("curated", s.getValue(ASSIGMENT_METHOD));
		Assert.assertEquals("ECO:0000219", s.getValue(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", s.getValue(EVIDENCE_QUALITY));
		Assert.assertEquals("SCN9A", s.getValue(GENE_NAME));
		Assert.assertEquals("1449", s.getValue(LOCATION_BEGIN));
		Assert.assertEquals("1449", s.getValue(LOCATION_END));
		Assert.assertEquals("NX_Q15858", s.getValue(NEXTPROT_ACCESSION));
		Assert.assertEquals("NX_Q15858", s.getValue(ENTRY_ACCESSION));
		Assert.assertEquals("BioEditor", s.getValue(SOURCE));
		Assert.assertEquals("publication", s.getValue(RESOURCE_TYPE));
		Assert.assertEquals("618c2674745cea56a67cfd6a9fb5718e", s.getValue(STATEMENT_ID));
		Assert.assertEquals("F", s.getValue(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("V", s.getValue(VARIANT_VARIATION_AMINO_ACID));

		s = statements.get(1);
		Assert.assertEquals(24, s.size());
		Assert.assertEquals("phenotypic-variation", s.getValue(ANNOTATION_CATEGORY));

		Assert.assertFalse(reader.hasStatement());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReadZeroStatements() throws IOException {

		new BufferedJsonStatementReader(new StringReader(getStatements()), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReadNegativeStatements() throws IOException {

		new BufferedJsonStatementReader(new StringReader(getStatements()), -2);
	}

	@Test
	public void shouldReadAtMostNStatements() throws IOException {

		StringReader sr = new StringReader(getStatements());

		BufferedJsonStatementReader reader = new BufferedJsonStatementReader(sr, 20);

		List<Statement> statements = reader.readStatements();
		Assert.assertFalse(statements.isEmpty());
		Assert.assertEquals(2, statements.size());

		Statement s = statements.get(0);
		Assert.assertEquals(17, s.size());
		Assert.assertEquals("variant", s.getValue(ANNOTATION_CATEGORY));
		Assert.assertEquals("SCN9A-iso3-p.Phe1449Val", s.getValue(ANNOTATION_NAME));
		Assert.assertEquals("N/A", s.getValue(ANNOT_SOURCE_ACCESSION));
		Assert.assertEquals("NextProt", s.getValue(ASSIGNED_BY));
		Assert.assertEquals("curated", s.getValue(ASSIGMENT_METHOD));
		Assert.assertEquals("ECO:0000219", s.getValue(EVIDENCE_CODE));
		Assert.assertEquals("GOLD", s.getValue(EVIDENCE_QUALITY));
		Assert.assertEquals("SCN9A", s.getValue(GENE_NAME));
		Assert.assertEquals("1449", s.getValue(LOCATION_BEGIN));
		Assert.assertEquals("1449", s.getValue(LOCATION_END));
		Assert.assertEquals("NX_Q15858", s.getValue(NEXTPROT_ACCESSION));
		Assert.assertEquals("NX_Q15858", s.getValue(ENTRY_ACCESSION));
		Assert.assertEquals("BioEditor", s.getValue(SOURCE));
		Assert.assertEquals("publication", s.getValue(RESOURCE_TYPE));
		Assert.assertEquals("618c2674745cea56a67cfd6a9fb5718e", s.getValue(STATEMENT_ID));
		Assert.assertEquals("F", s.getValue(VARIANT_ORIGINAL_AMINO_ACID));
		Assert.assertEquals("V", s.getValue(VARIANT_VARIATION_AMINO_ACID));

		s = statements.get(1);
		Assert.assertEquals(24, s.size());
		Assert.assertEquals("phenotypic-variation", s.getValue(ANNOTATION_CATEGORY));

		Assert.assertFalse(reader.hasStatement());
	}
}