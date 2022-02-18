export class RemediationAction {
    remediationplanid?: number;
    remediationplanname?: string;
    servertype?:string;
    platformname?:string;
    observabletype?:string;
    isuserdefined?: boolean;
    actions: Action[]
}

export class Action {
    isdeleted?: boolean = false;
    remediationplanactionid?: string;
    actionsequence: number;
    actionstageid: string;
    actionstagename?: string;
    actionid: number;
    scriptid?:number;
    parameters?: Parameter[];
    actionname?:string;
    categoryid?:number;
    actiondetails?:string;
}

export class Parameter {
    remediationplanactionid?: string;
    paramid?: number;
    name: string;
    providedvalue: string;
    isfield?: boolean;
    ismandatory?: boolean;
    defaultvalue?: string;

}


export class ActionType {
    actionStageId: number;
    actionStageName: string
}

export class RemediationPlan {
    remediationPlanid: number;
    remediationPlanName: string;
    dateCreated: string;
    lastRunDateTime: string;
    lastRunBy: string;
    runDuration: string;
    isUerDefined: string
    isPlanExecuting: boolean;
}
export class ActionScript {
    actionid?: number;
    actionname?: string;
    params: Parameter[];
}


export class ScriptArray{
    environmentName?:string;
    environmentVersion? :string;
    businessProcesses?:BusinessProcesses[]
}


export class BusinessProcesses {
    id: string;
    categoryId: string;
    name: string;
    version?: string;
    description?: string;
    outputVariables?: string;
    runtimeResourceDefinition?: string;
    inputVariables?:InputVariables[]
}
export class InputVariables {
    remediationplanactionid?: string;
    paramid?: number;
    defaultvalue?: string;
    name: string;
    dataType?: string;
    value: string;
    sequence?: string;
    defaultValue?: string;
    allowedValues?: string;
    mandatory: boolean;
}

export class ExecuteJson{
    remediationplanid: string;
    resourceid: string;
    observableid: string;
    domain:string;
    iapnodetransport:number;
    username:string;
    password:string;
    referencekey:string;
    remoteservernames:string;
}

export class HistoryDetails{
    remediationplanid: string;
    remediationplanactionid:string;
    status:string;
    logdata:string;
    output: string;
    scriptname: string;
}