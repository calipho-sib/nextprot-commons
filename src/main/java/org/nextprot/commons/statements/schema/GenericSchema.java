package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.GenericStatementField;
import org.nextprot.commons.statements.StatementField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericSchema implements Schema {

	private final MutableSchema schema;

	public GenericSchema(/*List<String> extraFields*/) {

		this.schema = new MutableSchema();

		for (StatementField field : GenericStatementField.values()) {

			schema.registerField(field);
		}

		//schema.registerField(new CompositeField("EXTRAS", new ArrayList<>()));
	}

	@Override
	public boolean hasField(String field) {

		return schema.hasField(field);
	}

	@Override
	public Collection<StatementField> getFields() {

		return schema.getFields();
	}

	@Override
	public CompositeField searchCompositeFieldOrNull(StatementField field) {

		return schema.searchCompositeFieldOrNull(field);
	}

	@Override
	public StatementField getField(String field) {

		return schema.getField(field);
	}

	@Override
	public int size() {

		return schema.size();
	}
}
