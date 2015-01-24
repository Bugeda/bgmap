package bgmap.core.model;

import bgmap.core.AppConfig;
import bgmap.core.MafsMarks;

public class MafHashValue{

	private short x;	
	private short y;
	private MafsMarks mafsmark;
	
	public MafHashValue(short x, short y, MafsMarks mafsmark) {		
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

	public MafsMarks getMafsMarks() {
		return mafsmark;
	}

	public void setFull(MafsMarks mafsmark) {
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
		if (Math.abs(x - other.x) > other.mafsmark.img.getWidth(null)/2 )
			return false;
		if (( other.y - y > other.mafsmark.img.getHeight(null))||(other.y - y < 0))
			return false;
		return true;
	}
}
