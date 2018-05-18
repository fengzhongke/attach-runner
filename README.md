# attach-runner
use can use this tool to comunicate with a running process like jstat or jstack
run external code in the running process

# run example
1、packaging jar with
   mvn clean package 
2、running main in com.ali.dbtech.test.MainProcess with ide or run command like
  java -cp . com.ali.dbtech.test.MainProcess
3、find out the process id created  step 2 
  jps
4、running code from com.ali.dbtech.test.DynamicCode in process created in step 2 （1234 is the process id）
java -jar target/attach-runner.jar 1234 com.ali.dbtech.test.DynamicCode
5、change code in com.ali.dbtech.test.DynamicCode and do step 4 again and see output from process created in step 2

# you can also test it in big process like spring and get output of beans 
