package org.nextprot.commons.statements.reader;

import org.junit.Assert;
import org.junit.Test;
import org.nextprot.commons.statements.Statement;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import static org.nextprot.commons.statements.reader.JsonReaderTest.getStatements;
import static org.nextprot.commons.statements.specs.CoreStatementField.*;

public class JsonStreamingReaderTest {

	@Test
	public void readStatement() throws IOException {

		StringReader sr = new StringReader(getStatements());

		JsonStreamingReader reader = new JsonStreamingReader(sr);

		Optional<Statement> statement = reader.readStatement();
		Assert.assertTrue(statement.isPresent());
		Statement s = statement.get();
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

		statement = reader.readStatement();
		Assert.assertTrue(statement.isPresent());
		s = statement.get();
		Assert.assertEquals(24, s.size());
		Assert.assertEquals("phenotypic-variation", s.getValue(ANNOTATION_CATEGORY));
		statement = reader.readStatement();
		Assert.assertFalse(statement.isPresent());
	}

	@Test
	public void readNStatements() throws IOException {

		StringReader sr = new StringReader(getStatements());

		JsonStreamingReader reader = new JsonStreamingReader(sr);

		List<Statement> statements = reader.readStatements(2);
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
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReadZeroStatements() throws IOException {

		StringReader sr = new StringReader(getStatements());

		JsonStreamingReader reader = new JsonStreamingReader(sr);
		reader.readStatements(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReadNegativeStatements() throws IOException {

		StringReader sr = new StringReader(getStatements());

		JsonStreamingReader reader = new JsonStreamingReader(sr);
		reader.readStatements(-2);
	}

	@Test
	public void shouldReadAtMostNStatements() throws IOException {

		StringReader sr = new StringReader(getStatements());

		JsonStreamingReader reader = new JsonStreamingReader(sr);

		List<Statement> statements = reader.readStatements(20);
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
	}
}