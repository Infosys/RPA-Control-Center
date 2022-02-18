import { Component, OnInit, OnDestroy } from '@angular/core';
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
// import { forEach } from '@angular/router/src/utils/collection';
import { debug } from 'util';
import { DataService } from "../cluster-details/data.service";
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { ComponentList,Observations } from '../component-health/components-list.model';
import { Chart } from 'chart.js';
import { Subscription, timer, pipe, ObjectUnsubscribedError } from 'rxjs';

import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment'
import { InitialLoadCheck } from '../cluster-details/initial-load.model'
import { JSONP_HOME } from '@angular/http/src/backends/browser_jsonp';
declare var jquery: any;
declare var $: any;
@Component({
  selector: 'app-component-health',
  templateUrl: './component-health.component.html',
  styleUrls: ['./component-health.component.scss']
})
export class ComponentHealthComponent implements OnInit, OnDestroy {

  subscription: Subscription;
  recentSubscription: Subscription;
  portfolioSubscription: Subscription;
  tktSubscription: Subscription;
  componentSubscription: Subscription;
  parameterSubscription: Subscription;
  jsonComponentHistoryData: any;
  inputResources: any = "";
  checkedElements: string[] = [];
  checkedElementsTemp: string[] = [];
  healthyCount: number = 0;
  warningCount: number = 0;
  criticalCount: number = 0;
  cardWarningCount: number = 0;
  cardCriticalCount: number = 0;
  countOfChecks: number = 0;
  serverCriticalCount: number = 0;
  serverWarnCount: number = 0;
  dbserverCriticalCount: number = 0;
  dbserverWarnCount: number = 0;
  otherCriticalCount: number = 0;
  otherWarnCount: number = 0;
  portfolioId: string;
  incidentId: string;
  jsonResourceData: any;
  jsonComponentData: any;
  componentDataVisible: any;
  componentDataVisibleTemp: any = [];
  componentDataVisibleTotal: any = [];
  selectedComponents: any;
  componentDataTemp: any;
  componentDataVisible_total: any;
  portfolioName: string;
  clusterName: string;
  jsonPortfolioData: any;
  componentDataFilter: any;
  jsonComponentDataFilter: any;
  jsonComponentDataFilterTemp: any = [];
  jsonComponentDataFilterVisible: any;
  jsonComponentDataFilterVisibleType: any;
  componentDataFinalHealth: any
  jsonParameterType: any;
  jsonRecentComponentData: any;
  componentState: string;
  resourceState: string;
  selectedGrpahData: any;
  selectedDataArray: KeyValue[] = [];
  tableFlag: number = 0;
  searchValue: string = ""
  doughnutChartLabels: string[] = ['Database', 'Server', 'Other'];
  doughnutChartDataCritical: number[];
  doughnutChartDataWarning: number[];
  doughnutChartType: string = 'doughnut';
  doughnutChartColors_Critical: {} = ['#f50028', '#ff8fa1', '#ff4b69'];
  //new code
  criticalStates:any[] = [];
  //end
  doughnutChartColors_Warning: {} = ['#ffcd7e', '#ffb23a', '#e48b00'] ;
  doughnutChartColors_dummy: {} = ['#485d69'] ;
  doughnutChartDataDummy: number[] = [1];
  options_critical = { legend: false, elements: { arc: { borderWidth: 0 } } };
  options_warning = { legend: false, elements: { arc: { borderWidth: 0 } } };
  optionsDummy = {
    legend: false,
    tooltips: {
      enabled: false
    }, elements: { arc: { borderWidth: 0 } }
  };

  column;
  isDesc: boolean = true;
  direction;
  
  chartClicked(e: any): void {
    console.log(e);
  }
  public chartHovered(e: any): void {
    console.log(e);
  }
  initialLoadJson: InitialLoadCheck[] = [
    { service: "getPortfolioDetails", initialLoad: false },
    { service: "getTicketDetails", initialLoad: false },
    { service: "getRecentComponents", initialLoad: false }
  ];
  constructor(private clusterDetailsService: ClusterDetailsService, private data: DataService, private _router: Router, private route: ActivatedRoute) { }

