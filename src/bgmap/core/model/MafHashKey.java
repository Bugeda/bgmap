package bgmap.core.model;

public class MafHashKey {

	private byte col;	
	private byte row;
	
	public byte getCol() {
		return col;
	}

	public void setCol(byte col) {
		this.col = col;
	}

	public byte getRow() {
		return row;
	}

	public void setRow(byte row) {
		this.row = row;
	}
	
	public MafHashKey(byte col, byte row) {		
		this.col = col;
		this.row = row;		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MafHashKey))
			return false;
		MafHashKey other = (MafHashKey) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MafHashKey [col=" + col + ", row=" + row + "]";
	}	
	
}
