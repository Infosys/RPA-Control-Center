import { Component } from '@angular/core';
import { DataService } from "./cluster-details/data.service";
import { Router } from '@angular/router';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';
  constructor(private data: DataService, private _router: Router) { }
/*  */
  ngOnInit() {
    this.data.currentMessage.subscribe(message => this.isCollapsed =  message)
  }
  isCollapsed: string = 'true';
  handleCollapse(){
    if (this.isCollapsed=='true')
    this.data.changeMessage("false");
    else
    this.data.changeMessage("true");
  }

  routeHealth() {

    this._router.navigate(['/main/health']);
  }
}
