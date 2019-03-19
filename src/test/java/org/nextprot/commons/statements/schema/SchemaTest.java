package org.nextprot.commons.statements.schema;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.nextprot.commons.statements.StatementField;

public class SchemaTest {

	@Test
	public void testRegisterField() {

		Schema schema = new Schema();
		schema.registerField(Mockito.mock(StatementField.class));
		Assert.assertEquals(1, schema.size());
	}

	@Test
	public void hasField() {

		Schema schema = new Schema();
		StatementField field = Mockito.mock(StatementField.class);
		Mockito.when(field.getName()).thenReturn("roudoudou");
		schema.registerField(field);
		Assert.assertTrue(schema.hasField("roudoudou"));
	}

	@Test
	public void getStatementFields() {

		Schema schema = new Schema();
		StatementField field = Mockito.mock(StatementField.class);
		Mockito.when(field.getName()).thenReturn("roudoudou");
		schema.registerField(field);
		Assert.assertEquals(1, schema.getFields().size());
		Assert.assertEquals("roudoudou", schema.getFields().iterator().next().getName());
	}

	@Test
	public void getStatementField() {

		Schema schema = new Schema();
		StatementField field = Mockito.mock(StatementField.class);
		Mockito.when(field.getName()).thenReturn("roudoudou");
		schema.registerField(field);
		Assert.assertEquals("roudoudou", schema.getField("roudoudou").getName());
	}
}