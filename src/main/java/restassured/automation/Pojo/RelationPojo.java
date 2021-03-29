package restassured.automation.Pojo;

public class RelationPojo {
	
	private String methodologyItemId;
	private String linkedMethodologyItemId;
	private String relationshipType;

	public String getLinkedMethodologyItemId() {
		return linkedMethodologyItemId;
	}

	public void setLinkedMethodologyItemId(String linkedMethodologyItemId) {
		this.linkedMethodologyItemId = linkedMethodologyItemId;
	}

	public String getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(String relationshipType) {
		this.relationshipType = relationshipType;
	}

	public String getMethodologyItemId() {
		return methodologyItemId;
	}

	public void setMethodologyItemId(String methodologyItemId) {
		this.methodologyItemId = methodologyItemId;
	}

}
