package org.nextprot.commons.statements;

import java.util.Map;
import java.util.TreeMap;

/**
 * DO NOT ADD public setters on this class.
 */
public class Statement extends TreeMap<String, String>{

	private static final long serialVersionUID = -4723168061980820149L;
	private boolean isProcessed = false;
	
	public Statement() {
		super();
	}

	// Keep the constructor package protected, so it enforces the use of the Builder
	Statement(Map<String, String> map) {
		super(new TreeMap<>(map));
	}

	public String getValue(StatementField field) {
		return get(field.name());
	}

	public String getDebugInfo() {
		return get(PredefinedStatementField.DEBUG_INFO.name());
	}

	String putValue(StatementField field, String value) {
		return put(field.name(), value);
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
		for (String s : this.keySet()) {
			sb.append("\t\"" + s + "\": \"" + this.get(s).replace("\"", "''") + "\",\n");
		}
		sb.append("}");
		return sb.toString();
	}

}
