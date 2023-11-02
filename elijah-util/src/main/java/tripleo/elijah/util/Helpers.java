/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created 9/10/20 3:44 PM
 */
public enum Helpers {
	;

	public static @NotNull Operation<String> getHashForFilename(final @NotNull String aFilename) {
		try {
			final String hdigest = new DigestUtils(MessageDigestAlgorithms.SHA_256).digestAsHex(new File(aFilename));

			if (hdigest != null) {
				return Operation.success(hdigest);
			} else {
				return Operation.failure(new Exception("apache digest returns null"));
			}
		} catch (IOException aE) {
			return Operation.failure(aE);
		}
	}

	@NotNull
	public static <E> List<E> List_of(@NotNull final E... e1) {
		final List<E> r = new ArrayList<E>();
		Collections.addAll(r, e1);
		return r;
	}

	public static <T> List<String> mapCollectionElementsToString(final List<T> instructionArguments) {
		return instructionArguments.stream().map(Object::toString).collect(Collectors.toList());
	}

	@NotNull
	public static String remove_single_quotes_from_string(final @NotNull String s) {
		return s.substring(1, s.length() - 1);
	}

	public static @NotNull String String_join(@NotNull String separator, @NotNull Iterable<String> stringIterable) {
		if (false) {
			final StringBuilder sb = new StringBuilder();

			for (final String part : stringIterable) {
				sb.append(part);
				sb.append(separator);
			}
			final String ss        = sb.toString();
			final String substring = separator.substring(0, ss.length() - separator.length());
			return substring;
		}
		// since Java 1.8
		return String.join(separator, stringIterable);
	}

	public static boolean String_equals(String aS, String aS1) {
		return Objects.equals(aS, aS1);
	}

	@NotNull
	private static <T> List<T> __prepend_list(final T b, final @NotNull List<T> list2) {
		final List<T> list3 = new ArrayList<>(list2.size() + 2);
		list3.add(b);
		list3.addAll(list2);
		return list3;
	}

	@NotNull
	public static <T> List<T> __combine_list_elements(final T b, final List<T> m, final T e) {
		final List<T> list3 = __prepend_list(b, m);
		list3.add(e);
		return list3;
	}

}

//
//
//
