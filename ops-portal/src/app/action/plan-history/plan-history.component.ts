import { Component, OnInit, Output, EventEmitter,ElementRef ,ViewChild} from '@angular/core';

import { ClusterDetailsService } from '../../cluster-details/cluster-details.service';
import { InitialLoadCheck } from '../../cluster-details/initial-load.model';
import { switchMap, takeUntil, catchError } from 'rxjs/operators';
import { environment } from '../../../environments/environment'
import { Subscription, timer, pipe } from 'rxjs';
declare var $: any;
@Component({
  selector: 'app-plan-history',
  templateUrl: './plan-history.component.html',
  styleUrls: ['./plan-history.component.scss']
})

export class PlanHistoryComponent implements OnInit {

  @ViewChild('logsModel', { static: false }) modal:ElementRef;
  plansHistoryArr = [];
  planStepJson: any;
  historySubscription: Subscription;

  constructor(private clusterDetailsService: ClusterDetailsService) {

  }

  options = {};
  showModal: boolean = false;
  planHistory: any;
  initialLoadJson: InitialLoadCheck[] = [
    { service: "getPlanHistoy", initialLoad: false },
  ]

  @Output() eventClick = new EventEmitter();
  ngOnInit() {

    this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('all', true, this.initialLoadJson);

    this.showModal = false;

    this.getPlanHistoy();

  }





  getPlanHistoy() {
    // To_do\
    let inputjson = {
      "resourcetype": "MQ_SERVER",
      "createdate": "2018-12-28",
      "limit": -1
    }
    let timerObj = timer(0, environment.interval).pipe(
      switchMap(() => this.clusterDetailsService.getPlanHistoy(inputjson, true || this.clusterDetailsService.isAutoRefreshEnabled)
      )
    );
    this.historySubscription = timerObj.subscribe(
      result => {
        if (InitialLoadCheck.getLoadInterval('getPlanHistoy', this.initialLoadJson) || this.clusterDetailsService.isAutoRefreshEnabled) {
          this.planHistory = JSON.parse(JSON.stringify(result));
          console.log("planHistory" + JSON.stringify(this.planHistory));
          this.initialLoadJson = InitialLoadCheck.chnageInitialLoad('getPlanHistoy', false, this.initialLoadJson);
        }
      }
    );
  }
  handleClickMe(data:any) {
    // this.eventClick.emit('block');
    this.eventClick.emit(data);
  }
  settings = {
    columns: {
      remediationplanexecid: {
        title: 'PlanExecId',
        type: 'html',
        width: '12%'
      },
      remediationplanname: {
        title: 'Plan Name',
        width: '13%'
      },
      executionenddatetime: {
        title: 'End time',
        width: '15%'
      },
      // runby: {
      //   title: 'Executed By',
      //   width: '15%'
      // },
      // executionstart: {
      //   title: 'Execution start',
      //   width: '15%'
      // },
      // executionend: {
      //   title: 'Execution End',
      //   width: '15%'
      // },
      
      resourcename: {
        title: 'Resource Name ',
        width: '15%'
      },
      // observablename: {
      //   title: 'Observable Name',
      //   width: '18%'
      // },
      
      // runduration: {
      //   title: 'Run Duration',
      //   width: '10%'
      // },

      status: {
        title: 'status',

        width: '10%'

      },

    },
    // mode: "external",
    // pager: { perPageSelect: [10, 20, 30] }
  };
  closeLogs() {

    this.showModal = false;
  }
  onSelectionChanged(planId: string) {
    debugger;
    // TO_DO: un Comment
    // document.getElementById("openStepModalBtn").click();
    this.handleClickMe({flag: 'none', id: planId})
    $("#stepModal").modal('show');
    // this.getPlanHistoryStepDetails(planId);
  };

 


}
