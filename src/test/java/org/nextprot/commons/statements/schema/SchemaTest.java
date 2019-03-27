package org.nextprot.commons.statements.schema;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.StatementField;
import org.nextprot.commons.statements.constants.StatementTableNames;

import java.util.Arrays;
import java.util.Collection;

public class SchemaTest {

	@Test
	public void testRegisterField() {

		MutableSchema schema = new MutableSchema();
		StatementField field = mockField("roudoudou");
		schema.registerField(field);
		Assert.assertTrue(schema.hasField("roudoudou"));
		Assert.assertEquals(1, schema.size());
	}

	@Test
	public void testGetStatementFields() {

		MutableSchema schema = new MutableSchema();
		StatementField field = mockField("roudoudou");
		schema.registerField(field);
		Collection<StatementField> fields = schema.getFields();
		Assert.assertEquals(1, fields.size());
		Assert.assertEquals("roudoudou", fields.iterator().next().getName());
	}

	@Test
	public void testGetStatementField() {

		MutableSchema schema = new MutableSchema();
		StatementField field = mockField("roudoudou");
		schema.registerField(field);
		Assert.assertTrue(schema.hasField("roudoudou"));
		Assert.assertEquals("roudoudou", schema.getField("roudoudou").getName());
	}

	@Test
	public void testSearchCompositeField() {

		StatementField f1 = mockField("f1");
		StatementField f2 = mockField("f2");
		StatementField f3 = new CompositeField("f3", Arrays.asList(f1, f2));

		MutableSchema schema = new MutableSchema();
		schema.registerFields(f1, f2, f3);

		CompositeField cf = schema.searchCompositeFieldOrNull(f1);
		Assert.assertNotNull(cf);
		Assert.assertEquals("f3", cf.getName());
	}

	@Test(expected = IllegalStateException.class)
	public void testSearchCompositeField2() {

		StatementField f1 = mockField("f1");
		StatementField f2 = mockField("f2");
		StatementField f3 = mockField("f3");
		StatementField f4 = new CompositeField("f4", Arrays.asList(f1, f2));
		StatementField f5 = new CompositeField("f5", Arrays.asList(f3, f1));

		MutableSchema schema = new MutableSchema();
		schema.registerFields(f1, f2, f3, f4, f5);

		schema.searchCompositeFieldOrNull(f1);
	}

	@Test
	public void testSearchCompositeFieldAndDoNotFind() {

		StatementField f1 = mockField("f1");
		StatementField f2 = mockField("f2");

		MutableSchema schema = new MutableSchema();
		schema.registerFields(f1, f2);

		CompositeField cf = schema.searchCompositeFieldOrNull(f1);
		Assert.assertNull(cf);
	}

