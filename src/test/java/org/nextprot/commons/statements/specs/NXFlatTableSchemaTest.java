package org.nextprot.commons.statements.specs;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.nextprot.commons.statements.constants.StatementTableNames;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.nextprot.commons.statements.specs.Specifications.EXTRA_FIELDS;

public class NXFlatTableSchemaTest {

	@Test
	public void testCreateGenericTableWithExtras() {

		String sql = new NXFlatTableSchema().generateCreateTableInSQL(StatementTableNames.ENTRY_TABLE);

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
				"\t"+EXTRA_FIELDS+" VARCHAR(10000),\n" +
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



	private List<String> getNxFlatColumns() {
		return Arrays.asList("statement_id",
				"nextprot_accession",
				"entry_accession",
				"gene_name",
				"location_begin_master",
				"location_end_master",
				"location_begin",
				"location_end",
				"subject_statement_ids",
				"subject_annotation_ids",
				"annotation_subject_species",
				"annotation_object_species",
				"annotation_category",
				"annot_description",
				"isoform_canonical",
				"target_isoforms",
				"annotation_id",
				"annotation_name",
				"is_negative",
				"evidence_quality",
				"evidence_intensity",
				"evidence_note",
				"evidence_statement_ref",
				"evidence_code",
				"evidence_properties",
				"annot_cv_term_terminology",
				"annot_cv_term_accession",
				"annot_cv_term_name",
				"variant_original_amino_acid",
				"variant_variation_amino_acid",
				"biological_object_type",
				"biological_object_accession",
				"biological_object_database",
				"biological_object_name",
				"object_statement_ids",
				"object_annotation_ids",
				"object_annot_iso_unames",
				"object_annot_entry_unames",
				"source",
				"annot_source_accession",
				"exp_context_eco_detect_method",
				"exp_context_eco_mutation",
				"exp_context_eco_iss",
				"reference_database",
				"reference_accession",
				"assigned_by",
				"assigment_method",
				"resource_type",
				"raw_statement_id",
				"extra_fields");
	}
}