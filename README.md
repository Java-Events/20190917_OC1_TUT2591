# 2019_OC1_TUT2591
JUnit5: From @Test Up to Your Own TestEngine

## From Test to Extensions

## Useless Engine

## Nano Engine

### IDE and @Testable
Show what happened with an Annotation including @Testable
IDE Support without EngineSupport
Split Annotations on Class- Method-Level

### NanoEngine-discover
* MethodSelector
* ClassSelector
* PackageSelector
* ClasspathRootSelector
 Show how this is done step by step in the IDE and compared to maven::test
 

## Micro Engine
Holding Resources


## ClassLoaders

-Djava.system.class.loader=org.rapidpm.junit.engine.distributed.shared.HZClassLoader







## Name is missing


## Was kann ich mit einer Engine machen?
Die Art wie ein Test beschrieben wird verändern
Resourcen verwalten, DB, ServletContainer, HZ Cluster...
LizenzManagement
Testausführung parallelisieren, async gestallten, ..
Dashboard über Metriken

Distributed TestEngine
Test definition ist KlaSSENNAME UND METHODEN NAME
HZClassLoader läd die Klasse und invoke method
wenn Klasse geladen werden muss



## Ideas
manage resource like ServletContainer
-> TestBench

performance report

RuntimeReport an zentrale Stelle mit infos
JVM/OS , Datum, Zeit, Execution Time
-> Darstellung in VaadinApp

Die Art Tests zu beschreiben.

Test gegen JavaFX und Vaadin

FlakyTests