	@Test
	public void testCreateGenericTable() {

		String sql = new GenericSchema().generateNXFlatTable(StatementTableNames.ENTRY_TABLE);

		Assert.assertEquals("DROP TABLE IF EXISTS nxflat.ENTRY_MAPPED_STATEMENTS;\n" +
				"CREATE TABLE nxflat.ENTRY_MAPPED_STATEMENTS (\n" +
				"\tANNOTATION_CATEGORY VARCHAR(10000),\n" +
				"\tANNOTATION_ID VARCHAR(10000),\n" +
				"\tANNOTATION_NAME VARCHAR(10000),\n" +
				"\tANNOTATION_OBJECT_SPECIES VARCHAR(10000),\n" +
				"\tANNOTATION_SUBJECT_SPECIES VARCHAR(10000),\n" +
				"\tANNOT_CV_TERM_ACCESSION VARCHAR(10000),\n" +
				"\tANNOT_CV_TERM_NAME VARCHAR(10000),\n" +
				"\tANNOT_CV_TERM_TERMINOLOGY VARCHAR(10000),\n" +
				"\tANNOT_DESCRIPTION VARCHAR(10000),\n" +
				"\tANNOT_SOURCE_ACCESSION VARCHAR(10000),\n" +
				"\tASSIGMENT_METHOD VARCHAR(10000),\n" +
				"\tASSIGNED_BY VARCHAR(10000),\n" +
				"\tBIOLOGICAL_OBJECT_ACCESSION VARCHAR(10000),\n" +
				"\tBIOLOGICAL_OBJECT_DATABASE VARCHAR(10000),\n" +
				"\tBIOLOGICAL_OBJECT_NAME VARCHAR(10000),\n" +
				"\tBIOLOGICAL_OBJECT_TYPE VARCHAR(10000),\n" +
				"\tDEBUG_INFO VARCHAR(10000),\n" +
				"\tENTRY_ACCESSION VARCHAR(10000),\n" +
				"\tEVIDENCE_CODE VARCHAR(10000),\n" +
				"\tEVIDENCE_INTENSITY VARCHAR(10000),\n" +
				"\tEVIDENCE_NOTE VARCHAR(10000),\n" +
				"\tEVIDENCE_PROPERTIES VARCHAR(10000),\n" +
				"\tEVIDENCE_QUALITY VARCHAR(10000),\n" +
				"\tEVIDENCE_STATEMENT_REF VARCHAR(10000),\n" +
				"\tEXP_CONTEXT_ECO_DETECT_METHOD VARCHAR(10000),\n" +
				"\tEXP_CONTEXT_ECO_ISS VARCHAR(10000),\n" +
				"\tEXP_CONTEXT_ECO_MUTATION VARCHAR(10000),\n" +
				"\tGENE_NAME VARCHAR(10000),\n" +
				"\tISOFORM_CANONICAL VARCHAR(10000),\n" +
				"\tIS_NEGATIVE VARCHAR(10000),\n" +
				"\tLOCATION_BEGIN VARCHAR(10000),\n" +
				"\tLOCATION_BEGIN_MASTER VARCHAR(10000),\n" +
				"\tLOCATION_END VARCHAR(10000),\n" +
				"\tLOCATION_END_MASTER VARCHAR(10000),\n" +
				"\tNEXTPROT_ACCESSION VARCHAR(10000),\n" +
				"\tOBJECT_ANNOTATION_IDS VARCHAR(10000),\n" +
				"\tOBJECT_ANNOT_ENTRY_UNAMES VARCHAR(10000),\n" +
				"\tOBJECT_ANNOT_ISO_UNAMES VARCHAR(10000),\n" +
				"\tOBJECT_STATEMENT_IDS VARCHAR(10000),\n" +
				"\tRAW_STATEMENT_ID VARCHAR(10000),\n" +
				"\tREFERENCE_ACCESSION VARCHAR(10000),\n" +
				"\tREFERENCE_DATABASE VARCHAR(10000),\n" +
				"\tRESOURCE_TYPE VARCHAR(10000),\n" +
				"\tSOURCE VARCHAR(10000),\n" +
				"\tSTATEMENT_ID VARCHAR(10000),\n" +
				"\tSUBJECT_ANNOTATION_IDS VARCHAR(10000),\n" +
				"\tSUBJECT_STATEMENT_IDS VARCHAR(10000),\n" +
				"\tTARGET_ISOFORMS VARCHAR(10000),\n" +
				"\tVARIANT_ORIGINAL_AMINO_ACID VARCHAR(10000),\n" +
				"\tVARIANT_VARIATION_AMINO_ACID VARCHAR(10000));\n" +
				"CREATE INDEX ENTRY_MAPP_ENTRY_AC_IDX ON nxflat.ENTRY_MAPPED_STATEMENTS ( ENTRY_ACCESSION );\n" +
				"CREATE INDEX ENTRY_MAPP_ANNOT_ID_IDX ON nxflat.ENTRY_MAPPED_STATEMENTS ( ANNOTATION_ID );\n\n", sql);
	}

	private StatementField mockField(String name) {

		StatementField field = Mockito.mock(StatementField.class);
		Mockito.when(field.getName()).thenReturn(name);
		return field;
	}
}