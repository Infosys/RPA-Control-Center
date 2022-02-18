import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, timer, pipe, of } from 'rxjs';
import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment'
import { Router } from '@angular/router';
import { RemediationAction, ExecuteJson, BusinessProcesses, ScriptArray, InputVariables, Action, Parameter, ActionType, RemediationPlan, ActionScript, HistoryDetails } from '../action/action.model';
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
import { InitialLoadCheck } from '../cluster-details/initial-load.model'
import { ActionPlan, Identifier, Scriptdetails, ActionParameters, Nodes } from '../action/acrion-script.model';
import { Expert } from '../action/expert.model';

import { DomSanitizer } from '@angular/platform-browser';
import { debug } from 'util';
import { CATCH_STACK_VAR } from '@angular/compiler/src/output/abstract_emitter';
import { error } from 'protractor';
import { EncryptionService } from '../../app/EncryptionService';
declare var $: any;

@Component({
  selector: 'app-action',
  templateUrl: './action.component.html',
  styleUrls: ['./action.component.scss']
})
export class ActionComponent implements OnInit, OnDestroy {
  @ViewChild("actionForm", { static: false }) actionForm;
  portfolioName: string;
  clusterName: string;
  resourceId: string;
  resourceID: string;
  ObservableId: string
  subscription: Subscription;
  planStepJson: HistoryDetails;
  historySubscription: Subscription;
  ActionName: string;
  jsonComponent: any;
  initialLoadCnt: number = 0;
  planSubscription: Subscription;
  actionStageSubscription: Subscription;
  planActionSubscription: Subscription;
  actionScriptSubscription: Subscription;
  platformsSubscription: Subscription;
  retrySubscription: Subscription;
  notificationSubscription: Subscription;
  nodesSubscription: Subscription;
  expertSubscription: Subscription;
  executionStatus: Subscription;
  remediAtionPlanjson: RemediationPlan[] = [];
  jsonScriptActionParam: BusinessProcesses[] = [];
  jsonActionStage: ActionType[];
  remediationPlanActions: RemediationAction[] = [];
  jsonActionstr: RemediationAction[] = [];
  jsonActionScript: any;
  jsonActionScriptVisible: ScriptArray = { businessProcesses: [] };
  jsonActionScriptTemp: any;
  currentDate: string;
  remediationPlanName: string = "";
  validPlanName: boolean = false;
  actionModified: boolean = false;
  remediationPlanId: string;
  scriptParameters: Parameter[] = [];
  saveActionPlan: ActionPlan[] = [];
  searchValue: string;
  searchScriptVal: string;
  planEnabled: string = "block";
  title: string = "Remediation Action";
  username: string = "";
  usernameFlag: boolean = false;
  passwordFlag: boolean = false;
  nodeFlag: boolean = false;
  selectedPlatform: string = "";
  password: string = "";
  enableScriptView: boolean = false;
  actionScriptRef: Action;
  categoriesList: any;
  categoryIdSelected: number = 0;
  selectedNodeId: string = "";
  executeJson: ExecuteJson;
  scriptdetailsJson: Scriptdetails = {
    // Nodes: [],
    Parameters: []
  };
  nodesJson: any = [];
  saveActionParameters: any = [];
  planExecutionStatus: string;
  expertsJson: Expert[] = [];
  nodesListJson: any;
  platformJson: any = [];
  expertsJsonVisible: Expert[] = [];
  showCredsDiv: boolean = false;
  portalUser: string;
  planNotifications: any;
  enableCredDiv: boolean = true;
  initialLoadJson: InitialLoadCheck[] = [
    { service: "getRemediationPlanDetails", initialLoad: false },
    // { service: "getActionScriptDetails", initialLoad: false },
    { service: "getActionStagesList", initialLoad: false },
    { service: "getPlanExecutionStatus", initialLoad: false },
    { service: "getPlatformDetails", initialLoad: false },
    { service: "getExpertsDetails", initialLoad: false },
    { service: "getNodesDetails", initialLoad: false },
    // { service: "getCategories", initialLoad: false }

  ];

  runstatId: number;
  executionPlanId: string;
  actionNameReq: boolean;

  isloader: boolean = false;
  constructor(private sanitizer: DomSanitizer, private route: ActivatedRoute, private _router: Router, private clusterDetailsService: ClusterDetailsService, private encrptnsvc: EncryptionService) { }

