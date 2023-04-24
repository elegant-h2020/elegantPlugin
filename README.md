# ElegantPlugin

In short, the IDE plugin shall allow developers to create operators in a simple and effective way. Data
operators constitute the basic building blocks to build Big Data/IoT applications. Data operators are
implemented in a way that enables their execution on both the IoT side and the Big Data side.
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