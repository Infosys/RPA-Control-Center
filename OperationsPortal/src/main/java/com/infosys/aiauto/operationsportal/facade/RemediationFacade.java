/*
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
  

package com.infosys.aiauto.operationsportal.facade;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.infosys.aiauto.operationsportal.dao.RemediationDAO;
import com.infosys.aiauto.operationsportal.dto.ActionStages;
import com.infosys.aiauto.operationsportal.dto.Remediation;
import com.infosys.aiauto.operationsportal.dto.RemediationDetails;
import com.infosys.aiauto.operationsportal.dto.RemediationExecStatus;
import com.infosys.aiauto.operationsportal.dto.RemediationPlan;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanAction;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanExecution;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanStatus;
import com.infosys.aiauto.operationsportal.dto.ScriptIdentifier;
import com.infosys.aiauto.operationsportal.dto.ScriptIdentifierDTO;

@Component
public class RemediationFacade {

	@Autowired
	private RemediationDAO remediationDAO;
	
	@Value("${serviceArea}")
	private String serviceArea;
	
	@Value("${topic_itcc_execution_notifications}")
	private String topic_itcc_execution_notifications;
	
	public List<Remediation> getRemediationActions(String resourcetype, String obsid) throws SQLException {
		List<Remediation> remediationActions = new ArrayList<Remediation>();
		remediationActions = remediationDAO.getRemediationActions(resourcetype, obsid);
		return remediationActions;
	}

	public List<ActionStages> getActionStages() throws SQLException {
		List<ActionStages> actionStages = new ArrayList<ActionStages>();
		actionStages = remediationDAO.getActionStages();
		return actionStages;
	}

	public String getRemediationActionDetails(RemediationPlan remediationPlan) throws SQLException {
		String actionStages = "";
		switch (remediationPlan.getSourcetype()) {
		case "defaultDB":
			actionStages = remediationDAO.getRemediationAction(remediationPlan.getRemediationplanid());
			break;

		case "blueprism":
			// call to blueprism classes
			break;

		case "automationanywhere":
			// call to automationanywhere classes
			break;

		case "scriptrepo":
			// call to script repo classes
			break;

		default:
			break;
		}

		return actionStages;
	}

	public String getRemediationActionsDetails() throws SQLException {
		String actionStages = remediationDAO.getRemediationAction();
		return actionStages;
	}

	public int getNumber(String type) throws SQLException {
		int num = 0;
		String lastValue = "";

		if (type.equalsIgnoreCase("remediationplan")) {
			lastValue = remediationDAO.getLastRemediationPlan();
			num = Integer.parseInt(lastValue.replaceFirst("^.*\\D", ""));
		} else if (type.equalsIgnoreCase("remediationplanaction")) {
			lastValue = remediationDAO.getLastRemediationPlanAction();
			num = Integer.parseInt(lastValue.replaceFirst("^.*\\D", ""));
		}

		return num + 1;
	}

	public String saveRemediationPlan(RemediationDetails remediationDetails) throws SQLException, JsonProcessingException, JSONException {
		String result = "{\"res\":\"", providedValueType = "", updateRes = "";
		String remediationplanid = "RP", remediationplanactionid = "RPACT";
		int updated_rows = 0, num = 0, actionid = 0, actid = 0;

		num = getNumber("remediationplan");
		remediationplanid += num;

		updated_rows = remediationDAO.saveRemediationPlan(remediationplanid,
				remediationDetails.getRemediationplanname(), remediationDetails.isIsuserdefined(), remediationDetails.getPlatformname());
		int resourcetypeid = Integer.parseInt(remediationDetails.getServertype());
		updated_rows = remediationDAO.saveResourcetypeRemediationPlanMap(remediationplanid, 
				remediationDetails.getObservabletype(), resourcetypeid);
		
		num = getNumber("remediationplanaction");

		for (int i = 0; i < remediationDetails.getActions().size(); i++) {
			actionid = remediationDAO.saveActions(remediationDetails.getActions().get(i).getActionid(), 
					remediationDetails.getActions().get(i).getActionname(), remediationDetails.getActions().get(i).getCategoryid());
			
			actid = remediationDAO.getActionId(remediationDetails.getActions().get(i).getActionname());
			updated_rows = remediationDAO.saveRemediationPlanActionMap((remediationplanactionid + num),
					remediationplanid, actid,
					remediationDetails.getActions().get(i).getActionstageid(),
					remediationDetails.getActions().get(i).getActionsequence());
			
			for (int j = 0; j < remediationDetails.getActions().get(i).getParameters().size(); j++) {
				if(actionid != 0){
					updated_rows = remediationDAO.saveActionParam(remediationDetails.getActions().get(i).getParameters().get(j).getParamid(), 
							remediationDetails.getActions().get(i).getParameters().get(j).getName(), 
							remediationDetails.getActions().get(i).getParameters().get(j).isIsmandatory(), 
							remediationDetails.getActions().get(i).getParameters().get(j).getDefaultvalue(), 
							actionid);
					
					if(remediationDetails.getActions().get(i).getParameters().get(j).isIsfield()) {
						providedValueType = "fieldname";
					}else {
						providedValueType = "value";
					}
					updated_rows = remediationDAO.saveRemediationPlanActionParamMap((remediationplanactionid + num),
							remediationDetails.getActions().get(i).getParameters().get(j).getParamid(),
							remediationDetails.getActions().get(i).getParameters().get(j).getProvidedvalue(), providedValueType);
				}
				if(remediationDetails.getActions().get(i).getParameters().get(j).isIsfield()) {
					providedValueType = "fieldname";
				}else {
					providedValueType = "value";
				}
				updated_rows = remediationDAO.saveRemediationPlanActionParamMap((remediationplanactionid + num),
						remediationDetails.getActions().get(i).getParameters().get(j).getParamid(),
						remediationDetails.getActions().get(i).getParameters().get(j).getProvidedvalue(), providedValueType);
			}
			num++;
		}
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}
	
	public String saveRemediationPlanDetails(RemediationDetails[] remediationPlan) throws SQLException, JsonProcessingException {
		String result = "{\"res\":\"", remediationplanid = "RP", updateRes = "";;
		int updated_rows = 0, num = 0;
		ObjectMapper mapper = new ObjectMapper();
		
		num = getNumber("remediationplan");
		remediationplanid += num;
		
		updated_rows = remediationDAO.saveRemediationPlanExecution(remediationplanid, mapper.writeValueAsString(remediationPlan));
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}
	
	public String executeRemediationPlan(String scriptActivityDetailsstr) throws AdapterException, ManaAuthException, IOException, JSONException, SQLException {
		int rpstatId = 0;
		String res = "{\"remediationplanrunstatid\":";
		String response = "", remediationPlanId = "";
		ScriptActivity scriptActivityDetails = new ScriptActivity();
		List<ScriptsContentDetails> scriptDetails = new ArrayList<ScriptsContentDetails>();
		JSONObject scriptActivityDetailsJSON = new JSONObject(scriptActivityDetailsstr);
		remediationPlanId = scriptActivityDetailsJSON.getString("remediationplanid");
		scriptActivityDetails.setRemediationplanid(scriptActivityDetailsJSON.getString("remediationplanid"));
		rpstatId = remediationDAO.saveRemediaitonRunStats(remediationPlanId);
		
		JSONArray scriptDetailsArr = new JSONArray(scriptActivityDetailsJSON.get("scriptdetails").toString());
		for(int i=0; i<scriptDetailsArr.length() ; i++) {
			ScriptsContentDetails scriptContentDetails = new ScriptsContentDetails();
			JSONObject scriptIdentifierJson = scriptDetailsArr.getJSONObject(i);
			JSONObject scriptIdentifier = scriptIdentifierJson.getJSONObject("scriptIdentifier");
			ScriptContent scriptContent = new ScriptContent();
			scriptContent.setCategoryId(scriptIdentifier.getInt("CategoryId"));
			scriptContent.setOperation(operation);
			scriptContent.setPassword(niapassword);
			scriptContent.setUserName(niauser);
			scriptContent.setReferenceKey(referenceKey);
			
			List<NodesContent> nodes = new ArrayList<NodesContent>();
			List<String> serviceAreas = new ArrayList<String>();
			NodesContent node = new NodesContent();
			
			String[] serviceAreaList = serviceArea.split(",");
			for(int k=0 ; k<serviceAreaList.length ; k++) {
				serviceAreas.add(serviceAreaList[k]);
			}
			node.setId(nodeid);
			node.setServiceAreas(serviceAreas);
			nodes.add(node);
			
			scriptContent.setNodes(nodes);
			
			List<ScriptParams> parameters = new ArrayList<ScriptParams>();
			JSONArray parameterArr = new JSONArray(scriptIdentifier.getJSONArray("Parameters").toString());
			
			for(int j=0 ; j<parameterArr.length() ; j++) {
				JSONObject parameterJson = parameterArr.getJSONObject(j);
				ScriptParams params = new ScriptParams();
				params.setParameterName(parameterJson.getString("ParameterName"));
				params.setParameterValue(parameterJson.getString("ParameterValue"));
				
				parameters.add(params);
			}
			
			scriptContent.setScriptId(scriptIdentifier.getInt("ScriptId"));
			scriptContent.setParameters(parameters);
			scriptContentDetails.setScriptIdentifier(scriptContent);
			
			scriptDetails.add(scriptContentDetails);
		}
		scriptActivityDetails.setScriptdetails(scriptDetails);
		System.out.println("JSON IS :: " + JSONUtility.javaToJsonString(scriptActivityDetails));
		HttpClient client = new HttpClient();
		for (int i = 0; i < scriptDetails.size(); i++) {
			String finalJson = JSONUtility.javaToJsonString(scriptDetails.get(i));
			finalJson = finalJson.replaceAll("scriptIdentifier", "ScriptIdentifier");
			finalJson = finalJson.replaceAll("categoryId", "CategoryId");
			finalJson = finalJson.replaceAll("nodes", "Nodes");
			finalJson = finalJson.replaceAll("operation", "Operation");
			finalJson = finalJson.replaceAll("parameters", "Parameters");
			finalJson = finalJson.replaceAll("parameterName", "ParameterName");
			finalJson = finalJson.replaceAll("parameterValue", "ParameterValue");
			finalJson = finalJson.replaceAll("userName", "UserName");
			finalJson = finalJson.replaceAll("password", "Password");
			finalJson = finalJson.replaceAll("scriptId", "ScriptId");
			finalJson = finalJson.replaceAll("wemscriptServiceUrl", "WEMScriptServiceUrl");
			finalJson = finalJson.replaceAll("referenceKey", "ReferenceKey");
			finalJson = finalJson.replaceAll("ipAddress", "IPAddress");
			finalJson = finalJson.replaceAll("id", "Id");
			finalJson = finalJson.replaceAll("name", "Name");
			finalJson = finalJson.replaceAll("os", "OS");
			finalJson = finalJson.replaceAll("serviceAreas", "ServiceAreas");
			finalJson = finalJson.replaceAll("stagePath", "StagePath");
			finalJson = finalJson.replaceAll("userId", "UserId");
			finalJson = finalJson.replaceAll("workingDir", "WorkingDir");
			
			System.out.println("JSON to be passed is :: " + finalJson);
			
			//call to the .Net service
			String requestBody = finalJson;
			PostMethod post = new PostMethod(restUrl);
			post.addRequestHeader("Content-Type", "application/json");
						
	        if (requestBody != null) {
	          post.setRequestEntity(new StringRequestEntity(requestBody));
	        }
	        client.executeMethod(post);
	        response = post.getResponseBodyAsString();
	        System.out.println("Response is :: " + response);
			
			
		}
		//start comment
		JSONArray scriptDetailsArr = new JSONArray(scriptActivityDetailsJSON.get("scriptdetails").toString());
		NiaAdapter niaAdapter = new NiaAdapter();
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("mapperSetting", "nia,NIA");
		fieldMap.put("operationEnvirnoment", "NIA");
		
		niaAdapter.init(fieldMap);
		
		for(int i=0; i<scriptDetailsArr.length() ; i++) {
			JSONObject scriptIdentifierJson = scriptDetailsArr.getJSONObject(i);
			JSONObject scriptIdentifier = scriptIdentifierJson.getJSONObject("scriptIdentifier");
			JSONArray parameterArr = new JSONArray(scriptIdentifier.getJSONArray("Parameters").toString());
			
			Map<String, String> paramMap = new HashMap<>();
			
			for(int j=0 ; j<parameterArr.length() ; j++) {
				JSONObject parameterJson = parameterArr.getJSONObject(j);
				paramMap.put(parameterJson.getString("ParameterName"), parameterJson.getString("ParameterValue"));
			}
			
			niaAdapter.executeScript(paramMap);
		}//close internal comment
		
		res += rpstatId + "}";
		remediationDAO.updateRemediaitonRunStats(rpstatId, "SUCCESS", "00:06:15");
		return res;
	}
	
	public String executeRemediationPlan(String scriptActivityDetailsstr) throws AdapterException, ManaAuthException, IOException, JSONException, SQLException {
		String res = "{\"remediationplanexecid\":", rpstatId = "";
		String response = "", remediationPlanId = "";
		ScriptActivity scriptActivityDetails = new ScriptActivity();
		List<ScriptsContentDetails> scriptDetails = new ArrayList<ScriptsContentDetails>();
		JSONObject scriptActivityDetailsJSON = new JSONObject(scriptActivityDetailsstr);
		remediationPlanId = scriptActivityDetailsJSON.getString("remediationplanid");
		scriptActivityDetails.setRemediationplanid(scriptActivityDetailsJSON.getString("remediationplanid"));
		rpstatId = remediationDAO.saveRemediaitonExec(remediationPlanId);
		
		JSONArray scriptDetailsArr = new JSONArray(scriptActivityDetailsJSON.get("scriptdetails").toString());
		for(int i=0; i<scriptDetailsArr.length() ; i++) {
			ScriptsContentDetails scriptContentDetails = new ScriptsContentDetails();
			JSONObject scriptIdentifierJson = scriptDetailsArr.getJSONObject(i);
			JSONObject scriptIdentifier = scriptIdentifierJson.getJSONObject("scriptIdentifier");
			ScriptContent scriptContent = new ScriptContent();
			scriptContent.setCategoryId(scriptIdentifier.getInt("CategoryId"));
			scriptContent.setOperation(operation);
			scriptContent.setPassword(niapassword);
			scriptContent.setUserName(niauser);
			scriptContent.setReferenceKey(referenceKey);
			
			List<NodesContent> nodes = new ArrayList<NodesContent>();
			List<String> serviceAreas = new ArrayList<String>();
			NodesContent node = new NodesContent();
			
			String[] serviceAreaList = serviceArea.split(",");
			for(int k=0 ; k<serviceAreaList.length ; k++) {
				serviceAreas.add(serviceAreaList[k]);
			}
			node.setId(nodeid);
			node.setServiceAreas(serviceAreas);
			nodes.add(node);
			
			scriptContent.setNodes(nodes);
			
			List<ScriptParams> parameters = new ArrayList<ScriptParams>();
			JSONArray parameterArr = new JSONArray(scriptIdentifier.getJSONArray("Parameters").toString());
			
			for(int j=0 ; j<parameterArr.length() ; j++) {
				JSONObject parameterJson = parameterArr.getJSONObject(j);
				ScriptParams params = new ScriptParams();
				params.setParameterName(parameterJson.getString("ParameterName"));
				params.setParameterValue(parameterJson.getString("ParameterValue"));
				
				parameters.add(params);
			}
			
			scriptContent.setScriptId(scriptIdentifier.getInt("ScriptId"));
			scriptContent.setParameters(parameters);
			scriptContentDetails.setScriptIdentifier(scriptContent);
			
			scriptDetails.add(scriptContentDetails);
		}
		scriptActivityDetails.setScriptdetails(scriptDetails);
		System.out.println("JSON IS :: " + JSONUtility.javaToJsonString(scriptActivityDetails));
		HttpClient client = new HttpClient();
		for (int i = 0; i < scriptDetails.size(); i++) {
			String finalJson = JSONUtility.javaToJsonString(scriptDetails.get(i));
			finalJson = finalJson.replaceAll("scriptIdentifier", "ScriptIdentifier");
			finalJson = finalJson.replaceAll("categoryId", "CategoryId");
			finalJson = finalJson.replaceAll("nodes", "Nodes");
			finalJson = finalJson.replaceAll("operation", "Operation");
			finalJson = finalJson.replaceAll("parameters", "Parameters");
			finalJson = finalJson.replaceAll("parameterName", "ParameterName");
			finalJson = finalJson.replaceAll("parameterValue", "ParameterValue");
			finalJson = finalJson.replaceAll("userName", "UserName");
			finalJson = finalJson.replaceAll("password", "Password");
			finalJson = finalJson.replaceAll("scriptId", "ScriptId");
			finalJson = finalJson.replaceAll("wemscriptServiceUrl", "WEMScriptServiceUrl");
			finalJson = finalJson.replaceAll("referenceKey", "ReferenceKey");
			finalJson = finalJson.replaceAll("ipAddress", "IPAddress");
			finalJson = finalJson.replaceAll("id", "Id");
			finalJson = finalJson.replaceAll("name", "Name");
			finalJson = finalJson.replaceAll("os", "OS");
			finalJson = finalJson.replaceAll("serviceAreas", "ServiceAreas");
			finalJson = finalJson.replaceAll("stagePath", "StagePath");
			finalJson = finalJson.replaceAll("userId", "UserId");
			finalJson = finalJson.replaceAll("workingDir", "WorkingDir");
			
			System.out.println("JSON to be passed is :: " + finalJson);
			
			//call to the .Net service
			String requestBody = finalJson;
			PostMethod post = new PostMethod(restUrl);
			post.addRequestHeader("Content-Type", "application/json");
						
	        if (requestBody != null) {
	          post.setRequestEntity(new StringRequestEntity(requestBody));
	        }
	        client.executeMethod(post);
	        response = post.getResponseBodyAsString();
	        System.out.println("Response is :: " + response);
			
			
		}
		
		res += rpstatId + "}";
		remediationDAO.updateRemediaitonExec(rpstatId, "SUCCESS");
		return res;
	}
	
	public String executeRemediationPlan(String scriptActivityDetailsstr) throws AdapterException, ManaAuthException, IOException, JSONException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		String res = "{\"queued\":\"", rpstatId = "", remediationPlanId = "";
		ObjectMapper mapper = new ObjectMapper();
		
		ScriptActivity scriptActivityDetails = new ScriptActivity();
		List<ScriptsContentDetails> scriptDetails = new ArrayList<ScriptsContentDetails>();
		JSONObject scriptActivityDetailsJSON = new JSONObject(scriptActivityDetailsstr);
		remediationPlanId = scriptActivityDetailsJSON.getString("remediationplanid");
		scriptActivityDetails.setRemediationplanid(scriptActivityDetailsJSON.getString("remediationplanid"));
		
		JSONArray scriptDetailsArr = new JSONArray(scriptActivityDetailsJSON.get("scriptdetails").toString());
		for(int i=0; i<scriptDetailsArr.length() ; i++) {
			ScriptsContentDetails scriptContentDetails = new ScriptsContentDetails();
			JSONObject scriptIdentifierJson = scriptDetailsArr.getJSONObject(i);
			JSONObject scriptIdentifier = scriptIdentifierJson.getJSONObject("scriptIdentifier");
			// Encrypt the password
			String password = scriptIdentifier.getString("Password");
			String encryptedPwd = new String(encrypt(password.getBytes()));
			
			ScriptContent scriptContent = new ScriptContent();
			scriptContent.setCategoryId(scriptIdentifier.getInt("CategoryId"));
			scriptContent.setOperation(operation);
			scriptContent.setPassword(encryptedPwd);
			scriptContent.setUserName(niauser);
			scriptContent.setReferenceKey(referenceKey);
			
			List<NodesContent> nodes = new ArrayList<NodesContent>();
			List<String> serviceAreas = new ArrayList<String>();
			NodesContent node = new NodesContent();
			
			String[] serviceAreaList = serviceArea.split(",");
			for(int k=0 ; k<serviceAreaList.length ; k++) {
				serviceAreas.add(serviceAreaList[k]);
			}
			node.setId(nodeid);
			node.setServiceAreas(serviceAreas);
			nodes.add(node);
			
			scriptContent.setNodes(nodes);
			
			List<ScriptParams> parameters = new ArrayList<ScriptParams>();
			JSONArray parameterArr = new JSONArray(scriptIdentifier.getJSONArray("Parameters").toString());
			
			for(int j=0 ; j<parameterArr.length() ; j++) {
				JSONObject parameterJson = parameterArr.getJSONObject(j);
				ScriptParams params = new ScriptParams();
				params.setParameterName(parameterJson.getString("ParameterName"));
				params.setParameterValue(parameterJson.getString("ParameterValue"));
				
				parameters.add(params);
			}
			
			scriptContent.setScriptId(scriptIdentifier.getInt("ScriptId"));
			scriptContent.setParameters(parameters);
			scriptContentDetails.setScriptIdentifier(scriptContent);
			
			scriptDetails.add(scriptContentDetails);
		}
		scriptActivityDetails.setScriptdetails(scriptDetails);
		
		try {
			rpstatId = remediationDAO.saveRemediationPlanExecution(remediationPlanId,mapper.writeValueAsString(scriptActivityDetails));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			rpstatId = "false";
		}finally {
			res += rpstatId + "\"}";
		}
		
		return res;
	}
	
	public String executeRemediationPlan(RemediationPlanExecution remediationPlanExecution)  {
		String res = "{\"queued\":\"", resState = "";
		try {
			resState = remediationDAO.saveRemediationPlanExecution(remediationPlanExecution);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resState = "false";
		}finally {
			res += resState + "\"}";
		}
		
		return res;
	}
	
	public static byte[] encrypt(byte[] textToEncrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		// Create and initialize the encryption engine
		String keyString = "SW5mb3N5czE=";  //Encryption Key String
		SecretKey key = generateKey(keyString);
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// Perform an encryption 
		byte[] cipherText = cipher.doFinal(textToEncrypt);
		return cipherText;
	}
	
	public String test(String pwd) {//throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		String encryptedPwd = new String(encrypt(pwd.getBytes()));
		System.out.println("Encrypted Pwd :: " + encryptedPwd);
		String result = "{ \"scriptExecuteResponse\":[{ \"ComputerName\":\"String content\", \"CurrentState\":\"String content\", \"ErrorMessage\":\"String content\", \"InputCommand\":\"String content\", \"IsNotified\":true, \"IsSuccess\":true, \"LogData\":\"String content\", \"NotificationRemarks\":\"String content\", \"OutParameters\":[{ \"ParameterName\":\"String content\", \"ParameterValue\":\"String content\" }], \"SourceTransactionId\":\"String content\", \"Status\":\"String content\", \"SuccessMessage\":\"String content\", \"TransactionId\":\"1627aea5-8e0a-4371-9022-9b504344e724\" }] }";
		return result;
	}
	
	public static void main(String args[]) {
		String pwd = "$4Iip12$";
		String encryptedPwd;
		try {
			encryptedPwd = new String(encrypt(pwd.getBytes()));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Encrypted Pwd :: " + encryptedPwd);
	}
	
	private static SecretKey generateKey(String keyString) {
		byte[] keyB = new byte[24];
		
		for (int i = 0; i < keyString.length() && i < keyB.length; i++) {
			keyB[i] = (byte) keyString.charAt(i);
		}
		SecretKey key = new SecretKeySpec(keyB, "DESede");
		return key;
	}

	public List<RemediationPlanStatus> getRemediationPlanStat(String userid) {
		List<RemediationPlanStatus> remediationPlanStatusList = new ArrayList<RemediationPlanStatus>();
		List<RemediationExecStatus> result = new ArrayList<RemediationExecStatus>();
		RemediationPlanStatus remediationPlanStatus = new RemediationPlanStatus();
		
		result = remediationDAO.remediationPlanStatus(userid);
		remediationPlanStatus.setStatus(result);
		remediationPlanStatusList.add(remediationPlanStatus);
		
		return remediationPlanStatusList;
	}
	
	public String updateRemediationPlanStat(String rpeId, String status) throws SQLException {
		String result = "{\"updatedrows\":";
		int updated_rows = remediationDAO.updateRemediaitonExec(rpeId, status);
		result += updated_rows + "}";
		return result;
	}
	
	public String getRemediationPlan() {
		String result = "";
		result = remediationDAO.remediationPlanDetails();
		return result;
	}
	
	public List<ScriptIdentifier> getRemediationPlan(String statusJSON) throws JSONException, ParseException {
		List<ScriptIdentifier> result = new ArrayList<ScriptIdentifier>();
		String rpexecid = "";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		rpexecid = statusDetailsJSON.getString("rpexecid");
		result = remediationDAO.remediationPlanDetails(rpexecid);
		return result;
	}
	
	public String getRemediationPlanHistory(String statusJSON) throws JSONException, ParseException {
		String result = "", createdate = "", resourcetype="", obsid="", resid="";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		int limit = 1;
		resourcetype = statusDetailsJSON.getString("resourcetype");
		createdate = statusDetailsJSON.getString("createdate");
		limit = statusDetailsJSON.getInt("limit");
		obsid = statusDetailsJSON.getString("obsid");
		resid = statusDetailsJSON.getString("resid");
		result = remediationDAO.remediationPlanHistoryDetails(resourcetype, createdate, limit, obsid, resid);
		return result;
	}
	
	public List<RemediationPlanAction> getRemediationPlanActions(String remediationplanid) throws SQLException {
		List<RemediationPlanAction> remediationActions = new ArrayList<RemediationPlanAction>();
		remediationActions = remediationDAO.getRemediationPlanActions(remediationplanid);
		return remediationActions;
	}
	
	public String retryRemediationPlan(String retryJSON) throws JSONException, ParseException {
		String res = "{\"queued\":\"", resState = "", planid = "", username = "";
		JSONObject retryDetailsJSON = new JSONObject(retryJSON);
		
		planid = retryDetailsJSON.getString("planid");
		username = retryDetailsJSON.getString("username");
		
		try {
			resState = remediationDAO.retryRemediationPlan(planid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			resState = "false";
		}finally {
			res += resState + "\"}";
		}
		return res;
	}
	
	public String getRemediationPlanDetails(String rpexecid) throws SQLException {
		String actionStages = "";
		actionStages = remediationDAO.getRemediationPlan(rpexecid);
		return actionStages;
	}
	
	public String updateCorrelationId(String statusJSON) throws JSONException, ParseException, SQLException {
		String result = "{\"res\":\"", updateRes = "", status = "", correlationid="", rpexecid="", rpactionid="";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		int updated_rows = 0;
		
		correlationid = statusDetailsJSON.getString("corelationid");
		rpexecid = statusDetailsJSON.getString("rpexecid");
		rpactionid = statusDetailsJSON.getString("rpactionid");
		status = statusDetailsJSON.getString("status");
		
		updated_rows = remediationDAO.updaterpActionsCorrelationId(rpexecid, rpactionid, correlationid, status);
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}
	
	public String updateStatusWithCorelationId(String statusJSON) throws JSONException, ParseException, SQLException {
		String result = "{\"res\":\"", updateRes = "", status = "", correlationid="", rpexecid="", rpstatus="";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		int updated_rows = 0;
		
		correlationid = statusDetailsJSON.getString("corelationid");
		status = statusDetailsJSON.getString("status");
		rpexecid = statusDetailsJSON.getString("rpexecid");
		rpstatus = statusDetailsJSON.getString("rpstatus");
		
		updated_rows = remediationDAO.updateStatusCorrelationId(correlationid, status, rpexecid, rpstatus);
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}

	public String updateStatusWithRpeId(String statusJSON) throws JSONException, ParseException, SQLException {
		// TODO Auto-generated method stub
		String result = "{\"res\":\"", updateRes = "", rpactionid="", rpexecid="", rpstatus="", rpactionStatus = "";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		int updated_rows = 0;
		
		rpexecid = statusDetailsJSON.getString("rpexecid");
		rpstatus = statusDetailsJSON.getString("rpstatus");
		
		JSONArray rpActionArr = new JSONArray(statusDetailsJSON.get("rpaction").toString());
		
		for(int i=0 ; i<rpActionArr.length() ; i++) {
			JSONObject rpAction = rpActionArr.getJSONObject(i);
			rpactionid = rpAction.getString("rpactionid");
			rpactionStatus = rpAction.getString("rpactionstatus");
			
			updated_rows = remediationDAO.updateStatusRpeId(rpactionid, rpactionStatus, rpexecid);
		}
		
		updated_rows = remediationDAO.updateStatusRpe(rpexecid, rpstatus);
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}

	public String updateStatusLog(String statusJSON) throws JSONException, ParseException, SQLException {
		// TODO Auto-generated method stub
		String result = "{\"res\":\"", updateRes = "", corelationid="", log="", output="", status = "";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		int updated_rows = 0;
		
		corelationid = statusDetailsJSON.getString("corelationid");
		log = statusDetailsJSON.getString("log");
		output = statusDetailsJSON.getString("output");
		status = statusDetailsJSON.getString("status");
		
		updated_rows = remediationDAO.updateStatusLogs(corelationid, log, output, status);
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}
	
	public String updateLog(String statusJSON) throws JSONException, ParseException, SQLException {
		// TODO Auto-generated method stub
		String result = "{\"res\":\"", updateRes = "", corelationid="", log="", output="", status = "";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		int updated_rows = 0;
		
		corelationid = statusDetailsJSON.getString("corelationid");
		log = statusDetailsJSON.getString("log");
		output = statusDetailsJSON.getString("output");
		status = statusDetailsJSON.getString("status");
		
		updated_rows = remediationDAO.updateLogs(corelationid, log, output, status);
		
		if (updated_rows > 0) {
			updateRes = "Updated " + updated_rows + " rows";
		}else {
			updateRes = "No rows updated.";
		}

		result += updateRes + "\"}";
		return result;
	}

	public String getExecId() {
		// TODO Auto-generated method stub
		String result = "{\"rpexecid\":\"", rpexecid = "";
		
		rpexecid = remediationDAO.getRemediationPlanExecId();
		
		result += rpexecid + "\"}";
		return result;
	}
	
	public String updateCorelation(String statusJSON) throws JSONException, ParseException {
		// TODO Auto-generated method stub
		String result = "{\"status\":\"", updateRes = "", transactionid="", status = "", topicInput = "";
		JSONObject statusDetailsJSON = new JSONObject(statusJSON);
		
		transactionid = statusDetailsJSON.getString("transactionid");
		status = statusDetailsJSON.getString("status");
		
		topicInput = transactionid.concat(",").concat(status);
		System.out.println("Topic input is :: " + topicInput);
		
		try {
			remediationDAO.inserttoKafka(topic_itcc_execution_notifications, topicInput);
			updateRes = "SUCCESS\",\"remarks\":\"Inserted to kafka";
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			updateRes = "FAIL\",\"remarks\":\"" + e.getMessage();
		}
		
		result += updateRes + "\"}";
		return result;
	}
	
	public String updateObservationsValue(int Value){
		String result="";
		int updatedRows=remediationDAO.updateObservationsValues(Value);
		if(updatedRows!=0){
			result="{\"Satus\":\"Success\"}";
		}
		else
			result="{\"Satus\":\"Failed\"}";
		return result;
	}
}
*/