  ngOnInit() {

    // var pwd = "$4Iip12$";
    // var encrypted = this.encrptnsvc.encrypt(pwd);
    // var decrypted = this.encrptnsvc.decrypt(encrypted);
   
    this.portalUser = "test_user4";
    this.username = "";
    this.password = "";
    this.actionNameReq = false;

    this.enableCredDiv = false;
    this.showCredsDiv = false;
    this.isloader = false;
    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);
    this.portfolioName = this.route.snapshot.params['portfolioId'];
    this.clusterName = this.route.snapshot.params['clusterId'];
    this.resourceId = this.route.snapshot.params['serverId'];
    this.resourceID = sessionStorage.getItem("resourceId");
    this.ObservableId = this.route.snapshot.params['paramId'];
    sessionStorage.setItem("serverType", this.ObservableId);
    this.jsonComponent = JSON.parse(sessionStorage.getItem("observation"));

    // console.log("observable data" + this.ObservableId);
    this.subscription = this.route.params.subscribe(params => {
      this.portfolioName = params['portfolioId'];
      this.clusterName = params['clusterId'];
      this.resourceId = params['serverId'];
      this.ObservableId = params['paramId'];
      sessionStorage.setItem("serverType", this.ObservableId);
    });

    if (this.ObservableId != null && this.ObservableId != undefined)
      this.getRemediationPlanDetails(this.ObservableId);

    this.getActionStagesList();
    this.currentDate = new Date().toJSON().slice(0, 10).split('-').join('-');

