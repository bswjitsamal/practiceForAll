package restassured.automation.Pojo;

public class Result {

	//private String id;
	private String sourceId;
	private String name;
	private String TargetId;

	public String getTargetId() {
		return TargetId;
	}

	public void setTargetId(String targetId) {
		TargetId = targetId;
	}
	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
