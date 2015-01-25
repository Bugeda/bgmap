package bgmap.core.model;

import bgmap.core.AppConfig;

public class MafHashValue{

	private short x;	
	private short y;
	private byte mafsmark;
	
	public MafHashValue(short x, short y, byte mafsmark) {		
		this.x = x;
		this.y = y;
		this.mafsmark = mafsmark;
	}
	
	public int getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}

	public byte getMafsMarks() {
		return mafsmark;
	}

	public void setFull(byte mafsmark) {
		this.mafsmark = mafsmark;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MafHashValue))
			return false;
		MafHashValue other = (MafHashValue) obj;
		if (Math.abs(x - other.x) > AppConfig.MafsMarks[other.mafsmark].getWidth(null)/2 )
			return false;
		if (( other.y - y > AppConfig.MafsMarks[other.mafsmark].getHeight(null))||(other.y - y < 0))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MafHashValue [x=" + x + ", y=" + y + ", mafsmark=" + mafsmark
				+ "]";
	}		
}
