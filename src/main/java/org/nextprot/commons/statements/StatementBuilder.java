package org.nextprot.commons.statements;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.nextprot.commons.constants.QualityQualifier;
import org.nextprot.commons.statements.constants.AnnotationType;
import org.nextprot.commons.utils.StringUtils;

import static org.nextprot.commons.statements.StatementField.*;

public class StatementBuilder {

	private Map<String, String> keyValues = new TreeMap<>();

	public static StatementBuilder createNew() {
		StatementBuilder sb = new StatementBuilder();
		return sb;
	}

	public StatementBuilder removeField(StatementField statementField) {
		this.keyValues.remove(statementField.name());
		return this;
	}
	
	public StatementBuilder addField(StatementField statementField, String statementValue) {
		this.keyValues.put(statementField.name(), statementValue);
		return this;
	}

	public StatementBuilder addTargetIsoformsField(TargetIsoformSet targetIsoforms) {
		this.keyValues.put(StatementField.TARGET_ISOFORMS.name(), targetIsoforms.serializeToJsonString());
		return this;
	}

	public StatementBuilder addSubjects(Collection<Statement> statements) {

		Set<String> sortedStatementIds = new TreeSet<String>();
		Set<String> sortedAnnotationIds = new TreeSet<String>();

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
			addField(StatementField.SUBJECT_ANNOTATION_IDS, subjectAnnotationIds);
		}

		return this;
	}

	public StatementBuilder addObject(Statement statement) {
		addField(OBJECT_STATEMENT_IDS, statement.getStatementId());
		if(statement.getAnnotationId() != null){
			addField(OBJECT_ANNOTATION_IDS, statement.getAnnotationId());
			
		}
		return this;
	}

	public Statement build() {
		Statement rs = new Statement(keyValues);
		rs.putValue(StatementField.STATEMENT_ID, StatementUtil.computeAndGetAnnotationId(rs, AnnotationType.STATEMENT));
		return rs;
	}

    public Statement buildWithAnnotationHash() {
        Statement rs = build();
        rs.putValue(StatementField.ANNOTATION_ID, StatementUtil.computeAndGetAnnotationId(rs, AnnotationType.ENTRY));
        return rs;
    }

	public StatementBuilder addMap(Map<String, String> map) {
		keyValues.putAll(map);
		return this;
	}

	public StatementBuilder addCompulsaryFields(String entryAccession, String isoformAccession, String annotationCategory, QualityQualifier quality) {
		addField(ENTRY_ACCESSION, entryAccession);
		addField(ANNOTATION_CATEGORY, annotationCategory);
		addQuality(quality);
		return this;
	}

	public StatementBuilder addCvTerm(String cvTermAccession, String cvTermName, String cvTerminology) {
		addField(ANNOT_CV_TERM_ACCESSION, cvTermAccession);
		addField(ANNOT_CV_TERM_NAME, cvTermName);
		addField(ANNOT_CV_TERM_TERMINOLOGY, cvTerminology);
		return this;
	}

	public StatementBuilder addQuality(QualityQualifier quality) {
		addField(StatementField.EVIDENCE_QUALITY, quality.name());
		return this;
	}

	
	public StatementBuilder addSourceInfo(String sourceAccession, String sourceDatabase) {
		addField(ANNOT_SOURCE_ACCESSION, sourceAccession);
		addField(SOURCE, sourceDatabase);
		return this;
	}


	public StatementBuilder addDebugInfo(String info) {
		addField(DEBUG_INFO, info);
		/*		if(!this.keyValues.containsKey(DEBUG_INFO.name())){
			addField(DEBUG_INFO, info);
		}else {
			String msg = this.keyValues.get(DEBUG_INFO.name());
			addField(DEBUG_INFO, msg + "; " + info);
		}*/
		return this;
	}

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

	public StatementBuilder addLocationFields(String locationBegin, String locationEnd) {
		addField(LOCATION_BEGIN, locationBegin);
		addField(LOCATION_END, locationEnd);
		return this;
	}

}
