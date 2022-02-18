/* 
  * Copyright 2021 Infosys Ltd. 
Use of this source code is governed by MIT license that can be found in the LICENSE file or at 
https://opensource.org/licenses/MIT
   

package com.infosys.aiauto.operationsportal.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.uuid.Generators;
import com.infosys.aiauto.operationsportal.dto.ActionStages;
import com.infosys.aiauto.operationsportal.dto.ParameterDTO;
import com.infosys.aiauto.operationsportal.dto.Remediation;
import com.infosys.aiauto.operationsportal.dto.RemediationExecStatus;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanAction;
import com.infosys.aiauto.operationsportal.dto.RemediationPlanExecution;
import com.infosys.aiauto.operationsportal.dto.ScriptIdentifier;
import com.infosys.aiauto.operationsportal.dto.ScriptIdentifierDTO;
import com.infosys.aiauto.operationsportal.dto.ScriptsDetails;
import com.infosys.aiauto.operationsportal.facade.RemediationFacade;

@Component
public class RemediationDAO {

	@Autowired
	private DataSource datasource;

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void setNamedParameterJdbcTemplate(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	private Properties kafkaProperties;

	public Properties getKafkaProperties() {
		return kafkaProperties;
	}

	public void setKafkaProperties(Properties kafkaProperties) {
		this.kafkaProperties = kafkaProperties;
	}

	@Value("${remediationActionQuery}")
	private String remediationActionQuery;

	@Value("${actionStageQuery}")
	private String actionStageQuery;

	@Value("${remediationPlanQuery}")
	private String remediationPlanQuery;

	@Value("${remediationActionsQuery}")
	private String remediationActionsQuery;

	@Value("${remdiationPlanLastQuery}")
	private String remdiationPlanLastQuery;

	@Value("${remediationPlanActionMapLastQuery}")
	private String remediationPlanActionMapLastQuery;

	@Value("${remediationPlanStatusQuery}")
	private String remediationPlanStatusQuery;

	@Value("${remediationPlanStatIdQuery}")
	private String remediationPlanStatIdQuery;

	@Value("${remediationplandetailsQuery}")
	private String remediationplandetailsQuery;

	@Value("${remediationplanRetrieveQuery}")
	private String remediationplanRetrieveQuery;

	@Value("${actionQuery}")
	private String actionQuery;

	@Value("${historyQuery}")
	private String historyQuery;

	@Value("${historyLimitQuery}")
	private String historyLimitQuery;

	@Value("${remediationPlanActionMapQuery}")
	private String remediationPlanActionMapQuery;

	@Value("${remediaitonPlanActionHistoryQuery}")
	private String remediaitonPlanActionHistoryQuery;

	@Value("${topic_itcc_orch_exelist}")
	private String topic_itcc_orch_exelist;

	@Value("${bootstrap_servers}")
	private String bootstrap_servers;

	@Value("${remediationPlanExecidQuery}")
	private String remediationPlanExecidQuery;

	@Value("${remediationPlanExecActionidQuery}")
	private String remediationPlanExecActionidQuery;

	@Value("${remediationPlanwithexecidQuery}")
	private String remediationPlanwithexecidQuery;

	@Value("${actionIdQuery}")
	private String actionIdQuery;

	@Value("${executionModeQuery}")
	private String executionModeQuery;

	@Value("${remediationPlanActionidQuery}")
	private String remediationPlanActionidQuery;

	@Value("${parametersQuery}")
	private String parametersQuery;

	@Value("${remediationPlanExecQuery}")
	private String remediationPlanExecQuery;

	@Value("${niauser}")
	private String niauser;

	@Value("${niapassword}")
	private String niapassword;

	@Value("${companyid}")
	private int companyid;

	@Value("${responsenotificationcallbackurl}")
	private String responsenotificationcallbackurl;

	@Value("${remediationPlanExecIdQuery}")
	private String remediationPlanExecIdQuery;

	@Value("${remediationPlanNameQuery}")
	private String remediationPlanNameQuery;

	
	 * @Value("${remediationActionCountQuery}") private String
	 * remediationActionCountQuery;
	 

	public List<Remediation> getRemediationActions(String resourcetype, String obsid) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Remediation> list = template.query(remediationActionQuery, new Object[] { resourcetype, obsid },
				new RowMapper<Remediation>() {
					public Remediation mapRow(ResultSet rs, int rownumber) throws SQLException {
						Remediation resource = new Remediation();
						resource.setRemediationPlanid(rs.getString("remediationplanid"));
						resource.setRemediationPlanName(rs.getString("remediationplanname"));
						resource.setStatus(rs.getString("status"));
						resource.setDateCreated(rs.getDate("createdate").toString());
						resource.setLastRunDateTime(rs.getDate("rundatetime").toString());
						resource.setLastRunBy(rs.getString("runby"));
						resource.setRunDuration(rs.getTime("runduration").toString());
						resource.setUserDefined(rs.getBoolean("isuserdefined"));
						return resource;
					}
				});
		return list;
	}

	public List<ActionStages> getActionStages() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<ActionStages> list = template.query(actionStageQuery, new Object[] {}, new RowMapper<ActionStages>() {
			public ActionStages mapRow(ResultSet rs, int rownumber) throws SQLException {
				ActionStages resource = new ActionStages();
				resource.setActionStageId(rs.getString("actionstageid"));
				resource.setActionStageName(rs.getString("actionstagename"));
				return resource;
			}
		});
		return list;
	}

	public String getRemediationAction(String remediationplanid) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(remediationPlanQuery, new Object[] { remediationplanid },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource = "";
						resource = rs.getString("outputjson");
						return resource;
					}
				});
		return list.get(0);
	}

	public String getRemediationAction() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(remediationActionsQuery, new Object[] {}, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("outputjson");
				return resource;
			}
		});
		return list.get(0);
	}

	public int saveRemediationPlan(String remediationplanid, String remediationplanname, boolean isuserdefined,
			String platformname) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String insertQuery = "INSERT INTO remediation_plan values('" + remediationplanid + "','" + remediationplanname
				+ "','" + remediationplanname + "','" + isuserdefined
				+ "','IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0),'" + platformname + "');";
		rows_updated = template.update(insertQuery);

		return rows_updated;
	}

	public int saveActions(int scriptid, String actionname, int categoryid) throws SQLException, JSONException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		int action = 0;

		action = getActions(actionname);

		if (action == 0) {
			JSONObject actiondetails = new JSONObject();
			actiondetails.put("scriptid", new Integer(scriptid));
			actiondetails.put("categoryid", new Integer(categoryid));
			String insertQuery = "INSERT INTO action (actionname, actiontypeid, createdby, createdate, modifiedby, modifieddate, validitystart, validityend, actiondetails) values('"
					+ actionname
					+ "',2,'test_user4',CURRENT_DATE,'iapadmin',LOCALTIMESTAMP(0),LOCALTIME(0),LOCALTIME(0),'"
					+ actiondetails.toString() + "');";
			rows_updated = template.update(insertQuery);
			rows_updated = getActionId(actionname);
		} else {
			rows_updated = 0;
		}

		return rows_updated;
	}

	public int getActionId(String actionname) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Integer> list = template.query(actionIdQuery, new Object[] { actionname }, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
				int resource = -1;
				resource = rs.getInt("actionid");
				return resource;
			}
		});

		return list.get(0);
	}

	public int getActions(String actionname) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<Integer> list = template.query(actionQuery, new Object[] { actionname }, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
				int resource = -1;
				resource = rs.getInt("total");
				return resource;
			}
		});

		return list.get(0);
	}

	public int saveResourcetypeRemediationPlanMap(String remediationplanid, String observableid, int resourcetypeid)
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String insertQuery = "INSERT INTO resourcetype_observable_remediation_plan_map values('" + resourcetypeid
				+ "','" + observableid + "','" + remediationplanid
				+ "','IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0));";
		rows_updated = template.update(insertQuery);

		return rows_updated;
	}

	public int saveRemediaitonRunStats(String remediationplanid) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0, rpstatId = 0;
		String insertQuery = "INSERT INTO remediation_plan_run_statistics(remediationplanid, runby, rundatetime,status,runduration,createdby, createdate, modifiedby,modifieddate) values('"
				+ remediationplanid
				+ "','test_user4',CURRENT_DATE,'QUEUED','0:00:00','IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0));";
		rows_updated = template.update(insertQuery);

		if (rows_updated > 0) {
			List<Integer> list = template.query(remediationPlanStatIdQuery, new Object[] { remediationplanid },
					new RowMapper<Integer>() {
						public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
							Integer resource = 0;
							resource = rs.getInt("remediationplanstatid");
							return resource;
						}
					});
			rpstatId = list.get(0);
		}

		return rpstatId;
	}

	public int updateRemediaitonRunStats(int remediationplanstatid, String status, String duration)
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_run_statistics set status = '" + status + "', runduration = '"
				+ duration + "' where remediationplanstatid = '" + remediationplanstatid + "';";
		rows_updated = template.update(updateQuery);

		return rows_updated;
	}

	public String getLastRemediationPlan() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		String remediationPlanCountQuery = "select count(*) from remediation_plan";
		List<Integer> list1 = template.query(remediationPlanCountQuery, new Object[] {}, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
				Integer resource = 0;
				resource = rs.getInt("count");
				return resource;
			}
		});

		int count = list1.get(0);
		if (count > 0) {
			
			 * List<String> list = template.query(remdiationPlanLastQuery, new
			 * Object[] {}, new RowMapper<String>() { public String
			 * mapRow(ResultSet rs, int rownumber) throws SQLException { String
			 * resource = ""; resource = rs.getString("remediationplanid");
			 * return resource; } });
			 
			// return list.get(0);
			return ("RP" + count);
		} else {
			return "RP0";
		}
	}

	public String getLastRemediationPlanAction() throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		String remediationPlanActionCountQuery = "select count(*) from remediation_plan_action_map";
		List<Integer> list1 = template.query(remediationPlanActionCountQuery, new Object[] {},
				new RowMapper<Integer>() {
					public Integer mapRow(ResultSet rs, int rownumber) throws SQLException {
						Integer resource = 0;
						resource = rs.getInt("count");
						return resource;
					}
				});

		int count = list1.get(0);
		if (count > 0) {
			
			 * List<String> list =
			 * template.query(remediationPlanActionMapLastQuery, new Object[]
			 * {}, new RowMapper<String>() { public String mapRow(ResultSet rs,
			 * int rownumber) throws SQLException { String resource = "";
			 * resource = rs.getString("remediationplanactionid"); return
			 * resource; } });
			 
			// return list.get(0);
			return ("RPACT" + count);
		} else {
			return "RPACT0";
		}
	}

	public int saveRemediationPlanActionMap(String remediationplanactionid, String remediationplanid, int actionid,
			String actionstageid, int actionsequence) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String insertQuery = "INSERT INTO  remediation_plan_action_map values('" + remediationplanactionid + "','"
				+ remediationplanid + "'," + actionid + ",'" + actionstageid + "'," + actionsequence
				+ ",'IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0));";
		rows_updated = template.update(insertQuery);

		return rows_updated;
	}

	public int saveRemediationPlanActionParamMap(String remediationplanactionid, int paramid, String providedvalue,
			String providedValueType) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String insertQuery = "INSERT INTO remdiation_plan_action_param_map values('" + remediationplanactionid + "',"
				+ paramid + ",'" + providedvalue + "','" + providedValueType
				+ "','IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0));";

		rows_updated = template.update(insertQuery);

		return rows_updated;
	}

	
	 * public String saveRemediationPlanExecution(String remediationplanid,
	 * String remediationplan) throws SQLException { Random rand = new Random();
	 * JdbcTemplate template = new JdbcTemplate(datasource); // int rows_updated
	 * = 0; // String rpstatId = "";
	 * 
	 * String insertQuery =
	 * "INSERT INTO remediation_plan_executions (remediationplanid, plandetails, executedby, executionstartdatetime, executionenddatetime, status, createdby, createdate, modifiedby, modifieddate, notificaitonflag) values('"
	 * + remediationplanid + "','" + remediationplan +
	 * "','test_user4',LOCALTIMESTAMP(0)," + "LOCALTIMESTAMP(0)+("+
	 * rand.nextInt(10) + " * interval '1 minute')" +
	 * ",'QUEUED','IIPADMIN',LOCALTIMESTAMP(0),'test_user4',LOCALTIMESTAMP(0),false);";
	 * // rows_updated = template.update(insertQuery);
	 * template.update(insertQuery);
	 * 
	 * if (rows_updated > 0) { List<String> list =
	 * template.query(remediationplandetailsQuery, new Object[] {
	 * remediationplanid }, new RowMapper<String>() { public String
	 * mapRow(ResultSet rs, int rownumber) throws SQLException { String resource
	 * = ""; resource = rs.getString("remediationplanexecid"); return resource;
	 * } }); rpstatId = list.get(0); }
	 * 
	 * return "true"; }
	 

	public String saveRemediationPlanExecution(RemediationPlanExecution remediationPlanExecution) throws Exception {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		Random rand = new Random();
		UUID remediationplanexecid = Generators.randomBasedGenerator().generate();
//		String encryptedPwd = new String(RemediationFacade.encrypt(remediationPlanExecution.getPassword().getBytes()));
		JSONObject nodedetails = new JSONObject();
		nodedetails.put("domain", remediationPlanExecution.getDomain());
		nodedetails.put("iapnodetransport", new Integer(remediationPlanExecution.getIapnodetransport()));
		nodedetails.put("username", remediationPlanExecution.getUsername());
//		nodedetails.put("password", encryptedPwd);
		nodedetails.put("password", remediationPlanExecution.getPassword());
		nodedetails.put("referencekey", remediationPlanExecution.getReferencekey());
		nodedetails.put("remoteservernames", remediationPlanExecution.getRemoteservernames());

		String insertQuery = "INSERT INTO remediation_plan_executions values('" + remediationplanexecid.toString()
				+ "','" + remediationPlanExecution.getRemediationplanid() + "','"
				+ remediationPlanExecution.getResourceid() + "','" + remediationPlanExecution.getObservableid() + "','"
				+ nodedetails.toString() + "','test_user4',LOCALTIMESTAMP(0)," + "LOCALTIMESTAMP(0)+("
				+ rand.nextInt(10) + " * interval '1 minute')"
				+ ",0,'QUEUED',false,'IIPADMIN',LOCALTIMESTAMP(0),'test_user4',LOCALTIMESTAMP(0));";
		System.out.println("Insert Query is :: " + insertQuery);
		rows_updated = template.update(insertQuery);

		if (rows_updated > 0) {
			inserttoKafka(topic_itcc_orch_exelist, remediationplanexecid.toString());
			List<String> list = template.query(remediationPlanActionMapQuery,
					new Object[] { remediationPlanExecution.getRemediationplanid() }, new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rownumber) throws SQLException {
							String resource = "";
							resource = rs.getString("remediationplanactionid");
							return resource;
						}
					});
			for (int i = 0; i < list.size(); i++) {
				saveRemediationPlanExecutionAction(remediationplanexecid.toString(), list.get(i));
			}
		}

		return "true";
	}

	public void inserttoKafka(String topic, String result) throws Exception {
		// insert into kafka
		Properties prop = new Properties();
		prop.put("bootstrap.servers", bootstrap_servers);
		prop.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		prop.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		setKafkaProperties(prop);
		KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(kafkaProperties);
		ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, result);
		kafkaProducer.send(record);
		kafkaProducer.close();

	}

	public String saveRemediationPlanExecutionAction(String remediationPlanExecutionId, String remediationPlanActionId)
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		UUID remediationplanexecactionid = Generators.randomBasedGenerator().generate();
		String insertQuery = "INSERT INTO remediation_plan_execution_actions values('"
				+ remediationplanexecactionid.toString() + "','" + remediationPlanExecutionId + "','"
				+ remediationPlanActionId
				+ "','','QUEUED','','','','IIPADMIN',LOCALTIMESTAMP(0),'test_user4',LOCALTIMESTAMP(0));";
		System.out.println("Insert Query for action :: " + insertQuery);
		template.update(insertQuery);
		return "true";
	}

	public String saveRemediaitonExec(String remediationplanid) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		Random rand = new Random();
		int rows_updated = 0;
		String remediationplan = "", rpstatId = "";
		List<String> list1 = template.query(remediationplandetailsQuery, new Object[] { remediationplanid },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource = "";
						resource = rs.getString("plandetails");
						return resource;
					}
				});
		remediationplan = list1.get(0);
		String insertQuery = "INSERT INTO remediation_plan_executions (remediationplanid, plandetails, executedby, executionstartdatetime, executionenddatetime, status, createdby, createdate, modifiedby, modifieddate) values('"
				+ remediationplanid + "','" + remediationplan + "','test_user4',LOCALTIMESTAMP(0),"
				+ "LOCALTIMESTAMP(0)+(" + rand.nextInt(10) + " * interval '1 minute')"
				+ ",'INPROGRESS','IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0));";
		
		 * String insertQuery =
		 * "INSERT INTO remediation_plan_run_statistics(remediationplanid, runby, rundatetime,status,runduration,createdby, createdate, modifiedby,modifieddate) values('"
		 * + remediationplanid +
		 * "','test_user4',CURRENT_DATE,'QUEUED','0:00:00','IIPADMIN',CURRENT_DATE,'test_user4',LOCALTIMESTAMP(0));";
		 
		rows_updated = template.update(insertQuery);

		if (rows_updated > 0) {
			List<String> list = template.query(remediationplandetailsQuery, new Object[] { remediationplanid },
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rownumber) throws SQLException {
							String resource = "";
							resource = rs.getString("remediationplanexecid");
							return resource;
						}
					});
			rpstatId = list.get(0);
		}

		return rpstatId;
	}

	public int updateRemediaitonExec(String remediationplanexecid, String status) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_executions set status = '" + status
				+ "', executionenddatetime = LOCALTIMESTAMP(0), isnotified=false where remediationplanexecid = '"
				+ remediationplanexecid + "';";
		rows_updated = template.update(updateQuery);

		return rows_updated;
	}

	public List<RemediationExecStatus> remediationPlanStatus(String userid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<RemediationExecStatus> list = template.query(remediationPlanStatusQuery, new Object[] { userid },
				new RowMapper<RemediationExecStatus>() {
					public RemediationExecStatus mapRow(ResultSet rs, int rownumber) throws SQLException {
						RemediationExecStatus resource = new RemediationExecStatus();
						System.out.println("Exec id :: " + rs.getString("remediationplanexecid"));
						resource.setRemediationplanexecid(rs.getString("remediationplanexecid"));
						resource.setRemediationplanid(remediationPlanName(rs.getString("remediationplanid")));
						resource.setStatus(rs.getString("status"));
						updateRemediaitonPlanExecFlag(resource.getRemediationplanexecid(), true);
						return resource;
					}
				});
		return list;
	}

	public String remediationPlanName(String rpid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(remediationPlanNameQuery, new Object[] { rpid }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("remediationplanname");
				return resource;
			}
		});
		return list.get(0);
	}

	public int updateRemediaitonPlanExecFlag(String remediationplanexecid, boolean notificationflag)
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_executions set isnotified=" + notificationflag
				+ ", executionenddatetime = LOCALTIMESTAMP(0) where remediationplanexecid = '" + remediationplanexecid
				+ "';";
		rows_updated = template.update(updateQuery);

		return rows_updated;
	}

	// to get the plan which is in queue state
	
	 * public String remediationPlanDetails() { JdbcTemplate template = new
	 * JdbcTemplate(datasource); List<String> list =
	 * template.query(remediationplanRetrieveQuery, new Object[] {}, new
	 * RowMapper<String>() { public String mapRow(ResultSet rs, int rownumber)
	 * throws SQLException { String resource = ""; resource =
	 * rs.getString("plan"); return resource; } }); return list.get(0); }
	 

	public List<ScriptIdentifier> remediationPlanDetails(String rpexecid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<ScriptIdentifier> list = template.query(remediationPlanActionidQuery, new Object[] { rpexecid },
				new RowMapper<ScriptIdentifier>() {
					public ScriptIdentifier mapRow(ResultSet rs, int rownumber) throws SQLException {
						ScriptIdentifier resource = new ScriptIdentifier();
						ScriptIdentifierDTO scriptIdentifier = new ScriptIdentifierDTO();
						ScriptsDetails scriptDetails = new ScriptsDetails();
						scriptIdentifier = remediationPlanDetail(rpexecid, rs.getString("remediationplanactionid"));
						scriptDetails.setScriptidentifier(scriptIdentifier);
						resource.setPlandetails(scriptDetails);
						resource.setRemediationplanexecid(rpexecid);
						resource.setRemediationplanactionid(rs.getString("remediationplanactionid"));
						return resource;
					}
				});
		return list;
	}

	public ScriptIdentifierDTO remediationPlanDetail(String rpexecid, String rpactid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<ScriptIdentifierDTO> list = template.query(remediationplanRetrieveQuery, new Object[] { rpexecid },
				new RowMapper<ScriptIdentifierDTO>() {
					public ScriptIdentifierDTO mapRow(ResultSet rs, int rownumber) throws SQLException {
						ScriptIdentifierDTO resource = new ScriptIdentifierDTO();
						try {
							JSONObject nodedetailsJson = new JSONObject(rs.getString("nodedetails"));
							resource.setPassword(nodedetailsJson.getString("password"));
//							resource.setPassword(niapassword);
							resource.setCompanyid(companyid);
							resource.setResponsenotificationcallbackurl(responsenotificationcallbackurl);
							resource.setReferencekey(nodedetailsJson.getString("referencekey"));
							resource.setUsername(nodedetailsJson.getString("username"));
//							resource.setUsername(niauser);
							resource.setRemoteservernames(nodedetailsJson.getString("remoteservernames"));
							resource.setExecutionmode(
									Integer.parseInt(getExecutionMode(nodedetailsJson.getString("referencekey"))));
							List<String> scritptDetailsList = getParameterDetails(rpactid);
							List<ParameterDTO> parameterList = new ArrayList<ParameterDTO>();
							for (int i = 0; i < scritptDetailsList.size(); i++) {
								ParameterDTO parameter = new ParameterDTO();
								JSONObject scriptdetailsJson = new JSONObject(scritptDetailsList.get(i));
								parameter.setParametername(scriptdetailsJson.getString("name"));
								parameter.setParametervalue(scriptdetailsJson.getString("defaultvalue"));
								parameterList.add(parameter);
								JSONObject actiondetailsJson = new JSONObject(
										scriptdetailsJson.getString("actiondetails"));
								resource.setCategoryid(actiondetailsJson.getInt("categoryid"));
								resource.setScriptid(actiondetailsJson.getInt("scriptid"));
							}
							resource.setParameters(parameterList);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return resource;
					}
				});
		return list.get(0);
	}

	public List<String> getParameterDetails(String rpactid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(parametersQuery, new Object[] { rpactid }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("paramdetails");
				return resource;
			}
		});
		return list;
	}

	public String getExecutionMode(String platformname) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(executionModeQuery, new Object[] { platformname }, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("executionmode");
				return resource;
			}
		});
		return list.get(0);
	}

	public int saveActionParam(int paramid, String name, boolean mandatory, String defaultValue, int actionid)
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String insertQuery = "INSERT INTO action_params values(" + paramid + ",'" + name + "'," + mandatory + ",'"
				+ defaultValue + "'," + actionid + ",'test_user4',CURRENT_DATE,'iapadmin',LOCALTIMESTAMP(0));";

		rows_updated = template.update(insertQuery);

		return rows_updated;
	}

	public String remediationPlanHistoryDetails(String resourcetype, String createdate, int limit, String obsid,
			String resid) throws ParseException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		setNamedParameterJdbcTemplate(datasource);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(createdate);
		String result = "";
		if (limit < 1) {
			// List<String> list = template.query(historyQuery, new Object[] {
			// resourcetype, obsid, resid, date },
			List<String> list = template.query(historyQuery, new Object[] { resourcetype, date },
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rownumber) throws SQLException {
							String resource = "";
							resource = rs.getString("historydetails");
							return resource;
						}
					});
			result = list.get(0);
		} else {
			// List<String> list = template.query(historyLimitQuery, new
			// Object[] { resourcetype, obsid, resid, date, limit },
			List<String> list = template.query(historyLimitQuery, new Object[] { resourcetype, date, limit },
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rownumber) throws SQLException {
							String resource = "";
							resource = rs.getString("historydetails");
							return resource;
						}
					});
			result = list.get(0);
		}
		return result;
	}

	public List<RemediationPlanAction> getRemediationPlanActions(String remediationplanid) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<RemediationPlanAction> list = template.query(remediaitonPlanActionHistoryQuery,
				new Object[] { remediationplanid }, new RowMapper<RemediationPlanAction>() {
					public RemediationPlanAction mapRow(ResultSet rs, int rownumber) throws SQLException {
						RemediationPlanAction resource = new RemediationPlanAction();
						// resource.setRemediationplanid(rs.getString("remediationplanid"));
						resource.setRemediationplanid(rs.getString("remediationplanexecid"));
						resource.setRemediationplanactionid(rs.getString("remediationplanactionid"));
						resource.setStatus(rs.getString("status"));
						resource.setLogdata(rs.getString("logdata"));
						resource.setOutput(rs.getString("output"));
						resource.setScriptname(rs.getString("actionname"));
						return resource;
					}
				});
		return list;
	}

	public String retryRemediationPlan(String remediationplanid) throws Exception {
		String remediationplanexecid = "";
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(remediationPlanExecidQuery, new Object[] { remediationplanid },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource = "";
						resource = rs.getString("remediationplanexecid");
						return resource;
					}
				});

		remediationplanexecid = list.get(0);

		int rows_updated = updateRemediationPlanExec(remediationplanexecid);

		if (rows_updated == 1) {
			List<String> list1 = template.query(remediationPlanExecActionidQuery,
					new Object[] { remediationplanexecid }, new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rownumber) throws SQLException {
							String resource = "";
							resource = rs.getString("remediationplanexecactionid");
							updateRemediationPlanExecActions(resource);
							return resource;
						}
					});
		}

		return "true";
	}

	private int updateRemediationPlanExec(String remediationplanexecid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String updateQuery = "update remediation_plan_executions set status = 'QUEUED' where remediationplanexecid=?";

		rows_updated = template.update(updateQuery, remediationplanexecid);

		try {
			inserttoKafka(topic_itcc_orch_exelist, remediationplanexecid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rows_updated;
	}

	private int updateRemediationPlanExecActions(String remediationplanexecactionid) {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String updateQuery = "update remediation_plan_execution_actions set status = 'QUEUED' where remediationplanexecactionid=?";

		rows_updated = template.update(updateQuery, remediationplanexecactionid);

		return rows_updated;
	}

	public String getRemediationPlan(String rpexecid) throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(remediationPlanwithexecidQuery, new Object[] { rpexecid },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource = "";
						resource = rs.getString("output");
						return resource;
					}
				});
		return list.get(0);
	}

	public int updaterpActionsCorrelationId(String rpexecid, String rpactionid, String correlationid, String status)
			throws SQLException {
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_execution_actions set correlationid='" + correlationid
				+ "', status = '" + status + "' where remediationplanexecid = '" + rpexecid
				+ "' and remediationplanactionid='" + rpactionid + "';";
		rows_updated = template.update(updateQuery);

		return rows_updated;
	}

	public int updateStatusCorrelationId(String correlationid, String status, String rpexecid, String rpstatus)
			throws SQLException {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_execution_actions set status='" + status
				+ "' where remediationplanexecid = '" + rpexecid + "' and correlationid='" + correlationid + "';";
		rows_updated = template.update(updateQuery);

		String updateQuery1 = "UPDATE remediation_plan_executions set status='" + rpstatus
				+ "' where remediationplanexecid = '" + rpexecid + "';";
		rows_updated = template.update(updateQuery1);
		updateRemediaitonPlanExecFlag(rpexecid, false);

		return rows_updated;
	}

	public int updateStatusRpeId(String rpactionid, String rpactionStatus, String rpexecid) throws SQLException {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_execution_actions set status='" + rpactionStatus
				+ "' where remediationplanactionid = '" + rpactionid + "' and remediationplanexecid='" + rpexecid
				+ "';";
		rows_updated = template.update(updateQuery);

		return rows_updated;
	}

	public int updateStatusRpe(String rpexecid, String rpstatus) throws SQLException {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "UPDATE remediation_plan_executions set status='" + rpstatus
				+ "' where remediationplanexecid = '" + rpexecid + "';";
		rows_updated = template.update(updateQuery);
		updateRemediaitonPlanExecFlag(rpexecid, false);
		return rows_updated;
	}

	public int updateStatusLogs(String corelationid, String log, String output, String status) throws SQLException {
		// TODO Auto-generated method stub

		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String remediationplanexecid = "";
		List<String> list = template.query(remediationPlanExecIdQuery, new Object[] { corelationid },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource = "";
						resource = rs.getString("remediationplanexecid");
						return resource;
					}
				});

		if (list.size() > 0) {
			remediationplanexecid = list.get(0);
		}

		String updateQuery = "UPDATE remediation_plan_execution_actions set status='" + status + "', logdata='" + log
				+ "', output='" + output + "' where correlationid = '" + corelationid + "';";
		rows_updated = template.update(updateQuery);

		if (status.equalsIgnoreCase("FAILED")) {
			rows_updated = updateRemediaitonExec(remediationplanexecid, status);
			updateRemediaitonPlanExecFlag(remediationplanexecid, false);
		} else {
			List<String> list1 = template.query(remediationPlanActionidQuery, new Object[] { remediationplanexecid },
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rownumber) throws SQLException {
							String resource = "";
							resource = rs.getString("remediationplanactionid");
							return resource;
						}
					});

			if (list1.size() > 0) {
				try {
					inserttoKafka(topic_itcc_orch_exelist, remediationplanexecid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				rows_updated = updateRemediaitonExec(remediationplanexecid, status);
				updateRemediaitonPlanExecFlag(remediationplanexecid, false);
			}
		}

		return rows_updated;
	}

	public int updateLogs(String corelationid, String log, String output, String status) throws SQLException {
		// TODO Auto-generated method stub
		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;

		String remediationplanexecid = "";
		List<String> list = template.query(remediationPlanExecIdQuery, new Object[] { corelationid },
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rownumber) throws SQLException {
						String resource = "";
						resource = rs.getString("remediationplanexecid");
						return resource;
					}
				});

		if (list.size() > 0) {
			remediationplanexecid = list.get(0);
		}

		String updateQuery = "UPDATE remediation_plan_execution_actions set status='" + status + "', logdata='" + log
				+ "', output='" + output + "' where correlationid = '" + corelationid + "';";
		rows_updated = template.update(updateQuery);

		
		 * rows_updated = updateRemediaitonExec(remediationplanexecid, status);
		 
		updateRemediaitonPlanExecFlag(remediationplanexecid, false);

		return rows_updated;
	}

	public String getRemediationPlanExecId() {
		// TODO Auto-generated method stub
		String remediationplanexecid = "";
		JdbcTemplate template = new JdbcTemplate(datasource);
		List<String> list = template.query(remediationPlanExecQuery, new Object[] {}, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rownumber) throws SQLException {
				String resource = "";
				resource = rs.getString("remediationplanexecid");
				return resource;
			}
		});

		if (list.size() > 0) {
			remediationplanexecid = list.get(0);
			String updateQuery = "update remediation_plan_executions set ispicked = 'TRUE' where remediationplanexecid=?";
			template.update(updateQuery, remediationplanexecid);
		}

		return remediationplanexecid;
	}

	public Integer updateObservationsValues(int Value) {

		JdbcTemplate template = new JdbcTemplate(datasource);
		int rows_updated = 0;
		String updateQuery = "Update observations set value= " + Value
				+ " where resourceid='OM_IIS_APP_SERVER_1' and observableid='OBS_DISK_UTIL_APP' and observationtime ='2019-03-29 11:22:42'";
		rows_updated = template.update(updateQuery);
		return rows_updated;
	}
}
*/