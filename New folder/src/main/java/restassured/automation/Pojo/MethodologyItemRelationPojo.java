package restassured.automation.Pojo;

public class MethodologyItemRelationPojo {
	
	private String relationType;
	private String linkedMethodologyItemId;
	private String relationId;
	private String methodologyItemId;
	
	
	
	public String getMethodologyItemId() {
		return methodologyItemId;
	}
	public void setMethodologyItemId(String methodologyItemId) {
		this.methodologyItemId = methodologyItemId;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getLinkedMethodologyItemId() {
		return linkedMethodologyItemId;
	}
	public void setLinkedMethodologyItemId(String linkedMethodologyItemId) {
		this.linkedMethodologyItemId = linkedMethodologyItemId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	

}
