/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
/**
 *
 */
package tripleo.elijah.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Tripleo(sb)
 *
 * Created 	Dec 9, 2019 at 3:30:44 PM
 */
public class LookupResultList {

	private List<LookupResult> _results = new ArrayList<LookupResult>();

	public void add(String name, int level, OS_Element element) {
		for (LookupResult result : _results) {
			if (result.getElement() == element)
				return; // TODO hack for bad algorithm
		}
		_results.add(new LookupResult(name, element, level));
	}

	public OS_Element chooseBest(List<Predicate> l) {
		List<LookupResult> r;
		if (l != null) {
			r = getMaxScoredResults(l);
		} else {
			r = results();
		}
		//
		if (r.size() == 1)
			return r.get(0).getElement();
		else if (r.size() == 2) {
			if (r.get(0).getElement() == r.get(1).getElement()) {
//				r.remove(1);
				return r.get(0).getElement();
			} else {
				return null;
			}
		} else if (r.size() == 3) {
			if (r.get(0).getElement() == r.get(1).getElement()
			&& (r.get(1).getElement() == r.get(2).getElement())) {
//				r.remove(1);
//				r.remove(2);
				return r.get(0).getElement();
			}
		}
		return null; //throw new NotImplementedException();
	}

	private List<LookupResult> getMaxScoredResults(List<Predicate> l) {
		Map<LookupResult, Integer> new_results = new HashMap<LookupResult, Integer>();
		int maxScore = 0;

		for (LookupResult lookupResult : _results) {
			int score = 0;
			for (Predicate predicate : l) {
				if (predicate.test(lookupResult.getElement()))
					score++;
			}
			if (score >= maxScore && maxScore != 0) {
				maxScore = score;
				new_results.clear();
				new_results.put(lookupResult, score);
			} else
				new_results.put(lookupResult, score);
		}
		return new ArrayList<LookupResult>(new_results.keySet());
	}

	public List<LookupResult> results() { // TODO want ImmutableList
		return _results;
	}
}

//
//
//
