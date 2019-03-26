package org.nextprot.commons.statements;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class CompositeFieldTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldBeComposedOfFields() {

		new CompositeField("infos", Collections.emptyList());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldBeComposedOfMultipleFields() {

		new CompositeField("infos", Collections.singletonList(Mockito.mock(StatementField.class)));
	}

	@Test
	public void testCompositeFieldValueAsString() {

		StatementField idField = new CustomStatementField("id", true);
		StatementField nameField = new CustomStatementField("name");
		StatementField ageField = new CustomStatementField("age");

		CompositeField propField = new CompositeField("infos", Arrays.asList(idField, nameField));

		Map<StatementField, String> values = new TreeMap<>(Comparator.comparing(StatementField::getName));
		values.put(idField, "1");
		values.put(nameField, "roudoudou");
		values.put(ageField, "23");

		Assert.assertEquals("{\"age\":\"23\",\"id\":\"1\",\"name\":\"roudoudou\"}", propField.valueAsString(values));
	}
}