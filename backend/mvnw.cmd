@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script for Windows
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET DP0=%~dp0
@SET MAVEN_WRAPPER_JAR="%DP0%.mvn\wrapper\maven-wrapper.jar"
@SET MAVEN_WRAPPER_PROPERTIES="%DP0%.mvn\wrapper\maven-wrapper.properties"
@SET DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

@FOR /F "usebackq tokens=1,2 delims==" %%A IN (%MAVEN_WRAPPER_PROPERTIES%) DO (
    @IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@SET JAVA_HOME_CANDIDATES="C:\Program Files\Eclipse Adoptium\jdk-17.0.10.7-hotspot"
@IF EXIST %JAVA_HOME_CANDIDATES% SET JAVA_HOME=%JAVA_HOME_CANDIDATES:"=%

@SET JAVA_EXE="%JAVA_HOME%\bin\java.exe"
@IF NOT EXIST %JAVA_EXE% SET JAVA_EXE=java

@IF NOT EXIST %MAVEN_WRAPPER_JAR% (
    @ECHO Downloading Maven Wrapper JAR from %DOWNLOAD_URL%...
    @powershell -Command "Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
)

@%JAVA_EXE% -jar %MAVEN_WRAPPER_JAR% %*
