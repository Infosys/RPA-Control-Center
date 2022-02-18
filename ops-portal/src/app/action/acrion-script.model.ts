export class ActionPlan{
    remediationplanid:string;
    scriptdetails:Identifier[];
}
export class Identifier{
    scriptIdentifier:Scriptdetails;
}
export class Scriptdetails{
    CategoryId?:number;
    // ActionSeqNo?:number;
    Operation?:string;
    ExecutionMode?:number;
    Parameters?:ActionParameters[];
    // Nodes?:Nodes[];
    Password?:string;
    ReferenceKey?:string;
    ScriptId?:number;
    UserName?:string;
    WEMScriptServiceUrl?:string;
    RemoteServerNames?:string;
    IapNodeTransport?:string;
    Domain?:string;

}
export class ActionParameters{
    ParameterName:string;
    ParameterValue:string;
}
export class Nodes{
    id:number;
	ipAddress:string;
	name:string;
	os:string;
	password:string;
	serviceAreas:string;
	stagePath:string;
	userId:string;
	workingDir:string;
}