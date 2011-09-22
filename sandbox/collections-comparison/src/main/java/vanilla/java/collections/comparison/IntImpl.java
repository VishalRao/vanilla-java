package vanilla.java.collections.comparison;

public class IntImpl implements Int {

	private int value;

	public IntImpl() {
		super();
	}
	
	public IntImpl(int i) {
		setInt(i);
	}
	
	public void setInt(int i) {
		this.value = i;
	}

	public int getInt() {
		return this.value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Int) {
			return value == ((Int) obj).getInt();
		}
		return false;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
