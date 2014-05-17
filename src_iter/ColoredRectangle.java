import java.awt.*;

/**
 * Created by William TASSOUX on 05/05/2014.
 */
public class ColoredRectangle {

    private Rectangle rect;
    private Color background;

    public ColoredRectangle(Rectangle rect)
    {
        this.rect = rect;
        background = Color.BLACK;
    }
    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

}
