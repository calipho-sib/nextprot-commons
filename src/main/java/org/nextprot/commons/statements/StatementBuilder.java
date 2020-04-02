package org.nextprot.commons.statements;

import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOTATION_CATEGORY;
import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOTATION_ID;
import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOT_CV_TERM_ACCESSION;
import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOT_CV_TERM_NAME;
import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOT_CV_TERM_TERMINOLOGY;
import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOT_SOURCE_ACCESSION;
import static org.nextprot.commons.statements.specs.CoreStatementField.DEBUG_INFO;
import static org.nextprot.commons.statements.specs.CoreStatementField.ENTRY_ACCESSION;
import static org.nextprot.commons.statements.specs.CoreStatementField.LOCATION_BEGIN;
import static org.nextprot.commons.statements.specs.CoreStatementField.LOCATION_END;
import static org.nextprot.commons.statements.specs.CoreStatementField.OBJECT_ANNOTATION_IDS;
import static org.nextprot.commons.statements.specs.CoreStatementField.OBJECT_STATEMENT_IDS;
import static org.nextprot.commons.statements.specs.CoreStatementField.SOURCE;
import static org.nextprot.commons.statements.specs.CoreStatementField.STATEMENT_ID;
import static org.nextprot.commons.statements.specs.CoreStatementField.SUBJECT_STATEMENT_IDS;
import static org.nextprot.commons.statements.specs.CoreStatementField.TARGET_ISOFORMS;
import static org.nextprot.commons.statements.specs.CoreStatementField.VARIANT_ORIGINAL_AMINO_ACID;
import static org.nextprot.commons.statements.specs.CoreStatementField.VARIANT_VARIATION_AMINO_ACID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.nextprot.commons.algo.MD5Algo;
import org.nextprot.commons.constants.QualityQualifier;
import org.nextprot.commons.statements.constants.UniqueKey;
import org.nextprot.commons.statements.specs.CoreStatementField;
import org.nextprot.commons.statements.specs.MutableStatementSpecifications;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;
import org.nextprot.commons.utils.StringUtils;

/**
 * A StatementID is computed based on the fields when build() is invoked
 */
public class StatementBuilder {

	private StatementSpecifications specifications;
	private final Map<StatementField, String> keyValues;
	private boolean withAnnotationHash;

	static {
		System.out.println("StatementBuilder version 1.1");
	}
	
	public StatementBuilder() {

		keyValues = new TreeMap<>(Comparator.comparing(StatementField::getName));
	}

	public StatementBuilder(Statement statement) {
		this();
		keyValues.putAll(statement);
		//System.out.println("StatementBuilder() statement specifications:" + statement.getSpecifications());

	}

	public StatementBuilder withAnnotationHash() {
		withAnnotationHash = true;
		return this;
	}

	public StatementBuilder removeField(StatementField statementField) {
		this.keyValues.remove(statementField);
		return this;
	}

	public StatementBuilder withSpecifications(StatementSpecifications specifications) {
		this.specifications = specifications;
		return this;
	}

	public StatementBuilder addField(StatementField statementField, String statementValue) {
		this.keyValues.put(statementField, statementValue);
		return this;
	}

	public StatementBuilder addTargetIsoformsField(TargetIsoformSet targetIsoforms) {
		addField(TARGET_ISOFORMS, targetIsoforms.serializeToJsonString());
		return this;
	}

	public StatementBuilder addSubjects(Collection<Statement> statements) {

		Set<String> sortedStatementIds = new TreeSet<>();
		Set<String> sortedAnnotationIds = new TreeSet<>();

		Iterator<Statement> statementsIt = statements.iterator();

		while (statementsIt.hasNext()) {
			Statement s = statementsIt.next();
			sortedStatementIds.add(s.getStatementId());
			if(s.getAnnotationId() != null){
				sortedAnnotationIds.add(s.getAnnotationId());
			}else {
				if(!sortedAnnotationIds.isEmpty()){
					throw new RuntimeException("Found a null annotation id when one was already set for statement id: " + s.getStatementId());
				}
			}
		}
		
		String subjectStatemendIds = StringUtils.mkString(sortedStatementIds, ",");
		String subjectAnnotationIds = StringUtils.mkString(sortedAnnotationIds, ",");

		addField(SUBJECT_STATEMENT_IDS, subjectStatemendIds);
		
		if(!subjectAnnotationIds.isEmpty()){
			addField(CoreStatementField.SUBJECT_ANNOTATION_IDS, subjectAnnotationIds);
		}

		return this;
	}

	// Note: Used in bed
	public StatementBuilder addObject(Statement statement) {
		addField(OBJECT_STATEMENT_IDS, statement.getStatementId());
		if(statement.getAnnotationId() != null){
			addField(OBJECT_ANNOTATION_IDS, statement.getAnnotationId());
			
		}
		return this;
	}

	// only used in tests
	// Fred: I guess we should impose their definition by adding them in the constructor of StatementBuilder
	public StatementBuilder addCompulsoryFields(String entryAccession, String isoformAccession, String annotationCategory, QualityQualifier quality) {
		addField(ENTRY_ACCESSION, entryAccession);
		addField(ANNOTATION_CATEGORY, annotationCategory);
		addQuality(quality);
		return this;
	}

