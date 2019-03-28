package org.nextprot.commons.statements;

import org.junit.Assert;
import org.junit.Test;

public class TargetIsoformDeserializationTest {

	@Test
	public void testRawStatementEquals() {

		TargetIsoformSet tis = new TargetIsoformSet();
		TargetIsoformStatementPosition tisp1 = new TargetIsoformStatementPosition("NX-P01308-1", 30, 40, null, "main");
		TargetIsoformStatementPosition tisp2 = new TargetIsoformStatementPosition("NX-P01308-2", 50, 40, "specific", "secondary");
		TargetIsoformStatementPosition tisp3 = new TargetIsoformStatementPosition("NX-P01308-3", null, null, "specific", "secondary");

		tis.add(tisp1);
		tis.add(tisp2);
		tis.add(tisp3);

		String jsonString = tis.serializeToJsonString();

		TargetIsoformSet tis2 = TargetIsoformSet.deSerializeFromJsonString(jsonString);

		Assert.assertEquals(tis, tis2);
	}

	@Test
	public void testBackWardCompatibility(){

		String jsonString1 = "[{\"isoformAccession\":\"NX_P52701-1\",\"specificity\":\"UNKNOWN\",\"begin\":null,\"end\":null,\"name\":\"MSH6-isoGTBP-N-p.Thr1219Asp\"},{\"isoformAccession\":\"NX_P52701-3\",\"specificity\":\"UNKNOWN\",\"begin\":null,\"end\":null,\"name\":\"MSH6-iso3-p.Thr1089Asp\"},{\"isoformAccession\":\"NX_P52701-4\",\"specificity\":\"UNKNOWN\",\"begin\":null,\"end\":null,\"name\":\"MSH6-iso4-p.Thr917Asp\"}]";
		String jsonString2 = "[{\"isoformAccession\":\"NX_P52701-1\",\"specificity\":null,\"begin\":5,\"end\":10,\"name\":\"MSH6-isoGTBP-N-p.Thr1219Asp\"},{\"isoformAccession\":\"NX_P52701-3\",\"specificity\":\"UNKNOWN\",\"begin\":null,\"end\":null,\"name\":\"MSH6-iso3-p.Thr1089Asp\"},{\"isoformAccession\":\"NX_P52701-4\",\"specificity\":\"UNKNOWN\",\"begin\":null,\"end\":null,\"name\":\"MSH6-iso4-p.Thr917Asp\"}]";

		TargetIsoformSet tis1 = TargetIsoformSet.deSerializeFromJsonString(jsonString1);
		TargetIsoformSet tis2 = TargetIsoformSet.deSerializeFromJsonString(jsonString2);

		Assert.assertEquals(tis1.size(), 3);
		Assert.assertEquals(tis2.size(), 3);
		Assert.assertEquals(5, (int) tis2.iterator().next().getBegin());
		Assert.assertNull(tis1.iterator().next().getBegin());
		Assert.assertNull(tis2.iterator().next().getSpecificity());
		Assert.assertEquals(tis1.iterator().next().getSpecificity(), "UNKNOWN");

	}

	
}