package org.nextprot.commons.statements;


import org.nextprot.commons.statements.schema.Schema;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Mechanism of mapping to nxflat db
 *
 * Statement (Set of Field -> Value)
 * F1   -> V1
 * F2   -> V2
 * ...
 * Fi   -> Vi
 * ...
 * Fn   -> Vn
 * Fn+1 -> {Fj:Vj, Fh:Vh,...} // a composite field that can be a column type (string or json) in nxflat
 *
 * DO NOT ADD public setters on this class.
 */
public class Statement extends TreeMap<StatementField, String> implements Map<StatementField, String> {

	private static final long serialVersionUID = 2L;

	private Schema schema;

	public Statement() {
		super(Comparator.comparing(StatementField::getName));
	}

	// Keep the constructor package protected, so it enforces the use of the Builder
	Statement(Map<StatementField, String> map) {
		this();
		putAll(map);
	}

	public boolean containsField(String field) {

		return getValueOrNull(field) != null;
	}

	public String getValueOrNull(String field) {

		if (!schema.hasField(field)) {
			return null;
		}

		return getValue(schema.getField(field));
	}

	/**
	 * @return the value associated with the given field or if the field type is a composite
	 * returns a map of field/value in json
	 */
	public String getValue(StatementField field) {

		if (field instanceof CompositeField) {
			CompositeField compositeField = (CompositeField) field;
			return compositeField.valueAsString(extractCompositeValuesFrom(compositeField.getFields()));
		}

		if (!containsKey(field)) {
			// search the field in a composite fields
			CompositeField compositeField = schema.searchCompositeFieldOrNull(field);
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
			return schema.jsonReader().readMap(jsonContent);
		} catch (IOException e) {
			throw new IllegalStateException("cannot deserialize json for field "+ compositeField.getName()+": "+jsonContent);
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

	public String getDebugInfo() {
		return get(GenericStatementField.DEBUG_INFO);
	}

	String putValue(StatementField field, String value) {
		return put(field, value);
	}

	void setSchema(Schema schema) {
		this.schema = schema;
	}

	public Schema getSchema() {
		return schema;
	}

	public String getSubjectStatementIds() {
		return get(GenericStatementField.SUBJECT_STATEMENT_IDS);
	}
	
	public String[] getSubjectStatementIdsArray() {
		String subjects = get(GenericStatementField.SUBJECT_STATEMENT_IDS);
		if(subjects == null) return null;
		else return subjects.split(",");
	}
	
	public String getStatementId() {
		return this.get(GenericStatementField.STATEMENT_ID);
	}
	
	public String getAnnotationId() {
		return this.get(GenericStatementField.ANNOTATION_ID);
	}
	
	public String getObjectStatementId() {
		return get(GenericStatementField.OBJECT_STATEMENT_IDS);
	}
	
	public boolean hasModifiedSubject() {
		return (get(GenericStatementField.SUBJECT_STATEMENT_IDS) != null);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (StatementField sf : this.keySet()) {
			sb.append("\t\"" + sf.getName() + "\": \"" + this.get(sf).replace("\"", "''") + "\",\n");
		}
		sb.append("}");
		return sb.toString();
	}
}
