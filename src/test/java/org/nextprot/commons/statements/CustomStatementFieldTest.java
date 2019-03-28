package org.nextprot.commons.statements;

import org.junit.Assert;
import org.junit.Test;
import org.nextprot.commons.statements.specs.CustomStatementField;

public class CustomStatementFieldTest {

	@Test
	public void isCreatableDBColumn() {

		CustomStatementField field = new CustomStatementField("foo");
		Assert.assertFalse(field.isNXFlatTableColumn());
	}
}