    this.getExpertsDetails();
    this.getPlatformDetails();

  }
  addCategory() {


    this.remediationPlanActions.forEach(server => {
      server.actions.forEach(action => {
        var actionDetails = JSON.parse(action.actiondetails);
        action.categoryid = actionDetails["categoryid"];
        action.scriptid = actionDetails["scriptid"];
      })
      console.log(this.remediationPlanActions);
    })
  }

  // getRemediationPlanNotifications() {
  //   debugger
  //   let timerObj = timer(0, environment.notificationInterval).pipe(
  //     switchMap(() => this.clusterDetailsService.getPlanNotifications(this.portalUser, true)
  //     )
  //   );
  //   this.notificationSubscription = timerObj.subscribe(
  //     result => {
  //       this.planNotifications = JSON.parse(JSON.stringify(result));

  //       console.log("Notifications");
  //       console.log(this.planNotifications);
  //       if (this.planNotifications.length > 0) {
  //         var selfref = this;
  //         this.planNotifications[0]["status"].forEach(function (plan) {

  //           if (plan.status == "QUEUED") {
  //             selfref.successMessage("Plan " + plan.remediationplanid + " has been queued successfully.");
  //           } else if (plan.status == "INPROGRESS") {
  //             //this.successMessage(plan.remediationplanid+ "has been queued successfully.");
  //           } else if (plan.status == "FAILED") {
  //             selfref.errorMessage("Plan " + plan.remediationplanid + " has been failed.");
  //             selfref.getRemediationPlanDetails(sessionStorage.getItem("serverType"));
  //           } else if (plan.status == "COMPLETED") {
  //             selfref.successMessage("Plan " + plan.remediationplanid + " has been executed successfully.");

  //           }
  //         }
  //         );
  //       }
  //     }
  //   );
  // }
  getPlatformDetails() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getPlatforms(InitialLoadCheck.getLoadInterval('getPlatformDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.platformsSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getPlatformDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.platformJson = JSON.parse(JSON.stringify(result));
          // console.log("platforms" + JSON.stringify(this.platformJson));
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getPlatformDetails', false, this.initialLoadJson);
        }
      }
    );
  }


  getExpertsDetails() {
  
    // expertSubscription getExpertsDetails
    // this.expertsJson = this.clusterDetailsService.getExperts();
    // this.expertsJsonVisible = this.expertsJson.slice(0);
    let inputJson: any = {
      "resourcetypeid": this.ObservableId,
      "observableid": this.jsonComponent.observableId
    };
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getExperts(inputJson, InitialLoadCheck.getLoadInterval('getExpertsDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.platformsSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getExpertsDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.expertsJson = JSON.parse(JSON.stringify(result));
          this.expertsJsonVisible = this.expertsJson.slice(0);
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getExpertsDetails', false, this.initialLoadJson);
        }
      }
    );
  }

  getCategories(platform: string) {
  
    this.isloader = true;
    let timerObj = timer(0).pipe(
      switchMap(() => this.clusterDetailsService.getCategories(platform, InitialLoadCheck.getLoadInterval('getCategories', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    )
    this.platformsSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getCategories', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.categoriesList = result;
          this.isloader = false;
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getCategories', false, this.initialLoadJson);
        }
      },
      error => {
        console.log(error);
        this.isloader = false;
      }
    )
  }

  getNodesDetails(platfrom: string) {
    let inputJson: any = { "component": "appserver3" };
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getNodes(platfrom, inputJson, (InitialLoadCheck.getLoadInterval('getNodesDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled))
      )
    );
    this.nodesSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getNodesDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
        
          this.nodesListJson = result;
         
          // this.expertsJsonVisible = this.expertsJson.slice(0);
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getNodesDetails', false, this.initialLoadJson);
        }
      }
    );
  }

  openHistory() {

    this.planEnabled = "none";
    this.title = "History";
  }

  historyEventHandler(data) {
    this.planEnabled = data["flag"];
    // this.planEnabled = data;
    // console.log("Received data from child is : ", data);
    let id = data["id"];
    if (id != undefined && id.trim().length > 0) {
      this.getPlanHistoryStepDetails(id);
    } else {
      this.title = "Remediation Action";
    }
  }

  getPlanHistoryStepDetails(planId: string) {

    let timerObj = timer(0).pipe(
      switchMap(() => this.clusterDetailsService.getPlanHistoryStepDetails(planId, true)
      )
    );
    this.historySubscription = timerObj.subscribe(
      result => {
        this.planStepJson = JSON.parse(JSON.stringify(result));
        console.log("planHistorystep" + JSON.stringify(this.planStepJson));
      }
    );
  }

  // getActionScriptDetails() {
  //   let timerObj = timer(0, environment.interval).pipe(
  //     switchMap(() => this.clusterDetailsService.getActionScripts(InitialLoadCheck.getLoadInterval('getActionScriptDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
  //     )
  //   );
  //   this.actionScriptSubscription = timerObj.subscribe(
  //     result => {
  //       if (InitialLoadCheck.getLoadInterval('getActionScriptDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
  //         this.jsonActionScript = JSON.parse(JSON.stringify(result));
  //         this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getActionScriptDetails', false, this.initialLoadJson);
  //       }
  //     }
  //   );
  clearSearch() {
    this.searchValue = "";
    this.searchExpert("");
  }

  clearScriptSearch() {
    this.searchScriptVal = "";
    this.searchScript("");
  }

  searchExpert(value: string) {
    if (value != "") {
      this.expertsJsonVisible = this.expertsJson.filter(expert => {
        var nameHasFilterText;
        nameHasFilterText = expert.expertName.toLowerCase().indexOf(value.toLowerCase()) >= 0
        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })
    }
    else {
      this.expertsJsonVisible = this.expertsJson.slice(0)
    }
  }

  searchScript(value: string) {
    if (value != "") {

      this.jsonActionScriptVisible[0].businessProcesses = this.jsonActionScript[0].businessProcesses.filter(script => {
        var nameHasFilterText;
        nameHasFilterText = script.name.toLowerCase().indexOf(value.toLowerCase()) >= 0
        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })
    }
    else {
      try {
        this.jsonActionScriptVisible[0].businessProcesses = this.jsonActionScript[0].businessProcesses.slice(0);
      } catch (e) {
        console.log(e);
      }
    }
  }

  sanitize(url: string) {

    return this.sanitizer.bypassSecurityTrustUrl("sip:" + url);
  }

  getActionScriptDetails(platform: string, categoryId: string) {
 
    this.isloader = true;
    let timerObj = timer(0).pipe(
      switchMap(() => this.clusterDetailsService.getActionScriptsFromEnv(platform, categoryId, (InitialLoadCheck.getLoadInterval('getActionScriptDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled))
      )
    );
    this.actionScriptSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getActionScriptDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonActionScript = JSON.parse(JSON.stringify(result));
          this.isloader = false;
          this.jsonActionScriptVisible = JSON.parse(JSON.stringify(result));
          this.jsonActionScript = Object.assign([], this.jsonActionScript);
          this.jsonActionScriptVisible[0].businessProcesses = Object.assign([], this.jsonActionScriptVisible[0].businessProcesses);
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getActionScriptDetails', false, this.initialLoadJson);
        }
      },
      error => {
        console.log(error);
        this.isloader = false;
      }
    );
  }

  getActionStagesList() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getActionStages(InitialLoadCheck.getLoadInterval('getActionStagesList', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.actionStageSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getActionStagesList', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonActionStage = JSON.parse(JSON.stringify(result));
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getActionStagesList', false, this.initialLoadJson);
        }
      }
    );
  }

  getRemediationPlanDetails(serverType: string) {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getRemediationPlanDetails(serverType, this.jsonComponent.observableId, InitialLoadCheck.getLoadInterval('getRemediationPlanDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.planSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getRemediationPlanDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.remediAtionPlanjson = JSON.parse(JSON.stringify(result));

          // this.addPlanDefaults('', false);
          // console.log('after adding');
          // console.log(this.remediAtionPlanjson);
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getRemediationPlanDetails', false, this.initialLoadJson);
        }
      }
    );
  }



  getRemediationPlanActions(planId: string) {

    this.planActionSubscription = this.clusterDetailsService.getRemediationActions(planId)
      .subscribe(
        result => {
          this.remediationPlanActions = JSON.parse(JSON.stringify(result));
          console.log(this.remediationPlanActions);

          try {
            if (this.remediationPlanActions != undefined && (this.remediationPlanActions[0] != undefined && this.remediationPlanActions[0].platformname != undefined)) {
              this.selectedPlatform = this.remediationPlanActions[0].platformname;
              this.addCategory();
              this.getNodesDetails(this.selectedPlatform);
            }
          } catch (e) {
            console.log(e);
          }
          this.addDefaults();
        });
  }

  onChangePlatform(platform: string) {
  
    this.remediationPlanActions[0].platformname = platform;
    this.selectedPlatform = platform;
    try {
      if (platform != undefined && platform.trim().length > 0) {
        this.getNodesDetails(platform.trim());
      }
    } catch (e) {
      console.log(e);
    }
  }

  removeAction(action: Action) {
  
    this.actionModified = true;
    action.isdeleted = true;

    this.reOrderActionStages();
    console.log(this.remediationPlanActions);
  }

  reOrderActionStages() {
    // console.log('this.jsonAction before re order');
    // console.log(this.remediationPlanActions);
    var i = 1;
    let tempjsn = this.remediationPlanActions[0].actions;
    this.remediationPlanActions[0].actions = [];
    this.remediationPlanActions[0].actions = tempjsn.filter(action => {
      var nameHasFilterText;
      nameHasFilterText = (action.actionsequence.toString().length > 0 && action.isdeleted == false)
      if (nameHasFilterText) {
        if (action.isdeleted == false) {
          action.actionsequence = i;
          i++;
        }
        return nameHasFilterText
      }
    })
    console.log('this.jsonAction after re order');
    console.log(this.remediationPlanActions);
  }

  addRow() {
   
    this.actionModified = true;
    try {
      let tmpar = {

        remediationplanactionid: '',
        actionsequence: 0,
        actionstageid: '',
        actionstagename: '',
        actionid: 0,
        isdeleted: false,
        parameters: []
      }
      let json = this.remediationPlanActions[0].actions;
      json.push(tmpar);
      this.remediationPlanActions[0].actions = JSON.parse(JSON.stringify(json));
      var i = 1;
      this.remediationPlanActions[0].actions = this.remediationPlanActions[0].actions.filter(action => {
        var nameHasFilterText;
        nameHasFilterText = (action.actionsequence.toString().length > 0 && action.isdeleted == false)
        if (nameHasFilterText) {
          if (action.isdeleted == false) {
            action.actionsequence = i;
            i++;
          }
          return nameHasFilterText
        }
      })
      //  this.delay(100);
    } catch (e) {
      console.log(e);
    }
    // console.log(this.remediationPlanActions);
  }

  //By default this method will make all plans executing as false and
  // if we pass particular plan and flag will update with that flag
  // addPlanDefaults(planId: string, flag: boolean) {
  //   debugger
  //   try {
  //     var tempPlan = JSON.parse(JSON.stringify(this.remediAtionPlanjson))
  //     this.remediAtionPlanjson = tempPlan.filter(plan => {
  //       var nameHasFilterText = plan.remediationPlanName.length > 0
  //       if (nameHasFilterText) {
  //         if (plan.remediationPlanid == planId)
  //           plan.isPlanExecuting = flag;
  //         else
  //           plan.isPlanExecuting = false;
  //         return nameHasFilterText
  //       }
  //     })
  //   } catch (e) {
  //     console.log(e);
  //   }
  // }

  addDefaults() {

    try {
      this.remediationPlanActions[0].actions = this.remediationPlanActions[0].actions.filter(action => {
        var nameHasFilterText = action.actionid.toString.length > 0
        if (nameHasFilterText) {
          action.isdeleted = false;
          action.scriptid = 0;
          return nameHasFilterText
        }
      })
    } catch (e) {
      console.log(e);
    }
    console.log(this.remediationPlanActions);
  }

  filterActionParam(value: number) {
    this.scriptParameters = [];
    this.jsonScriptActionParam = [];
    if (value > 0) {
      this.jsonActionScriptTemp = JSON.parse(JSON.stringify(this.jsonActionScript).slice(0));
      this.jsonScriptActionParam = this.jsonActionScriptTemp[0].businessProcesses.filter(component => {
        var nameHasFilterText;
        nameHasFilterText = component.id == value
        if (nameHasFilterText) {
          return nameHasFilterText;
        }
      })
      if (this.jsonScriptActionParam.length > 0) {
        let index = 1;
        this.jsonScriptActionParam[0].inputVariables = this.jsonScriptActionParam[0].inputVariables.filter(parameter => {
          var nameHasFilterText;

          nameHasFilterText = parameter.name.length > 0
          if (nameHasFilterText) {
            parameter.value = parameter.defaultValue;
            parameter.paramid = index;
            parameter.remediationplanactionid = "0";
            parameter.defaultValue = "";
            index++;
            return nameHasFilterText
          }

        })
      }
    }
    else {
      this.jsonScriptActionParam = undefined;
    }

    if (this.jsonScriptActionParam != undefined && this.jsonScriptActionParam[0] != undefined) {
      for (var i = 0; i < this.jsonScriptActionParam[0].inputVariables.length; i++) {
        this.scriptParameters.push({ remediationplanactionid: '', paramid: this.jsonScriptActionParam[0].inputVariables[i].paramid, defaultvalue: "", providedvalue: this.jsonScriptActionParam[0].inputVariables[i].defaultValue, name: this.jsonScriptActionParam[0].inputVariables[i].name, ismandatory: this.jsonScriptActionParam[0].inputVariables[i].mandatory, isfield: false })
      }
    }
  }

  onChangeFieldParameter(value: string, param: Parameter) {
    this.actionModified = true;
    param.providedvalue = value;
  }

  changeFieldType(ischecked: boolean, parameter: Parameter) {
    this.actionModified = true;
    parameter.isfield = !ischecked;
    parameter.providedvalue = '';
  }

  onChangeParamValue(text: string) {

    this.actionModified = true;
  }

  onChangeActionScript(type: number, action: Action, ) {
    this.actionModified = true;
    action.categoryid = this.categoryIdSelected;
    action.actionid = type;
    this.filterActionParam(type);
    if (type > 0 && this.jsonScriptActionParam.length > 0) {
      action.parameters = []
      action.parameters = <Parameter[]>this.scriptParameters.slice(0);
    }
    // console.log(this.remediationPlanActions)
  }

  onChangeType($event: any, action: Action) {

    this.actionModified = true;
    action.actionstageid = $event.target.value;

    console.log($event.target.id);
  }

  navigateToPortfolio(clusterId: string) {
    this._router.navigate(['/main/clusters', clusterId]);
  }

  navigateToMoniterHealth() {
    this._router.navigate(['main/clusters', this.clusterName, this.portfolioName]);
  }

  navigateToComponentHealth() {
    this._router.navigate(['/main/clusters/components', this.clusterName, this.portfolioName]);
  }

  cancelAction() {
    this.ActionName = "";
    this.remediationPlanName = "";
  }
  onSelect(event) {
  
    console.log(event);
    this.categoryIdSelected = event.node.id;
    // this.searchScript("==");
    this.getActionScriptDetails(this.selectedPlatform.trim(), event.node.id);
  }

  // checkExecutionStatus(finalCall: boolean) {
  //   console.log('checkExecutionStatus called')
  //   debugger
  //   this.executionStatus = timer(0).pipe(
  //     switchMap(() => this.clusterDetailsService.checkExecutionActionPlan(this.runstatId))
  //   ).subscribe(
  //     result => {
  //       console.log('checkExecutionStatus got result')
  //       let statusObj: any = result;
  //       this.planExecutionStatus = statusObj.status;
  //       console.log('statusObj.status ' + statusObj.status);
  //       if (statusObj.status == 'FAILURE') {
  //         this.errorMessage("Remediation action  failed!");
  //         this.planExecution(this.remediationPlanId, false);
  //         // sessionStorage.setItem('executionPlanId',undefined);
  //         // sessionStorage.setItem('runstatId',undefined);

  //       } else if (statusObj.status == 'SUCCESS') {
  //         this.planExecution(this.remediationPlanId, false);
  //         this.successMessage("Remediation action executed successfully");
  //         // sessionStorage.setItem('executionPlanId',undefined);
  //         // sessionStorage.setItem('runstatId',undefined);
  //         this.executionStatus.unsubscribe();
  //       } else if (statusObj.status == 'ABORTED') {
  //         this.planExecution(this.remediationPlanId, false);
  //         this.errorMessage(" Remediation action aborted");

  //         // sessionStorage.setItem('executionPlanId',undefined);
  //         // sessionStorage.setItem('runstatId',undefined);
  //         this.executionStatus.unsubscribe();
  //       } else {
  //         if (finalCall)
  //           this.planExecution(this.remediationPlanId, false);
  //       }
  //     });
  // }

  onChangeNode(nodeId: string) {
 
    this.selectedNodeId = nodeId;
  }

  executeAction() {
  
    this.enableCredDiv = true;
    var e = <HTMLSelectElement>document.getElementById("server_nodes");
    var selectedNode = e.options[e.selectedIndex].value;
    let formcred: boolean = true

    // if(this.remediationPlanActions[0]!=null && this.remediationPlanActions[0]!= undefined && (this.remediationPlanActions[0].remediationplanname=="Demo Update Healthy" || this.remediationPlanActions[0].remediationplanname=="Demo Revert") ){
    //  var providedvalue;
    //   this.remediationPlanActions[0].actions.forEach(action=>{
    //     action.parameters.forEach(param=>{
    //       providedvalue= param.providedvalue;
    //     })

    //   });
    //   // providedvalue
    //   let timerObj = timer(0).pipe(
    //     switchMap(() => this.clusterDetailsService.changeServerState(true,providedvalue)
    //     )
    //   );

    //     timerObj.subscribe(
    //     result => {
    //       let res: string = result["Satus"];
    //       if (res!= null && res!= undefined && res=="Success") {
    //         this.successMessage("Health changed successfully");
    //         $("#actionModal").modal("hide");
    //       }
    //     }
    //   );

    // }else{
    if (this.username.trim().length == 0) {
      formcred = false;
      this.usernameFlag = true;
      //this.errorMessage("Please enter username");
    } else {
      this.usernameFlag = false;
    }
    if (this.password.trim().length == 0) {
      formcred = false;
      this.passwordFlag = true;
      //this.errorMessage("Please enter password");
    } else {
      this.passwordFlag = false;
    }
    if (selectedNode.trim().length == 0 || selectedNode == "0") {
      formcred = false;
      this.nodeFlag = true;
      //this.errorMessage("Please select node");
    } else {
      this.nodeFlag = false;
    }



    if (formcred) {
      $("#actionModal").modal("hide");
      this.actionModified = false;
      // let curPlanid = sessionStorage.getItem('executionPlanId');
      if (true) { //curPlanid==undefined

        this.remediationPlanName = "";
        this.saveActionPlan = [
          {
            remediationplanid: '',

            scriptdetails: []
          }
        ];
        // this.nodesJson = [{ id: 0, ipAddress: null, name: null, os: null, password: null, serviceAreas: [], stagePath: null, userId: null, workingDir: null }]
        this.saveActionPlan[0].remediationplanid = this.remediationPlanActions[0].remediationplanid.toString();
        // for (var k = 0; k < this.remediationPlanActions[0].actions.length; k++) {

        //   this.scriptdetailsJson = {

        //     Parameters: []
        //   };

        //   this.scriptdetailsJson.CategoryId = this.remediationPlanActions[0].actions[k].categoryid;
        //   this.scriptdetailsJson.ScriptId = parseInt(this.remediationPlanActions[0].actions[k].actionid);

        //   this.scriptdetailsJson.Password = this.password;
        //   this.scriptdetailsJson.ReferenceKey = this.remediationPlanActions[0].platformname;
        //   this.scriptdetailsJson.UserName = this.username;
        //   this.scriptdetailsJson.WEMScriptServiceUrl = null;
        //   this.scriptdetailsJson.RemoteServerNames = this.selectedNodeId;
        //   this.scriptdetailsJson.ExecutionMode = 7;
        //   this.scriptdetailsJson.Domain = "";
        //   this.scriptdetailsJson.IapNodeTransport = "";

        //   this.saveActionParameters = [];
        //   this.scriptdetailsJson.Parameters = [];
        //   for (var l = 0; l < this.remediationPlanActions[0].actions[k].parameters.length; l++) {
        //     this.saveActionParameters.push({ ParameterName: this.remediationPlanActions[0].actions[k].parameters[l].name, ParameterValue: this.remediationPlanActions[0].actions[k].parameters[l].providedvalue })
        //   }
        //   this.scriptdetailsJson.Parameters = this.saveActionParameters;

        //   this.scriptdetailsJson.Parameters.push({ ParameterName: "rpe_correlationid", ParameterValue: this.remediationPlanActions[0].actions[k].actionsequence.toString() })
        //   let identifier: Identifier = { scriptIdentifier: {} };
        //   //this.scriptdetailsJson.Nodes = this.nodesJson;
        //   let scriptDetails: any = JSON.parse(JSON.stringify(this.scriptdetailsJson))
        //   identifier.scriptIdentifier = scriptDetails;
        //   this.saveActionPlan[0].scriptdetails[k] = identifier;
        // }
        // console.log(JSON.stringify(this.saveActionPlan[0]));
        // this.executionPlanId = this.remediationPlanActions[0].remediationplanid.toString();
        // this.planExecution(this.remediationPlanId, true);
        this.executeJson = {
          remediationplanid: this.remediationPlanActions[0].remediationplanid.toString(),
          resourceid: this.resourceID,
          observableid: this.jsonComponent.observableId,
          domain: "",
          iapnodetransport: 0,
          username: this.username,
          password: this.password,
          referencekey: this.remediationPlanActions[0].platformname,
          remoteservernames: this.selectedNodeId

        };

        console.log(JSON.stringify(this.executeJson));
        timer(0).pipe(
          switchMap(() => this.clusterDetailsService.executeActionPlan(JSON.stringify(this.executeJson)))
        ).subscribe(
          result => {
            if (result != undefined) {
              let res: boolean = result["queued"];
              console.log(res);

              if (res)
                this.successMessage(this.ActionName + " Submitted successfully.");
              this.ActionName = "";
              // this.checkExecutionStatus(false);

              // if (this.planExecutionStatus == undefined || (this.planExecutionStatus != undefined && this.planExecutionStatus != 'SUCCESS')) {
              //   this.delay(20000);
              // }
            }
          });
      }
    }


    // 
  }
  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
  // async delay(ms: number) {
  //   await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => this.checkExecutionStatus(true));
  // }

  //Change plan execution flag
  // planExecution(planId: string, flag: boolean) {
  //   debugger
  //   try {
  //     let tempPlan: any = JSON.parse(JSON.stringify(this.remediAtionPlanjson))
  //     tempPlan.filter(plan => {
  //       var nameHasFilterText = plan.remediationPlanName.length > 0
  //       if (nameHasFilterText) {
  //         if (plan.remediationPlanid == planId)
  //           plan.isPlanExecuting = flag;
  //         else {
  //           plan.isPlanExecuting = plan.isPlanExecuting;
  //         }
  //       }
  //     })
  //     this.remediAtionPlanjson = tempPlan.slice(0);
  //   } catch (e) {
  //     console.log(e);
  //   }
  // }
  //TO_DO:Testing purpose sample service. remove after testing
  // getPlanExecutionStatus(planid: string) {

  //   let timerObj = timer(0, environment.interval).pipe(
  //     switchMap(() => this.clusterDetailsService.getParameterType()
  //     )
  //   );
  //   this.planSubscription = timerObj.subscribe(
  //     result => {
  //       let planid;
  //        this.planExecution(planid, false);

  //     }
  //   );
  // }

  openModel(action: string, planId: string) {
    this.actionNameReq = false;
    this.selectedPlatform = "";
    this.username = "";
    this.password = "";
    this.usernameFlag = false;
    this.passwordFlag = false;
    this.nodeFlag = false;
    this.enableScriptView = false;
    this.showCredsDiv = false;
    this.remediationPlanId = planId;
    if (planId == '0')
      this.actionModified = true;
    else
      this.actionModified = false;
    this.remediationPlanActions = [];
    this.ActionName = action;
    console.log('planId' + planId);
    if (action == '') {
      this.remediationPlanActions = [
        {
          remediationplanid: 0,
          remediationplanname: '',
          actions: []
        }
      ];
    } else {
      this.getRemediationPlanActions(planId);

    }
  }
  validateUsername(username: string) {
    //  this.actionModified = true;

  }
  validatePassword(password: string) {

    if (this.username.length > 0)
      this.actionModified = false;
  }
  validatePlanName(planName: string) {
    //TO-Do un comment this
    this.actionModified = true;
    if (this.ActionName.toLocaleLowerCase().trim().length > 0 && this.ActionName.toLocaleLowerCase().trim() == this.remediationPlanName.toLocaleLowerCase().trim()) {
      this.validPlanName = false;
      //
      document.getElementById('ActionName').className = 'form-control ng-pristine ng-invalid ng-touched';
    } else {
      this.validPlanName = true;
      document.getElementById('ActionName').className = 'form-control ng-pristine ng-valid ng-touched';
    }
  }

  saveAction(validForm: boolean) {
  
    this.actionNameReq = true;
    if (validForm || this.validPlanName) {
      if (this.remediationPlanName.toLocaleLowerCase().trim().length == 0 || this.ActionName.toLocaleLowerCase().trim() == this.remediationPlanName.toLocaleLowerCase().trim()) {
        if (this.remediationPlanName.toLocaleLowerCase().trim().length == 0) {
          // this.errorMessage("Action name can't be empty");
          this.actionNameReq = true;
        }
        else {
          if (this.remediationPlanId != '0')
            this.errorMessage("Action name should not be same as existing Action name");
        }

      } else if (validForm) {


        $("#actionModal").modal("hide");
        this.remediationPlanActions[0].remediationplanid = 0;
        this.remediationPlanActions[0].isuserdefined = true;

        this.remediationPlanActions[0].servertype = this.ObservableId;
        //this.remediationPlanActions[0].platformname="NIA";
        this.remediationPlanActions[0].observabletype = this.jsonComponent.observableId;

        this.remediationPlanActions[0].remediationplanname = this.remediationPlanName;
        //remove all deleted flag actions from json
        this.remediationPlanActions[0].actions = this.remediationPlanActions[0].actions.filter(action => {
          var nameHasFilterText;

          nameHasFilterText = action.isdeleted == false
          if (nameHasFilterText) {
            return nameHasFilterText
          }
        })
        //remove all deleted flag actions from json
        this.remediationPlanActions[0].actions = this.remediationPlanActions[0].actions.filter(action => {
          var nameHasFilterText;

          nameHasFilterText = action.isdeleted == false
          if (nameHasFilterText) {
            return nameHasFilterText
          }
        })
        console.log('this.jsonAction for save');
        console.log(Object.assign({}, this.remediationPlanActions));
        this.ActionName = "";
        this.remediationPlanName = "";
        console.log(JSON.stringify(this.remediationPlanActions));
        this.clusterDetailsService.saveRemediationPlanDetails(JSON.stringify(this.remediationPlanActions))
          .subscribe(
            result => {
              console.log(result);
              this.successMessage("Action Saved successfully");
            });


      }
    } else {
      if (this.validPlanName == false) {
        if (this.remediationPlanName.toLocaleLowerCase().trim().length == 0) {
          //this.errorMessage("Action name can't be empty");
        }
        else
          this.errorMessage("Action name should not be same as existing Action name");
      }
      else
        this.errorMessage("Please fill all mandatory fields");
    }
  }

  errorMessage(message: string) {
    $.bootstrapGrowl(message, {
      type: 'danger',
      delay: 3000,
    });
  }

  successMessage(message: string) {
    $.bootstrapGrowl(message, {
      type: 'success',
      delay: 3000,
    });
  }

  openRecorder() {
    window.open('file:///D:/recorder/Infosys.ATR.UIAutomation.Recorder.exe');
  }

  selectScript(id: number, scriptName: string) {
 
    this.actionScriptRef.actionname = scriptName;
    this.onChangeActionScript(id, this.actionScriptRef);
    this.enableScriptView = false;
  }

  browseScript(action: Action) {
    if (this.selectedPlatform.trim().length > 0) {
      this.categoriesList = [];
      this.getCategories(this.selectedPlatform.trim());
      this.actionScriptRef = action;
      this.enableScriptView = true;
      this.jsonActionScriptVisible = { businessProcesses: [] };
    }


  }

  retryPlanExecution(planId: string) {
    var inputjson: any = {
      "planid": planId,
      "username": this.portalUser
    }

    let timerObj = timer(0).pipe(
      switchMap(() => this.clusterDetailsService.retryPlanExecution(inputjson, planId, true)
      )
    );

    this.retrySubscription = timerObj.subscribe(
      result => {
        let res: string = result["queued"];
        if (res != null && res != undefined && res == "true") {
          this.successMessage("Plan queued for execution");
        }
      }
    );
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    this.planSubscription.unsubscribe();
    this.actionStageSubscription.unsubscribe();
    //this.actionScriptSubscription.unsubscribe();
    // try {
    //   this.planActionSubscription.unsubscribe();
    // } catch (e) {
    //   console.log(e);
    // }

  }
}

interface actionRunStat {
  planId: string;
  runStatId: number;
}