export class Server{

    componentHealth?:string;
    componentParam?: number;
    criticalCount?: number;
    healthyCount?: number;
    observations?: Parameter[];
    resourceId?: string;
    resourceName?: string;
    resourceTypeID?: string;
    resourcetypeId?: string;
    warningCount?:  number;
    selected?:boolean=false;
    current?:boolean=false;
}

export class Parameter{
    observableId?: string;
    observableName?: string;
    observationTime?:string;
    serverState?: string;
    //TO-DO chnaged to string as per superbot intgreation change jan 3rd 2020
    threshold?: string;
    thresholdType?: string;
    selected:boolean=false;

}