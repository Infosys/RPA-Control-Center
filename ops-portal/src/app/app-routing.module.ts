import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MonitorHealthComponent } from './monitor-health/monitor-health.component';
import { ComponentHealthComponent } from './component-health/component-health.component';
import { ConfigurationComponent } from './configuration/configuration.component';
import { KpiReportComponent } from './kpi-report/kpi-report.component';
import { GraphsComponent } from './graphs/graphs.component';
import { ClusterDetailsComponent } from './cluster-details/cluster-details.component';
import { ClusterHealthComponent } from './cluster-health/cluster-health.component';
import { PortfolioDetailsComponent } from './portfolio-details/portfolio-details.component';
import { ActionComponent } from './action/action.component';
import { BaselineComparisonComponent } from './baseline-comparison/baseline-comparison.component';

const routes: Routes = [

  { path: '', redirectTo: '/main/clusters', pathMatch: 'full' },
  { path: 'graphs', component: GraphsComponent },
  {
    path: 'main', component: ClusterDetailsComponent, children: [
      {path: '', redirectTo: 'clusters' , pathMatch: 'full' },
      { path: 'clusters', component: ClusterHealthComponent },
      { path: 'clusters/:clusterId', redirectTo: 'clusters/:clusterId/portfolios', pathMatch: 'full' },
      { path: 'clusters/:clusterId/portfolios', component: PortfolioDetailsComponent },
      { path: 'clusters/:clusterId/:portfolioId', redirectTo: 'clusters/:clusterId/portfolios/:portfolioId', pathMatch: 'full' },
      { path: 'clusters/:clusterId/portfolios/:portfolioId', component: MonitorHealthComponent },
      { path: 'clusters/components/:clusterId/:portfolioId', redirectTo: 'clusters/:clusterId/portfolios/:portfolioId/components', pathMatch: 'full' },
      { path: 'clusters/:clusterId/portfolios/:portfolioId/components', component: ComponentHealthComponent },
      {path: 'clusters/actions/:clusterId/:portfolioId/:serverId/:paramId', redirectTo: 'clusters/:clusterId/portfolios/:portfolioId/components/:serverId/:paramId', pathMatch: 'full'  },
      {path : 'clusters/:clusterId/portfolios/:portfolioId/components/:serverId/:paramId',component:ActionComponent},
      { path: 'configuration', component: ConfigurationComponent },
      { path: 'kpi-report', component: KpiReportComponent },
      { path: 'es-report', component: BaselineComparisonComponent }
      
     
    ]
  },


  // { path: '', component: ClusterHealthComponent, pathMatch: 'full' }
];


// const routes: Routes = [
//   { path: 'health', component: MonitorHealthComponent },

//   { path: 'component-health', component: ComponentHealthComponent },
//   { path: 'configuration', component: ConfigurationComponent },
//   { path: 'kpi-report', component: KpiReportComponent },
//   { path: 'graphs', component: GraphsComponent },
//   { path: '**', component: MonitorHealthComponent }

// ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
