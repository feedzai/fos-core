========================= FOS - FeedZai Open Scoring Server =========================

1. Introduction

FOS is a open source scoring server that provides general machine learning processes 
independently of the implementations. It allows to train machine learning models and 
then use them to score data based on those models.


2. Licensing

FOS is free software: you can redistribute it and/or modify it under the terms of the 
GNU General Public License as published by the Free Software Foundation, either 
version 3 of the License, or (at your option) any later version.

FOS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
PURPOSE. See the GNU General Public License for more details (at LICENSE.txt).


3. Requirements

FOS is a Java based application, though is platform independent. Currently was tested
under the following Operating Systems:

  - Centos 6.4
  - Ubuntu 13.04
  - Windows Server 2008
  - Windows 7
  - Windows 8
 
It requires JDK 1.7 being only fully tested under JDK 1.7u40.


4. Installing

To install FOS, just extract the tar.gz file and place it in the directory that you
want (eg.: /opt/fos in linux, or C:\Program Files\FeedZai\FOS in windows).

The package contains an init script for Centos / RedHat systems that allows you to
deploy FOS as a service in Centos operating systems:

  - Copy the init script to init.d folder:
    - cp bin/init.d /etc/init.d/fos
  - Set the file as executable
    - chmod +x /etc/init.d/fos
  - If you didn't deploy FOS in /opt/fos change the location in the init script
  - Add the service to the startup
    - chkconfig --add fos
  - Set it to autostart (optional)
    - chkconfig fos on


5. Running FOS

To start FOS server just use the bin/startup scripts:

  - In Windows: 
    - bin/startup.bat 
  - In Linux:
    - bin\startup.sh
	
If you installed FOS as a service, just start the service with:

  - /etc/init.d/fos start
  

6. Configuration

FOS configuration is very small and concise. Please see the conf/fos.properties for
advanced configuration.

The most common properties to change are the ports where it will bind the system.