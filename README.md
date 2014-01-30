# feedzai Open Scoring Server (FOS)

FOS is machine learning model training and scoring server in Java.

Latest version [![Build Status](https://feedzaios.ci.cloudbees.com/buildStatus/icon?job=fos-core)](https://feedzaios.ci.cloudbees.com/job/fos-core/)


## Why FOS

There are pretty good machine learning training and scoring frameworks/libraries out there, but they don't provide the
following benefits:

1. Common API: fos provides a common abstraction for model attributes, model training and model scoring. Using a [Weka]
based classifier will use have exactly the same API as using a R based classifier.
1. Scoring & Training as a remote service: Training and scoring can be farmed to dedicated servers in the network
enabling both vertical and horizontal scaling.
1. Import and Export models: A model could be trained in a development box and imported seamlessly into a remote server
1. Scalable and low latency scoring: Marshalling and Unmarshalling scoring requests/responses can be responsible
for a significant amount of overhead. Along with the slow RMI based interface, fos also supports scoring using [Kryo].

## Compiling FOS

You need:

1. [Java SDK]: Java 7
1. [Maven]: Tested with Maven 3
1. Access to maven central repo (or a local proxy)

After both the [Java SDK] and [Maven] are installed run the following command

`mvn clean install`

This should compile fos-core, ran all the tests and install all modules into your local maven repo.


## Running FOS

In order to start a FOS sever you need to create a bundle that contains both the core componentes and one or more fos backend implementations. 

To create a bundle type 
`make package` on the project root. This will:

1. Build fos core
2. Copy all dependencies listed in `non-core-dependencies.xml` into fos-server lib directory (this includes the weka and R API implementations)
3. Create a tar.gz bundle with all the necessary code plus shell scripts to bootstrap the process. The file will be available as `fos-server/target/fos-server-bin.tar.gz`. 
4. You can now deploy this self contained server.


## FAQ

### I need to support library X. How can I do it?
fos-core does not provide any concrete implementation. You may want to peek at fos-impl-dummy module in fos-core.
After you've familiarized with the API (Manager, ManagerFactory and Scorer) you can take a peek at a real implementation,
say [fos-weka].

[Kryo]: https://github.com/EsotericSoftware/kryo
[fos-r]: https://github.com/feedzai/fos-r
[fos-weka]: https://github.com/feedzai/fos-weka
[Weka]: http://www.cs.waikato.ac.nz/ml/weka/
[R]: http://www.r-project.org/
[Maven]: http://maven.apache.org/
[Java SDK]: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html



