package org.nextprot.commons.statements;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.Set;
import java.util.TreeSet;

public class TargetIsoformSet extends TreeSet<TargetIsoformStatementPosition> {

	private static final long serialVersionUID = -8387267990628898963L;

	public TargetIsoformSet() {
		super();
	}
	
	public TargetIsoformSet(Set<TargetIsoformStatementPosition> targets) {
		super(targets);
	}

	public static TargetIsoformSet deSerializeFromJsonString(String tistatementAsJson) {

		TargetIsoformSet tis = new TargetIsoformSet();
		JsonArray array = Json.parse(tistatementAsJson).asArray();

		array.forEach(a -> {
			JsonObject jo = a.asObject();

			String name = getStringBackwardCompatibleFormat(jo, "name");
			String isoformAccession = getStringBackwardCompatibleFormat(jo,"isoformAccession");
			String specificity = getStringBackwardCompatibleFormat(jo, "specificity");

			Integer begin = getIntBackwardCompatibleFormat(jo, "begin");
			Integer end = getIntBackwardCompatibleFormat(jo, "end");

			tis.add(new TargetIsoformStatementPosition(isoformAccession, begin, end, specificity, name));

		});

		return tis;

	}


	private static String getStringBackwardCompatibleFormat(JsonObject jo, String name){

		try {
			String value =  jo.getString(name, "NULL");
			if(value.equals("NULL")) return null;
			else return value;

		}catch (UnsupportedOperationException e) {
			JsonValue val = jo.get(name);
			if(val == null || val.toString().equalsIgnoreCase("null")) {
				return null;
			}
			else throw e;
		}

	}

	private static Integer getIntBackwardCompatibleFormat(JsonObject jo, String name){

		try {
			Integer value =  jo.getInt(name, -1);
			if(value == -1) {
				return null;
			}else return value;

		}catch (UnsupportedOperationException e) {
			JsonValue val = jo.get(name);
			if(val == null || val.toString().equalsIgnoreCase("null")) {
				return null;
			}
			else throw e;
		}

	}

	private static void addIfPresent(JsonObject jo, String name, Integer value){
		if(value != null)
			jo.add(name, value);
	}

	private static void addIfPresent(JsonObject jo, String name, String value){
		if(value != null)
			jo.add(name, value);
	}

	public String serializeToJsonString() {

		JsonArray array = Json.array();
		for (TargetIsoformStatementPosition tisp : this) {
			JsonObject jo = Json.object();

			addIfPresent(jo, "isoformAccession", tisp.getIsoformAccession());
			addIfPresent(jo, "specificity", tisp.getSpecificity());

			addIfPresent(jo, "begin", tisp.getBegin());
			addIfPresent(jo, "end", tisp.getEnd());
			addIfPresent(jo, "name", tisp.getName());

			array.add(jo);

		}

		return array.toString();

	}


}
