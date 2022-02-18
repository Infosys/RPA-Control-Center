import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { EventEmitter } from '@angular/core';
import 'rxjs/add/operator/map';
import { environment } from '../../environments/environment';

@Injectable()
export class ClusterDetailsService {
  private serverAddress = environment.serverAddress;
  private services_app_name = environment.services_app_name;  

  private REST_SERVICE_URI_CLUSTER_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/clusters/health-details';
  private REST_SERVICE_URI_CLUSTER_DATA = 'http://' + this.serverAddress + '/' + this.services_app_name + '/clusters/health-counts';
  private REST_SERVICE_URI_CLUSTER_CHILD = 'http://' + this.serverAddress + '/' + this.services_app_name + '/clusters/';
  private REST_SERVICE_URI_CLUSTER = 'http://' + this.serverAddress + '/' + this.services_app_name + '/clusters/';
  private REST_SERVICE_URI_PORTFOLIO_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/portfolios/';
  private REST_SERVICE_URI_COMPONENT_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/portfolios/';
  private REST_SERVICE_URI_RECENT_COMPONENT_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/portfolios/';
  private REST_SERVICE_URI_PARAMETER_TYPE = 'http://' + this.serverAddress + '/' + this.services_app_name + '/portfolios/resourcetypes';
  private REST_SERVICE_URI_COMPONENT_HISTORY = 'http://' + this.serverAddress + '/' + this.services_app_name + '/resource/history';
  private REST_SERVICE_URI_FILTERED_DATA = 'http://' + this.serverAddress + '/' + this.services_app_name + '/resource/filtereddata';
  private REST_SERVICE_URI_ERRORLOG_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/errorlog';
  private REST_SERVICE_URI_TICKET_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/tickets';
  private REST_SERVICE_URI_REMEDIATION_PLAN_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediation-details/';
  private REST_SERVICE_URI_ACTION_STAGES = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediation-action/stages';
  private REST_SERVICE_URI_REMEDIATION_PLAN_ACTION_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediationplan_details';
  private REST_SERVICE_URI_ACTION_SCRIPTS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediation-actions';
  private REST_SERVICE_URI_SAVE_REMEDIATION_PLAN = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/save_remediationplan_details';
  // private REST_SERVICE_URI_EXECUTION_STATUS_REMEDIATION_PLAN = 'http://' + this.ip_port + '/' + this.app_name + '/remediation/remediationplan/status/';
  // private REST_SERVICE_URI_EXECUTE_REMEDIATION_PLAN = 'http://' + this.ip_port + '/' + this.app_name + '/remediation/execute';
  private REST_SERVICE_URI_EXECUTE_REMEDIATION_PLAN = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/queue_plan';
  private REST_SERVICE_URI_PLATFORM_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/platform/all';
  private REST_SERVICE_URI_EXPERT_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/experts/all';
  private REST_SERVICE_URI_NODES_LIST = 'http://' + this.serverAddress + '/' + this.services_app_name + '/nia-api/nodes/all';
  private REST_SERVICE_URI_NODES_LIST_IAP = 'http://scc_servername/WEMScriptExecutor_Dev/WEMNodeService.svc/GetRegisteredNodes?v=domain&t=2&c=1';
  private REST_SERVICE_URI_SCRIPTS_LIST = 'http://' + this.serverAddress + '/' + this.services_app_name + '/nia-api/category/';
  private REST_SERVICE_URI_SCRIPTS_LIST_IAP = 'http://scc_servername/WEMScriptExecutor_Dev/WEMScriptService.svc/GetAllScriptDetails/';

  private REST_SERVICE_URI_CATEGORIES_LIST = 'http://' + this.serverAddress + '/' + this.services_app_name + '/nia-api/categories/all';
  private REST_SERVICE_URI_CATEGORIES_LIST_IAP = 'http://scc_servername/WEMScriptExecutor_Dev/WEMCommonService.svc/GetAllCategoriesByCompany?companyId=1&module=2';

  private REST_SERVICE_URI_REMEDIATION_PLAN_NOTIFICATIONS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediationplan/status/';
  private REST_SERVICE_URI_REMEDIATION_PLAN_HISTORY = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/status/executionhistory';
  private REST_SERVICE_URI_REMEDIATION_PLAN_HISTORY_STEP_DETAILS = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediation-history-details/';
  private REST_SERVICE_URI_PLAN_RETRY = 'http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/remediation-plan/retry';
  // private REST_SERVICE_URI_DEMO_UPDATE_HEALTHY='http://' + this.serverAddress + '/' + this.services_app_name + '/remediation/update_observatios_value/';
   

