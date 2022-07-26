# RPA-Control-Center
The RPA Control Center provides a cumulative view of the health of the various RPA or IT applications or infrastructure. It allows deep dive into the individual KPI of the system or application.

# Build Instructions
1.Download the release package zip files RPA_Control_Center-Win and Source from github <br/>
2.Unblock the zip files and extract all the files <br/>
3.Copy the file PasswordUtility-0.0.1 from the extracted directory ..\Source\OperationsPortal\target\OperationsPortal\WEB-INF\lib to the directory C:\Users\\[username]\\.m2\repository\com\infosys\aina\solutions\PasswordUtility\0.0.1 <br/>
4.Launch the eclipse IDE <br/>
5.Click on File menu > Import <br/>
6.In the pop up window Import, select Maven > Existing Maven Projects > click on Next <br/>
7.Click on browse button and select the folder ..\RPA-Control-Center-main\OperationsPortal <br/>
8.Make sure pom.xml file is selected/checked under projects and click on Finish <br/>
8.Right click on OperationsPortal in Project Explorer > Run As > Maven install <br/>
9.BUILD SUCCESS message should be displayed in the console <br/>
