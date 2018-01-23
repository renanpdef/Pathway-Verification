# ProtocolVerification

Perform a verification of protocols on XMI file. To right protocols, it is analysed if there are not deadlock or nondeterminism in the file.

## Getting Started

These instructions will help you to install and configure the project and run it.

### Prerequisites

It is used a development software to Java (Eclipse - http://www.eclipse.org/).

### Installing

First, you have to add a JDK:

1. Download the latest JDK version (http://www.oracle.com/technetwork/pt/java/javase/downloads/index.html - Java)
2. On Eclipse, click on Window tab > Preferences > Installed JREs > Add...
3. Select Standard VM > Directory... > add the JDK folder > Finish.
4. Mark the JDK and click Apply > Apply and close.

After that, you need to configure two external JARS, protocolv3.jar and org.eclipse.emf.ecore-2.12.0.jar, that contains in the project.

1. With the project downloaded, click right bottom in the project and select Build Path > Configute Build Path...
2. If these JARS are in errors, remove them. Select the JAR and click Remove.
3. If these JARS are not with errors or were removed, add them one by one. Click Add External JARs... > select a JAR > Open > Apply > Apply and Close.

## Deployment

The project use an Open-Source java library for constraint programming (Choco - http://www.choco-solver.org/) and dependecies of EMF to get informations of the protocols on XMI file (EMF - https://www.eclipse.org/modeling/emf/)

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
