package tripleo.elijah.lang.nextgen.names2;

import tripleo.elijah.lang.OS_Element;

import java.util.List;

public interface EN_Name {
	OS_Element principal();
	List<ENU_Understanding> understandings();
	void addUnderstanding(ENU_Understanding u);
}
