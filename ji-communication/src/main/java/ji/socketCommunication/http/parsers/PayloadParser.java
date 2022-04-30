package ji.socketCommunication.http.parsers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import ji.common.Logger;
import ji.common.structures.DictionaryValue;

public class PayloadParser {
	
	private final Logger logger;
	
	public PayloadParser(Logger logger) {
		this.logger = logger;
	}
	
	public String createPayload(Map<String, Object> params) {
		StringBuilder b = new StringBuilder();
		createPayload((name, value)->{
			 try {
				if (!b.toString().isEmpty()) {
					b.append("&");
				}
				b.append(String.format(
					"%s=%s",
					// TODO use encode?
					name, // URLEncoder.encode(name, StandardCharsets.UTF_8.toString()),
					URLEncoder.encode(value + "", StandardCharsets.UTF_8.toString()) // + "" is fix, varialbe could be null
				));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}, null, params);
		return b.toString();
	}
	
	private void createPayload(BiConsumer<String, Object> addValue, String parent, Map<String, Object> params) {
		params.forEach((name, value)->{
			createPayload(
				addValue,
				parent == null ? name : String.format("%s[%s]", parent, name), 
				params.get(name)
			);
		});
	}
	
	private void createPayload(BiConsumer<String, Object> addValue, String name, List<Object> params) {
		for (Object value : params) {
			createPayload(addValue, name + "[]", value);
		}
	}
	
	public void createPayload(BiConsumer<String, Object> addValue, String name, Object value) {
		if (value == null) {
			addValue.accept(name, value);
		} else if (value instanceof Map) {
			createPayload(addValue, name, new DictionaryValue(value).getMap());
		} else if (value instanceof List) {
			createPayload(addValue, name, new DictionaryValue(value).getList());
		} else {
			addValue.accept(name, value);
		}
	}
	
/*	private String createItem(String name, Object value) throws UnsupportedEncodingException {
		return String.format(
			"&%s=%s",
			// TODO use encode?
			name, // URLEncoder.encode(name, StandardCharsets.UTF_8.toString()),
			URLEncoder.encode(value + "", StandardCharsets.UTF_8.toString()) // + "" is fix, varialbe could be null
		);
	}*/
	
	public void parsePayload(
			BiConsumer<String, Object> addParameter,
			Function<String, Object> getParameter,
			String payload) throws UnsupportedEncodingException {
		if (payload.isEmpty()) {
			return;
		}
		String[] params = payload.split("\\&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 1) {
				parseParams(addParameter, getParameter, keyValue[0], "");
			} else if (keyValue.length == 2) {
				parseParams(addParameter, getParameter, keyValue[0], keyValue[1]);
			} else {
		    	logger.warn("Invalid param " + param);
		    }
		}
	}

	public void parseParams(
			BiConsumer<String, Object> addParameter,
			Function<String, Object> getParameter,
			String key, String value) throws UnsupportedEncodingException {
		//key = key.replace("[]", "&=").replace("][", "&").replace("[", "&").replace("]", "&");
		key = URLDecoder.decode(key, StandardCharsets.UTF_8.toString());
		key = key.replace("[]", "[=]").replace("[", "&").replace("]", "");
		String[] keys = key.split("\\&");
		value = URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		int keyCount = StringUtils.countMatches(key, "&");
		if (keyCount == 0) {
			addParameter.accept(key, value);
			return;
		}
		parseParams(addParameter, getParameter, keys, value);
	}

	private void parseParams(
			BiConsumer<String, Object> addParameter,
			Function<String, Object> getParameter,
			String[] keys, String value) throws UnsupportedEncodingException {
		String key = keys[0]; // URLDecoder.decode(keys[0], StandardCharsets.UTF_8.toString());
		Object o = getParameter.apply(key);
		if (o == null && keys[1].equals("=")) {
			addParameter.accept(key, new LinkedList<>());
		} else if (o == null) {
			addParameter.accept(key, new HashMap<>());
		}
		parseParams(getParameter.apply(key), keys, 1, value);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void parseParams(Object o, String[] keys, int index, String value) throws UnsupportedEncodingException {
		if (index == keys.length) {
			return;
		}
		
		Object sub = null;
		if (o instanceof Map) {
			// sub = Map.class.cast(o).get(URLDecoder.decode(keys[index], StandardCharsets.UTF_8.toString()));
			sub = Map.class.cast(o).get(keys[index]);
		} else if (o instanceof List) {
			List l = List.class.cast(o);
			if (!l.isEmpty()) {
				sub = l.get(l.size() - 1);
			}
		}
		boolean needInsert = false;
		if (index == keys.length - 1) {
			needInsert = true;
			sub = value;
		} else if (sub == null && keys[index+1].equals("=")) {
			needInsert = true;
			sub = new LinkedList<>();
		} else if (sub == null) {
			needInsert = true;
			sub = new HashMap<>();
		}
		if (needInsert && o instanceof Map) {
			Map.class.cast(o).put(keys[index], sub);
		} else if (needInsert && o instanceof List) {
			List.class.cast(o).add(sub);
		}
		parseParams(sub, keys, index+1, value);
	}
}
