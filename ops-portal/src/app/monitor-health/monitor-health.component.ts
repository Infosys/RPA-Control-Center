import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ClusterDetailsComponent } from '../cluster-details/cluster-details.component';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';

import { Subscription, timer, pipe } from 'rxjs';
import { Observable, Subject } from 'rxjs';
import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment'
import { InitialLoadCheck } from '../cluster-details/initial-load.model'
@Component({
  selector: 'app-monitor-health',
  templateUrl: './monitor-health.component.html',
  styleUrls: ['./monitor-health.component.scss']
})
export class MonitorHealthComponent implements OnInit, OnDestroy {

  subscription: Subscription;
  errorSubscription: Subscription;
  tktSubscription: Subscription;
  jsonPortfolioData: any;
  jsonErrorLogData: any;
  jsonTicketData: any;
  countOfChecks: number = 0;
  healthyCount: number = 0;
  warningCount: number = 0;
  criticalCount: number = 0;
  errorLogCount: number = 0;
  ticketCount: number = 0;
  automationTicketCount: number = 0;
  manualTicketCount: number = 0;
  portfolioName: string = "";
  clusterName: string = "";
  initialLoadJson: InitialLoadCheck[] = [
    { service: "getPortfolioDetails", initialLoad: false },
    { service: "getErrorLogDetails", initialLoad: false },
    { service: "getTicketDetails", initialLoad: false }
    
  ];

  @Input() isMenuCollapsed: boolean;
  constructor(private clusterDetailsService: ClusterDetailsService, private route: ActivatedRoute, private _router: Router) { }

  getPortfolioDetails(portfolioName: string, clusterName: string) {
 
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getPortfolioIncidentDetails(portfolioName,InitialLoadCheck.getLoadInterval('getPortfolioDetails',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
	
    this.subscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getPortfolioDetails',this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonPortfolioData = result;
          // console.log(this.jsonPortfolioData);
          this.clusterDetailsService.somevent.emit(this.jsonPortfolioData);
          this.initialLoadJson= InitialLoadCheck.chnageInitialLoad('getPortfolioDetails', false,this.initialLoadJson);
        }
      }
    );



    this.clusterDetailsService.saveData(clusterName);
    //set boolen var here
  }
  ngOnInit() {
    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);
    this.clusterName = this.route.snapshot.params['clusterId'];
    this.portfolioName = this.route.snapshot.params['portfolioId'];

    this.subscription = this.route.params.subscribe(params => {
      this.clusterName = params['clusterId'];
      this.portfolioName = params['portfolioId'];
      this.getPortfolioDetails(this.portfolioName, this.clusterName);
    });

    this.clusterDetailsService.somevent.subscribe((res) => {
      this.jsonPortfolioData = res;
      this.countOfChecks = this.jsonPortfolioData.statusByDate[0].components;
      this.criticalCount = this.jsonPortfolioData.criticalCount;
      this.warningCount = this.jsonPortfolioData.warningCount;
      this.healthyCount = this.jsonPortfolioData.healthyCount;
      //this.portfolioName = this.jsonPortfolioData.resourceId;
      this.clusterName = this.clusterDetailsService.getData();

    });

    this.getErrorLogDetails();
    this.getTicketDetails();


  }
  getErrorLogDetails() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getErrorLogDetails(InitialLoadCheck.getLoadInterval('getErrorLogDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.errorSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getErrorLogDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonErrorLogData = result;
          this.errorLogCount = this.jsonErrorLogData.countOfErrors;
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getErrorLogDetails', false, this.initialLoadJson);
        }
      }
    );
  }

  getTicketDetails() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getTicketDetails(InitialLoadCheck.getLoadInterval('getTicketDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.tktSubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getTicketDetails', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonTicketData = result;
          this.ticketCount = this.jsonTicketData.totalTickets;
          this.automationTicketCount = this.jsonTicketData.automationTickets;
          this.manualTicketCount = this.jsonTicketData.manualTickets;
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getTicketDetails', false, this.initialLoadJson);
        }
      }
    );

  }


  setValues() {
    console.log("setVal :: ");
    console.log(this.jsonPortfolioData);
    this.clusterDetailsService.saveData(this.jsonPortfolioData);
    this._router.navigate(['/main/clusters/components', this.clusterName, this.portfolioName]);
  }
  navigateToPortfolio(clusterId: string) {
    this._router.navigate(['/main/clusters', clusterId]);
  }
  navigateToCLusters() {
    this._router.navigate(['/main/clusters']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    this.errorSubscription.unsubscribe();
    this.tktSubscription.unsubscribe();

  }
}
