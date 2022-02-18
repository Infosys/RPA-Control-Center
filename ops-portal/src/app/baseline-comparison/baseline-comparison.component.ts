import { Component, OnInit } from '@angular/core';
import { BaselineService } from '../../services/baseline-services/baseline.service';
import { IResourceDetails } from '../interfaces/resourceDetails';
import { IReportDetails, IInstalledSoftware, Attribute, IOsDetails, IScreenResolution } from '../interfaces/reportDetails';
import { NgForm } from '../../../node_modules/@angular/forms';

@Component({
  selector: 'app-baseline-comparison',
  templateUrl: './baseline-comparison.component.html',
  styleUrls: ['./baseline-comparison.component.scss']
})
export class BaselineComparisonComponent implements OnInit {

  resourceId: string;
  startDate: string;
  endDate: string;
  platformId: string;
  tenantId: string;
  reportDetails: IReportDetails;
  installedSoftwareList: IInstalledSoftware[];
  osDetails: IOsDetails;
  screenRes: IScreenResolution;
  resourcceDetails: any[];
  resourcelist: IResourceDetails[];
  resourceTypelist: IResourceDetails[];
  showRecords: boolean = true;
  constructor(private _baseline: BaselineService) { }

  ngOnInit() {
    this._baseline.getResourceTypes().subscribe(
      res => {
        this.resourceTypelist = [];
        this.resourcceDetails = <any[]>res;
        for (let resObj of this.resourcceDetails) {
          for (let resTypeObj of resObj.resourcetypedetails) {
            //let arrObj = {} as IResourceDetails;
            //arrObj.Id=resTypeObj.resourcetypeid;
            //arrObj.Name=resTypeObj.resourcetypename;
            //this.resourceTypelist.push(arrObj);
            let ListObj: IResourceDetails = { Id: resTypeObj.resourcetypeid, Name: resTypeObj.resourcetypename };
            this.resourceTypelist.push(ListObj);
            //this.resourceTypelist.push({Id:resTypeObj.resourcetypeid,Name:resTypeObj.resourcetypename});
          }
        }
        console.log("services done");
      },
      err => {
        console.log(err);
      },
      () => console.log("getResourceTypes method excuted successfully")
    );

  }

  filterResource(resType: string) {
    console.log("res type name" + resType);
    this._baseline.getResources(resType).subscribe(
      resources => {
        this.resourcelist = [];
        this.resourcceDetails = <any[]>resources;
        console.log(this.resourcceDetails[0].resourcedetails[0].resourcename);
        for (let resObj of this.resourcceDetails) {
          for (let resTypeObj of resObj.resourcedetails) {
            //let arrObj = {} as IResourceDetails;
            //arrObj.Id=resTypeObj.resourcetypeid;
            //arrObj.Name=resTypeObj.resourcetypename;
            //this.resourceTypelist.push(arrObj);
            let ListObj: IResourceDetails = { Id: resTypeObj.resourceid, Name: resTypeObj.resourcename };
            this.resourcelist.push(ListObj);
            //this.resourceTypelist.push({Id:resTypeObj.resourcetypeid,Name:resTypeObj.resourcetypename});
          }
        }
        console.log(this.resourcelist);
      },
      errorMsg => {
        console.log("FilterResource Failed with error:" + errorMsg);
      },
      () => { console.log("FilterResource executed successfully"); }
    );
  }

  submitForm(form: NgForm) {
    this.resourceId = form.value.ResName;
    this.startDate = form.value.startDate;
    this.endDate = form.value.endDate;
    this.platformId = "1";
    this.tenantId = "1";
    console.log("Res Id = " + this.resourceId);
    console.log("Res Type = " + form.value.resType);
    console.log("start Date = " + this.startDate);
    console.log("End Date = " + this.endDate);

    this._baseline.getComparisonReportDetails(this.resourceId, this.startDate, this.endDate, this.platformId, this.tenantId).subscribe(
      reportDetail => {
        if (reportDetail == null) {
          this.showRecords = false;
        }
        else {
          this.showRecords = true;
          this.reportDetails = reportDetail;
          this.mapDetails(this.reportDetails);
        }

        //console.log(this.reportDetails.ResourceDetails.MetricDetails.MetricValue[0].Attributes[0].AttributeValue);
      },
      e => {
        console.log(e);
      },
      () => { console.log("got the report"); }
    );

  }

