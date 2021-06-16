package common.functions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import common.annotations.ParseIgnored;
import common.annotations.ParseParameter;
import common.structures.DictionaryValue;
import common.structures.MapDictionary;

public class Mapper {
	
	public static Mapper get() {
		return new Mapper();
	}

	public Map<String, Object> serialize(Object value) {
		try {
			Map<String, Object> json = new HashMap<>();
			Field[] fields = value.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("this$0")) {
					continue;
				}
				if (field.isAnnotationPresent(ParseIgnored.class)) {
					continue;
				}
				field.setAccessible(true);
				String name = field.getName();
				if (field.isAnnotationPresent(ParseParameter.class)) {
					name = field.getAnnotation(ParseParameter.class).value();
				}
				json.put(name, field.get(value));
			}
			return json;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T parse(Class<T> clazz, Object source) throws Exception {
		return read(clazz, source, null, null);
	}
	
	private <T> T read(Class<T> clazz, Object source, Object valueCandidate, Type generic) throws Exception {
		DictionaryValue parameterValue = new DictionaryValue(source);
		T target = valueCandidate == null ? clazz.newInstance() : new DictionaryValue(valueCandidate).getValue(clazz);
		if ( Map.class.isAssignableFrom(source.getClass()) && !Map.class.isAssignableFrom(clazz)) {
			Field[] fields = clazz.getDeclaredFields();
			MapDictionary<String, Object> values = parameterValue.getDictionaryMap();
			for (Field field : fields) {
				field.setAccessible(true);
				String parameterName = null;
				if (field.isAnnotationPresent(ParseParameter.class)) {
					parameterName = field.getAnnotation(ParseParameter.class).value();
				} else {
					parameterName = field.getName();
				}
				Object value = read(field.getType(), values.get(parameterName), field.get(target), field.getGenericType());
				Method m = getMethod("set", parameterName, clazz, field.getType());
				if (m == null) {
					field.set(target, value);
				} else {
					m.invoke(target, value);
				}
			}
		} else if (Collection.class.isAssignableFrom(clazz)) {
			Class<?> collectionItemClass = getGenericClass(generic, 0);
			Method m = getMethod("add", clazz, Object.class);
			parameterValue.getDictionaryList().forEach((item)->{
				m.invoke(target, read(collectionItemClass, item.getValue(), null, null));
			});
		} else if (Map.class.isAssignableFrom(clazz)) {
		//	Class<?> mapKeyClass = getGenericClass(generic, 0);
			Class<?> mapValueClass = getGenericClass(generic, 1);
			Method m = getMethod("put", clazz, Object.class, Object.class);
			parameterValue.getDictionaryMap().forEach((key, item)->{
				m.invoke(target, key, read(mapValueClass, item.getValue(), null, null));
			});
		} else {
			return parameterValue.getValue(clazz);
		}
		return target;
	}
	
	private Class<?> getGenericClass(Type field, int index) {
		return Class.class.cast(
			ParameterizedType.class.cast(
				field
			).getActualTypeArguments()[index]
		);
	}
	
	private Method getMethod(String prefix, String parameterName, Class<?> clazz, Class<?>...classes) {
		String name = prefix + (parameterName.charAt(0) + "").toUpperCase() + parameterName.substring(1);
		return getMethod(name, clazz, classes);
	}
	
	private Method getMethod(String name, Class<?> clazz, Class<?>...classes) {
		try {
			return clazz.getMethod(name, classes);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

}