  getPortfolioDetails(portfolioName: string) {


  let timerObj = timer(0, environment.interval).pipe(
    switchMap(() => this.clusterDetailsService.getPortfolioIncidentDetails(portfolioName,InitialLoadCheck.getLoadInterval('getPortfolioDetails',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
    )
  );
  this.portfolioSubscription = timerObj.subscribe(
    result => {
      if (InitialLoadCheck.getLoadInterval('getPortfolioDetails',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
        this.jsonPortfolioData = result;
        this.jsonResourceData = this.jsonPortfolioData;
        this.clusterDetailsService.somevent.emit(this.jsonPortfolioData);
        this.clusterDetailsService.saveData(this.jsonPortfolioData);
        this.setValues();
        
        this.getRecentComponents();
        this.getTicketDetails();
     
        this.initialLoadJson= InitialLoadCheck.chnageInitialLoad('getPortfolioDetails', false,this.initialLoadJson);
      }
    }
  );

    

  }
  ngOnInit() {

    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);

    //this.data.currentGraph.subscribe(message => this.selectedGrpahData = message)
    this.portfolioName = this.route.snapshot.params['portfolioId'];
    this.clusterName = this.route.snapshot.params['clusterId'];
    this.subscription = this.route.params.subscribe(params => {

      this.portfolioName = params['portfolioId'];
      this.clusterName = params['clusterId'];
      // this.service.get(term).then(result => { console.log(result); });
      //this.getPortfolioDetails(this.portfolioName);
    });

    this.getPortfolioDetails(this.portfolioName);
    this.jsonResourceData = this.clusterDetailsService.getData();
    this.setValues();
    this.getTicketDetails();
    this.getRecentComponents();
    
    $(document).ready(function () {
      $('[data-toggle="tooltip"]').tooltip();
    });
  }

  setValues() {
    try {
      this.portfolioId = this.jsonResourceData.resourceId;
      this.incidentId = this.jsonResourceData.statusByDate[0].incidentId;
      this.countOfChecks = this.jsonResourceData.statusByDate[0].components;
      this.criticalCount = this.jsonResourceData.criticalCount;
      this.warningCount = this.jsonResourceData.warningCount;
      this.healthyCount = this.jsonResourceData.healthyCount;
      this.cardCriticalCount = this.jsonResourceData.statusByDate[0].stateCount.critical;
      this.cardWarningCount = this.jsonResourceData.statusByDate[0].stateCount.warning;
      this.dbserverCriticalCount = this.jsonResourceData.statusByDate[0].stateCount.dbserverCritical;
      this.serverCriticalCount = this.jsonResourceData.statusByDate[0].stateCount.serverCritical;
      this.otherCriticalCount = this.jsonResourceData.statusByDate[0].stateCount.otherCritical;
      this.serverWarnCount = this.jsonResourceData.statusByDate[0].stateCount.serverWarn;
      this.dbserverWarnCount = this.jsonResourceData.statusByDate[0].stateCount.dbserverWarn;
      this.otherWarnCount = this.jsonResourceData.statusByDate[0].stateCount.otherWarn;

      this.filterCriticalDetails(this.jsonResourceData.statusByDate[0].stateCount);

      

      if (this.warningCount == 0) {
        this.doughnutChartDataWarning = this.doughnutChartDataDummy;
        this.doughnutChartColors_Warning = this.doughnutChartColors_dummy;
        this.options_warning = this.optionsDummy;
      } else {
        this.doughnutChartDataWarning = [this.dbserverWarnCount, this.serverWarnCount, this.otherWarnCount];
        this.doughnutChartColors_Warning = ['#ffcd7e', '#ffb23a', '#e48b00'];
        this.options_warning = { legend: false, elements: { arc: { borderWidth: 0 } } };
      }
      if (this.criticalCount == 0) {
        this.doughnutChartDataCritical = this.doughnutChartDataDummy;
        this.doughnutChartColors_Critical = this.doughnutChartColors_dummy;
        this.options_critical = this.optionsDummy;
      } else {
        this.doughnutChartDataCritical = [this.dbserverCriticalCount, this.serverCriticalCount, this.otherCriticalCount];
        this.doughnutChartColors_Critical = ['#f50028', '#ff8fa1', '#ff4b69'] ;
        this.options_critical = { legend: false, elements: { arc: { borderWidth: 0 } } };
      }
      //this.portfolioName = this.jsonResourceData.resourceId;

      this.generateGraph(this.doughnutChartDataCritical, this.options_critical, 'canvas_critical', this.doughnutChartLabels, this.doughnutChartColors_Critical);
      this.generateGraph(this.doughnutChartDataWarning, this.options_warning, 'canvas_warning', this.doughnutChartLabels, this.doughnutChartColors_Warning)

    } catch (e) {

    }
  }

  filterCriticalDetails(stateCount:any){
    this.criticalStates = []; //emptying the array to fill up new data
    for(let kk of Object.keys(stateCount)){
      if(kk.indexOf('Critical') !== -1 && kk != 'critical'){
        this.criticalStates.push({"name":kk,"value":stateCount[kk]});  
      }
    }
  }
  generateGraph(data_set: any, options: any, id: string, labels_doughnut: any, chartColors: any) {
    
    var canvas = <HTMLCanvasElement>document.getElementById(id);
    var ctx = canvas.getContext("2d");
   

    var myDoughnutChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        datasets: [{
          data: data_set,
          backgroundColor: chartColors
        }],
        labels: labels_doughnut,
      },
      options: options,
    });
  }
  getTicketDetails() {

    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getComponentDetails(this.portfolioId, this.incidentId,InitialLoadCheck.getLoadInterval('getTicketDetails',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.tktSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getTicketDetails',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonComponentData = result;
          this.jsonComponentDataFilter = this.jsonComponentData;
          this.componentDataFilter = this.jsonComponentData;
          this.componentDataVisible = this.jsonComponentData;
          this.jsonComponentDataFilterVisible = this.jsonComponentDataFilter;
          this.componentDataFinalHealth = this.jsonComponentDataFilter.slice(0);
          this.jsonComponentDataFilterVisibleType = this.jsonComponentDataFilterVisible;
          console.log("Component Data :: ", this.jsonComponentData);
          this.initialLoadJson= InitialLoadCheck.chnageInitialLoad('getTicketDetails', false,this.initialLoadJson);
        }
      }
    );



  }
  
  getParameterType() {
    // this.tableFlag = 1;

    this.parameterSubscription = timer(0, 600000).pipe(
      switchMap(() => this.clusterDetailsService.getParameterType())
    ).subscribe(
      result => {
       
        this.jsonParameterType = result;
        console.log("parameter type Data :: ", this.jsonParameterType);
      });



    this.checkedElementsTemp = this.checkedElementsTemp.concat(this.checkedElements);
    this.unCheckDropdowns();
    console.log("parameter type Data :: ", this.jsonParameterType);
  }

  getRecentComponents() {
    this.componentSubscription = timer(0, 600000).pipe(
      switchMap(() => this.clusterDetailsService.getComponentDetails(this.portfolioId, this.incidentId,true))
    ).subscribe(
      result => {
        if ( this.clusterDetailsService.isAutoRefreshEnabled) {
        this.jsonComponentData = result;
        this.jsonComponentDataFilterVisible = this.jsonComponentData;
        console.log("Component Data :: ", this.jsonComponentData);
      }});


      let timerObj = timer(0, environment.interval).pipe(
        switchMap(() => this.clusterDetailsService.getRecentComponents(this.portfolioId, this.incidentId,InitialLoadCheck.getLoadInterval('getRecentComponents',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
        )
      );
      this.recentSubscription = timerObj.subscribe(
        result => {
          if (InitialLoadCheck.getLoadInterval('getRecentComponents',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
            this.jsonRecentComponentData = result;
            this.initialLoadJson= InitialLoadCheck.chnageInitialLoad('getRecentComponents', false,this.initialLoadJson);
          }
        }
      );

  }

  changeClass(strcls: string, id: any, isChecked: any) {
    if (strcls == "img-tbl-unchk" && isChecked == 0) {
      document.getElementById(id).classList.toggle("img-tbl-chk");
    } else if ((strcls == "img-tbl-chk" || strcls == "img-tbl-unchk img-tbl-chk") && isChecked == 1) {
      document.getElementById(id).classList.toggle("img-tbl-chk");
    }
  }


  toggleMainheaderCheck($event: any, count: any) {

    let id = $event;

    if (id != undefined) {
      var strcls = document.getElementById(id).className;
      try {
        document.getElementById(id).classList.toggle("img-tbl-chk");
      } catch (e) {
      }
      if (strcls == "img-tbl-unchk") {
        var i: any;
        for (i = 0; i < count; i++) {

          if (id != undefined) {
            try {
              strcls = document.getElementById(i).className;
              this.changeClass(strcls, i, 0);
              this.checkBoxCheck(i + 1, i);
            } catch (e) {

            }
          }
        }
      } else {
        try {
          for (i = 0; i < count; i++) {
            if (id != undefined) {
              strcls = document.getElementById(i).className;
              this.changeClass(strcls, i, 1);

              this.checkBoxUnCheck(i + 1, i);
            }
          }
        } catch (e) {

        }

      }
    }
  }
  toggleSubTable($event: any, row: number) {


    let id = $event.currentTarget.id;
    if (id != undefined) {
      document.getElementById(id).classList.toggle("img-table-minus");
    }
    let row_id = "row_" + row;
    document.getElementById(row_id).classList.toggle("row-border");
  }
  checkedCount(rowCount, num) {
    var count = 0;
    let strCls;
    var id;
    for (var i = 0; i < rowCount; i++) {
      id = "sub_row_chk_" + num + i;
      strCls = document.getElementById(id).className;
      try {
        if (strCls == "img-tbl-chk" || strCls == "img-tbl-unchk img-tbl-chk") {
          count++;
        }
      } catch (e) {

      }
    }

    return count;
  }

  toggleSubTableCheck($event: any, count: any, i: any, j: any) {

    let id = $event.currentTarget.id;
    document.getElementById(id).classList.toggle("img-tbl-chk");

    let checked = this.checkedCount(count, i);
    var strcls1 = document.getElementById(i).className;
    if (checked >= 1) {
      if (strcls1 == "img-tbl-chk" || strcls1 == "img-tbl-unchk img-tbl-chk") {

      } else {
        document.getElementById(i).classList.toggle("img-tbl-chk");
      }

    } else {
      if (strcls1 == "img-tbl-chk" || strcls1 == "img-tbl-unchk img-tbl-chk") {
        document.getElementById(i).classList.toggle("img-tbl-chk");
      }
    }
  }
  checkBoxCheck(count: number, id: any) {
    var j: number;
    var sub_id: string;
    for (j = 0; j <= count; j++) {
      sub_id = "sub_row_chk_" + id + j;
      if (sub_id != undefined) {
        try {
          var strcls = document.getElementById(sub_id).className;
        } catch (e) {

        }
        if (strcls == "img-tbl-unchk") {
          try {
            document.getElementById(sub_id).classList.toggle("img-tbl-chk");
          } catch (e) {
          }
        }
      }
    }
  }
  checkBoxUnCheck(count: number, id: number) {
    var j: number;
    var sub_id: string;
    for (j = 0; j <= count; j++) {
      sub_id = "sub_row_chk_" + id + j;
      try {
        var strcls = document.getElementById(sub_id).className
      } catch (e) {

      }
      if (strcls == "img-tbl-chk" || strcls == "img-tbl-unchk img-tbl-chk") {
        if (sub_id != undefined) {

          try {
            document.getElementById(sub_id).classList.toggle("img-tbl-chk");
          } catch (e) {

          }
        }
      }
    }
  }
  toggleTableCheck($event: any, row: any, count: any) {

    let id = $event.currentTarget.id;
    var strcls;
    document.getElementById(id).classList.toggle("img-tbl-chk");
    var str = document.getElementById(id).className;
    if (str == "img-tbl-unchk") {
      this.checkBoxUnCheck(3, id);
      // const index: number = this.checkedElements.indexOf(row);
      // if (index !== -1) {
      //     this.checkedElements.splice(index, 1);
      // }   
    } else {
      this.checkBoxCheck(3, id);
    }
  }

  popupOkFun(length: number) {
    this.checkedElements = [];
    this.checkedElements = this.checkedElements.concat(this.checkedElementsTemp);
    if (this.checkedElements.length == 0) {
      this.tableFlag = 0;
    } else {
      this.tableFlag = 1;
    }
    this.checkedElementsTemp = [];
  }
  popupCancelFun() {

    if (this.checkedElements.length == 0) {
      this.tableFlag = 0;
    } else {
      this.tableFlag = 1;
    }
    this.checkedElementsTemp = [];
    // this.checkedElements=[];
  }
  toggleTablePopupCheck($event, row: number, data: string) {

    let id = $event.currentTarget.id;
    var strcls;
    document.getElementById(id).classList.toggle("img-tbl-chk");
    var str = document.getElementById(id).className;
    if (str == "img-tbl-unchk") {
      this.checkBoxUnCheck(3, id);
      const index: number = this.checkedElementsTemp.indexOf(data);
      if (index !== -1) {
        this.checkedElementsTemp.splice(index, 1);
      }
    } else {
      this.checkedElementsTemp.push(data);
    }
  }
  clearCheckedElements() {
    this.checkedElements = [];
    this.tableFlag = 0;
  }
  deleteComponentBox(value: any) {

    const index: number = this.checkedElements.indexOf(value);
    try {
      if (index !== -1) {
        this.checkedElements.splice(index, 1);

        // this.checkedElements.splice(index, 1);
      }
    } catch (e) {

    }
    if (this.checkedElements.length == 0) {
      this.tableFlag = 0;
    } else {
      this.tableFlag = 1;
    }
  }

  sortByHeader(property: any, length: any) {


    this.isDesc = !this.isDesc; //change the direction    
    this.column = property;
    this.direction = this.isDesc ? 1 : -1;

    this.sortBycloseCheckbox(length);
  }


  sortBycloseCheckbox(length: number) {

    var i, k: any;
    let id, strcls;

    let count = 0;
    var strrowcls;
    for (k = 0; k < length; k++) {
      id = "sub_tbl_row" + k;
      let row_id = "row_" + k;
      strcls = document.getElementById(id).className;
      if (strcls != undefined && strcls.trim().search("show") != -1) {
        document.getElementById(id).classList.toggle("show");
      }
      try {
        strrowcls = document.getElementById(row_id).className;
      } catch (e) {

      }
      if (strrowcls != undefined && strrowcls.trim().search("row-border") != -1) {
        document.getElementById(row_id).classList.toggle("row-border");
      }

    }


    for (i = 0; i < length; i++) {
      id = "row" + i;
      strcls = document.getElementById(id).className;
      if (strcls != undefined && strcls.trim() == "img-table-plus img-table-minus") {
        document.getElementById(id).classList.toggle("img-table-minus");
      }
    }

  }

  getComponentValues(index: number) {


    var id, strcls;

    this.selectedComponents = JSON.parse(JSON.stringify(this.componentDataVisible));
    for (var i = 0; i < this.selectedComponents.length; i++) {
      if (i == index) {
        for (var j = 0; j < this.selectedComponents[i].observations.length; j++) {
          id = 'sub_row_chk_' + i + j;
          try {
            strcls = document.getElementById(id).className;
            if ((strcls == "img-tbl-chk" || strcls == "img-tbl-unchk img-tbl-chk")) {
              this.selectedDataArray.push({ resourceid: this.selectedComponents[i].resourceId, observableid: this.selectedComponents[i].observations[j].observableId });
            }
          } catch (e) {
            break;
          }

        }

      }
    }
  }
  openWindow() {
    this.selectedDataArray = [];

    var i, j, id, strcls, length = this.componentDataVisible.length;

    for (i = 0; i < length; i++) {
      try {
        strcls = document.getElementById(i).className;
      } catch (e) {

      }

      if ((strcls == "img-tbl-chk" || strcls == "img-tbl-unchk img-tbl-chk")) {
        this.getComponentValues(i);
      }
    }


    var myJsonString = JSON.stringify(this.selectedDataArray);
    // console.log("selected graph data", myJsonString);
    localStorage.setItem('selectedGraphData', myJsonString);
    localStorage.setItem('portfolioId', this.portfolioId);
    localStorage.setItem('incidentId', this.incidentId);
    
    
  }


  clearSearch() {
    this.searchValue = "";
    this.searchComponent("");


  }
  getRecent(value: string) {
    this.componentDataVisible_total = [];
    if (this.componentDataTemp != null) {
      this.componentDataVisible_total = this.componentDataTemp.filter(component => {
        var nameHasFilterText;

        nameHasFilterText = component.resourceId.toLowerCase().indexOf(value.toLowerCase()) >= 0

        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })
    }
  }
  filterMainTableRecent() {
    let filteredData: any;
    let componentDataVisible_tmp: any = [];
    let tempArray = JSON.parse(JSON.stringify(this.jsonRecentComponentData));
    let testData = new Set<string>();

    for (var i = 0; i < tempArray.length; i++) {
      testData.add(tempArray[i].resourceId);
    }
    filteredData = Array.from(testData);

    this.componentDataTemp = JSON.parse(JSON.stringify(this.componentDataFilter));
    for (var i = 0; i < filteredData.length; i++) {
      this.getRecent(filteredData[i]);
      componentDataVisible_tmp = componentDataVisible_tmp.concat(this.componentDataVisible_total.slice(0))
    }
    this.componentDataVisible = componentDataVisible_tmp.slice(0);

  }


  filterMainTable(value: string) {
    this.componentDataTemp = JSON.parse(JSON.stringify(this.componentDataFilter));
    if (value != "" && value.trim() != "all") {
      this.componentDataVisible = this.componentDataTemp.filter(component => {
        var nameHasFilterText;
        nameHasFilterText = component.componentHealth.toLowerCase().indexOf(value.toLowerCase()) >= 0
        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })

    }
    else {
      this.componentDataVisible = this.componentDataTemp.slice(0)
    }
  }


  getData(value: string, dropDown: string) {
    this.componentDataVisibleTemp = [];
    if (this.jsonComponentDataFilterTemp != null) {
      this.componentDataVisibleTemp = this.jsonComponentDataFilterTemp.filter(component => {
        var nameHasFilterText;
        if (dropDown == "health") {
          nameHasFilterText = component.componentHealth.toLowerCase().indexOf(value.toLowerCase()) >= 0
        } else {
          nameHasFilterText = component.resourcetypeId.toLowerCase().indexOf(value.toLowerCase()) >= 0
        }

        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })
    }
  }

  unCheckDropdowns() {
    var typeId, typeClass, selected = 0;
    document.getElementById('popup_health_h').className = "img-tbl-unchk";
    document.getElementById('popup_health_c').className = "img-tbl-unchk";
    document.getElementById('popup_health_w').className = "img-tbl-unchk";
    let jsontemp: any[] = [];

    jsontemp = this.jsonParameterType; JSON.parse(JSON.stringify(this.jsonParameterType || null));

    for (var k = 0; k < jsontemp.length; k++) {
      typeId = 'popup_type_' + k;
      document.getElementById(typeId).className = "img-tbl-unchk";
    }
  }

  //search component
  searchComponent(value: string) {

    this.unCheckDropdowns();
    if (value != "" && value.trim() != "all") {
      this.checkedElementsTemp = [];
      this.jsonComponentDataFilterVisible = this.jsonComponentDataFilter.filter(component => {
        var nameHasFilterText;
        nameHasFilterText = component.resourceId.toLowerCase().indexOf(value.toLowerCase()) >= 0
        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })

    }
    else {
      this.jsonComponentDataFilterVisible = this.jsonComponentDataFilter.slice(0)
    }
  }
  filterBy(value: string, flag: string) {

    this.checkedElementsTemp = [];
    this.searchValue = "";

    this.componentDataVisibleTemp = [];
    this.componentDataVisibleTotal = [];
    let jsontemp: any[] = [];
    if (flag.trim() == "type") {

      this.jsonComponentDataFilterTemp = JSON.parse(JSON.stringify(this.componentDataFinalHealth) || null);
      jsontemp = this.jsonParameterType; JSON.parse(JSON.stringify(this.jsonParameterType) || null);

      var typeId, typeClass, selected = 0;
      //toggle checkbox
      for (var k = 0; k < jsontemp.length; k++) {
        typeId = 'popup_type_' + k;
        if (value.trim() == jsontemp[k].resourceTypeID) {
          document.getElementById(typeId).classList.toggle("img-tbl-chk")
          break;
        }

      }
      // collect data of all checked
      for (var k = 0; k < jsontemp.length; k++) {

        typeId = 'popup_type_' + k;
        typeClass = document.getElementById(typeId).className;
        if ((typeClass == "img-tbl-chk" || typeClass == "img-tbl-unchk img-tbl-chk")) {
          this.getData(jsontemp[k].resourceTypeID, 'type');
          this.componentDataVisibleTotal = this.componentDataVisibleTotal.concat(this.componentDataVisibleTemp.slice(0));
          selected++;

        }

      }
      this.jsonComponentDataFilterVisible = this.componentDataVisibleTotal.slice(0)
      if (selected == 0) {

        this.jsonComponentDataFilterVisible = this.componentDataFinalHealth.slice(0)
      }

    } else {
      if (value == "healthy") {
        document.getElementById('popup_health_h').classList.toggle("img-tbl-chk");
      } else if (value == "Critical") {
        document.getElementById('popup_health_c').classList.toggle("img-tbl-chk");
      } else if (value == "Warning") {
        document.getElementById('popup_health_w').classList.toggle("img-tbl-chk");
      }
      this.jsonComponentDataFilterTemp = JSON.parse(JSON.stringify(this.jsonComponentDataFilter));
    }


    if (value != "") {
      if (value != "all") {
        var nameHasFilterText: any = [];
        var nameHasFilterText1: any = [];
        if (flag.trim() == "type") {
        } else if (flag.trim() == "health") {

          var strclsall, strclsc, strclsw, strclsh;
          try {
            strclsc = document.getElementById('popup_health_c').className;
            strclsw = document.getElementById('popup_health_w').className;
            strclsh = document.getElementById('popup_health_h').className;
          } catch (e) {

          }

          var selected = 0;
          if ((strclsh == "img-tbl-chk" || strclsh == "img-tbl-unchk img-tbl-chk")) {
            this.getData("healthy", "health");
            selected++;
            this.componentDataVisibleTotal = this.componentDataVisibleTemp.slice(0);

          }
          if ((strclsc == "img-tbl-chk" || strclsc == "img-tbl-unchk img-tbl-chk")) {
            this.getData("critical", "health");
            selected++;
            this.componentDataVisibleTotal = this.componentDataVisibleTotal.concat(this.componentDataVisibleTemp.slice(0));
          }
          if ((strclsw == "img-tbl-chk" || strclsw == "img-tbl-unchk img-tbl-chk")) {
            this.getData("warning", "health");
            selected++;
            this.componentDataVisibleTotal = this.componentDataVisibleTotal.concat(this.componentDataVisibleTemp.slice(0));
          }

          this.jsonComponentDataFilterVisible = this.componentDataVisibleTotal.slice(0);
          this.componentDataFinalHealth = this.componentDataVisibleTotal.slice(0);
          if (selected == 0) {
            this.jsonComponentDataFilterVisible = this.jsonComponentDataFilter.slice(0);
            this.componentDataFinalHealth = this.jsonComponentDataFilter.slice(0);

          }
        }
      } else {
        this.jsonComponentDataFilterVisible = this.jsonComponentDataFilterTemp.slice(0)
      }
    }
    else {
      this.jsonComponentDataFilterVisible = this.jsonComponentDataFilterTemp.slice(0)
    }


  }

  sortBy(property: any, length: any) {

    this.isDesc = !this.isDesc; //change the direction    
    this.column = property;
    this.direction = this.isDesc ? 1 : -1;
    if (property == "resourceId") {
      if (this.isDesc) {
        this.componentDataVisible.sort(this.sortByResourceId);
      } else {
        this.componentDataVisible.sort(this.sortByResourceIdDesc);
      }

    } else if (property == "resourcetypeId") {
      if (this.isDesc) {
        this.componentDataVisible.sort(this.sortByResourcetypeId);
      } else {
        this.componentDataVisible.sort(this.sortByResourcetypeIdDesc);
      }

    } else if (property == "componentHealth") {

      if (this.isDesc) {
        this.componentDataVisible.sort(this.sortByComponentHealth);
      } else {
        this.componentDataVisible.sort(this.sortByComponentHealthDesc);
      }
    } else if (property == "componentParam") {

      if (this.isDesc) {
        this.componentDataVisible.sort(this.sortByComponentParam);
      } else {
        this.componentDataVisible.sort(this.sortByComponentParamDesc);
      }
    } else if (property == "criticalCount") {
      if (this.isDesc) {
        this.componentDataVisible.sort(this.sortByCriticalCount);
      } else {
        this.componentDataVisible.sort(this.sortByCriticalCountDesc);
      }

    } else if (property == "warningCount") {
      if (this.isDesc) {
        this.componentDataVisible.sort(this.sortByWarningCount);
      } else {
        this.componentDataVisible.sort(this.sortByWarningCountDesc);
      }

    }
    this.sortBycloseCheckbox(length);

  }
  sortByResourceIdDesc(s1: ComponentList, s2: ComponentList) {
    var nameA = s1.resourceId.toLowerCase(), nameB = s2.resourceId.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1
    return 0 //default return value (no sorting)
  }
  sortByResourcetypeIdDesc(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.resourcetypeId.toLowerCase(), nameB = s2.resourcetypeId.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1

    return 0 //default return value (no sorting)
  }
  sortByResourcetypeId(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.resourcetypeId.toLowerCase(), nameB = s2.resourcetypeId.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1

    return 0 //default return value (no sorting)
  }
  sortByResourceId(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.resourceId.toLowerCase(), nameB = s2.resourceId.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1
    return 0 //default return value (no sorting)
  }
  sortByComponentHealthDesc(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.componentHealth.toLowerCase(), nameB = s2.componentHealth.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1
    return 0 //default return value (no sorting)
  }
  sortByComponentHealth(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.componentHealth.toLowerCase(), nameB = s2.componentHealth.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1
    return 0 //default return value (no sorting)
  }
  sortByComponentParamDesc(s1: ComponentList, s2: ComponentList) {
    return s2.componentParam - s1.componentParam;
  }
  sortByComponentParam(s1: ComponentList, s2: ComponentList) {
    return s1.componentParam - s2.componentParam;
  }
  sortByWarningCountDesc(s1: ComponentList, s2: ComponentList) {
    return s2.warningCount - s1.warningCount;
  }
  sortByWarningCount(s1: ComponentList, s2: ComponentList) {
    return s1.warningCount - s2.warningCount;
  }

  sortByCriticalCountDesc(s1: ComponentList, s2: ComponentList) {
    return s2.criticalCount - s1.criticalCount;
  }
  sortByCriticalCount(s1: ComponentList, s2: ComponentList) {
    return s1.criticalCount - s2.criticalCount;
  }
  navigateToPortfolio(clusterId: string) {
    this._router.navigate(['/main/clusters', clusterId]);
  }
  navigateToMoniterHealth() {
    this._router.navigate(['main/clusters', this.clusterName, this.portfolioName]);
  }
  navigateToAction(serverName:string,type:string,observation:Observations,resourceId:string) {
    // console.log(JSON.stringify(observation))
    sessionStorage.setItem("observation", JSON.stringify(observation) );
 
    window.sessionStorage.setItem('resourceId',resourceId);
    this._router.navigate(['main/clusters/actions', this.clusterName, this.portfolioName,serverName,type]);
  }
  ngOnDestroy() {
    try {
      this.subscription.unsubscribe();
     
      this.componentSubscription.unsubscribe();
      this.recentSubscription.unsubscribe();
      this.portfolioSubscription.unsubscribe();

    } catch (e) {

    }
    try {
      this.parameterSubscription.unsubscribe();
    } catch (e) {

    }
    try {
      this.tktSubscription.unsubscribe();
    } catch (e) {

    }
  }
}

interface KeyValue {
  resourceid: string,
  observableid: string
}
