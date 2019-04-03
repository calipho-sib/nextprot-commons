package org.nextprot.commons.statements.specs;

import org.nextprot.commons.utils.EnumConstantDictionary;
import org.nextprot.commons.utils.EnumDictionarySupplier;

import java.util.Map;

public enum CoreStatementField implements StatementField, EnumDictionarySupplier<CoreStatementField> {

	//Generated automatically from the builder when all fields are set	
	STATEMENT_ID, 
	
	// According with https://calipho.isb-sib.ch/wiki/display/cal/Raw+statements+specifications
	NEXTPROT_ACCESSION, //Used for raw statements
	
	ENTRY_ACCESSION(true), //Used for mapped statement
	GENE_NAME,//TODO should be a list of gene names

	LOCATION_BEGIN_MASTER (true),
	LOCATION_END_MASTER (true),
	
	LOCATION_BEGIN (false),
	LOCATION_END (false),

	
	// SUBJECTS ///////////////////////////////////////////////////////
	SUBJECT_STATEMENT_IDS(true),
	SUBJECT_ANNOTATION_IDS(true),

	ANNOTATION_SUBJECT_SPECIES,
	ANNOTATION_OBJECT_SPECIES,
	////////////////////////////////////////////////////////////////////

	ANNOTATION_CATEGORY(true),
	ANNOT_DESCRIPTION(true),

	ISOFORM_CANONICAL,

	TARGET_ISOFORMS,

	ANNOTATION_ID, 
	ANNOTATION_NAME, 

	IS_NEGATIVE, 

	EVIDENCE_QUALITY, 
	EVIDENCE_INTENSITY,
	EVIDENCE_NOTE,
	EVIDENCE_STATEMENT_REF,
	EVIDENCE_CODE,
	EVIDENCE_PROPERTIES,

	ANNOT_CV_TERM_TERMINOLOGY(true),
	ANNOT_CV_TERM_ACCESSION(true),
	ANNOT_CV_TERM_NAME,
	

	VARIANT_ORIGINAL_AMINO_ACID(true),
	VARIANT_VARIATION_AMINO_ACID(true),

	BIOLOGICAL_OBJECT_TYPE(true),
	BIOLOGICAL_OBJECT_ACCESSION(true),
	BIOLOGICAL_OBJECT_DATABASE(true),
	BIOLOGICAL_OBJECT_NAME(true),
	
	// OBJECT ANNOTATION ///////////////////////////////////////////////////////////
	OBJECT_STATEMENT_IDS(true),
	OBJECT_ANNOTATION_IDS(true),

	OBJECT_ANNOT_ISO_UNAMES,
	OBJECT_ANNOT_ENTRY_UNAMES,
	///////////////////////////////////////////////////////////////////////////////

	SOURCE,
	ANNOT_SOURCE_ACCESSION,
	
	//Experimental context fields
	EXP_CONTEXT_ECO_DETECT_METHOD,
	EXP_CONTEXT_ECO_MUTATION,
	EXP_CONTEXT_ECO_ISS,
	
	//Publications or Xrefs (it corresponds to the resource id of an evidence)
	REFERENCE_DATABASE, //Can be a Pubmed or another database
	REFERENCE_ACCESSION,
	
	//EVIDENCE
	ASSIGNED_BY,
	//TODO: TYPO TO FIX: ADD A name() method for backward compatibility
	ASSIGMENT_METHOD,
	RESOURCE_TYPE,
	
	RAW_STATEMENT_ID //Keep a reference to the Raw statement
	;

	private static EnumConstantDictionary<CoreStatementField> dictionaryOfConstants =
			new EnumConstantDictionary<CoreStatementField>(CoreStatementField.class, values()) {
				@Override
				protected void updateDictionaryOfConstants(Map<String, CoreStatementField> dictionary) {

					for (CoreStatementField db : values()) {
						dictionary.put(db.getName(), db);
					}
				}
			};

	private final boolean isUnicityField;

	CoreStatementField(boolean isUnicityField) {

		this.isUnicityField = isUnicityField;
	}
	
	CoreStatementField() {

		this(false);
	}

	@Override
	public String getName() {

		return name();
	}

	@Override
	public boolean isPartOfAnnotationUnicityKey() {

		return isUnicityField;
	}

	public static boolean hasKey(String name) {

		return dictionaryOfConstants.haskey(name);
	}

	@Override
	public EnumConstantDictionary<CoreStatementField> getEnumConstantDictionary() {

		return dictionaryOfConstants;
	}
}