	// Note: Used in bed
	public StatementBuilder addCvTerm(String cvTermAccession, String cvTermName, String cvTerminology) {
		addField(ANNOT_CV_TERM_ACCESSION, cvTermAccession);
		addField(ANNOT_CV_TERM_NAME, cvTermName);
		addField(ANNOT_CV_TERM_TERMINOLOGY, cvTerminology);
		return this;
	}

	public StatementBuilder addQuality(QualityQualifier quality) {
		addField(CoreStatementField.EVIDENCE_QUALITY, quality.name());
		return this;
	}

	
	public StatementBuilder addSourceInfo(String sourceAccession, String sourceDatabase) {
		addField(ANNOT_SOURCE_ACCESSION, sourceAccession);
		addField(SOURCE, sourceDatabase);
		return this;
	}

	public StatementBuilder addDebugInfo(String info) {
		addField(DEBUG_INFO, info);
		return this;
	}

	// Note: Used in bed
	public StatementBuilder addVariantInfo(String annotationCategory, String firstPosition, String lastPosition, String variationOrigin, String variationVariation) {

		if(annotationCategory == null || !(annotationCategory.equals("variant") || annotationCategory.equals("mutagenesis"))) {
			throw new RuntimeException("annotation category " + annotationCategory + " is not allowed for variant");
		}
		addField(ANNOTATION_CATEGORY, annotationCategory);
		
		addField(LOCATION_BEGIN, firstPosition);
		addField(LOCATION_END, lastPosition);

		addField(VARIANT_ORIGINAL_AMINO_ACID, variationOrigin);
		addField(VARIANT_VARIATION_AMINO_ACID, variationVariation);

		return this;
	}

	public Statement build() {

		Statement statement = new Statement(keyValues);
		
		// tag StatementExtractionTest
		// uncomment line below for StatementExtractionTest
		// String initialSmtId = statement.getStatementId();
		
		if (keyValues.isEmpty()) throw new IllegalStateException("cannot build empty statement");

		statement.setSpecifications((specifications == null) ? buildSpecifications(statement) : specifications);
		statement.putValue(CoreStatementField.STATEMENT_ID, MD5Algo.computeMD5(extractUniqueFieldValues(statement, UniqueKey.STATEMENT)));
		if (withAnnotationHash) {
			statement.putValue(CoreStatementField.ANNOTATION_ID,
					MD5Algo.computeMD5(extractUniqueFieldValues(statement, UniqueKey.ENTRY)));
		}

		// tag StatementExtractionTest
		// uncomment line below for StatementExtractionTest
		//if (!statement.getStatementId().equals(initialSmtId)) System.out.println("ERROR stmt id " + initialSmtId + " has changed" );
		
		return statement;
	}


	/** Build the schema based on the statement content */
	private StatementSpecifications buildSpecifications(Map<StatementField, String> keyValues) {

		//System.out.println("Building specifications from statement key values");
		
		MutableStatementSpecifications specs = new MutableStatementSpecifications();

		for (StatementField field : keyValues.keySet()) {
			if (!specs.hasField(field.getName())) {
				specs.specifyField(field);
			}
		}
		if (!specs.hasField(STATEMENT_ID.getName())) {
			specs.specifyField(STATEMENT_ID);
		}
		if (!specs.hasField(ANNOTATION_ID.getName()) && withAnnotationHash) {
			specs.specifyField(ANNOTATION_ID);
		}
		return specs;
	}

	/**
	 * This method compute a MD5 unique key based on the combination of selected statement fields
	 *
	 * @param statement the statement to compute unique key on
	 * @param uniqueKey the type of unique key to create
	 * @return a unique key as string
	 * @implSpec at https://calipho.isb-sib.ch/wiki/display/cal/Raw+statements+specifications
	 */
	static String extractUniqueFieldValues(Statement statement, UniqueKey uniqueKey) {

		// Filter fields which are used to compute unicity key
		List<StatementField> unicityFields = new ArrayList<>();
				
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		// start with core statement fields because they are returned in a controlled order
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		for (StatementField field : CoreStatementField.values()) {
			if (field.equals(DEBUG_INFO)) continue;

			// ENTRY TYPE: only fields that are part of uniqueness key are considered
			if (uniqueKey.equals(UniqueKey.ENTRY)) {
				if (field.isPartOfAnnotationUnicityKey()) unicityFields.add(field);
			}
			// STATEMENT TYPE: (almost) all fields are considered to build the unique key
			else if (uniqueKey.equals(UniqueKey.STATEMENT)) { 
				if (! field.equals(STATEMENT_ID)) unicityFields.add(field);
			}
		}
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		// add now custom statement fields in sorted order
		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		statement.getSpecifications().getCustomFields()
				.stream().sorted(StatementFieldComparator.getInstance())
				.filter(f -> uniqueKey.equals(UniqueKey.STATEMENT) || f.isPartOfAnnotationUnicityKey())
				.forEach(f -> unicityFields.add(f));

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		if (unicityFields.isEmpty()) {
			throw new IllegalStateException("missing fields used to compute a unique key for statement "+statement + " (type="+ uniqueKey +")");
		}

		//System.out.print("field  names in unique key " + uniqueKey + ": ");
		//unicityFields.stream().filter(f -> statement.get(f) != null).forEach(f -> System.out.print(f.getName() + "=" + statement.get(f) + " | " ));
		//System.out.println(".");
		
		String uk = unicityFields.stream().map(statement::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(""));

		//System.out.println("field values in unique key " + uniqueKey + ": " + uk);
		
		return uk;
	}

}
