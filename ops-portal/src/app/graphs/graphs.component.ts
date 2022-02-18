import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';

import { ClusterDetailsService } from '../cluster-details/cluster-details.service';
import { IMyDpOptions, IMyDate } from 'mydatepicker';
import { DataService } from '../cluster-details/data.service';
import { ComponentList } from '../component-health/components-list.model';
import { Chart } from 'chart.js';
import { Subscription } from 'rxjs';
import { Server, Parameter } from './components.model';
import { ViewChild, ElementRef } from '@angular/core';
declare var jquery: any;
declare var $: any;
@Component({
  selector: 'app-graphs',
  templateUrl: './graphs.component.html',

  styleUrls: ['./graphs.component.scss']
})
export class GraphsComponent implements OnInit, OnDestroy {

  public context: CanvasRenderingContext2D;



  Chart: any;
  showParameters: boolean;
  myChart: any;
  subscription: Subscription;
  lineChartJson: any;
  lineChartJson_tmp: any;
  lineChartData: any;
  inputResources: any = "[   {     \"resourceid\": \"10.177.120.70\",     \"observableid\": \"OBS_CPU_UTIL\"   },{     \"resourceid\": \"10.177.120.76\",     \"observableid\": \"OBS_MEM_UTIL\"   },{     \"resourceid\": \"10.177.120.76\",     \"observableid\": \"OBS_CPU_UTIL\"   },{     \"resourceid\": \"10.177.120.70\",     \"observableid\": \"OBS_MEM_UTIL\"   }  ]";
  serverId: string;
  obsName: string;
  graphHistoryData: any
  jsonComponentDataFilterVisible: Server[] = [];
  jsonComponentData: Server[] = [];
  jsonParameterType: any = [];
  jsonResourceData: any = [];
  componentParameters: any = [];
  portfolioId: string;
  incidentId: string;
  selectedDataArray: KeyValue[] = [];
  rowList: Server[];
  componentArray: any;
  searchValue = '';
  tmpDate: any = '';
  fromDate: any = '';
  toDate: any = '';
  fromDate_page: any = '';
  toDate_page: any = '';
  observableId = 'OBS_CPU_UTIL';
  resourceName: string;
  selDate: any;
  finalSelectedComponent: string[] = [];
  isloader = false;
  // tempGraphDataArray:ComponentList[]=[];
  // tempGraphData:any;
  constructor(private clusterDetailsService: ClusterDetailsService, private data: DataService) { }

  settings = {
    bigBanner: true,
    timePicker: true,
    format: 'yyyy-MM-dd hh:mm a',
    defaultOpen: false
  };

  public myDatePickerOptions: IMyDpOptions = {
    // other options...
    // showClearDateBtn :false,
    dateFormat: 'yyyy-mm-dd',


  };
  // adding extra boolean paramaters to existing json : which will use in make selected those components while display
  addDefaults() {

    this.jsonComponentData.filter(server => {
      var nameHasFilterText;
      nameHasFilterText = server.resourceId.length >= 0
      if (nameHasFilterText) {
        server.selected = false;
        server.current = false;
        return nameHasFilterText;
      }

    })

    this.jsonComponentData[0].observations.filter(parameter => {
      var nameHasFilterText;
      nameHasFilterText = parameter.observableName.length >= 0
      if (nameHasFilterText) {
        parameter.selected = false;
        return nameHasFilterText;
      }

    })

    let tempjson = JSON.parse(JSON.stringify(this.jsonComponentData))

    this.jsonComponentDataFilterVisible = <Server[]>tempjson;
  }

  checkSelectedComponent(resourceId: string, observableId: string) {
    // debugger
    if (resourceId === '') {
      this.jsonComponentDataFilterVisible.forEach(server => {

        let minSelected = 0;
        server.observations.forEach(parameter => {
          if (parameter.selected) {
            minSelected++;
          }
        });
        if (minSelected > 0) {
          server.selected = true;

        } else {
          server.selected = false;
        }



      })
    } else {
      this.jsonComponentDataFilterVisible.forEach(server => {

        if (server.resourceId === resourceId) {
          server.selected = true;
          server.observations.forEach(parameter => {
            if (parameter.observableId === observableId) {
              parameter.selected = true;
            }
          });
        }
      })
    }

    this.modifyData();
  }

