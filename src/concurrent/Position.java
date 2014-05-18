package concurrent;

public class Position {
	

	private int x;
	private int y;
	
	public Position(int x, int y){
		this.y=y;
		this.x=x;
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

    public static Position substract(Position p1, Position p2)
    {
        return new Position (p1.x - p2.x, p1.y - p2.y);
    }

    public static Position add(Position p1, Position p2)
    {
        return new Position (p1.x + p2.x, p1.y + p2.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        if (y != position.y) return false;

        return true;
    }
    
    

    public String toString()
    {
        return ("(" + x + ", " + y + ")");
    }
}
