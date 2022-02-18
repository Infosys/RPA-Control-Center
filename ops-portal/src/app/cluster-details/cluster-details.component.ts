import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
import { MonitorHealthComponent } from '../monitor-health/monitor-health.component';
import { Router } from '@angular/router';
import { DataService } from './data.service';
import { ClusterList, Cluster } from '../cluster-details/cluster-list.model';

import { InitialLoadCheck } from '../cluster-details/initial-load.model'
import { Subscription, timer, pipe } from 'rxjs';
import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment'
declare var jquery: any;
declare var $: any;
@Component({
  selector: 'app-cluster-details',
  templateUrl: './cluster-details.component.html',
  styleUrls: ['./cluster-details.component.scss']
})
export class ClusterDetailsComponent implements OnInit, OnDestroy {
  isMenuCollapsed: string;
  jsonClusterData: any;
  jsonPortfolioData: any;
  style: string = 'menu-arrow';
  selectedPortFolio: string = "";
  searchValue: string = "";
  selectedCluster: string = "";
  subscriptionCluster: Subscription;
  subscriptionClusterList: Subscription;
  notificationSubscription:Subscription;
  subscriptionPage: Subscription;
  visibleClusterData: ClusterList[]
  tempClusterData: ClusterList[] = []
  visibleCluster: Cluster[];
  public filterText: string = "";
  planNotifications: any;
  portalUser:string;
  initialLoadJson: InitialLoadCheck[] = [
    { service: "getClusterList", initialLoad: false },
    { service: "getCluster", initialLoad: false },
  ];
  constructor(private clusterDetailsService: ClusterDetailsService, private objMonitorHealthComponent: MonitorHealthComponent, private _router: Router, private data: DataService) {

  }