  selectParameter(Observations: Parameter) {
    // debugger
    Observations.selected = !Observations.selected;
    this.checkSelectedComponent('', '');
  }
  ngOnInit() {
    this.isloader = false;
    this.showParameters = false;
    // debugger
    this.getParameterType();
    this.setValues();
    this.getComponentsDetails();


    let d: Date = new Date();
    let mnth: number;
    let year: number;

    if (d.getMonth() === 0) {
      mnth = 12;
      year = d.getFullYear() - 1;
    } else {
      mnth = d.getMonth();
      year = d.getFullYear();
    }


    this.fromDate = year + "-" + (mnth) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes();
    this.toDate = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes();



    this.selDate = { year: d.getFullYear(), month: d.getMonth() + 1, day: d.getDate() };
    // this.fromDate = d.getFullYear() + "-" + (d.getMonth()) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes();
    // this.toDate = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate() + " " + d.getHours() + ":" + d.getMinutes();
    this.fromDate_page = this.fromDate;
    this.toDate_page = this.toDate;
    // this.getComponentsDetails();
    // this.data.currentGraph.subscribe(

    //   message => this.lineChartJsonDummy = message

    // );

    this.getComponenetHistoryData();


    // console.log("inputreso (graph):: ", this.inputResources);
  }

  setValues() {
    this.portfolioId = localStorage.getItem('portfolioId');
    this.incidentId = localStorage.getItem('incidentId');

  }
  getComponentsDetails() {
      this.subscription = this.clusterDetailsService.getComponentDetailsForGraph(this.portfolioId, this.incidentId)
      .subscribe(res => {
        this.jsonComponentData = <Server[]>res;
        // this.jsonComponentDataFilterVisible = this.jsonComponentData.slice(0);

        console.log("Component Data :: ", this.jsonComponentData);
        this.addDefaults();
      });
    }
  loadData() {

    this.check();
    this.modifyData();
    this.selectedCompoenets = [];
  }
  modifyData() {

    //this.jsonComponentDataFilterVisible= this.lineChartJson;
    this.rowList = [];
    //var rowList = [];
    var colCount = 3;
    var cntr = 0;
    var row = []
    console.log("Total length : " + this.jsonComponentDataFilterVisible.length)
    console.log(this.jsonComponentDataFilterVisible)
    while (cntr < this.jsonComponentDataFilterVisible.length) {
      var component = this.jsonComponentDataFilterVisible[cntr]
      if (cntr !== 0 && cntr % colCount === 0) {
        this.rowList.push(<Server>row);
        console.log("All rows as of now : ")
        console.log(this.rowList);
        row = []
      }
      console.log("Element at " + cntr + " : Comopinitklj" + component)
      console.log(component);

      row.push(component);
      cntr++
    }
    if (row.length > 0) {
      this.rowList.push(<Server>row)
    }

    console.log("Final RowList : ")
    console.log(this.rowList);
    // if (this.rowList.length > 0) {
    //   this.jsonComponentDataFilterVisible =  this.rowList;
    // }
    console.log("modified Data :: ", this.jsonComponentDataFilterVisible);
  }

  getParameterType() {

    this.subscription = this.clusterDetailsService.getParameterType()
      .subscribe(res => {
        this.jsonParameterType = res;
        console.log("parameter type Data :: ", this.jsonParameterType);
      });
  }
  getComponenetHistoryData() {
    this.inputResources = localStorage.getItem('selectedGraphData');
    this.getGraphData(this.inputResources);
  }

