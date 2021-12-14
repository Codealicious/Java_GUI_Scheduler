# Global Inc. 
## Appointment Scheduler
#### Version 2.0

<br>__DESCRIPTION__<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;The application allows users to view, create, update, and delete both customer records and appointments.<br>
The application also provides three reports- schedules for selected employees or customers and a summary<br>
of the total appointments for each month in the current year along with the total appointments by type for each month.

<br>__INSTRUCTIONS__<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;Launch the application by running the main static method in class Main. The fist window to display will be the<br>
login form. Access the application using username: test and password: test. Upon successful authentication<br>
the application dashboard should be displayed.<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;Customer and Appointment records are displayed in tabular views within a TabPane. Each table can be viewed by<br>
clicking tabs of the same name, by default the Customer table is displayed.<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;The Add and Update buttons in the bottom right-hand corner launch forms that allow a user to add or update either<br>
a customer or appointment based on the currently selected tab and table being displayed. If the currently displayed<br>
table has an item selected the appropriated update form is launched, otherwise the appropriate add form is launched.<br>
The Delete button deletes a record based on the currently selected tab and whether or not there is an item selected<br>
in the currently visible table.<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;The Appointments table view has additional functionality. Located below the Appointment table is a group of<br>
toggle selections. The records in the Appointments table can be filtered by all appointments, those for the <br>
current week, or those for the current month.<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;Above the table views on the left-hand side is a reports button. Clicking this button launchs a window with three<br>
buttons. The Employee Schedule button launches a window where contacts at the company can be selected from<br>
a drop-down list; A schedule is displayed for the selected contact. The Customer Schedule button launches a <br>
similar reports window for customers. The third report is a summary of the total appointments for each month in<br>
the current year as well as the total of appointments by type for each month.<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;Finally the Logout button in the upper right-hand of the dashboard takes the user back to the login form.<br><br>
---
<br>__UPDATES:__&nbsp;Release 2.0<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;* All Lambda and method references used in public facing methods have been documented and explained.<br>


---
<br>__SPECS:__<br><br>
&nbsp;&nbsp;Developed Using:<br>
&nbsp;&nbsp;&nbsp;&nbsp;Java SDK version 15<br>
&nbsp;&nbsp;&nbsp;&nbsp;JavaFX version 16<br>
&nbsp;&nbsp;&nbsp;&nbsp;JDBC Driver: MySQL Connector/J 8.0.27<br><br>
&nbsp;&nbsp;Configured to run in VM using:<br>
&nbsp;&nbsp;&nbsp;&nbsp; Java 11<br>
&nbsp;&nbsp;&nbsp;&nbsp; JDBC Driver: MySQL Connector/J 8.0.25<br><br>
&nbsp;&nbsp;NOTE: VM options must be set with-<br>
--module-path C:\Users\LabUser\Desktop\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml<br><br>
&nbsp;&nbsp;IDE: <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;IntelliJ IDEA 2021.2.3 (Community Edition)<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Build #IC-212.5457.46, built on October 12, 2021<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Runtime version: 11.0.12+7-b1504.40 x86_64<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;macOS 11.6 <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GC: G1 Young Generation, G1 Old Generation Memory: 1024M Cores: 4<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Non-Bundled Plugins: org.jetbrains.kotlin (212-1.5.31-release-546-IJ4638.7)
Kotlin: 212-1.5.31-release-546-IJ4638.7
---

__Author: Alex Hanson__<br>
__Email: ahans01@wgu.edu__<br>
__12/5/2021 Software 2 Project Assessment__


