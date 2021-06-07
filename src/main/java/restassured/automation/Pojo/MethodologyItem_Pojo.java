package restassured.automation.Pojo;

import java.util.Map;

import com.google.gson.Gson;

public class MethodologyItem_Pojo {

	private String revisions;
	private String title;
	private String index;
	private String itemType;
	private String parentId;
	private String WPId;
	private InitData initData;
	private String workProgramType;
	private String tailoring;
	private String ruleContextType;
	private String ruleContextSource;
	private String[] optionTitles;
	private String visibility;
	private String itemizedWorkProgramType;
	private String workProgramItemType;
	private String financialStatementType;
	private String decimalsAllowed;
	private String format;
	private String type;
	private String lineItem;
	private String displayName;
	private String filterId;
	private String filterName;
	
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String[] getOptionTitles() {
		return optionTitles;
	}

	public void setOptionTitles(String[] optionTitles) {
		this.optionTitles = optionTitles;
	}

	public String getRuleContextType() {
		return ruleContextType;
	}

	public void setRuleContextType(String ruleContextType) {
		this.ruleContextType = ruleContextType;
	}

	public String getRuleContextSource() {
		return ruleContextSource;
	}

	public void setRuleContextSource(String ruleContextSource) {
		this.ruleContextSource = ruleContextSource;
	}

	public String getWorkProgramType() {
		return workProgramType;
	}

	public void setWorkProgramType(String workProgramType) {
		this.workProgramType = workProgramType;
	}

	public String getTailoring() {
		return tailoring;
	}

	public void setTailoring(String tailoring) {
		this.tailoring = tailoring;
	}

	public String getWPId() {
		return WPId;
	}

	public void setWPId(String wPId) {
		WPId = wPId;
	}

	public InitData getInitData() {
		return initData;
	}

	public void setInitData(InitData initData) {
		this.initData = initData;
	}

	public String getRevisions() {
		return revisions;
	}

	public void setRevisions(String revisions) {
		this.revisions = revisions;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	

	public String getItemizedWorkProgramType() {
		return itemizedWorkProgramType;
	}

	public void setItemizedWorkProgramType(String itemizedWorkProgramType) {
		this.itemizedWorkProgramType = itemizedWorkProgramType;
	}
	


	public String getWorkProgramItemType() {
		return workProgramItemType;
	}

	public void setWorkProgramItemType(String workProgramItemType) {
		this.workProgramItemType = workProgramItemType;
	}



	public String getFinancialStatementType() {
		return financialStatementType;
	}

	public void setFinancialStatementType(String financialStatementType) {
		this.financialStatementType = financialStatementType;
	}



	public String getDecimalsAllowed() {
		return decimalsAllowed;
	}

	public void setDecimalsAllowed(String decimalsAllowed) {
		this.decimalsAllowed = decimalsAllowed;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}



	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}



	public static class InitData {

		private String workProgramType;
		private String tailoring;

		public String getWorkProgramType() {
			return workProgramType;
		}

		public void setWorkProgramType(String workProgramType) {
			this.workProgramType = workProgramType;
		}

		public String getTailoring() {
			return tailoring;
		}

		public void setTailoring(String tailoring) {
			this.tailoring = tailoring;
		}
		

	}

}
