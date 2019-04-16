package org.nextprot.commons.statements;


import org.nextprot.commons.statements.specs.CompositeField;
import org.nextprot.commons.statements.specs.CoreStatementField;
import org.nextprot.commons.statements.specs.StatementField;
import org.nextprot.commons.statements.specs.StatementSpecifications;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.nextprot.commons.statements.specs.CoreStatementField.NEXTPROT_ACCESSION;

/**
 * A statement is a set of Field/Values
 * Each field can be a simple or a composite.
 * A composite is composed of multiple fields.
 */
public class Statement extends TreeMap<StatementField, String> implements Map<StatementField, String> {

	private static final long serialVersionUID = 2L;

	// TODO: specs should be final
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
	 * @return the value associated with the given field even if the field is part of another composite field
	 *  or if the field type is a composite it returns a map of field/value in json
	 */
	public String getValue(StatementField field) {

		if (field instanceof CompositeField) {
			CompositeField compositeField = (CompositeField) field;
			return compositeField.valueAsString(extractCompositeValuesFrom(compositeField.getFields()));
		}

		if (!containsKey(field)) {
			// search the field in a composite fields
			CompositeField compositeField = specifications.searchCompositeFieldOrNull(field);
			if (compositeField != null) {
				Map<StatementField, String> map = readMapFromCompositeField(compositeField);
				if (map.containsKey(field)) {
					return map.get(field);
				}
			}
		}

		return get(field);
	}

	private Map<StatementField, String> readMapFromCompositeField(CompositeField compositeField) {

		String jsonContent = get(compositeField);

		try {
			return specifications.jsonReader().readMap(jsonContent);
		} catch (IOException e) {
			throw new IllegalStateException("cannot deserialize json field "+ compositeField.getName()+" with value "+jsonContent);
		}
	}

	private Map<String, String> extractCompositeValuesFrom(List<StatementField> compositeFields) {

		Map<String, String> map = new TreeMap<>();

		for (StatementField field : compositeFields) {
			if (containsKey(field)) {
				map.put(field.getName(), get(field));
			}
		}

		return map;
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

	public Optional<String> getOptionalIsoformAccession() {

		String accession = getValue(NEXTPROT_ACCESSION);

		if (accession != null && accession.contains("-")) { //It is iso specific for example NX_P19544-4 means only specifc to iso 4
			return Optional.of(accession);
		}

		return Optional.empty();
	}

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
}
