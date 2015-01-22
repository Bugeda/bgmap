package bgmap.core;

public class AdminPanelStatus {

	private static boolean editMaf = false;

	public static boolean isEditMaf() {
		return editMaf;
	}

	public static void setEditMaf(boolean editMaf) {
		AdminPanelStatus.editMaf = editMaf;
	}
	
	
}