  constructor(private httpClient: HttpClient) { }


  somevent = new EventEmitter<string>();
  jsonData: any;
  historyStr: any = '/history/1';
  private _isAutoRefreshEnabled = true;
  properties: any;


  public get isAutoRefreshEnabled(): boolean {
    return this._isAutoRefreshEnabled;
  }
  public set isAutoRefreshEnabled(value: boolean) {

    this._isAutoRefreshEnabled = value;
  }


  loadProperties() {
    
    console.log('inside service12');

  }

  getClusterList(shouldCall: boolean) {

    if (shouldCall) {
      console.log('getClusterList called');
      return this.httpClient.get(this.REST_SERVICE_URI_CLUSTER_DETAILS);
    } else {
      return [];
    }
  }

  getCluster(shouldCall: boolean) {

    if (shouldCall) {
      console.log('getCluster called');
      return this.httpClient.get(this.REST_SERVICE_URI_CLUSTER);
    } else {
      return [];
    }
  }

  getPortfolioIncidentDetails(portfolioId: string, shouldCall: boolean) {

    if (shouldCall) {
      console.log('getPortfolioIncidentDetails called');
      return this.httpClient.get(this.REST_SERVICE_URI_PORTFOLIO_DETAILS + portfolioId + '/component-health-counts');
    } else {
      return [];
    }
  }

  getErrorLogDetails(shouldCall: boolean) {
    if (shouldCall) {
      console.log('getErrorLogDetails called');
      return this.httpClient.get(this.REST_SERVICE_URI_ERRORLOG_DETAILS);
    } else {
      return [];
    }
  }

  getTicketDetails(shouldCall: boolean) {
    if (shouldCall) {
      console.log('getTicketDetails called');
      return this.httpClient.get(this.REST_SERVICE_URI_TICKET_DETAILS);
    } else {
      return [];
    }
  }

  saveData(jsonStr) {
    this.jsonData = jsonStr;
  }

  getData() {
    return this.jsonData;
  }

  getComponentDetails(portfolioId: string, incidentId: string, shouldCall: boolean) {

    if (shouldCall) {
      console.log("getComponentDetails called");
      return this.httpClient.get(this.REST_SERVICE_URI_COMPONENT_DETAILS + portfolioId + "/component-health-details");
    }
    else
      return [];
  }
  getComponentDetailsForGraph(portfolioId: string, incidentId: string) {
    return this.httpClient.get(this.REST_SERVICE_URI_COMPONENT_DETAILS + portfolioId + "/component-health-details");

  }

  getRecentComponents(portfolioId: string, incidentId: string, shouldCall: boolean) {
    if (shouldCall) {
      console.log("getRecentComponents called");
      return this.httpClient.get(this.REST_SERVICE_URI_RECENT_COMPONENT_DETAILS + portfolioId + "/recent");
    }
    else
      return [];
  }
  getComponentHistory(inputResources: any) {
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    return this.httpClient.post(this.REST_SERVICE_URI_COMPONENT_HISTORY, inputResources, httpOptions);
  }

  getParameterType() {
    return this.httpClient.get(this.REST_SERVICE_URI_PARAMETER_TYPE);
  }

  getFilteredData(inputResources: any) {
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    return this.httpClient.post(this.REST_SERVICE_URI_FILTERED_DATA, inputResources, httpOptions);
  }

  getClusterData(shouldCall: boolean) {
    if (shouldCall) {
      console.log('getClusterData called');
      return this.httpClient.get(this.REST_SERVICE_URI_CLUSTER_DATA);
    } else {
      return [];
    }
  }

  getClusterChild(clusterid: string, shouldCall: boolean) {
    if (shouldCall) {
      console.log('getClusterChild called');
      return this.httpClient.get(this.REST_SERVICE_URI_CLUSTER_CHILD + clusterid);
    } else {
      return [];
    }
  }

  getRemediationPlanDetails(serverType: string,observableId:string, shouldCall: boolean) {
    if (shouldCall) {
      console.log('getRemediationPlanDetails called');
      return this.httpClient.get(this.REST_SERVICE_URI_REMEDIATION_PLAN_DETAILS + serverType +'/'+observableId);
    } else {
      }
  }

  getActionStages(shouldCall: boolean) {
    if (shouldCall) {
      console.log('getActionStages called');
      return this.httpClient.get(this.REST_SERVICE_URI_ACTION_STAGES);
    } else {
      return [];
    }
  }

  getRemediationActions(planId: string) {
    const json = {
      'remediationplanid': planId,
      'sourcetype': 'defaultDB'
    };
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    console.log(this.REST_SERVICE_URI_REMEDIATION_PLAN_ACTION_DETAILS);
    return this.httpClient.post(this.REST_SERVICE_URI_REMEDIATION_PLAN_ACTION_DETAILS, json, httpOptions);
  }