  mapDetails(reportDetails: IReportDetails) {
    console.log("mapDetails method called");
    this.installedSoftwareList = [];
    for (let metricVal of reportDetails.ResourceDetails.MetricDetails.MetricValue) {
      switch (metricVal.MetricName.toLowerCase()) {
        case "installed software": {
          let installedSoftwareObj: IInstalledSoftware = this.mapInstalledSoftware(metricVal.Attributes);
          installedSoftwareObj.Status = metricVal.Status;
          this.installedSoftwareList.push(installedSoftwareObj);
          break;
        }
        case "osdetails": {
          this.osDetails = this.mapOsDetails(metricVal.Attributes);
          this.osDetails.Status = metricVal.Status;
          break;
        }
        case "screen resolution": {
          this.screenRes = this.mapSreenRes(metricVal.Attributes);
          this.screenRes.Status = metricVal.Status;
          break;
        }
        default:
          console.log("switch case default");
          break;
      }
    }
  }

  mapInstalledSoftware(attributeList: Attribute[]) {
    console.log("map installed software method called");
    let installedSoftwareObj: IInstalledSoftware = {};
    for (let attObj of attributeList) {
      switch (attObj.AttributeName.toLowerCase()) {
        case "display name": {
          installedSoftwareObj.SoftwareName = attObj.AttributeValue;
          break;
        }
        case "old display name": {
          installedSoftwareObj.SoftwareName = attObj.AttributeValue;
          break;
        }
        case "version": {
          installedSoftwareObj.NewVersion = attObj.AttributeValue;
          break;
        }
        case "old version": {
          installedSoftwareObj.OldVersion = attObj.AttributeValue;
          break;
        }
        case "installdate": {
          installedSoftwareObj.NewInstalledDate = attObj.AttributeValue;
          break;
        }
        case "old installdate": {
          installedSoftwareObj.OldInstalledDate = attObj.AttributeValue;
          break;
        }
        case "publisher": {
          installedSoftwareObj.NewPublisher = attObj.AttributeValue;
          break;
        }
        case "old publisher": {
          installedSoftwareObj.OldPublisher = attObj.AttributeValue;
          break;
        }
        default: {
          break;
        }
      }
    }
    console.log("Installed softweare Object: " + installedSoftwareObj);
    return installedSoftwareObj;
  }

  mapOsDetails(attributeList: Attribute[]) {
    console.log("map Os details method called");
    let osdetails: IOsDetails = {};
    for (let attObj of attributeList) {
      switch (attObj.AttributeName.toLowerCase()) {
        case "computer name": {
          osdetails.ComputerName = attObj.AttributeValue;
          break;
        }
        case "old computer name": {
          osdetails.ComputerName = attObj.AttributeValue;
          break;
        }
        case "version": {
          osdetails.NewVersion = attObj.AttributeValue;
          break;
        }
        case "old version": {
          osdetails.OldVersion = attObj.AttributeValue;
          break;
        }
        case "caption": {
          osdetails.NewCaption = attObj.AttributeValue;
          break;
        }
        case "old caption": {
          osdetails.OldCaption = attObj.AttributeValue;
          break;
        }
        case "os architecture": {
          osdetails.NewOsArchitecture = attObj.AttributeValue;
          break;
        }
        case "old os architecture": {
          osdetails.OldOsArchitecture = attObj.AttributeValue;
          break;
        }
        case "buildnumber": {
          osdetails.NewBuildNumber = attObj.AttributeValue;
          break;
        }
        case "old buildnumber": {
          osdetails.OldBuildNumber = attObj.AttributeValue;
          break;
        }
        default: {
          break;
        }
      }
    }
    console.log("OS details Object: " + osdetails);
    return osdetails;
  }

