export class InitialLoadCheck {
    service: string
    initialLoad: boolean

    static chnageInitialLoad(service: string, value: boolean, initialLoadJson:InitialLoadCheck[]) {
        initialLoadJson.forEach((item) => {
          if (item.service == service || service == "all") {
            item.initialLoad = value;
          }
        })
        return initialLoadJson;
      }
    
      static  getLoadInterval(service: string, initialLoadJson:InitialLoadCheck[]) {
        var flag = false;
        initialLoadJson.forEach((item) => {
          if (item.service == service) {
            flag = item.initialLoad;
          }
        })
        return flag;
      }
     
  }
 