  ngOnInit() {
    this.portalUser="test_user4";
    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);
    this.subscriptionPage = this.data.currentMessage.subscribe(

      message => this.isMenuCollapsed = message
    );
    this.getCluster();
    this.getClusterList();
    //TO-DO commented as per superbot intgreation change jan 3rd 2020
    // this.getRemediationPlanNotifications();
  }
  getRemediationPlanNotifications() {
   
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getPlanNotifications(this.portalUser, true)
      )
    );
    this.notificationSubscription = timerObj.subscribe(
      result => {
        this.planNotifications = JSON.parse(JSON.stringify(result));
       
        console.log("Notifications");
        console.log(this.planNotifications);
        if (this.planNotifications.length > 0) {
          var selfref = this;
            
         try{
          this.planNotifications[0]["status"].forEach(function (plan) {

            if (plan.status == "QUEUED") {
              selfref.successMessage("Plan " + plan.remediationplanid + " has been queued successfully.");
            } else if (plan.status == "FAILED") {
              selfref.errorMessage("Plan " + plan.remediationplanid + " has been failed.");
             // selfref.getRemediationPlanDetails(sessionStorage.getItem("serverType"));
            } else if (plan.status == "SUCCESS") {
              selfref.successMessage("Plan " + plan.remediationplanid + " has been executed successfully.");

            }


          }

          );
         }catch(e){

         }
        }
      }
    );
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
  getClusterList() {

    let timerObj = timer(0, 600000).pipe(
      switchMap(() => this.clusterDetailsService.getClusterList(InitialLoadCheck.getLoadInterval('getClusterList', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    try{
      this.subscriptionClusterList = timerObj.subscribe(
        result => {
          // excluding sidebar from auto refresh
          // InitialLoadCheck.getLoadInterval('getClusterList', this.initialLoadJson)
          if (InitialLoadCheck.getLoadInterval('getClusterList', this.initialLoadJson) ) {
            this.jsonClusterData = result;
            this.visibleClusterData = this.jsonClusterData;
            console.log("Before unsubscription :: ", this.jsonClusterData);
            this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getClusterList', false, this.initialLoadJson);
          }
        }
      );
  
    }catch(e){
      console.log(e)
    }
    

  }
  getCluster() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getCluster(InitialLoadCheck.getLoadInterval('getCluster', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.subscriptionCluster = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getCluster', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonClusterData = result;
          this.visibleCluster = this.jsonClusterData;
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getCluster', false, this.initialLoadJson);
        }
      }
    );

  }

  filterClusterData(value: string) {

    this.filterText = value;
    this.tempClusterData=<ClusterList[]>JSON.parse(JSON.stringify(this.jsonClusterData));
    // this.tempClusterData.clusterChild=JSON.parse(JSON.stringify(this.jsonClusterD))

    if (this.filterText != "") {
      this.visibleClusterData = this.tempClusterData.filter(cluster => {
        var nameHasFilterText = cluster.clusterName.toLowerCase().indexOf(this.filterText.toLowerCase()) >= 0
        if (nameHasFilterText) {
          cluster.visible = true;
          return nameHasFilterText
        }

      try{
        var filteredMarksArray = cluster.clusterChild.filter(portfolio => {
          cluster.visible = true;
          return portfolio.portfolioName.toLowerCase().indexOf(this.filterText.toLowerCase()) >= 0
        })

        cluster.clusterChild = filteredMarksArray
        return filteredMarksArray.length > 0
      }catch(e){
        console.log(e)
      }
      })
    }
    else {
      this.visibleClusterData = this.tempClusterData.slice(0)
    }
  }

  onLoad() {
  }

  clearSearch() {
    this.searchValue = "";
    this.filterClusterData("");
  }
  getPortfolioDetailsMoniterHealth() {

    try {
      var portfolioName = sessionStorage.getItem("portfolioName");
      var clusterName = sessionStorage.getItem("clusterId");
      this._router.navigate(['main/clusters', clusterName, portfolioName]);
    } catch (e) {

    }
  }

  getPortfolioDetails(portfolioName: string, clusterName: string) {
    // console.log("jsonClusterData after :: ", this.jsonClusterData);
    this.selectedPortFolio = portfolioName;
    this.selectedCluster = clusterName;
    sessionStorage.setItem("portfolioName", portfolioName);
    sessionStorage.setItem("clusterId", clusterName);
    this._router.navigate(['main/clusters', clusterName, portfolioName]);
  }


  toggleMenu(index: number, clusterId: any) {
 
    var id = 'sub-item-' + index;
    var img_id = "menu_image_" + index;
    var strcls = document.getElementById(id).className;
    if ((strcls != undefined) && strcls.trim() != "sub-menu list-group rounded-0 collapse show") {
      document.getElementById(id).className = "sub-menu list-group rounded-0 collapse show ";
    } else {
      document.getElementById(id).className = "sub-menu collapse list-group rounded-0";
    }
    document.getElementById(img_id).classList.toggle("img-menu-plus");
    this._router.navigate(['/main/clusters', clusterId]);
  }
 

  mouseHoverMenu($event, i: number, j: number) {

    let menu_id = "menu_child_" + i + j;
    if (menu_id != undefined) {
      var clsName = document.getElementById(menu_id).className.trim();
      if (clsName.search('menu-li-bg-hover') == -1) {
        document.getElementById(menu_id).className = clsName.trim() + ' menu-li-bg-hover';
      }
    }
    let img_id = "menu_child_img_" + i + j;
    if (img_id != undefined) {
      var clsName = document.getElementById(img_id).className;
      if (clsName.search('img-menu-arrow-hover') == -1) {
        document.getElementById(img_id).className = clsName.trim() + " img-menu-arrow-hover";
      }
    }
  }

  mouseOutMenu($event, i: number, j: number) {

    let menu_id = "menu_child_" + i + j;
    if (menu_id != undefined) {
      var clsName = document.getElementById(menu_id).className;
      if (clsName.trim().search('menu-li-bg-hover') != -1) {
        var cls = clsName.trim().replace("menu-li-bg-hover", " ");
        document.getElementById(menu_id).className = cls;
      }
    }
    let img_id = "menu_child_img_" + i + j;
    if (img_id != undefined) {
      var clsName = document.getElementById(img_id).className;
      if (clsName.trim().search('img-menu-arrow-hover') != -1) {
        var cls = clsName.trim().replace("img-menu-arrow-hover", " ");
        document.getElementById(img_id).className = cls;
      }
    }
  }

  ngOnDestroy() {
    this.subscriptionPage.unsubscribe();
    this.subscriptionCluster.unsubscribe();
    this.subscriptionClusterList.unsubscribe();
  }
}
