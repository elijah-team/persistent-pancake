package tripleo.elijah.comp.bus;

public class COutputString implements CB_OutputString {

	private final String _text;

	public COutputString(final String aText) {
		_text = aText;
	}

	@Override
	public String getText() {
		return _text;
	}
}
