export interface ComponentList{
    resourcetypeId: string,
    resourceTypeID?:string
    resourceId?: string,
    resourceName?:string,
    componentHealth?: string, 
    componentParam?: number,
    criticalCount?: number,
    warningCount?:number,
    observations? : Observations[]
}

export interface Observations {

    observableName?:string,
    observableId?:string,
    observationTime?:string,
    serverState?:string,
    threshold?:number,
    thresholdType?:string,
    value?:number
    
}
