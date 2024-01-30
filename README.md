# ElegantPlugin

Allows developers to create operators in a simple and effective way and to perform verification checks on the code. Data operators constitute the basic building blocks to build Big Data/IoT applications. Data operators are implemented in a way that enables their execution on both the IoT side and the Big Data side.

The provided NebulaStream APIs are the base to produce the intermediate software layers for transforming the code to be compatible for execution on IoT devices and the Big Data stack.

## Plugin Functionalities:

### ELEGANT Core Functions:

- **DockerSandbox:** Setting up a docker environment with all necessary services to execute queries and access the UI of the platform. Also, the UI configurations mechanism allows for more complex setups of the platform.
- **Submit Query:** The user can submit a query to the platform. The execution of it takes place to either Cloud or Edge Nodes according to the placement policies.
- **Configure Host:** To submit queries via the Elegant API, the host and port of the NebulaStream Coordinator need to be specified. Users can provide this information for queries and monitoring activities to commence.
- **Get Status of Query:** After submission, the ID of the query is provided. Users can utilize this information and monitor the state of the submitted queries for debugging and time-estimation purposes.
- **Get Query Plan:** The execution of the query processed by the NebulaStream Coordinator follows several placement policies and optimization procedures. Such information is displayed by simply providing the ID of the query.
- **Clean Docker Artifacts:** By setting a docker sandbox environment, docker containers run in the background of the system. Also, temporary directories containing configuration files and input data are produced. The user can easily remove this content and reclaim space by using this functionality.

### ELEGANT Verification functionalities:

- **Configure Verification Service:** Configure the IP and port of the verification service, enabling the usage of a local or remote server of the ELEGANT verification service.
- **Start Docker / Stop Docker:** Setting through Docker all necessary containers for running and stopping the verification service.
- **Verify Code:** Can be used by the developers to verify the Java class files.
- **Verify Marked Code:** Allows the verification of the code that has been selected.
- **Get verification status:** Provides the status of the verification process based on the provided id.
- **Get verification results:** Provides verification results, based on the provided id.


## How To Install

1. In order to install locally on Intellij platform download the zip file which contains the packaged application `ELEGANTPlugin-1.0-SNAPSHOT.jar`
2. Select Files and then Settings attribute from the main IntelliJ toolbar
3. In the displayed panel select Plugins and Install Plugin from Disk.
   ![Alt text](images/elegant.png?raw=true "Title")
4. Select the zip artifact of the downloaded ElegantPlugin and click OK.
   If all the above steps have been followed successfully, the toolbar should look like this:
   ![Alt text](images/elegant2.png?raw=true "Title")
   By clicking on ELEGANT button on the toolbar the list of functionalities is displayed. 

### NOTES
1. Setting up docker-containers via ELEGANT-Plugin can be time-consuming. Thus is
recommended to have images pre-built (e.g https://github.com/elegant-h2020/Elegant-Code-Verification-Service).
```bash
docker build -t code-verification-service-container .
```