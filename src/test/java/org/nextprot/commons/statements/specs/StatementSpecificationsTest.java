package org.nextprot.commons.statements.specs;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

public class StatementSpecificationsTest {

	@Test
	public void testRegisterField() {

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		StatementField field = mockField("roudoudou");
		schema.specifyField(field);
		Assert.assertTrue(schema.hasField("roudoudou"));
		Assert.assertEquals(1, schema.size());
	}

	@Test
	public void testGetStatementFields() {

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		StatementField field = mockField("roudoudou");
		schema.specifyField(field);
		Collection<StatementField> fields = schema.getFields();
		Assert.assertEquals(1, fields.size());
		Assert.assertEquals("roudoudou", fields.iterator().next().getName());
	}

	@Test
	public void testGetStatementField() {

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		StatementField field = mockField("roudoudou");
		schema.specifyField(field);
		Assert.assertTrue(schema.hasField("roudoudou"));
		Assert.assertEquals("roudoudou", schema.getField("roudoudou").getName());
	}

	@Test
	public void testSearchCompositeField() {

		StatementField f1 = mockField("f1");
		StatementField f2 = new CustomStatementField("f2");
		StatementField f3 = new CustomStatementField("f3");

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		schema.specifyFields(f1, f2, f3);

		Assert.assertEquals("f3", schema.getField("f3").getName());
	}

	private StatementField mockField(String name) {

		StatementField field = Mockito.mock(StatementField.class);
		Mockito.when(field.getName()).thenReturn(name);
		return field;
	}
	
}