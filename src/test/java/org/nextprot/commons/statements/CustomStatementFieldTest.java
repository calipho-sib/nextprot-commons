package org.nextprot.commons.statements;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomStatementFieldTest {

	@Test
	public void isCreatableDBColumn() {

		CustomStatementField field = new CustomStatementField("foo");
		Assert.assertFalse(field.isCreatableDBColumn());
	}
}
