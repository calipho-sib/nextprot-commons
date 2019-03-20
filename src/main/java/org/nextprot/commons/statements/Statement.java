package org.nextprot.commons.statements;

import org.nextprot.commons.utils.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 *
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
public class Statement extends TreeMap<StatementField, String> {

	private static final long serialVersionUID = 2L;

	public Statement() {
		super(Comparator.comparing(StatementField::getName));
	}

	// Keep the constructor package protected, so it enforces the use of the Builder
	Statement(Map<StatementField, String> map) {
		this();
		putAll(map);
	}

	public String getValueOrNull(String field) {

		if (!NextProtSource.isStatementFieldExist(field)) {
			return null;
		}

		return getValue(NextProtSource.getStatementField(field));
	}

	public String getValue(StatementField field) {

		// TODO: not oop
		if (field instanceof CompositeField) {

			Map<String, String> map = new HashMap<>();

			for (StatementField cf : ((CompositeField) field).getFields()) {
				map.put(cf.getName(), get(cf));
			}

			return StringUtils.serializeAsJsonStringOrNull(map);
		}

		return get(field);
	}

	public String getDebugInfo() {
		return get(GenericStatementField.DEBUG_INFO);
	}

	String putValue(StatementField field, String value) {
		return put(field, value);
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
			sb.append("\t\"" + sf.getName() + "\": \"" + this.get(sf.getName()).replace("\"", "''") + "\",\n");
		}
		sb.append("}");
		return sb.toString();
	}
}
