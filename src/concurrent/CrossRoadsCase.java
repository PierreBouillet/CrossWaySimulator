package concurrent;

/**
 * Created by William TASSOUX on 05/05/2014.
 */
public class CrossRoadsCase
{
    private int x;
    private int y;
    private Car content;
    private boolean isRelevant;
    private boolean isCrossroad;

    public CrossRoadsCase(int x, int y, boolean isRelevant, boolean isCrossroad)
    {
        this.isRelevant = isRelevant;
        this.isCrossroad = isCrossroad;
        this.x = x;
        this.y = y;
        content = null;
    }

    public Car getContent()
    {
        return content;
    }

    public void setContent(Car content)
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
