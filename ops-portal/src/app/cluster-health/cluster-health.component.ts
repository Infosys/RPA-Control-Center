import { Component, OnInit, OnDestroy } from '@angular/core';
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
import { Router } from '@angular/router';
import { ClusterHealthDetails } from '../cluster-health/cluster-details.model';
import { Chart } from 'chart.js';
import { Subscription, timer, pipe } from 'rxjs';
import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment'
import { InitialLoadCheck } from '../cluster-details/initial-load.model'
@Component({
  selector: 'app-cluster-health',
  templateUrl: './cluster-health.component.html',
  styleUrls: ['./cluster-health.component.scss']
})
export class ClusterHealthComponent implements OnInit, OnDestroy {

  chartLabels: string[] = ['Critical', 'Warning', 'Healthy'];
  chartLabelsCritical: string[] = ['Critical', 'Other'];
  chartLabelsWarning: string[] = ['Warning', 'Other'];
  chartDataCritical: number[];
  chartDataWarning: number[] = [1, 2, 3];
  chartDataTotal: number[];
  chartColorsTotal: {} = ['#FF183E', '#FFA518', '#82AB26'];
  chartColorsCritical: {} = ['#FF183E', '#485d69'];
  chartColorsWarning: {} = ['#FFA518', '#485d69'];
  options = { legend: false, elements: { arc: { borderWidth: 0 } } };
  countSubscription: Subscription;
  dataSubscription: Subscription;
  jsonClusterData?: any;
  jsonClusterDataTmp?: any;
  jsonClusterDetails?: ClusterHealthDetails = { "totalCount": 0, "criticalCount": 0, "warningCount": 0, "healthyCount": 0 };
  isDesc: boolean = true;
  direction: number;

  initialLoadJson: InitialLoadCheck[] = [
    { service: "getClusterData", initialLoad: false },
    { service: "getClusterDetails", initialLoad: false },
  ];
  constructor(private clusterDetailsService: ClusterDetailsService, private _router: Router) { }

  ngOnInit() {
    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);

    this.getClusterData();
    this.getClusterDetails();

