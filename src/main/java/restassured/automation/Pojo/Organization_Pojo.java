package restassured.automation.Pojo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.gson.Gson;

import restassured.automation.utils.Excel_Data_Source_Utils;

public class Organization_Pojo {

	private String name;
	private String countryCode;
	private String id;
	private String orgId;
	private String resourceId;
	private String memberFirmId;
	private String permissionSet;
	private String methodologyId;

	public String getMemberFirmId() {
		return memberFirmId;
	}

	public void setMemberFirmId(String memberFirmId) {
		this.memberFirmId = memberFirmId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setCountryCode(String string) {
		this.countryCode = string;
	}
	

	public void setPermissionSet(String permissionSet) {
		this.permissionSet = permissionSet;
	}

	public String postOrganization() throws Exception {

		Organization_Pojo Organization = new Organization_Pojo();

		//Organization.setCountryCode(61);
		Organization.setName("AUS");

		System.out.println("Values to be passed to JSON are " + Organization.getName());

		Gson Josnbody = new Gson();
		return Josnbody.toJson(Organization);
	}

	public String getMethodologyId() {
		return methodologyId;
	}

	public void setMethodologyId(String methodologyId) {
		this.methodologyId = methodologyId;
	}

	@Override
	public String toString() {
		return "Create_Organization_Pojo [name=" + this.name + ", countryCode=" + this.countryCode + ", id=" + this.id
				+ "]";
	}

}
