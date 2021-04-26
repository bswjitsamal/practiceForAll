package restassured.automation.Pojo;

public class ServiceDetailsPojo {
	
	//private String name;
	private RootConditionGroup rootConditionGroup;
	private Result result;
	private boolean isComplex;
	
	
	public boolean getIsComplex() {
		return isComplex;
	}
	public void setIsComplex(boolean b) {
		this.isComplex = b;
	}
	/*public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}*/
	public RootConditionGroup getRootConditionGroup() {
		return rootConditionGroup;
	}
	public void setRootConditionGroup(RootConditionGroup rootConditionGroup) {
		this.rootConditionGroup = rootConditionGroup;
	}
	
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}

}
