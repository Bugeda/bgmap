package bgmap.core.model;

import java.util.ArrayList;
import java.util.HashMap;

public class MafHashMap extends HashMap<MafHashKey, ArrayList<MafHashValue>>{

	private MafHashKey mafKey;
	
	private ArrayList<MafHashValue> mafValue;

	public MafHashKey getMafKey() {
		return mafKey;
	}

	public void setMafKey(MafHashKey mafKey) {
		this.mafKey = mafKey;
	}

	public ArrayList<MafHashValue> getMafValue() {
		return mafValue;
	}

	public void setMafValue(ArrayList<MafHashValue> list) {
		this.mafValue = list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mafKey == null) ? 0 : mafKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof MafHashMap))
			return false;
		MafHashMap other = (MafHashMap) obj;
		if (mafValue == null) {
			if (other.mafValue != null)
				return false;
		} else if (!mafValue.equals(other.mafValue))
			return false;
		return true;
	}		
}
