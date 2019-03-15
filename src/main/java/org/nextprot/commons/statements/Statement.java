package org.nextprot.commons.statements;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * DO NOT ADD public setters on this class.
 */
public class Statement extends TreeMap<StatementField, String>{

	private static final long serialVersionUID = 2L;
	private boolean isProcessed = false;
	
	public Statement() {
		super(Comparator.comparing(StatementField::name));
	}

	// Keep the constructor package protected, so it enforces the use of the Builder
	Statement(Map<StatementField, String> map) {
		this();
		putAll(map);
	}

	public String getValue(StatementField field) {
		return get(field);
	}

	public String getDebugInfo() {
		return get(PredefinedStatementField.DEBUG_INFO);
	}

	String putValue(StatementField field, String value) {
		return put(field, value);
	}

	public String getSubjectStatementIds() {
		return getValue(PredefinedStatementField.SUBJECT_STATEMENT_IDS);
	}
	
	public String[] getSubjectStatementIdsArray() {
		String subjects = getValue(PredefinedStatementField.SUBJECT_STATEMENT_IDS);
		if(subjects == null) return null;
		else return subjects.split(",");
	}
	
	public String getStatementId() {
		return this.getValue(PredefinedStatementField.STATEMENT_ID);
	}
	
	public String getAnnotationId() {
		return this.getValue(PredefinedStatementField.ANNOTATION_ID);
	}
	
	public String getObjectStatementId() {
		return getValue(PredefinedStatementField.OBJECT_STATEMENT_IDS);
	}
	
	public boolean hasModifiedSubject() {
		return (getValue(PredefinedStatementField.SUBJECT_STATEMENT_IDS) != null);
	}

	public boolean isProcessed() {
		return isProcessed;
	}

	public void processed() {
		this.isProcessed = true;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (StatementField sf : this.keySet()) {
			sb.append("\t\"" + sf.name() + "\": \"" + this.get(sf.name()).replace("\"", "''") + "\",\n");
		}
		sb.append("}");
		return sb.toString();
	}

}
