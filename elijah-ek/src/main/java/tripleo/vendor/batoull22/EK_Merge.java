package tripleo.vendor.batoull22;

/**
 * @author tripleo
 */
class EK_Merge {
	EK_Fact first;
	EK_Fact second;
	EK_Fact result;

	EK_Merge(EK_Fact first, EK_Fact second, EK_Fact result) {
		this.first  = first;
		this.second = second;
		this.result = result;
	}

	public EK_Fact result() {
		return this.result;
	}

	public EK_Fact second() {
		return this.second;
	}

	public EK_Fact first() {
		return this.first;
	}
}
