set cp=.;dist\lwdba.jar

@echo off
set LOCALCLASSPATH=%cp%
for %%i in ("lib\*.jar") do call ".\lcp.bat" %%i
set cp=%LOCALCLASSPATH%

java -cp %cp% tw.qing.lwdba.sample.LWDBASample %1 %2 %3 %4