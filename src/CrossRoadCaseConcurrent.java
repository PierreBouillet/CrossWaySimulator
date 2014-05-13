
public class CrossRoadCaseConcurrent {
	 private int x;
	    private int y;
	    private CarConcurrent content;
	    private boolean isRelevant;
	    private boolean isCrossroad;

	    public CrossRoadCaseConcurrent(int x, int y, boolean isRelevant, boolean isCrossroad)
	    {
	        this.isRelevant = isRelevant;
	        this.isCrossroad = isCrossroad;
	        this.x = x;
	        this.y = y;
	        content = null;
	    }

	    public CarConcurrent getContent()
	    {
	        return content;
	    }

	    public void setContent(CarConcurrent content)
	    {
	        this.content = content;
	    }

	    public int getX() {
	        return x;
	    }

	    public void setX(int x) {
	        this.x = x;
	    }

	    public int getY() {
	        return y;
	    }

	    public void setY(int y) {
	        this.y = y;
	    }

	    public boolean isRelevant() {
	        return isRelevant;
	    }

	    public void setRelevant(boolean isRelevant) {
	        this.isRelevant = isRelevant;
	    }

	    public boolean isCrossroad() {
	        return isCrossroad;
	    }
}
