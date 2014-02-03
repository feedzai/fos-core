# feedzai Open Scoring Server (FOS)

FOS is machine learning model training and scoring server in Java.

[![Build Status](https://feedzaios.ci.cloudbees.com/buildStatus/icon?job=fos-core)](https://feedzaios.ci.cloudbees.com/job/fos-core/)

[![CloudbeesDevCloud](http://www.cloudbees.com/sites/default/files/Button-Built-on-CB-1.png)](http://www.cloudbees.com/dev)


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

# FOS Quickstart

## Running FOS

In order to start a FOS sever you need to create a bundle that contains both the core components and one or more fos backend implementations.

## Creating a FOS server bundle

To create a bundle type 
`make package` on the `fos-core` project root. This will:

1. Build fos core
2. Copy all dependencies listed in `non-core-dependencies.xml` into fos-server lib directory (this includes the weka and R API implementations)
3. Create a tar.gz bundle with all the necessary code plus shell scripts to bootstrap the process. The file will be available as `fos-server/target/fos-server-bin.tar.gz`. 

## Running FOS

1. Untar the server bundle on your install location. Let's assume FOS will be installed in the home dir

```
$ cd ~
$ tar xf <git clone dir>/fos-server/target/fos-server-bin.tar.gz
```

By now you should have a `fos-server` on your home.

```
$ cd fos-server
$ ~/fos-server $ ls
bin  conf  lib  LICENSE.txt  log  models  README.txt
```

Inside there are a couple directories:

1. `bin` with startup scripts
1. `conf` with FOS configuration scripts
1. `models` where trained models are going to be found

To start fos, run the bundled startup script `bin/startup.sh`

```
$ bin/startup.sh
03-Feb 04:28:31  INFO   com.feedzai.fos.server.Runner                  Starting fos server using configuration from conf/fos.properties
03-Feb 04:28:31  INFO   com.feedzai.fos.server.Runner                  FOS Server started in 272ms
```

We've just started a FOS server with [Weka] support built into the [fos-weka] fos module.
Currently FOS can only support an active module per runtime instance. The active server is
set in the `fos.factoryName` configuration option inside `conf/fos.properties` file:

```
# the fos implementation to launch
fos.factoryName=com.feedzai.fos.impl.weka.WekaManagerFactory
```
## Training and scoring my first model

We've prepared a couple [FOS samples]. Check them out

## Implementing a FOS Module


### Creating your own manager

fos-core does not provide any concrete implementation. However, a bundled fos server includes both a [fos-weka] (active by default) and a [fos-r] implementation. It is pretty easy to create a new implementation if you want to leverage existing code.


Your first step will be to understand [ManagerFactory] interface. A [ManagerFactory] should perform all the necessary boostrapping for a given [Manager] implementation, which must provide implementations for:

1. Model training
2. Model management (add, removal, import and export)
3. A [Scorer] implementation 

Lets dissect a real example:

```java
public class WekaManagerFactory implements ManagerFactory {

    @Override
    public Manager createManager(FosConfig configuration) {
        WekaManagerConfig wekaManagerConfig = new WekaManagerConfig(configuration);
        return new WekaManager(wekaManagerConfig);
    }
}
```

The first step is parse [Manager] specific configuration parameters

```java
    WekaManagerConfig wekaManagerConfig = new WekaManagerConfig(configuration);
```

Now that we have specific config, we can create a new [WekaManager]:

```java
    return new WekaManager(wekaManagerConfig);
```

Most of the heavy lifting is done by [WekaManager] implementation. Since most of them are Weka specifc, it is not worthwhile to go into implementation details here. The following operations are performed:

1. Search for previously saved models and load their configuration. 
1. Create a `fos-weka` [Scorer] implementation.
1. Start listening to requests via RMI and [Kryo].
1. Start a thread pool to allow parallel scoring.


### Implementing model training

In order to implement [model training], you need to supply the [model configuration] and training instances. You can see a practical example in [fos training sample]. 
A model configuration is composed by:

1. A set of key-value properties relevant from each implementation. We recomend to define all configuration options in a dedicated class (see [weka model configuration] for example).

2. An [attribute] list. The number of atributes in the training data must match the configuration attribute list. An attribute can be:
    1. [Numerical attribute]: Numeric attributes can be real or integer types.
    2. [Categorical attribute]: For attributes with a limited set of valid values.



You'll have to convert these FOS abstractions to a format your implementation understands. 


There are multiple training entry points:

```java
 UUID trainAndAdd(ModelConfig config,List<Object[]> instances) throws FOSException;
```

`traindAndAdd`  must train a new classifier with the given configuration and using the given `instances`. It should return the serialized classifier and automatically make it avaiable for scoring

```java
 UUID trainAndAddFile(ModelConfig config,String path) throws FOSException;
```
`trainAndAddFile`  Same as above, but instances are read from a CSV file. 


```java
byte[] train(ModelConfig config,List<Object[]> instances) throws FOSException;
```
`train` Trains a model and returns its serialized representation. The model is not made available for scoring.


```java
byte[] trainFile(ModelConfig config, String path) throws FOSException;
```
`trainFile` same as above, but instances are read from a CSV file.


### Implementing model Scoring

Along with training models, the manager is also responsible for providing a [Scorer] implementation. There is a weka [weka scoring] example for reference.





### Managing models






[Kryo]: https://github.com/EsotericSoftware/kryo
[fos-r]: https://github.com/feedzai/fos-r
[fos-weka]: https://github.com/feedzai/fos-weka
[Weka]: http://www.cs.waikato.ac.nz/ml/weka/
[R]: http://www.r-project.org/
[Maven]: http://maven.apache.org/
[Java SDK]: http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html
[FOS samples]: https://github.com/feedzai/FosSample
[ManagerFactory]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/ManagerFactory.java?source=cc
[Manager]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/Manager.java?source=cc
[Scorer]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/Scorer.java?source=cc
[WekaManager]: https://github.com/feedzai/fos-weka/blob/master/src/main/java/com/feedzai/fos/impl/weka/WekaManager.java?source=cc
[model training]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/Manager.java?source=c#L120
[fos training sample]: https://github.com/feedzai/fos-sample/blob/master/src/main/java/com/feedzai/fos/samples/weka/WekaTraining.java#L65
[attribute]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/Attribute.java?source=cc#L37
[Numerical attribute]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/NumericAttribute.java?source=c#L32
[Categorical attribute]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/CategoricalAttribute.java#L46
[model configuration]: https://github.com/feedzai/fos-core/blob/master/fos-api/src/main/java/com/feedzai/fos/api/ModelConfig.java?source=cc#L45
[weka model configuration]: https://github.com/feedzai/fos-weka/blob/master/src/main/java/com/feedzai/fos/impl/weka/config/WekaModelConfig.java?source=c#L45
[weka scoring]: https://github.com/feedzai/fos-sample/blob/master/src/main/java/com/feedzai/fos/samples/weka/WekaScoring.java#L45

