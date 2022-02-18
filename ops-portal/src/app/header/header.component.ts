import { Component, OnInit, OnDestroy } from '@angular/core';
import { DataService } from "../cluster-details/data.service";
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { environment } from '../../environments/environment'
import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  projectTitle:string;
  subscription: Subscription;

  constructor(private data: DataService, private _router: Router,private clusterDetailsService: ClusterDetailsService) { }

  ngOnInit() {
    this.projectTitle = environment.projectTitle;
    this.isIntevalFlagEnabled=this.clusterDetailsService.isAutoRefreshEnabled;
    this.subscription = this.data.currentMessage.subscribe(message => this.isCollapsed = message);
    document.getElementById('appbank_nav').style.display="block";
    document.getElementById('dashboard_nav').className="navbar navbar-expand-lg navbar-light initial-width";
  }

  isCollapsed: string = 'true';
  isIntevalFlagEnabled:boolean=false;
  handleCollapse() {
    if (this.isCollapsed == 'true'){
      document.getElementById('appbank_nav').style.display="block";
      document.getElementById('dashboard_nav').className="navbar navbar-expand-lg navbar-light initial-width";
      this.data.changeMessage("false");
    } 
    else{
      document.getElementById('appbank_nav').style.display="none";
      document.getElementById('dashboard_nav').className="navbar navbar-expand-lg navbar-light space-width"; 
      this.data.changeMessage("true");
    }
      
  }

  routeHealth() {
    this._router.navigate(['/main/clusters']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
  changeInterval(ischecked:boolean){
  
   if(ischecked){
    this.clusterDetailsService.isAutoRefreshEnabled=true;
    this.isIntevalFlagEnabled=true;
   }else{
    this.clusterDetailsService.isAutoRefreshEnabled=false;
    this.isIntevalFlagEnabled=false;
   }
   console.log(this.clusterDetailsService.isAutoRefreshEnabled);
  }
}
