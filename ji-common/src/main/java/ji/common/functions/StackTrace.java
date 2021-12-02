package ji.common.functions;

import ji.common.structures.ThrowingFunction;

// TODO improve and add to documentation
public class StackTrace {

	public static String classParent(ThrowingFunction<StackTraceElement, Boolean, Exception> function) {
		String className = null;
		try {
			for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
				if (function.apply(ste)) {
					className = ste.getClassName();
					break;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return className;
	}
	
}
