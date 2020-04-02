package org.nextprot.commons.statements;

import static org.nextprot.commons.statements.specs.CoreStatementField.ANNOTATION_CATEGORY;
import static org.nextprot.commons.statements.specs.CoreStatementField.ENTRY_ACCESSION;
import static org.nextprot.commons.statements.specs.CoreStatementField.NEXTPROT_ACCESSION;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.nextprot.commons.statements.specs.CoreStatementField;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;

/**
 * A statement is a set of Field/Values
 * Each field can be a core or a custom.
 */
public class Statement extends TreeMap<StatementField, String> implements Map<StatementField, String> {

	private static final long serialVersionUID = 2L;

	private StatementSpecifications specifications;

	public Statement() {
		super(Comparator.comparing(StatementField::getName));
	}

	// Keep the constructor package protected, so it enforces the use of the Builder
	Statement(Map<StatementField, String> map) {
		this();
		putAll(map);
	}

	public boolean hasField(String field) {
		return getOptionalValue(field).isPresent();
	}

	public Optional<String> getOptionalValue(String field) {
		if (!specifications.hasField(field)) {
			return Optional.empty();
		}
		return Optional.ofNullable(getValue(specifications.getField(field)));
	}

	/**
	 * Get the value from a specific field
	 * @return the value associated with the given field 
	 */
	public String getValue(StatementField field) {
		return get(field);
	}

	String putValue(StatementField field, String value) {
		return put(field, value);
	}

	void setSpecifications(StatementSpecifications specifications) {
		this.specifications = specifications;
	}

	public StatementSpecifications getSpecifications() {
		return specifications;
	}

	public String getSubjectStatementIds() {
		return get(CoreStatementField.SUBJECT_STATEMENT_IDS);
	}

	public String[] getSubjectStatementIdsArray() {
		String subjects = get(CoreStatementField.SUBJECT_STATEMENT_IDS);
		if(subjects == null) return null;
		else return subjects.split(",");
	}

	public String getStatementId() {
		return this.get(CoreStatementField.STATEMENT_ID);
	}

	public String getAnnotationId() {
		return this.get(CoreStatementField.ANNOTATION_ID);
	}

	public String getObjectStatementId() {
		return get(CoreStatementField.OBJECT_STATEMENT_IDS);
	}

	public boolean hasModifiedSubject() {
		return (get(CoreStatementField.SUBJECT_STATEMENT_IDS) != null);
	}

	public String getEntryAccession() {
		return getValue(ENTRY_ACCESSION);
	}

	public String getAnnotationCategory() {
		return getValue(ANNOTATION_CATEGORY);
	}

	public Optional<String> getOptionalIsoformAccession() {
		String accession = getValue(NEXTPROT_ACCESSION);
		if (accession != null && accession.contains("-")) { //It is iso specific for example NX_P19544-4 means only specifc to iso 4
			return Optional.of(accession);
		}
		return Optional.empty();
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Specs: [").append(
				specifications.getFields().stream()
						.map(field -> field.getName()+((field.isPartOfAnnotationUnicityKey()) ? "*":""))
						.collect(Collectors.joining(", ")))
				.append("]\n");
		sb.append("Values: {\n");
		for (StatementField sf : this.keySet()) {
			sb.append("\t\"").append(sf.getName()).append("\": \"").append(get(sf).replace("\"", "''")).append("\"\n");
		}
		sb.append("}\n*: fields contributing to the calculation of the ANNOTATION_ID key");
		return sb.toString();
	}

	public String toJsonString() {
		return "{" + keySet().stream()
				.map(sf -> "\"" + sf.getName() + "\": \"" + get(sf).replace("\"", "''") + "\"")
				.collect(Collectors.joining(",")) + "}";
	}

	public static String toJsonString(List<Statement> statements) {
		return "[" + statements.stream()
				.map(Statement::toJsonString)
				.collect(Collectors.joining(",")) + "]";
	}

}
