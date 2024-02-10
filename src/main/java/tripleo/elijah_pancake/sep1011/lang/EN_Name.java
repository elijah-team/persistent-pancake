package tripleo.elijah_pancake.sep1011.lang;

import tripleo.elijah.lang.OS_Element;
import tripleo.elijah_pancake.sep1011.lang.ENU_Understanding;

import java.util.List;

public interface EN_Name {
	OS_Element principal();
	List<ENU_Understanding> understandings();
	void addUnderstanding(ENU_Understanding u);
}
