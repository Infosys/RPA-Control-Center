import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { ClusterDetailsComponent } from './cluster-details/cluster-details.component';
import { ClusterDetailsService } from './cluster-details/cluster-details.service';
import { AppRoutingModule } from './app-routing.module';

import { FormsModule } from '@angular/forms';
import { MonitorHealthComponent } from './monitor-health/monitor-health.component';
import { ComponentHealthComponent } from './component-health/component-health.component';
import { ConfigurationComponent } from './configuration/configuration.component';
import { KpiReportComponent } from './kpi-report/kpi-report.component';
import { GraphsComponent } from './graphs/graphs.component';
import { ChartsModule } from 'ng2-charts';
import { DataService } from './cluster-details/data.service';
import { Chart } from '../../node_modules/chart.js';
import { MyDatePickerModule } from 'mydatepicker';
import { RouterModule } from '@angular/router';
import { ClusterHealthComponent } from './cluster-health/cluster-health.component';
import { PortfolioDetailsComponent } from './portfolio-details/portfolio-details.component';
import { HeaderComponent } from './header/header.component';
import { AngularDateTimePickerModule } from 'angular2-datetimepicker';
import { ActionComponent } from './action/action.component';
import { PlanHistoryComponent } from './action/plan-history/plan-history.component';
import { Ng2SmartTableModule } from './ng2-smart-table/ng2-smart-table.module'; 
// import {TreeModule} from 'primeng/tree';
// import {TreeNode} from 'primeng/api';
import { TreeModule } from 'angular-tree-component';
import { LoaderComponent } from './loader/loader.component';
import { EncryptionService } from '../app/EncryptionService';
import { BaselineComparisonComponent } from './baseline-comparison/baseline-comparison.component';
import { BaselineService } from '../services/baseline-services/baseline.service';
@NgModule({
  declarations: [
    AppComponent,
    ClusterDetailsComponent,
    MonitorHealthComponent,
    ComponentHealthComponent,
    ConfigurationComponent,
    KpiReportComponent,
    GraphsComponent,
    ClusterHealthComponent,
    PortfolioDetailsComponent,
    HeaderComponent,
    ActionComponent,
    LoaderComponent,
    PlanHistoryComponent,
    BaselineComparisonComponent,
    
    
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ChartsModule,
    MyDatePickerModule,
    AngularDateTimePickerModule,
     Ng2SmartTableModule,
    TreeModule.forRoot(),
  
    RouterModule.forRoot([
      { path: 'graphs', component: GraphsComponent},
    ],
    { useHash: true})
  ],
  providers: [ClusterDetailsService, MonitorHealthComponent,DataService,EncryptionService,BaselineService
   
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
