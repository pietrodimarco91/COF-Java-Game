package client.actions;

public class SwitchPermitTilesAction extends Action {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String regionName;
	
	public SwitchPermitTilesAction(String typeOfAction,String regionName) {
		super(typeOfAction);
		this.regionName=regionName;
	}
	
	public String getRegionName() {
		return this.regionName;
	}

}