    try {
      sessionStorage.setItem("portfolioName", this.jsonClusterData[0].clusterChild[0].portfolioName);
      sessionStorage.setItem("clusterId", this.jsonClusterData[0].clusterName);
    } catch (e) {

    }

  }

  
  navigateToCluster(clusterName: string) {

    this._router.navigate(['/main/clusters', clusterName]);
  }

  getClusterDetails() {

    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getClusterList(InitialLoadCheck.getLoadInterval('getClusterDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.dataSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getClusterDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonClusterData = result;
          this.jsonClusterDataTmp = result;
          // console.log("ClusterData :: ", this.jsonClusterData);
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getClusterDetails', false, this.initialLoadJson);
        }
      }
    );
  }

  getClusterData() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getClusterData(InitialLoadCheck.getLoadInterval('getClusterData', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.countSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getClusterData', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonClusterDetails = result;
          if (this.jsonClusterDetails != null && this.jsonClusterDetails != undefined) {
            this.generateGraph([this.jsonClusterDetails.criticalCount, this.jsonClusterDetails.warningCount, this.jsonClusterDetails.healthyCount], this.options, 'tot_canvas', this.chartLabels, this.chartColorsTotal);
            this.generateGraph([this.jsonClusterDetails.criticalCount, (this.jsonClusterDetails.criticalCount + this.jsonClusterDetails.warningCount + this.jsonClusterDetails.healthyCount) - this.jsonClusterDetails.criticalCount], this.options, 'critical_canvas', this.chartLabelsCritical, this.chartColorsCritical);
            this.generateGraph([this.jsonClusterDetails.warningCount, (this.jsonClusterDetails.criticalCount + this.jsonClusterDetails.warningCount + this.jsonClusterDetails.healthyCount) - this.jsonClusterDetails.warningCount], this.options, 'warning_canvas', this.chartLabelsWarning, this.chartColorsWarning);
          };
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getClusterData', false, this.initialLoadJson);
        }
      }
    );

  }

  generateGraph(data_set: any, options: any, id: string, labels_doughnut: any, chartColors: any) {
    var canvas = <HTMLCanvasElement>document.getElementById(id);
    var ctx = canvas.getContext("2d");
    ctx.restore();
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

  sortBy(property: any) {
    this.isDesc = !this.isDesc; //change the direction    

    this.direction = this.isDesc ? 1 : -1;
    if (property == "clusterName") {
      if (this.isDesc) {
        this.jsonClusterData.sort(this.sortByClusterName);
      } else {
        this.jsonClusterData.sort(this.sortByClusterNameDesc);
      }
    } else if (property == "clusterState") {
      if (this.isDesc) {
        this.jsonClusterData.sort(this.sortByClusterState);
      } else {
        this.jsonClusterData.sort(this.sortByClusterStateDesc);
      }
    } else if (property = "portfolioCount") {
      if (this.isDesc) {
        this.jsonClusterData.sort(this.sortByPortfolio);
      } else {
        this.jsonClusterData.sort(this.sortByPortfolioDesc);
      }
    } else if (property = "criticalCount") {
      if (this.isDesc) {
        this.jsonClusterData.sort(this.sortByCritical);
      } else {
        this.jsonClusterData.sort(this.sortByCriticalDesc);
      }
    } else if (property == "warningCount") {
      if (this.isDesc) {
        this.jsonClusterData.sort(this.sortByWarning);
      } else {
        this.jsonClusterData.sort(this.sortByWarningDesc);
      }
    }
  }
  sortByClusterName(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.clusterName.toLowerCase(), nameB = s2.clusterName.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1
    return 0 //default return value (no sorting)
  }
  sortByClusterNameDesc(s1: ComponentList, s2: ComponentList) {
    var nameA = s1.clusterName.toLowerCase(), nameB = s2.clusterName.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1
    return 0 //default return value (no sorting)
  }
  sortByClusterState(s1: ComponentList, s2: ComponentList) {

    var nameA = s1.clusterState.toLowerCase(), nameB = s2.clusterState.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1
    return 0 //default return value (no sorting)
  }
  sortByClusterStateDesc(s1: ComponentList, s2: ComponentList) {
    var nameA = s1.clusterState.toLowerCase(), nameB = s2.clusterState.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1
    return 0 //default return value (no sorting)
  }
  sortByPortfolio(s1: ComponentList, s2: ComponentList) {
    return s1.portfolioCount - s2.portfolioCount;
  }
  sortByPortfolioDesc(s1: ComponentList, s2: ComponentList) {
    return s2.portfolioCount - s1.portfolioCount;
  }
  sortByCritical(s1: ComponentList, s2: ComponentList) {
    return s1.criticalCount - s2.criticalCount;
  }
  sortByCriticalDesc(s1: ComponentList, s2: ComponentList) {
    return s2.criticalCount - s1.criticalCount;
  }
  sortByWarning(s1: ComponentList, s2: ComponentList) {
    return s1.warningCount - s2.warningCount;
  }
  sortByWarningDesc(s1: ComponentList, s2: ComponentList) {
    return s2.warningCount - s1.warningCount;
  }
  filterMainTable(value: string) {

    if (value != "" && value.trim() != "all") {

      this.jsonClusterData = this.jsonClusterDataTmp.filter(component => {
        var nameHasFilterText;

        nameHasFilterText = component.clusterState.toLowerCase().indexOf(value.toLowerCase()) >= 0

        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })

    }
    else {
      this.jsonClusterData = this.jsonClusterDataTmp.slice(0);
    }
  }

  ngOnDestroy() {
    this.countSubscription.unsubscribe();
    this.dataSubscription.unsubscribe();
  }
}

interface ComponentList {
  clusterName: string;
  clusterState: string;
  portfolioCount: number;
  criticalCount: number;
  warningCount: number;
}