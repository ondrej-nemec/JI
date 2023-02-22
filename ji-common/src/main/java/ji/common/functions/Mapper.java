package ji.common.functions;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import ji.common.annotations.MapperIgnored;
import ji.common.annotations.MapperParameter;
import ji.common.annotations.MapperType;
import ji.common.structures.DictionaryValue;
import ji.common.structures.ListDictionary;
import ji.common.structures.MapDictionary;
import ji.common.structures.Tuple2;

public class Mapper {
	
	public static Mapper get() {
		return new Mapper();
	}
	public Map<String, Object> serialize(Object value) {
		return serialize(value, null);
	}
	
	public Map<String, Object> serialize(Object value, String key) {
		try {
			Map<String, Object> json = new HashMap<>();
			Field[] fields = value.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("this$0")) {
					continue;
				}
				if (field.isAnnotationPresent(MapperIgnored.class)) {
					String[] annotationKeys = field.getAnnotation(MapperIgnored.class).value();
					if (annotationKeys.length == 0 || Arrays.asList(annotationKeys).contains(key)) {
						continue;
					}
				}
				field.setAccessible(true);
				String name = field.getName();
				if (field.isAnnotationPresent(MapperParameter.class)) {
					MapperType toUse = null;
					for (MapperType mapperType : field.getAnnotation(MapperParameter.class).value()) {
						if (mapperType.key().equals("")) {
							toUse = mapperType; // default
						} else if (key != null && mapperType.key().equals(key)) {
							toUse = mapperType; // key match
						}
					}
					if (toUse != null && toUse.ignoreOnNull() && field.get(value) == null) {
						continue;
					} else if (toUse != null) {
						name = toUse.value();
					}
				}
				json.put(name, field.get(value));
			}
			return json;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public <T> T parse(Class<T> clazz, Object source) throws Exception {
		return read(clazz, source, null, null, null, (v)->{});
	}
	
	public <T> T parse(Class<T> clazz, Object source, String key) throws Exception {
		return read(clazz, source, key, null, null, (v)->{});
	}
	
	private <T> T read(Class<T> clazz, Object source, String only, Object valueCandidate, Type generic, Consumer<DictionaryValue> onValue) throws Exception {
		DictionaryValue parameterValue = new DictionaryValue(source);
		onValue.accept(parameterValue);
		T target = createNewInstance(valueCandidate, clazz);
		if (source == null) {
			return null;
		} else if ( Map.class.isAssignableFrom(source.getClass()) && !Map.class.isAssignableFrom(clazz)) {
			Field[] fields = clazz.getDeclaredFields();
			MapDictionary<String, Object> values = parameterValue.getDictionaryMap();
			for (Field field : fields) {
				field.setAccessible(true);
				String parameterName = field.getName();
				
				MapperType toUse = null;
				if (field.isAnnotationPresent(MapperParameter.class)) {
					for (MapperType mapperType : field.getAnnotation(MapperParameter.class).value()) {
						if (mapperType.key().equals("")) {
							toUse = mapperType; // default
						} else if (only != null && mapperType.key().equals(only)) {
							toUse = mapperType; // key match
						}
					}
					if (toUse != null) {
						parameterName = toUse.value();
					}
				}
				
				Object newCandidate = values.get(parameterName);
				if (newCandidate == null) {
					continue;
				}
				
				Object value = read(field.getType(), newCandidate, only, field.get(target), field.getGenericType(), setDV(toUse));
				Method m = getMethod("set", parameterName, clazz, field.getType());
				if (m == null) {
					field.set(target, value);
				} else {
					m.invoke(target, value);
				}
			}
		} else if (Collection.class.isAssignableFrom(clazz) || ListDictionary.class.isAssignableFrom(clazz)) {
			Tuple2<Class<?>, Type> types = getGenericClass(generic, 0);
			Class<?> collectionItemClass = types._1();
			Method m = getMethod("add", clazz, Object.class);
			parameterValue.getDictionaryList().forEach((item)->{
				m.invoke(target, read(collectionItemClass, item.getValue(), only, null, types._2(), onValue));
			});
		} else if (Map.class.isAssignableFrom(clazz) || MapDictionary.class.isAssignableFrom(clazz)) {
		//	Class<?> mapKeyClass = getGenericClass(generic, 0);
			Tuple2<Class<?>, Type> types = getGenericClass(generic, 1);
			Class<?> mapValueClass = types._1();
			Method m = getMethod("put", clazz, Object.class, Object.class);
			parameterValue.getDictionaryMap().forEach((key, item)->{
				m.invoke(target, key, read(mapValueClass, item.getValue(), only, null, types._2(), onValue));
			});
		} else {
			return parameterValue.getValue(clazz);
		}
		return target;
	}
	
	private Consumer<DictionaryValue> setDV(MapperType toUse) {
		return (v)->{
			if (toUse != null && !toUse.dateTimeFormat().equals("")) {
				v.setDateTimeFormat(toUse.dateTimeFormat());
				if (!toUse.key().equals("")) {
					v.setOnlyKey(toUse.key());
				}
			}
		};
	}
		
	@SuppressWarnings("unchecked")
	private <T> T createNewInstance(Object valueCandidate, Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if (valueCandidate != null) {
			return new DictionaryValue(valueCandidate).getValue(clazz);
		}
		if (clazz.equals(Map.class)) {
			return (T)new HashMap<>();
		}
		if (clazz.isInterface() && List.class.isAssignableFrom(clazz)) {
			return (T)new LinkedList<>();
		}
		if (clazz.isInterface() && Set.class.isAssignableFrom(clazz)) {
			return (T)new HashSet<>();
		}
		try {
			clazz.getConstructor();
		} catch (NoSuchMethodException e) {
			// not non-parameters constructor - enum, primitives, numbers, ...
			return null;
		}
		/*if (clazz.isEnum()) {
			return null;
		}*/
		 // return clazz.newInstance();
		return clazz.getDeclaredConstructor().newInstance();
	}

	private Tuple2<Class<?>, Type> getGenericClass(Type field, int index) {
		Type type = ParameterizedType.class.cast(field)
				.getActualTypeArguments()[index];
		if (ParameterizedType.class.isInstance(type)) {
			ParameterizedType parametrized = ParameterizedType.class.cast(type);
			return new Tuple2<>(Class.class.cast(parametrized.getRawType()),parametrized);
		}
		return new Tuple2<>(Class.class.cast(type), null);
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