  getActionScripts(shouldCall: boolean) {
    if (shouldCall) {
      console.log('getActionScripts called');
      return this.httpClient.get(this.REST_SERVICE_URI_ACTION_SCRIPTS);
    } else {
      return [];
    }
  }

  saveRemediationPlanDetails(planJson: any) {
    console.log('save plan');
    console.log(planJson);
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    return this.httpClient.post(this.REST_SERVICE_URI_SAVE_REMEDIATION_PLAN, planJson, httpOptions);

  }

  getActionScriptsFromEnv(platform: string, categoryId: string, shouldCall: boolean) {
    // debugger;
    if (shouldCall) {

      if (platform === 'NIA') {
        return this.httpClient.get(this.REST_SERVICE_URI_SCRIPTS_LIST + categoryId + '/scripts');
      } else if (platform === 'IAP') {
        return this.httpClient.get(this.REST_SERVICE_URI_SCRIPTS_LIST_IAP + categoryId );
      }
    } else {
      return [];
    }
  }

  // checkExecutionActionPlan(runStatId: number) {
  //   return this.httpClient.get(this.REST_SERVICE_URI_EXECUTIONON_PLAN + runStatId);

  // }

  executeActionPlan(inputJson: any) {
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    return this.httpClient.post(this.REST_SERVICE_URI_EXECUTE_REMEDIATION_PLAN, inputJson, httpOptions);

  }

  getPlatforms(shouldCall: boolean) {

    if (shouldCall) {
      console.log('getActionScripts called');
      return this.httpClient.get(this.REST_SERVICE_URI_PLATFORM_DETAILS)} else {
      return [];
    }
  }

  getExperts(inputJson: any, shouldCall: boolean) {
    // debugger;
    // console.log("getExperts called");
    console.log(JSON.stringify(inputJson));

    // let inputJson: any = {
    //   "Component": "appserver3"
    // };

    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

    if (shouldCall) {
      console.log('getExperts called');
      return this.httpClient.post(this.REST_SERVICE_URI_EXPERT_DETAILS, JSON.stringify(inputJson), httpOptions);
    } else {
      return [];
    }
  }

  getNodes(platfrom: string, inputJson: any, shouldCall: boolean) {
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    if (shouldCall) {
      if (platfrom === 'NIA') {
        return this.httpClient.post(this.REST_SERVICE_URI_NODES_LIST, inputJson, httpOptions);
      } else if (platfrom === 'IAP') {
        return this.httpClient.get(this.REST_SERVICE_URI_NODES_LIST_IAP);
      }
    } else {
      return [];
    }
    return [];
  }

  getCategories(type: string, shouldCall: boolean, ) {
    console.log(this.REST_SERVICE_URI_CATEGORIES_LIST_IAP);
    if (shouldCall) {
      if (type === 'NIA') {
        return this.httpClient.get(this.REST_SERVICE_URI_CATEGORIES_LIST);
      } else if (type === 'IAP') {
        return this.httpClient.get(this.REST_SERVICE_URI_CATEGORIES_LIST_IAP);
      }
    } else {
      return [];
    }
  }


  getPlanHistoryStepDetails(planId: string, shouldCall: boolean) {
    if (shouldCall) {
      return this.httpClient.get(this.REST_SERVICE_URI_REMEDIATION_PLAN_HISTORY_STEP_DETAILS + planId);
    } else {
      return [];
    }
  }

  getPlanNotifications(userId: string, shouldCall: boolean) {
    console.log('getPlanNotifications called');
    if (shouldCall) {
      return this.httpClient.get(this.REST_SERVICE_URI_REMEDIATION_PLAN_NOTIFICATIONS + userId);
    } else {
      return [];
    }
  }


  getPlanHistoy(inputJson: any, shouldCall: boolean) {

    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };

    if (shouldCall) {
      console.log(this.REST_SERVICE_URI_REMEDIATION_PLAN_HISTORY);
      return this.httpClient.post(this.REST_SERVICE_URI_REMEDIATION_PLAN_HISTORY, inputJson, httpOptions);
    } else {
      return [];
    }

  }

  retryPlanExecution(inputjson,planId:string,shouldCall:boolean){
    const httpOptions = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
    if (shouldCall) {
      console.log("retryPlanExecution called");
      return this.httpClient.post(this.REST_SERVICE_URI_PLAN_RETRY,JSON.stringify(inputjson) );
    }
    else
      return [];
  }
  
}    
