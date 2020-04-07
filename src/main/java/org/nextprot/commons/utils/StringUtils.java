package org.nextprot.commons.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class StringUtils {

	private static Logger LOGGER = Logger.getLogger(StringUtils.class.getName());

	public static String mkString(Iterable<?> values, String sep) {
		return mkString(values, "", sep, "");
	}

	public static String mkString(Iterable<?> values, String start, String sep,
			String end) {
		// if the array is null or empty return an empty string
		if (values == null || !values.iterator().hasNext())
			return "";

		// move all non-empty values from the original array to a new list
		// (empty is a null, empty or all-whitespace string)
		List<String> nonEmptyVals = new LinkedList<String>();
		for (Object val : values) {
			//if (val != null && val.toString().trim().length() > 0) {
				if(val == null) {
					nonEmptyVals.add(null);
				}
				else {
					nonEmptyVals.add(start + val.toString() + end);
				}
			//}
		}

		// if there are no "non-empty" values return an empty string
		if (nonEmptyVals.size() == 0)
			return "";

		// iterate the non-empty values and concatenate them with the separator,
		// the entire string is surrounded with "start" and "end" parameters
		StringBuilder result = new StringBuilder();
		int i = 0;
		for (String val : nonEmptyVals) {
			if (i > 0)
				result.append(sep);
			result.append(val);
			i++;
		}

		return result.toString();
	}

	public static String mkString(Object[] values, String start, String sep,
			String end) {
		return mkString(Arrays.asList(values), start, sep, end);
	}

	
	public static Map<String,String> deserializeAsMapOrNull(String str) {

		Map<String,String> result = null;
		TypeReference<HashMap<String,String>> typeRef = new TypeReference<HashMap<String, String>>() {};
		try { 
			result = new ObjectMapper().readValue(str, typeRef);
		} catch (Exception e) {}
		return result;
	}
	
	public static String serializeAsJsonStringOrNull(Object content) {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(content);
		} catch (JsonProcessingException e) {
			LOGGER.warning("cannot serialize content in json (content="+content+":"+ e.getMessage());
			return null;
		}
	}
}
