package tripleo.eljiah_pancake_durable.lang;

/**
 * Created 8/20/20 7:24 PM
 */
public interface Resolvable {
	boolean hasResolvedElement();

	OS_Element getResolvedElement();

	void setResolvedElement(OS_Element element);
}
