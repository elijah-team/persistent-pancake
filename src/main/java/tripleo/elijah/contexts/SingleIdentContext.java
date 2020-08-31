package tripleo.elijah.contexts;

import tripleo.elijah.lang.Context;
import tripleo.elijah.lang.IdentExpression;
import tripleo.elijah.lang.LookupResultList;
import tripleo.elijah.lang.OS_Element;

import java.util.List;

/**
 * Created 8/30/20 6:51 PM
 */
public class SingleIdentContext extends Context {
	private Context _parent;
	public IdentExpression carrier;
	private OS_Element element;

	public void setString(IdentExpression carrier) {
		this.carrier = carrier;
	}

	public SingleIdentContext(Context _parent, OS_Element element) {
		this._parent = _parent;
		this.element = element;
	}

	@Override
	public LookupResultList lookup(String name, int level, LookupResultList Result, List<Context> alreadySearched, boolean one) {
		alreadySearched.add(element.getContext());

		if (carrier != null && carrier.getText().equals(name))
			Result.add(name, level, element, this);

		if (getParent() != null) {
			final Context context = getParent();
			if (!alreadySearched.contains(context) && !one)
				context.lookup(name, level + 1, Result, alreadySearched, false);
		}
		return Result;
	}

	@Override
	public Context getParent() {
		return _parent;
	}
}