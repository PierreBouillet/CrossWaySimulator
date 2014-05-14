
class MutableInt {
  private int value; // note that we start at 1 since we're counting
  public void increment () { ++value;      }
  public int  get ()       { return value; }
  
	MutableInt(){
		value=1;
		}
}
