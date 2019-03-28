package org.nextprot.commons.statements.schema;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.nextprot.commons.statements.CompositeField;
import org.nextprot.commons.statements.StatementField;

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
		StatementField f2 = mockField("f2");
		StatementField f3 = new CompositeField("f3", Arrays.asList(f1, f2));

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		schema.specifyFields(f1, f2, f3);

		CompositeField cf = schema.searchCompositeFieldOrNull(f1);
		Assert.assertNotNull(cf);
		Assert.assertEquals("f3", cf.getName());
	}

	@Test(expected = IllegalStateException.class)
	public void testSearchCompositeField2() {

		StatementField f1 = mockField("f1");
		StatementField f2 = mockField("f2");
		StatementField f3 = mockField("f3");
		StatementField f4 = new CompositeField("f4", Arrays.asList(f1, f2));
		StatementField f5 = new CompositeField("f5", Arrays.asList(f3, f1));

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		schema.specifyFields(f1, f2, f3, f4, f5);

		schema.searchCompositeFieldOrNull(f1);
	}

	@Test
	public void testSearchCompositeFieldAndDoNotFind() {

		StatementField f1 = mockField("f1");
		StatementField f2 = mockField("f2");

		MutableStatementSpecifications schema = new MutableStatementSpecifications();
		schema.specifyFields(f1, f2);

		CompositeField cf = schema.searchCompositeFieldOrNull(f1);
		Assert.assertNull(cf);
	}

	private StatementField mockField(String name) {

		StatementField field = Mockito.mock(StatementField.class);
		Mockito.when(field.getName()).thenReturn(name);
		return field;
	}
}