  getGraphData(inputResources: any) {
    console.log("Inside graphData");
    this.isloader = true;
    this.subscription = this.clusterDetailsService.getComponentHistory(inputResources)
      .subscribe(res => {
        this.lineChartJson_tmp = res;
        this.lineChartData = res;
        console.log("ComponentHistory Data (graph):: ", this.lineChartJson_tmp);
        this.getDataOfDatesPage();
        this.isloader = false;
      },
        error => {
          this.isloader = false;
        }
      );
  }
  getGraphDataPopup(inputResources: any) {
    console.log("Inside graphData");
    this.subscription = this.clusterDetailsService.getComponentHistory(inputResources)
      .subscribe(res => {
        this.lineChartJson_tmp = res;
        this.getDataOfDatesPage();
        console.log("ComponentHistory Data (graph):: ", this.lineChartJson);
      });
  }
  thresholdArray: number[] = [];
  recordedArray: number[] = [];
  lineChartLabels: string[] = ['Jun', 'Jul', 'Aug'];//this.lineChartJson.currentDate;
  lineChartDataCritical: any = [[1, 33, 44], [14, 16, 73]];
  lineChartType: string = 'line';
  observableValue: number[] = [];
  thresholdValue: number[] = [];
  upperThreshold: number[] = [];
  lineChartColors_side: {}[] = [

    {
      backgroundColor: "#5fc2f6",
      hoverBackgroundColor: "#5fc2f6",
      borderColor: "#5fc2f6",
      hoverBorderColor: "#5fc2f6",
      borderWidth: 2
    },
    {
      backgroundColor: "#ffc611",
      hoverBackgroundColor: "#ffc611",
      borderColor: "#ffc611",
      hoverBorderColor: "#ffc611",
      borderWidth: 2
    },
    {
      backgroundColor: "#FF183E",
      hoverBackgroundColor: "#FF183E",
      borderColor: "#FF183E",
      hoverBorderColor: "#FF183E",
      borderWidth: 2
    },

  ];
  lineChartColors_content: {}[] = [

    {
      backgroundColor: "#FF183E",
      hoverBackgroundColor: "#FF183E",
      borderColor: "#FF183E",
      hoverBorderColor: "#FF183E",
      borderWidth: 3
    },
    {
      backgroundColor: "#82AB26",
      hoverBackgroundColor: "#82AB26",
      borderColor: "#82AB26",
      hoverBorderColor: "#82AB26",
      borderWidth: 3
    },

  ];

  // options_critical = {scale: {  scaleLabel: { fontSize:30 } }, };
  options_side = {
    showXLabels: 0,
    showYLabels: 0,
    layout: {
      padding: {
        left: 5,
        right: 5,
        top: 5,
        bottom: 5
      }
    },
    scales: {
      xAxes: [{
        display: true,
        gridLines: {
          // color: 'rgba(37, 49, 56)', // makes grid lines from y axis red
          color: "rgb(134, 136, 138)",
          display: false

        },
        ticks: {
          display: false //this will remove only the label
        }
      }],
      yAxes: [{
        display: true,
        gridLines: {
          color: "rgb(134, 136, 138)",
          display: false

        },
        ticks: {
          display: false //this will remove only the label
        }

      }],
    },
    scaleShowLabels: false,
    legend: {
      display: false
    },
    elements: {

      legend: {
        display: false
      },
      line: {
        fill: false,

        tension: 0
      },
      point: {
        radius: 0
      },


    }

  };
  options_critical = {
    scales: {
      xAxes: [{
        display: true,
        gridLines: {

          color: "rgb(134, 136, 138)",
          display: false

        },
        ticks: {
          fontSize: 10,
          fontColor: "#FFF", // this here

        },
        scaleLabel: {
          display: true,
          labelString: 'Duration (in days)',
          fontSize: 35,
          fontColor: "#FFFFFF"
        }
      }],
      yAxes: [{
        display: true,
        gridLines: {

          color: "rgb(134, 136, 138)",
          display: false

        },
        ticks: {
          fontSize: 13,
          fontColor: "#FFF", // this here
        },

      }],
    },
    scaleShowLabels: false,

    elements: {

      legend: {
        display: false
      },
      line: {
        fill: false,

        tension: 0
      },
      point: {
        radius: 0
      },


    }

  };




  // addToGraphList(data_re: any, data_th: any) {
  //   debugger
  //   this.thresholdArray.push(data_th);
  //   this.recordedArray.push(data_re);
  // }
  // removeFromGraphList(data_re: any, data_th: any) {
  //   var index = this.recordedArray.indexOf(data_re, 0);
  //   if (index > -1) {
  //     this.thresholdArray.splice(data_th);
  //   this.recordedArray.splice(data_re);
  //   }

