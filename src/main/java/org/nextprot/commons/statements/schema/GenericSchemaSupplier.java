package org.nextprot.commons.statements.schema;

import org.nextprot.commons.statements.GenericStatementField;
import org.nextprot.commons.statements.StatementField;

import java.util.function.Supplier;

public class GenericSchemaSupplier implements Supplier<Schema> {

	@Override
	public Schema get() {

		SchemaImpl schema = new SchemaImpl();

		for (StatementField field : GenericStatementField.values()) {

			schema.registerField(field);
		}

		return schema;
	}
}
