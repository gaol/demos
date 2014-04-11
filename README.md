Build
=====

>> mvn clean install


Run
====

>> cd common
>> ./demo.sh

Run help to see the commands 
>> help


Write your own Demo
====
 1 A public class with no-argument constructor
 1 All no-argument public methods can be annotated with @Demo
 1 Rebuild and run the 'demo.sh' to start the demo console
 1 run 'list' in the demo console, you can see the new Demo
 1 run 'run DEMO-NAME' to run the annotated demo method

>> NOTE: demo runs will create a new instance of the class where the demo method is declared, and call the method
