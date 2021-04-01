package restassured.automation.Pojo;

import java.util.List;

public class RootConditionGroup {
	
	private String id;
	private String operator;
	private String children;
	private List<Conditions> conditions;
	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	public String getChildren() {
		return children;
	}


	public void setChildren(String children) {
		this.children = children;
	}


	public List<Conditions> getConditions() {
		return conditions;
	}


	public void setConditions(List<Conditions> conditions) {
		this.conditions = conditions;
	}


	public static class Conditions{
		
		private String id;
		private String sourceId;
		private String name;
		private String multipleInstanceConjunction;
		private String type;
		private String value;
		private String singleLogicOperator;
		private String resourceId;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
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
		public String getMultipleInstanceConjunction() {
			return multipleInstanceConjunction;
		}
		public void setMultipleInstanceConjunction(String multipleInstanceConjunction) {
			this.multipleInstanceConjunction = multipleInstanceConjunction;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getSingleLogicOperator() {
			return singleLogicOperator;
		}
		public void setSingleLogicOperator(String singleLogicOperator) {
			this.singleLogicOperator = singleLogicOperator;
		}
		public String getResourceId() {
			return resourceId;
		}
		public void setResourceId(String resourceId) {
			this.resourceId = resourceId;
		}
		
	}
	
}
