@echo off
echo "JAVA_HOME=%JAVA_HOME%"
PATH %PATH%;"%JAVA_HOME%"\bin\
for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -fullversion 2^>^&1') do set "jver=%%j%%k%%l%%m"
if %errorlevel% NEQ 0 (
  echo java.exe not found. Please check JAVA_HOME.
  GOTO EXIT0
)
echo "Detect Java Version %jver%"
if %jver% LSS 18000 (
    echo "not supported version. Java 1.8 or later is required."
    GOTO EXIT0
)
java -cp * org.lff.handwriting.Main
:EXIT0
echo Exited.