  mapSreenRes(attributeList: Attribute[]) {
    console.log("map Screen Res method called");
    let screenResObj: IScreenResolution = {};
    let oldHeight: string;
    let oldWidth: string;
    let newHeight: string;
    let newWidth: string;
    for (let attObj of attributeList) {
      switch (attObj.AttributeName.toLowerCase()) {
        case "pelsheight": {
          newHeight = attObj.AttributeValue;
          break;
        }
        case "old pelsheight": {
          oldHeight = attObj.AttributeValue;
          break;
        }
        case "pelswidth": {
          newWidth = attObj.AttributeValue;
          break;
        }
        case "old pelswidth": {
          oldWidth = attObj.AttributeValue;
          break;
        }
        default: {
          break;
        }
      }
    }
    screenResObj.NewSR = newHeight + "*" + newWidth;
    screenResObj.OldSR = oldHeight + "*" + oldWidth;
    console.log("Screen Res Object: " + screenResObj);
    return screenResObj;
  }

  myFunction(x) {
    x.classList.toggle("fa fa-minus");
  }

  csvJson = [
    {
      "Name": "InstalledSoftware",
      "Value": "Resource,Resource Type,Software Name,Old Version,Old Installed Date,Old Publisher,New Version,New Installed Date,New Publisher,Status\nHostname,Bot Runner,Notepad++ (64-bit x64),7.3.3,,Notepad++ Team,7.3.3_1,,Notepad++ Team,MODIFIED\r\nnHostname,Bot Runner,Test Adobe Acrobat Reader DC,,,,18.011.20040,20180523,Adobe Systems Incorporated,ADDED\r\nnHostname,Bot Runner,,12.0.2269.0,20180526,Microsoft Corporation,,,,DELETED\r\nnHostname,Bot Runner,,18.011.20040,20180523,Adobe Systems Incorporated,,,,DELETED\r\nnHostname,Bot Runner,Notepad++ (64-bit x64),7.3.3,,Notepad++ Team,7.3.3_1,,Notepad++ Team,MODIFIED\r\nnHostname,Bot Runner,Test Adobe Acrobat Reader DC,,,,18.011.20040,20180523,Adobe Systems Incorporated,ADDED\r\nnHostname,Bot Runner,,12.0.2269.0,20180526,Microsoft Corporation,,,,DELETED\r\nnHostname,Bot Runner,,18.011.20040,20180523,Adobe Systems Incorporated,,,,DELETED\r\n"
    },
    {
      "Name": "OSDetails",
      "Value": "Resource,Resource Type,Name,Old Version,Old Build Number,New Version,New Build Number,Status\nnHostname,Bot Runner,Microsoft Windows Server 2012 R2 Standard,6.3.9600,9600,6.3.9600,9600_1,MODIFIED\r\nnHostname,Bot Creator,Microsoft Windows Server 2012 R2 Standard,6.3.9600,9600,6.3.9600,9600_1,MODIFIED\r\n"
    },
    {
      "Name": "SRDetails",
      "Value": "Resource,Resource Type,Old Resolution,New Resolution,Status\nnHostname,Bot Runner,768*1024,768_1*,MODIFIED\r\nnHostnamem,Control Tower,768*1024,768_1*,MODIFIED\r\n"
    }
  ]

  finalJson:CsvObj[];
  trnafromJson() {
this.finalJson=[];
var tempObj:CsvObj={};
    for (let index = 0; index < this.csvJson.length; index++) {
      tempObj.MetricName=this.csvJson[index].Name;

        let str=this.csvJson[index].Value;

        let header=str.split("\n")[0].split(",");

        // let values=


      
      
    }
  }
}
interface CsvObj {
  MetricName?: string;
  details?: any[];
}
