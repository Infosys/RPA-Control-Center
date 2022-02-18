export interface IReportDetails{
    PlatformId:string
    TenantId:string
    PlatformName:string
    TenantName:string
    StartDate:string
    EndDate:string
    ResourceDetails:ResourceDetails
}
export interface ResourceDetails{    
    ResourceId:string
    ResourceName:string
    ResourceTypeId:string
    ResourceTypeName:string
    MetricDetails:MetricDetail
}
export interface  MetricDetail{
    ObservableId:string
    ObservableName:string
    MetricValue:MetricValues[]
}
export interface MetricValues{
    MetricId:string
    MetricName:string
    MetricKey:string
    Status:string
    Attributes:Attribute[]
}
export interface Attribute{
    AttributeName:string
    AttributeValue:string
    DisplayName:string
}

export interface IInstalledSoftware{
    SoftwareName?:string
    OldVersion?:string
    NewVersion?:string
    OldInstalledDate?:string
    NewInstalledDate?:string
    OldPublisher?:string
    NewPublisher?:string
    Status?:string
}
export interface IOsDetails{
    ComputerName?:string 
    OldCaption?:string
    NewCaption?:string
    OldVersion?:string
    NewVersion?:string
    OldOsArchitecture?:string
    NewOsArchitecture?:string
    OldBuildNumber?:string
    NewBuildNumber?:string
    Status?:string
}
export interface IScreenResolution{
    OldSR?:string 
    NewSR?:string
    Status?:string
}

