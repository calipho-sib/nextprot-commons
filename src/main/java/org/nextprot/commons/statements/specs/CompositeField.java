package org.nextprot.commons.statements.specs;

import org.nextprot.commons.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A field that is the combination of other fields
 *
 * The main use-case is when we need to add more informations via a supplementary column in the nxflat db
 * that correspond to a combo of multiple fields where the value will be in the form of a map indexed by field getName
 */
public class CompositeField implements StatementField {

	private final String name;
	private final List<StatementField> fields;
	private final boolean creatableDBColumn;

	public CompositeField(String name, List<StatementField> fields) {

		this(name, fields, true);
	}

	public CompositeField(String name, List<StatementField> fields, boolean creatableDBColumn) {

		if (fields.size() < 2) {
			throw new IllegalArgumentException("Missing fields for composite field "+ name+", fields="+fields);
		}
		this.name = name;
		this.fields = new ArrayList<>(fields);
		this.creatableDBColumn = creatableDBColumn;
	}

	@Override
	public String getName() {
		return name;
	}

	public List<StatementField> getFields() {

		return Collections.unmodifiableList(fields);
	}

	public boolean hasField(StatementField field) {

		return fields.contains(field);
	}

	@Override
	public boolean isPartOfAnnotationUnicityKey() {

		return false;
	}

	@Override
	public boolean isNXFlatTableColumn() {

		return creatableDBColumn;
	}

	@Override
	public String valueAsString(Object value) {

		return StringUtils.serializeAsJsonStringOrNull(value);
	}

	@Override
	public String toString() {

		return name;
	}
}
