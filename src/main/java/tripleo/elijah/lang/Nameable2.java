package tripleo.elijah.lang;

import tripleo.elijah.lang.nextgen.names2.ENU_Understanding;
import tripleo.elijah.lang.nextgen.names2.EN_Name;

import java.util.List;

public interface Nameable2 {
	EN_Name getEnName();
	void addUnderstanding(ENU_Understanding u);
	List<ENU_Understanding> getUnderstandings();
}
