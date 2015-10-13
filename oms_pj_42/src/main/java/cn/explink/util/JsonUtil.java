package cn.explink.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.getSerializationConfig().withSerializationInclusion(Inclusion.NON_EMPTY);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	}

	public static String translateToJson(Object obj) throws IOException, JsonGenerationException, JsonMappingException {
		return objectMapper.writeValueAsString(obj);
	}

	public static <T> T readValue(String json, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, type);
	}

	// public static void main(String[] args) throws JsonGenerationException,
	// JsonMappingException, IOException {
	// DeliveryRateAggregation req = new DeliveryRateAggregation();
	// req.setQueryType(DeliveryRateQueryType.byBranch);
	// Map<Integer, DeliveryRateBranchOrCustomerAggregation>
	// branchOrCustomerAggMap = new HashMap<Integer,
	// DeliveryRateBranchOrCustomerAggregation>();
	// DeliveryRateBranchOrCustomerAggregation bocAgg = new
	// DeliveryRateBranchOrCustomerAggregation();
	// Map<String, DeliveryRateDateAggregation> dateAggMap = new HashMap<String,
	// DeliveryRateDateAggregation>();
	// DeliveryRateDateAggregation dateAgg = new DeliveryRateDateAggregation();
	// Map<DeliveryRateTimeType, DeliveryRate> timeTypeMap = new
	// HashMap<DeliveryRateTimeType, DeliveryRate>();
	//
	// timeTypeMap.put(DeliveryRateTimeType.hour12, null);
	//
	// dateAgg.setTimeTypeMap(timeTypeMap);
	// dateAggMap.put("abc", dateAgg);
	// bocAgg.setDateAggMap(dateAggMap);
	// branchOrCustomerAggMap.put(1, bocAgg);
	// req.setBranchOrCustomerAggMap(branchOrCustomerAggMap);
	// List<DeliveryRateTimeType> timeTypeList = new
	// ArrayList<DeliveryRateTimeType>();
	// timeTypeList.add(DeliveryRateTimeType.hour12);
	// req.setTimeTypes(timeTypeList);
	//
	// String json = translateToJson(req);
	// System.out.println(json);
	//
	// DeliveryRateAggregation obj = readValue(json,
	// DeliveryRateAggregation.class);
	// DeliveryRateAggregation obj2 = readValue(json,
	// DeliveryRateAggregation.class);
	// System.out.println(obj.getTimeTypes().get(0));
	// System.out.println(obj2.getTimeTypes().get(0));
	// System.out.println(obj.getTimeTypes().get(0).equals(obj2.getTimeTypes().get(0)));
	// }

}
