package bgmap.core;

public class AdminPanelStatus {

	private static boolean addMaf = false;

	public static boolean isAddMaf() {
		return addMaf;
	}

	public static void setAddMaf(boolean addMaf) {
		AdminPanelStatus.addMaf = addMaf;
	}
	
	
}
