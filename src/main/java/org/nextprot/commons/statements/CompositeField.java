package org.nextprot.commons.statements;

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

	public CompositeField(String name, List<StatementField> fields) {

		this.name = name;
		this.fields = new ArrayList<>(fields);
	}

	@Override
	public String getName() {
		return name;
	}


	public List<StatementField> getFields() {

		return Collections.unmodifiableList(fields);
	}

	@Override
	public boolean isPartOfAnnotationUnicityKey() {

		return fields.stream().anyMatch(StatementField::isPartOfAnnotationUnicityKey);
	}
}
