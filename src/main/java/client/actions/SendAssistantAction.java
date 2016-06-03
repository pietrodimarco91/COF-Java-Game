package client.actions;

public class SendAssistantAction extends Action {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String region;
	
	private String color;
	
	public SendAssistantAction(String type, String region, String color) {
		super(type);
		this.region=region;
		this.color=color;
	}
	
	public String getRegion() {
		return this.region;
	}
	
	public String getColor() {
		return this.color;
	}

}
