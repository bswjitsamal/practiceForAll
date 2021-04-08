package restassured.automation.Pojo;

import java.util.List;

public class RootConditionGroup {

	private String tempId;
	private String operator;
	private String isNew;
	private List<Conditions> conditions;

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<Conditions> getConditions() {
		return conditions;
	}

	public void setConditions(List<Conditions> conditions) {
		this.conditions = conditions;
	}

	public static class Conditions {

		private String name;
		private String tempId;
		private boolean isNew;
		private boolean isMultipleInstancesConjunction;
		private String sourceId;
		private String type;
		private String value;
		private String[] values;
		private String multipleLogicOperator;

		public String getTempId() {
			return tempId;
		}

		public void setTempId(String tempId) {
			this.tempId = tempId;
		}

		public boolean getIsNew() {
			return isNew;
		}

		public void setIsNew(boolean b) {
			this.isNew = b;
		}

		public boolean getIsMultipleInstancesConjunction() {
			return isMultipleInstancesConjunction;
		}

		public void setIsMultipleInstancesConjunction(boolean b) {
			this.isMultipleInstancesConjunction = b;
		}

		public String[] getValues() {
			return values;
		}

		public void setValues(String[] strings) {
			this.values = strings;
		}

		public String getMultipleLogicOperator() {
			return multipleLogicOperator;
		}

		public void setMultipleLogicOperator(String multipleLogicOperator) {
			this.multipleLogicOperator = multipleLogicOperator;
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

	}

}
