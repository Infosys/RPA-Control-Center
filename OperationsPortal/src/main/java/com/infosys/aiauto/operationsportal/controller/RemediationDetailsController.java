/* 
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
   
package com.infosys.aiauto.operationsportal.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.aiauto.operationsportal.dto.ActionStages;
import com.infosys.aiauto.operationsportal.dto.Remediation;
import com.infosys.aiauto.operationsportal.dto.RemediationDetails;
import com.infosys.aiauto.operationsportal.dto.RemediationPlan;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanAction;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanExecution;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanStatus;
import com.infosys.aiauto.operationsportal.dto.ScriptIdentifier;
import com.infosys.aiauto.operationsportal.facade.RemediationFacade;

@RestController
@CrossOrigin
@RequestMapping(value = "/remediation", produces = MediaType.APPLICATION_JSON_VALUE)
public class RemediationDetailsController {
	
	private static Logger LOG = LoggerFactory.getLogger(RemediationDetailsController.class);
	
	@Autowired
	private RemediationFacade remediationFacade;
	
	@RequestMapping(method = RequestMethod.GET, value = "/remediation-details/{resourcetype}/{obsid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Remediation>> getRemediationAction(@PathVariable("resourcetype") String resourcetype,@PathVariable("obsid") String obsid){
		List<Remediation> allRemediationActions = new ArrayList<Remediation>();
		try{
			allRemediationActions = remediationFacade.getRemediationActions(resourcetype,obsid);
			return new ResponseEntity<List<Remediation>>(allRemediationActions, HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<Remediation>>(allRemediationActions, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/remediation-action/stages", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ActionStages>> getActionStages(){
		List<ActionStages> allActionStages = new ArrayList<ActionStages>();
		try{
			allActionStages = remediationFacade.getActionStages();
			return new ResponseEntity<List<ActionStages>>(allActionStages, HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<ActionStages>>(allActionStages, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/remediationplan_details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRemediationActionDetails(@RequestBody RemediationPlan remediationPlan){
		String remediationPlanDetailsJson = "";
		try{
			remediationPlanDetailsJson = remediationFacade.getRemediationActionDetails(remediationPlan);
			LOG.info("Successfully completed.");
			return new ResponseEntity<String>(remediationPlanDetailsJson, HttpStatus.OK);
			
		} catch (Exception e) {
			remediationPlanDetailsJson = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(remediationPlanDetailsJson, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/remediation-actions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRemediationActionsDetails(){
		String allActionStages = "";
		try{
			allActionStages = remediationFacade.getRemediationActionsDetails();
			return new ResponseEntity<String>(allActionStages, HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(allActionStages, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/save_remediationplan_details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> putRemediationActionDetails(@RequestBody RemediationDetails[] remediationDetails){
		String resultValue = "";
		try{
			resultValue = remediationFacade.saveRemediationPlan(remediationDetails[0]);
			LOG.info("Successfully inserted.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/test_save", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveRemediationActionDetails(@RequestBody String remediationDetails){
		String resultValue = "";
		
		try{
//			resultValue = remediationFacade.saveRemediationPlanDetails(remediationDetails);
			resultValue = remediationFacade.test(remediationDetails);
			LOG.info("Successfully inserted.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/queue_plan", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> executeRemediationPlanStatistics(@RequestBody RemediationPlanExecution remediationPlanExecution){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.executeRemediationPlan(remediationPlanExecution);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	@RequestMapping(method = RequestMethod.GET, value = "/remediationplan/status/{userid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RemediationPlanStatus>> getRemediationPlanStatisticsStatus(@PathVariable("userid") String userid){
		List<RemediationPlanStatus> resultValue = new ArrayList<RemediationPlanStatus>();
		
		try{
			resultValue = remediationFacade.getRemediationPlanStat(userid);
			return new ResponseEntity<List<RemediationPlanStatus>>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<RemediationPlanStatus>>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/update/{rpeid}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateRemediationPlanStatistics(@PathVariable("rpeid") String rpeid, @PathVariable("status") String status){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateRemediationPlanStat(rpeid, status);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/remediation_plan_retrieve", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRemediationPlanExecution(){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.getRemediationPlan();
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/remediation_plan_retrieve", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ScriptIdentifier>> getRemediationPlanExecution(@RequestBody String statusJSON){
		List<ScriptIdentifier> resultValue = new ArrayList<ScriptIdentifier>();
		
		try{
			resultValue = remediationFacade.getRemediationPlan(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<List<ScriptIdentifier>>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<ScriptIdentifier>>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/status/executionhistory", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRemediationPlanExecutionHistory(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.getRemediationPlanHistory(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/remediation-history-details/{remediationplanid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RemediationPlanAction>> getRemediationPlanAction(@PathVariable("remediationplanid") String remediationplanid){
		List<RemediationPlanAction> allRemediationActions = new ArrayList<RemediationPlanAction>();
		try{
			allRemediationActions = remediationFacade.getRemediationPlanActions(remediationplanid);
			return new ResponseEntity<List<RemediationPlanAction>>(allRemediationActions, HttpStatus.OK);
			
		} catch (Exception e) {
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<List<RemediationPlanAction>>(allRemediationActions, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/remediation-plan/retry", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> retryRemediationPlan(@RequestBody String retryJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.retryRemediationPlan(retryJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/remediationplan_details/{rpexecid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRemediationPlanDetails(@PathVariable("rpexecid") String rpexecid){
		String remediationPlanDetailsJson = "";
		try{
			remediationPlanDetailsJson = remediationFacade.getRemediationPlanDetails(rpexecid);
			LOG.info("Successfully completed.");
			return new ResponseEntity<String>(remediationPlanDetailsJson, HttpStatus.OK);
			
		} catch (Exception e) {
			remediationPlanDetailsJson = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(remediationPlanDetailsJson, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/update/corelationid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateCorrelationId(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateCorrelationId(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/update/withcorelid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateStatuswithCorrelationId(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateStatusWithCorelationId(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/update/withrpeid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateStatuswithRpeId(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateStatusWithRpeId(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/update/statuslog", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateStatusLog(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateStatusLog(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/update/log", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateLogs(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateLog(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/remediation-plan-execid", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getRemediationPlanExecId(){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.getExecId();
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/transactionid_status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateCorelationStatus(@RequestBody String statusJSON){
		String resultValue = "";
		
		try{
			resultValue = remediationFacade.updateCorelation(statusJSON);
			LOG.info("Successfully updated.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
			
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(method = RequestMethod.POST, value = "/iap_mockservice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> TestMock(@RequestBody String remediationDetails){
		String resultValue = "";
		try{
			resultValue = "{ \"ScriptResponse\":[{ \"ComputerName\":\"10.170.120.70\", \"ErrorMessage\":\"null\", \"InputCommand\":\"String content\", \"IsSuccess\":true, \"LogData\":\"Data logged\", \"OutParameters\":[{ \"ParameterName\":\"String content\", \"ParameterValue\":\"String content\" }], \"SourceTransactionId\":\"String content\", \"Status\":\"SUCCESS\", \"SuccessMessage\":\"Script executed successfully\", \"TransactionId\":\"12wsdesa-15411wc-1545fs\" }] }";
			LOG.info("Successfully inserted.");
			return new ResponseEntity<String>(resultValue, HttpStatus.OK);
		} catch (Exception e) {
			resultValue = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(resultValue, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/update_observatios_value/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> UpdateObservationValues(@PathVariable("value") int value){
		String observationUpdateDetailsJson = "";
		try{
			//remediationPlanDetailsJson = remediationFacade.getRemediationPlanDetails(rpexecid);
			observationUpdateDetailsJson=remediationFacade.updateObservationsValue(value);
			LOG.info("Successfully completed.");
			return new ResponseEntity<String>(observationUpdateDetailsJson, HttpStatus.OK);
			
		} catch (Exception e) {
			observationUpdateDetailsJson = "Error occured, please check the log files for more details.";
			LOG.error("Error while retreiving the data. Please check the log files");
			return new ResponseEntity<String>(observationUpdateDetailsJson, new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
*/