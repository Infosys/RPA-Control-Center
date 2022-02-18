export interface ClusterList{
    clusterName?: string
    clusterId?:string
    dateCreated?:string
    resourceType?:string
    visible?:boolean
    clusterChild? : Portfolio[]
}

export interface Portfolio {
    portfolioName? : string
    portfolioId?:string
    dateCreated?:string
    state?:string
    resourceType?:string
    
}

export interface Cluster{
    clusterId?: string
    clusterName?: string
    portfolioId?: string
    portfolioName?: string
}
