import { Component, OnInit, OnDestroy } from '@angular/core';
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Subscription, timer, pipe } from 'rxjs';
import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment'
import { InitialLoadCheck } from '../cluster-details/initial-load.model'
@Component({
  selector: 'app-portfolio-details',
  templateUrl: './portfolio-details.component.html',
  styleUrls: ['./portfolio-details.component.scss']
})
export class PortfolioDetailsComponent implements OnInit, OnDestroy {

  subscriptionPage: Subscription;
  subscriptionLoad: Subscription;
  jsonClusterChild: any;
  portfolioDetails: any;
  portfolioDetailsTmp: any = [];
  portfolioDetailsTot: any = [];
  clusterId: string = "";
  searchValue: string = "";
  isDesc: boolean = false;
  direction: number;
  initialLoadJson: InitialLoadCheck[] = [
    { service: "getClusterChilds", initialLoad: false }
  ];

  constructor(private clusterDetailsService: ClusterDetailsService, private route: ActivatedRoute, private _router: Router) { }

  ngOnInit() {
    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);
    this.clusterId = this.route.snapshot.params['clusterId'];
    this.subscriptionLoad = this.route.params.subscribe(params => {
      this.clusterId = params['clusterId'];
      this.getClusterChilds();
    });

  }

  getClusterChilds() {
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getClusterChild(this.clusterId,InitialLoadCheck.getLoadInterval('getClusterChilds', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.subscriptionPage = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getClusterChilds', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.jsonClusterChild = result;
          this.portfolioDetails = result;
          // console.log("result" + result);
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getClusterChilds', false, this.initialLoadJson);
        }
      }
    );

    // this.subscription = timer(0, environment.interval).pipe(
    //   switchMap(() => this.clusterDetailsService.getClusterChild(this.clusterId))
    // ).subscribe(
    //   result => {
    //     if (this.initialLoad || this.clusterDetailsService.isAutoRefreshEnabled) {
    //       this.jsonClusterChild = result;
    //       this.portfolioDetails = result;
    //       console.log("result" + result);
    //       this.initialLoad = false;
    //     }
    //   });

    // this.subscription = this.clusterDetailsService.getClusterChild(this.clusterId).subscribe(res => {
    //   this.jsonClusterChild = res;
    //   this.portfolioDetails = res;
    // });
  }
  navigateToPortfolio(portfolioName: string) {
    sessionStorage.setItem("portfolioName", portfolioName);
    sessionStorage.setItem("clusterId", this.clusterId);
    this._router.navigate(['main/clusters', this.clusterId, portfolioName]);
  }

  getData(value: string) {

    if (this.portfolioDetailsTmp != null) {
      this.portfolioDetailsTmp = this.jsonClusterChild.filter(component => {
        var nameHasFilterText;
        nameHasFilterText = component.status.toLowerCase().indexOf(value.toLowerCase()) >= 0
        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })
    }
  }

  filterBy(value: string) {
    this.portfolioDetailsTmp = [];
    this.portfolioDetailsTot = [];
    var typeClass = "";
    var selected = 0;
    if (value == "healthy") {
      document.getElementById('popup_health_h').classList.toggle("img-tbl-chk");

    } else if (value == "Critical") {
      document.getElementById('popup_health_c').classList.toggle("img-tbl-chk");

    } else if (value == "Warning") {
      document.getElementById('popup_health_w').classList.toggle("img-tbl-chk");

    }

    var typeClassh = document.getElementById('popup_health_h').className;
    if ((typeClassh == "img-tbl-chk" || typeClassh == "img-tbl-unchk img-tbl-chk")) {
      this.getData('healthy');
      selected++;
      this.portfolioDetailsTot = this.portfolioDetailsTot.concat(this.portfolioDetailsTmp.slice(0));
    }
    var typeClassc = document.getElementById('popup_health_c').className;
    if ((typeClassc == "img-tbl-chk" || typeClassc == "img-tbl-unchk img-tbl-chk")) {
      this.getData('critical');
      selected++;
      this.portfolioDetailsTot = this.portfolioDetailsTot.concat(this.portfolioDetailsTmp.slice(0));
    }
    var typeClassw = document.getElementById('popup_health_w').className;
    if ((typeClassw == "img-tbl-chk" || typeClassw == "img-tbl-unchk img-tbl-chk")) {
      this.getData('warning');
      selected++;
      this.portfolioDetailsTot = this.portfolioDetailsTot.concat(this.portfolioDetailsTmp.slice(0));
    }

    if (this.portfolioDetailsTot != null) {
      this.portfolioDetails = this.portfolioDetailsTot.slice(0);
    }
    if (selected == 0) {
      this.portfolioDetails = this.jsonClusterChild.slice(0);
    }

  }

  clearSearch() {
    this.searchValue = "";
    this.searchComponent("");
  }

  searchComponent(value: string) {
    if (value != "") {
      this.portfolioDetails = this.jsonClusterChild.filter(component => {
        var nameHasFilterText;
        nameHasFilterText = component.portfolioId.toLowerCase().indexOf(value.toLowerCase()) >= 0
        if (nameHasFilterText) {
          return nameHasFilterText
        }
      })
    }
    else {
      this.portfolioDetails = this.jsonClusterChild.slice(0)
    }
  }

  sortBy(property: any) {
    this.isDesc = !this.isDesc; //change the direction    
    this.direction = this.isDesc ? 1 : -1;
    if (property == "clusterName") {
      if (this.isDesc) {
        this.portfolioDetails.sort(this.sortByPortfolioId);
      } else {
        this.portfolioDetails.sort(this.sortByPortfolioIdDesc);
      }
    } else if (property == "status") {
      if (this.isDesc) {
        this.portfolioDetails.sort(this.sortBystatus);
      } else {
        this.portfolioDetails.sort(this.sortBystatusDesc);
      }
    } else if (property = "componentCount") {
      if (this.isDesc) {
        this.portfolioDetails.sort(this.sortByComponentCount);
      } else {
        this.portfolioDetails.sort(this.sortByComponentCountDesc);
      }
    }
  }

  sortBystatus(s1: PortfolioList, s2: PortfolioList) {
    var nameA = s1.status.toLowerCase(), nameB = s2.status.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1
    return 0 //default return value (no sorting)
  }

  sortBystatusDesc(s1: PortfolioList, s2: PortfolioList) {
    var nameA = s1.status.toLowerCase(), nameB = s2.status.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1
    return 0 //default return value (no sorting)
  }

  sortByPortfolioId(s1: PortfolioList, s2: PortfolioList) {

    var nameA = s1.portfolioId.toLowerCase(), nameB = s2.portfolioId.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return -1
    if (nameA > nameB)
      return 1
    return 0 //default return value (no sorting)
  }

  sortByPortfolioIdDesc(s1: PortfolioList, s2: PortfolioList) {
    var nameA = s1.portfolioId.toLowerCase(), nameB = s2.portfolioId.toLowerCase()
    if (nameA < nameB) //sort string ascending
      return 1
    if (nameA > nameB)
      return -1
    return 0 //default return value (no sorting)
  }

  sortByComponentCount(s1: PortfolioList, s2: PortfolioList) {
    return s1.componentCount - s2.componentCount;
  }

  sortByComponentCountDesc(s1: PortfolioList, s2: PortfolioList) {
    return s2.componentCount - s1.componentCount;
  }

  navigateToCLusters() {
    this._router.navigate(['/main/clusters']);
  }

  ngOnDestroy() {
    this.subscriptionPage.unsubscribe();
    this.subscriptionLoad.unsubscribe();
  }

}
interface PortfolioList {
  portfolioId: string;
  componentCount: number;
  status: string;
}