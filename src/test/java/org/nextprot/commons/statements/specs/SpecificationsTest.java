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
		Assert.assertEquals(0,specifications.getCustomFields().size());
	}

	@Test
	public void testWithExtraFields() {

		Specifications specifications = new Specifications.Builder()
				.withExtraFields(Arrays.asList("f1", "f2")).build();
		
		// 50 Core fields + 2 Custom ones (f1, f2)
		Assert.assertEquals(52, specifications.size());

		// EXTRA_FIELDS does not exist as a field specification
		// It only exists as a SQL table column where we store custom fields (here f1 and f2)
		Assert.assertFalse(specifications.hasField(EXTRA_FIELDS)); 
		
		Assert.assertTrue(specifications.hasField("f1"));
		Assert.assertTrue(specifications.getField("f1") instanceof CustomStatementField);
		Assert.assertFalse(specifications.getField("f1").isPartOfAnnotationUnicityKey());
		Assert.assertTrue(specifications.hasField("f2"));
		Assert.assertTrue(specifications.getField("f2") instanceof CustomStatementField);
		Assert.assertFalse(specifications.getField("f2").isPartOfAnnotationUnicityKey());
		
	}

	@Test
	public void testWithExtraFieldsOneBeingPartOfUniqueKey() {

		Specifications specifications = new Specifications.Builder()
				.withExtraFields(Arrays.asList("f1", "f2"))
				.withExtraFieldsContributingToUnicityKey(Arrays.asList("f3")).build();

		Assert.assertEquals(53, specifications.size());

		Assert.assertTrue(specifications.hasField("f1"));
		Assert.assertTrue(specifications.getField("f1") instanceof CustomStatementField);
		Assert.assertFalse(specifications.getField("f1").isPartOfAnnotationUnicityKey());
		Assert.assertTrue(specifications.hasField("f2"));
		Assert.assertTrue(specifications.getField("f2") instanceof CustomStatementField);
		Assert.assertFalse(specifications.getField("f2").isPartOfAnnotationUnicityKey());
		Assert.assertTrue(specifications.hasField("f3"));
		Assert.assertTrue(specifications.getField("f3") instanceof CustomStatementField);
		Assert.assertTrue(specifications.getField("f3").isPartOfAnnotationUnicityKey());

	}

}