import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class DataService  {
  private messageSource = new BehaviorSubject('false');
  currentMessage = this.messageSource.asObservable();
  
  private graphSource = new BehaviorSubject('');
  currentGraph = this.graphSource.asObservable();

  constructor() { }

  changeMessage(message: any) {
  
    this.messageSource.next(message)
  }
  getGraphData(message: any) {
  
    this.graphSource.next(message)
   
  }

}