  // }
  showGraphDiv(index: any, data_re: number[], data_th: number[], graphlable: any, resourceId: string, observableName: string, observableId: string, unitOfMeasure: any, upperThreshold: number[]) {
    try {
      var id = "sub_graph_" + index;
      document.getElementById(id).style.display = "block";

      var side = 'side_graph_' + index
      document.getElementById(side).className = "containerbox containerbox-check";
      var canvas_id = "sub_graph_canvas_id_" + index;
      this.generatePageGraph(data_re, data_th, graphlable, observableId, canvas_id, unitOfMeasure, upperThreshold);

    } catch (e) {

    }



  }
  hideGraphDiv(index: any) {
    var id = "sub_graph_" + index;
    document.getElementById(id).style.display = "none";
    var side = 'side_graph_' + index
    document.getElementById(side).className = 'containerbox ';


  }
  showSideGraph(index: any) {
    var id = 'side_graph_' + index;
    document.getElementById(id).style.display = 'block';
  }
  hideSideGraph(index: any) {
    var id = 'side_graph_' + index;
    document.getElementById(id).style.display = 'none';
  }

  generateGraph(data_re: any, data_th: any, graphlable: any, observableId: any, id: any, unitOfMeasure: any, upperThreshold: any) {

    var canvas = <HTMLCanvasElement>document.getElementById(id);

    this.observableValue = data_re;
    this.thresholdValue = data_th;


    var ctx = canvas.getContext('2d');
    //ctx.clear();
    try {

      this.myChart.destroy();
      this.myChart = null;
    } catch (e) {

    }

    this.myChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: graphlable,

        datasets: [{

          label: 'Recorded Value',

          pointRadius: 0,
          data: data_re,
          backgroundColor: '#5fc2f6',
          hoverBackgroundColor: '#5fc2f6',
          borderColor: '#5fc2f6',
          hoverBorderColor: '#5fc2f6',
          fill: false,

          lineTension: 0,
          borderWidth: 2
        },
        {
          label: 'Lower Threshold',
          fontColor: '#FFFFFF',
          data: data_th,
          fill: false,
          pointRadius: 0,
          backgroundColor: '#ffc611',
          hoverBackgroundColor: '#ffc611',
          borderColor: '#ffc611',
          hoverBorderColor: '#ffc611',
          lineTension: 0,
          borderWidth: 2
        },

        {
          label: 'Upper Threshold',
          fontColor: '#FFFFFF',
          data: upperThreshold,
          fill: false,
          pointRadius: 0,
          backgroundColor: '#FF183E',
          hoverBackgroundColor: '#FF183E',
          borderColor: '#FF183E',
          hoverBorderColor: '#FF183E',
          lineTension: 0,
          borderWidth: 2
        },
        ]
      },
      options: {

        legend: {
          labels: {
            fontColor: 'white'
          }
        },
        tooltips: false,

        scales: {
          xAxes: [{
            gridLines: {

              color: 'rgb(134, 136, 138)',
              display: false

            },
            ticks: {
              fontSize: 10,
              fontColor: '#FFF', // this here
            },
            scaleLabel: {
              display: true,
              fontSize: 30,
              fontColor: '#FFFFFF',
              labelString: 'Duration (in days)'
            }
          }],
          yAxes: [{
            gridLines: {

              color: 'rgb(134, 136, 138)',
              display: false

            },
            // stacked:true,
            ticks: {
              fontSize: 10,
              fontColor: '#FFF', // this here

            },
            scaleLabel: {
              display: true,
              labelString: unitOfMeasure,
              fontSize: 30,
              fontColor: '#FFFFFF'
            }
          }]
        }
      }
    });
  }
  generatePageGraph(data_re: any, data_th: any, graphlable: any, observableId: any, id: any, unitOfMeasure: any, upperThreshold: any) {

    const canvas = <HTMLCanvasElement>document.getElementById(id)
    this.observableValue = data_re;
    this.thresholdValue = data_th;
    this.upperThreshold = upperThreshold

    const ctx = canvas.getContext('2d');
    ctx.restore();
    const myChart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: graphlable,

        datasets: [{

          data: data_re,
          backgroundColor: '#5fc2f6',
          hoverBackgroundColor: '#5fc2f6',
          borderColor: '#5fc2f6',
          hoverBorderColor: '#5fc2f6',
          fill: false,


          borderWidth: 2
        },
        {

          fontColor: '#FFFFFF',
          data: data_th,
          fill: false,

          backgroundColor: '#ffc611',
          hoverBackgroundColor: '#ffc611',
          borderColor: '#ffc611',
          hoverBorderColor: '#ffc611',
          borderWidth: 2
        },
        {

          fontColor: '#FFFFFF',
          data: upperThreshold,
          fill: false,

          backgroundColor: '#FF183E',
          hoverBackgroundColor: '#FF183E',
          borderColor: '#FF183E',
          hoverBorderColor: '#FF183E',
          borderWidth: 2
        },

        ]
      },
      options: {

        legend: {
          display: false
        },
        tooltips: false,

        scales: {
          xAxes: [{
            display: true,
            gridLines: {

              color: 'rgb(134, 136, 138)',
              display: false

            },
            ticks: {
              fontSize: 10,
              fontColor: '#FFF', // this here

            },
            scaleLabel: {
              display: true,
              labelString: 'Duration (in days)',
              fontSize: 35,
              fontColor: '#FFFFFF'
            }
          }],
          yAxes: [{
            display: true,
            gridLines: {

              color: 'rgb(134, 136, 138)',
              display: false

            },
            ticks: {
              fontSize: 13,
              fontColor: '#FFF', // this here
            },
            scaleLabel: {
              display: true,
              labelString: unitOfMeasure,
              fontSize: 35,
              fontColor: '#FFFFFF'
            }
          }],
        },
        scaleShowLabels: false,

        elements: {

          legend: {
            display: false
          },
          line: {
            fill: false,

            tension: 0
          },
          point: {
            radius: 0
          },


        }
      }
    });
  }
  showPopup(flag: string, data_re: number[], data_th: number[], graphlable: any, resourceId: string, observableName: string, observableId: string, unitOfMeasure: any, upperThreshold: any, resourceName: string) {

    this.serverId = resourceId;
    this.obsName = observableName;
    this.observableId = observableId;
    this.resourceName = resourceName;
    this.generateGraph(data_re, data_th, graphlable, observableId, 'mycanvas', unitOfMeasure, upperThreshold);
    if (flag === 'true') {

      document.getElementById('sub_header').style.display = 'none';
      document.getElementById('side_page').style.display = 'none';
      document.getElementById('content_page').style.display = 'none';
      document.getElementById('sub_header_graph').style.display = 'block';
      document.getElementById('graph_popup_div').style.display = 'block';

    } else {
      document.getElementById('sub_header').style.display = 'block';

      document.getElementById('side_page').style.display = 'block';
      document.getElementById('content_page').style.display = 'block';
      document.getElementById('sub_header_graph').style.display = 'none';
      document.getElementById('graph_popup_div').style.display = 'none';
    }

  }
  resetData() {

    this.lineChartJson_tmp = this.lineChartData;
    this.getDataOfDatesPage();
    for (let i = 0; i < this.lineChartJson.length; i++) {
      this.showSideGraph(i);
    }
  }
  getDataOfDatesPage() {

    console.log('StartDate :: ', this.fromDate_page);
    console.log('End Date :: ', this.toDate_page);

    if (this.fromDate_page !== '' && this.fromDate_page !== undefined && this.fromDate_page.indexOf(':') <= 0) {
      this.fromDate_page = this.fromDate_page + ' 00:00:00';
    }
    if (this.toDate_page !== '' && this.toDate_page !== undefined && this.toDate_page.indexOf(':') <= 0) {
      this.toDate_page = this.toDate_page + ' 00:00:00';
    }
    if (this.fromDate_page === '') {
      this.errorMessage('FromDate can not be empty');
    } else if (this.toDate_page === '') {
      this.errorMessage('ToDate can not be empty');
    } else
      if (new Date(this.fromDate_page).getTime() > new Date(this.toDate_page).getTime()) {

        this.errorMessage('ToDate should be Greater than FromDate');
      } else {


        const sideBarGraphs = JSON.parse(JSON.stringify(this.lineChartJson_tmp));
        const inputResource: inputJson[] = [];

        for (let i = 0; i < sideBarGraphs.length; i++) {
          inputResource.push({ resourceId: sideBarGraphs[i].resourceId, startDate: this.fromDate_page, endDate: this.toDate_page, observableId: sideBarGraphs[i].observableId });
        }

        console.log('input json filter data :: ', this.fromDate_page, '  ', this.toDate_page);

        this.subscription = this.clusterDetailsService.getFilteredData(inputResource)
          .subscribe(res => {
            this.graphHistoryData = res;
            this.lineChartJson = res;

            // let  tempArray= JSON.parse(JSON.stringify(this.graphHistoryData));

            // if( (tempArray!=null && tempArray!=undefined && tempArray.length>0) ){
            //   this.generateGraph(tempArray[0].observableValue,tempArray[0].thresholdValue,tempArray[0].currentDate,'');
            // }
            console.log('graph history data', this.graphHistoryData);

          });

      }
  }
  getDataOfDates() {

    if (this.fromDate !== '' && this.fromDate !== undefined && this.fromDate.indexOf(':') <= 0) {
      this.fromDate = this.fromDate + ' 00:00:00';
    }
    if (this.toDate !== '' && this.toDate !== undefined && this.toDate.indexOf(':') <= 0) {
      this.toDate = this.toDate + ' 00:00:00';
    }

    if (this.fromDate === '') {
      this.errorMessage('FromDate can not be empty');
    } else if (this.toDate === '') {
      this.errorMessage('ToDate can not be empty');
    } else
      if (new Date(this.fromDate).getTime() > new Date(this.toDate).getTime()) {

        this.errorMessage('ToDate should be greater than FromDate');
      } else {

        const inputResource: inputJson[] = [];

        inputResource.push({ resourceId: this.serverId, startDate: this.fromDate, endDate: this.toDate, observableId: this.observableId });
        console.log('Dates :: ', this.fromDate, '  ', this.toDate);
        this.subscription = this.clusterDetailsService.getFilteredData(inputResource)
          .subscribe(res => {
            this.graphHistoryData = res;
            const tempArray = JSON.parse(JSON.stringify(this.graphHistoryData));

            if ((tempArray != null && tempArray !== undefined && tempArray.length > 0)) {
              this.generateGraph(tempArray[0].observableValue, tempArray[0].thresholdValue, tempArray[0].currentDate, '', 'mycanvas', tempArray[0].unitOfMeasure, tempArray[0].upperthresholdValue);
            }
            console.log('graph history data');
            console.log(this.graphHistoryData);
          });
      }

  }

  onDateSelect(event: any, type: string) {
    /*if(type=="from"){
      this.fromDate_page=event.formatted;
    }else{
      this.toDate_page=event.formatted;
    }*/
    const day = event.getDate();
    const year = event.getFullYear();
    const month = event.getMonth();
    console.log('Date :: ' + year + '-' + month + '-' + day);
  }

  /*onDateChangedPage(event: IMyDateModel, type: string) {
      debugger
          if(type=="from"){
            this.fromDate_page=event.formatted;
          }else{
            this.toDate_page=event.formatted;
          }

  }
    onDateChanged(event: IMyDateModel, type: string) {
  debugger
      if(type=="from"){
        this.fromDate=event.formatted;
      }else{
        this.toDate=event.formatted;
      }

    }*/

  onDateChangedPage(event: any, type: string) {

    const day = event.getDate();
    const year = event.getFullYear();
    const month = event.getMonth();
    const hours = event.getHours();
    const mins = event.getMinutes();

    const formatted_date = year + '-' + (month + 1) + '-' + day + ' ' + hours + ':' + mins + ':00';

    if (type === 'from') {
      this.fromDate_page = formatted_date;
    } else {
      this.toDate_page = formatted_date;
    }

  }
  onDateChanged(event: any, type: string) {
    const day = event.getDate();
    const year = event.getFullYear();
    const month = event.getMonth();
    const hours = event.getHours();
    const mins = event.getMinutes();
    const formatted_date = year + '-' + (month + 1) + '-' + day + ' ' + hours + ':' + mins + ':00';

    if (type === 'from') {
      this.fromDate = formatted_date;
    } else {
      this.toDate = formatted_date;
    }

  }

  // getParameters(value: string) {
  //   this.componentParameters = this.jsonComponentDataFilterVisible.filter(component => {
  //     var nameHasFilterText;

  //     nameHasFilterText = component.resourceId.toLowerCase().indexOf(value.toLowerCase()) >= 0

  //     if (nameHasFilterText) {
  //       return nameHasFilterText
  //     }
  //   })
  // }
  selectedCompoenets: string[] = [];

  makeSelected(resourceId: string) {
    this.jsonComponentDataFilterVisible.filter(server => {
      let nameHasFilterText;
      nameHasFilterText = server.resourceId.length >= 0;
      if (nameHasFilterText) {
        if (server.resourceId === resourceId) {
          server.current = true;
        } else {
          server.current = false;
        }
        return nameHasFilterText;
      }

    });
  }
  cancelModal() {
    this.showParameters = false;
  }
  selectComponent(id: string, component: Server) {
    this.showParameters = true;
    // var clsname = document.getElementById(id).className;
    // if (clsname != undefined && (clsname.trim() == "filter-data btn-component-ini" || clsname.trim() == "filter-data btn-component")) {
    // document.getElementById(id).className = "filter-data btn-component-fill";
    // this.getParameters(resourceId);
    // component.selected=true;
    component.current = true;
    this.componentParameters = component;
    // this.selectedCompoenets.push(id);
    this.makeSelected(component.resourceId);
    this.checkSelectedComponent('', '');
    // }


  }
  // getParameterClass(component: string, param: string, paramName: string) {

  //   var index = -1;
  //   const item = <KeyValue>{
  //     observableid: param,
  //     resourceid: component

  //   }
  //   //var item1={"resourceid":"10.177.120.68","observableid/":"OBS_DB_CPU_UTIL"};
  //   if (this.selectedDataArray != null && this.selectedDataArray.length > 0) {
  //     // var  index  = this.selectedDataArray.indexOf(item1);

  //     for (var i = 0; i < this.selectedDataArray.length; i++) {
  //       if (this.selectedDataArray[i].resourceid == item.resourceid && (this.selectedDataArray[i].observableid == item.observableid || this.selectedDataArray[i].observableid == paramName)) {

  //         var index = this.checkPresent(component, paramName);
  //         if (index !== -1) {
  //           this.selectedDataArray.splice(index, 1);
  //           this.selectedDataArray.push({ resourceid: item.resourceid, observableid: param });
  //         }

  //         index = i;
  //         break;
  //       }
  //     }

  //   }
  //   if (index == -1)
  //     return 'img-tbl-unchk'
  //   else
  //     return 'img-tbl-unchk img-tbl-chk';
  // }
  // checkPresent(component: string, param: string) {

  //   var index = -1;
  //   const item = <KeyValue>{
  //     observableid: param,
  //     resourceid: component

  //   }
  //   //var item1={"resourceid":"10.177.120.68","observableid/":"OBS_DB_CPU_UTIL"};
  //   if (this.selectedDataArray != null && this.selectedDataArray.length > 0) {
  //     // var  index  = this.selectedDataArray.indexOf(item1);

  //     for (var i = 0; i < this.selectedDataArray.length; i++) {
  //       if (this.selectedDataArray[i].resourceid == item.resourceid && this.selectedDataArray[i].observableid == item.observableid) {
  //         index = i;
  //         break;
  //       }
  //     }
  //     return index;
  //   }
  // }
  selectedDataType() {
    this.jsonComponentDataFilterVisible = this.lineChartJson.slice(0);
    this.modifyData();
  }

  popupCancel() {
    this.selectedDataArray = [];
  }

  popupOk() {
    this.selectedDataArray = [];
    this.jsonComponentDataFilterVisible.forEach(server => {
      if (server.selected) {
        server.observations.forEach(parameter => {

          if (parameter.selected) {
            this.selectedDataArray.push({ resourceid: server.resourceId, observableid: parameter.observableId });
          }

        });
      }
    });

    const myJsonString = JSON.stringify(this.selectedDataArray);
    this.getGraphDataPopup(myJsonString);
    this.selectedDataArray = [];
  }

  // selectCompParameters(id: string, resourceId: string, observableid: string) {

  //   document.getElementById(id).classList.toggle("img-tbl-chk");
  //   var clsname = document.getElementById(id).className;
  //   if (clsname == "img-tbl-chk" || clsname == "img-tbl-unchk img-tbl-chk") {
  //     this.selectedDataArray.push({ resourceid: resourceId, observableid: observableid });
  //   } else {
  //     var index = this.checkPresent(resourceId, observableid);
  //     if (index !== -1)
  //       this.selectedDataArray.splice(index, 1);
  //   }

  //   this.finalSelectedComponent = [];
  //   for (let value of Array.from(this.selectedDataArray.values())) {
  //     this.finalSelectedComponent.push(value.resourceid);
  //   }

  // }

  check() {
    // this.jsonComponentDataFilterVisible = this.jsonComponentData.slice(0);
    const tempjson = JSON.parse(JSON.stringify(this.jsonComponentData));

    this.jsonComponentDataFilterVisible = <Server[]>tempjson;
    const testData = new Set<string>();


    const tempArray = JSON.parse(JSON.stringify(this.lineChartJson));
    for (let i = 0; i < tempArray.length; i++) {
      testData.add(tempArray[i].resourceId);
      // this.selectedDataArray.push({ resourceid: tempArray[i].resourceId, observableid: tempArray[i].observableId })
      this.checkSelectedComponent(tempArray[i].resourceId, tempArray[i].observableId);
    }



    this.componentArray = Array.from(testData);
    console.log('componentArray');
    console.log(this.componentArray);

    // this.finalSelectedComponent = [];
    // for (let value of Array.from(this.selectedDataArray.values())) {
    //   this.finalSelectedComponent.push(value.resourceid);
    // }
  }

  // isCompoentPresent(component: string) {
  //   debugger
  //   console.log('.....................')
  //   console.log(component);
  //   console.log(this.componentArray);
  //   console.log('.....................')
  //   for (var i = 0; i < this.componentArray.length; i++) {
  //     if (this.componentArray[i] == component) {
  //       return 'filter-data btn-component';
  //     }
  //   }
  //   return 'filter-data btn-component-ini';
  // }
  clearSearch() {
    this.searchValue = '';
    this.searchComponent('');

  }
  // popup search filter
  searchComponent(value: string) {

    if (value !== '' && value.trim() !== 'all') {
      const checkedElementsTemp = [];
      this.jsonComponentDataFilterVisible = this.jsonComponentDataFilterVisible.filter(component => {
        let nameHasFilterText;

        nameHasFilterText = component.resourceName.toLowerCase().indexOf(value.toLowerCase()) >= 0;

        if (nameHasFilterText) {
          return nameHasFilterText;
        }
      });

    } else {
      this.jsonComponentDataFilterVisible = this.jsonComponentData.slice(0);
    }
    this.modifyData();
  }

  // getGrapData(){
  //   debugger
  //   let  tempArray= JSON.parse(JSON.stringify(this.graphHistoryData));

  //   if(tempArray.length>0){
  //     this.generateGraph(tempArray[0].observableValue,tempArray[0].thresholdValue,tempArray[0].currentDate,'');
  //   }

  // }

  errorMessage(message: string) {
    $.bootstrapGrowl(message, {
      type: 'danger',
      delay: 3000,
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
export interface IMyDateModel {
  date: IMyDate;
  jsdate: Date;
  formatted: string;
  epoc: number;
}
interface KeyValue {
  resourceid: string;
  observableid: string;
}

interface inputJson {
  resourceId: string;
  startDate: string;
  endDate: string;
  observableId: string;

}
