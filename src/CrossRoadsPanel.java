import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CrossRoadsPanel extends JPanel {

    private ArrayList<ColoredRectangle> cells;
    private int size = Consts.size;
    private int roadSize = Consts.roadSize;

    public CrossRoadsPanel()
    {
        setSize(getPreferredSize());
        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / size;
        int cellHeight = height / size;

        int xOffset = (width - (size * cellWidth)) / 2;
        int yOffset = (height - (size * cellHeight)) / 2;

        cells = new ArrayList<ColoredRectangle>(size * size);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Rectangle rect = new Rectangle(
                        xOffset + (col * cellWidth),
                        yOffset + (row * cellHeight),
                        cellWidth,
                        cellHeight);
                cells.add(new ColoredRectangle(rect));
            }
        }
    }

    public void updateGraphicsFromLogic(ArrayList<CrossRoadsCase> logicCells)
    {
        int i = 0;
        for (CrossRoadsCase logicCell: logicCells)
        {
            ColoredRectangle matchingRectangle = cells.get(i);
            if (logicCell.isRelevant())
            {
                if (logicCell.getContent() != null) {
                    matchingRectangle.setBackground(logicCell.getContent().getClr());
                } else {
                    matchingRectangle.setBackground(Color.GRAY);
                }
            }
            else
            {
                matchingRectangle.setBackground(Color.DARK_GRAY);
            }
            ++i;
        }
    }
    
    public void updateGraphicsFromLogicConc(ArrayList<CrossRoadCaseConcurrent> logicCells)
    {
        int i = 0;
        for (CrossRoadCaseConcurrent logicCell: logicCells)
        {
            ColoredRectangle matchingRectangle = cells.get(i);
            if (logicCell.isRelevant())
            {
                if (logicCell.getContent() != null) {
                    matchingRectangle.setBackground(logicCell.getContent().getClr());
                } else {
                    matchingRectangle.setBackground(Color.GRAY);
                }
            }
            else
            {
                matchingRectangle.setBackground(Color.DARK_GRAY);
            }
            ++i;
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 800);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g.create();

        for (ColoredRectangle cell : cells) {
            g2d.setColor(cell.getBackground());
            g2d.fill(cell.getRect());
        }
        g2d.dispose();
    }
}