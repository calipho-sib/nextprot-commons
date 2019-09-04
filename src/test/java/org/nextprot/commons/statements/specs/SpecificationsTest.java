package org.nextprot.commons.statements.specs;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.nextprot.commons.statements.specs.Specifications.EXTRA_FIELDS;

public class SpecificationsTest {

	@Test
	public void testConstr() {

		Specifications specifications = new Specifications.Builder().build();
		Assert.assertEquals(50, specifications.size());
		Assert.assertNull(specifications.getCustomFields());
	}

	@Test
	public void testWithExtraFields() {

		Specifications specifications = new Specifications.Builder()
				.withExtraFields(Arrays.asList("f1", "f2")).build();
		Assert.assertEquals(53, specifications.size());

		Assert.assertTrue(specifications.hasField("f1"));
		Assert.assertTrue(specifications.hasField("f2"));
		Assert.assertTrue(specifications.hasField(EXTRA_FIELDS));
		Assert.assertNotNull(specifications.getField(EXTRA_FIELDS));
		StatementField cf = specifications.getField(EXTRA_FIELDS);
		Assert.assertTrue(cf instanceof CompositeField);
		Assert.assertTrue(((CompositeField)cf).hasField(new CustomStatementField("f1")));
		Assert.assertFalse(specifications.getField("f1").isPartOfAnnotationUnicityKey());
		Assert.assertTrue(((CompositeField)cf).hasField(new CustomStatementField("f2")));
		Assert.assertFalse(specifications.getField("f2").isPartOfAnnotationUnicityKey());
	}

	@Test
	public void testWithExtraFields2() {

		Specifications specifications = new Specifications.Builder()
				.withExtraFields(Arrays.asList("f1", "f2"))
				.withExtraFieldsContributingToUnicityKey(Collections.singletonList("f3")).build();
		Assert.assertEquals(54, specifications.size());

		Assert.assertTrue(specifications.hasField("f1"));
		Assert.assertTrue(specifications.hasField("f2"));
		Assert.assertTrue(specifications.hasField("f3"));
		Assert.assertTrue(specifications.hasField(EXTRA_FIELDS));
		Assert.assertNotNull(specifications.getField(EXTRA_FIELDS));
		StatementField cf = specifications.getField(EXTRA_FIELDS);
		Assert.assertTrue(cf instanceof CompositeField);
		Assert.assertTrue(((CompositeField)cf).hasField(new CustomStatementField("f1")));
		Assert.assertFalse(specifications.getField("f1").isPartOfAnnotationUnicityKey());
		Assert.assertTrue(((CompositeField)cf).hasField(new CustomStatementField("f2")));
		Assert.assertFalse(specifications.getField("f2").isPartOfAnnotationUnicityKey());
		Assert.assertTrue(((CompositeField)cf).hasField(new CustomStatementField("f3", true)));
		Assert.assertTrue(specifications.getField("f3").isPartOfAnnotationUnicityKey());
	}

	@Test
	public void shouldCreateSpecsFromExtraFieldsValue() {

		String extraFieldValue = "{\"ALLELE_COUNT\":\"1\",\"ALLELE_SAMPLED\":\"217610\",\"DBSNP_ID\":\"rs745905374\"}";

		Specifications spec = new Specifications.Builder().withExtraFieldsValue(extraFieldValue).build();

		Arrays.stream(CoreStatementField.values()).forEach(field -> Assert.assertTrue(spec.hasField(field.getName())));
		Assert.assertTrue(spec.hasCustomFields());
		Assert.assertTrue(spec.hasField("ALLELE_COUNT"));
		Assert.assertTrue(spec.hasField("ALLELE_SAMPLED"));
		Assert.assertTrue(spec.hasField("DBSNP_ID"));
		Assert.assertEquals(CoreStatementField.values().length+4, spec.size());
	}

	@Test(expected = IllegalStateException.class)
	public void shouldNotCreateSpecsFromInvalidExtraFieldsValue() {

		new Specifications.Builder().withExtraFieldsValue("roudouroud");
	}
}