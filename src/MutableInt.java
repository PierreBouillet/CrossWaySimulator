
class MutableInt {
  private int value; // note that we start at 1 since we're counting
  public void increment () { ++value;      }
  public int  get ()       { return value; }
  
	MutableInt(){
		value=1;
		}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MutableInt))
			return false;
		MutableInt other = (MutableInt) obj;
		if (value != other.value)
			return false;
		return true;
	}
	
}
