import { Injectable } from '@angular/core';
import { IResourceDetails } from '../../app/interfaces/resourceDetails';
import { HttpClient,HttpErrorResponse  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { IReportDetails } from '../../app/interfaces/reportDetails';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BaselineService {
  private serverAddress_superbot = environment.serverAddress_superbot;
  private services_app_name_config_management = environment.services_app_name_config_mgmnt;
  private services_app_name_superbot = environment.services_app_name_superbot;

  private REST_SERVICE_URI_CONFIGMANAGEMENT_GET_RESOURCETYPE='http://'+this.serverAddress_superbot+'/'+this.services_app_name_config_management+'/Metadata/getResourceTypeMetaData?TenantId=1';
  private REST_SERVICE_URI_CONFIGMANAGEMENT_GET_RESOURCE='http://'+this.serverAddress_superbot+'/'+this.services_app_name_config_management+'/ResourceModel/GetActiveResourceModelConfiguration?PlatformInstanceId=1&TenantId=1&ResourceTypeName=';
  private REST_SERVICE_URI_SUPERBOT_GETREPORT='http://'+this.serverAddress_superbot+'/'+this.services_app_name_superbot+'/ResourceHandler/GetEnvironmentScanComparisonReport';
  resourceTypes:IResourceDetails[];
  resources:IResourceDetails[];
  constructor(private _http:HttpClient) { }

  getResourceTypes(){
    return this._http.get(this.REST_SERVICE_URI_CONFIGMANAGEMENT_GET_RESOURCETYPE).pipe(catchError(this.errorHandler));
  }
  getResources(resourceType:string){
    //this.resources=[{Id:"1_1",Name:"resource Name 1"},{Id:"1_2",Name:"resource Name 2"},{Id:"1_3",Name:"resource Name 3"}]
    return this._http.get(this.REST_SERVICE_URI_CONFIGMANAGEMENT_GET_RESOURCE+resourceType).pipe(catchError(this.errorHandler));
  } 
  getComparisonReportDetails(resourceId:string,startDate:string,endDate:string,platformId:string,tenantId:string):Observable<IReportDetails>{
    let params = "?resourceId="+resourceId+"&startDate="+startDate+"&endDate="+endDate+"&platformId="+platformId+"&tenantId="+tenantId;
    return this._http.get<IReportDetails>(this.REST_SERVICE_URI_SUPERBOT_GETREPORT+params).pipe(catchError(this.errorHandler));;
  }
  errorHandler(error: HttpErrorResponse) {
    console.error(error);
    return  throwError(error.message  ||  "Server Error");
  } 
}
