# BrightFlux-Persistence

This project provides the basic access to persist and retrieve configuration data on disk.

## Main Idea

We often see a lot of things to persist. E.g. the current state of a UI, the current state and configuration for
a single component. We want this component to be able to have its own storage independent from other components
we might not even know. So basically a plugin might want its own persistence. We maybe want also to provide a
preference dialog, where the main interface is a Persistence-Module and a Mapping. And every time a change is 
performed in such a PreferencesPage we want to store this configuration on disk for next startup and also want
to listen to changes and apply the new configuration changes which are changed in a preferences dialog immediately.

So the main thing the BrightFlux-Persistence provides is
* writing persisted data to disk
* reading persisted data from disk
* update persisted data whenever a key-value-pair update is performed
* provide registry of change listeners, for each PersistenceModule  
** who want to be informed on changes in the persistence, so they can change behavior according to the new persisted data
* we want to provide current setup values
** Either from a file
** Or a method using an annotation
* we want to provide default values
** Either from a file
** Or a method using an annotation
* provide a flush operation

* Provide a Registry of all PersistenceModules, such that all Persistence Modules can be saved
* Provide handling for some kind of write protection, such that it can be configured, whether a particular name-space is read-only on flush, but behaves normally application is on.
* Provide a PolicyModule and Policy configuration which values violate data-protection laws if stored, or the user decide not to store.

* This module will be available while startup and should be brought up a a component which can initialize the PersistenceModule
* Such a component would be something which accesses an early persistence, to configure on where to locate and load the Persistence-Module-Data according to a given name-space.
* This component will be able to create or provide already created instances of PersistenceModules

* This component should be a first class citizen in a preference page concept in the future

* such a Component would allow to skip the use of too many hard coded values in the source code, like path information and default texts

## MVP

The minimal viable product should just provide the most simple functionality. It doesn't need to be fancy at the 
first time and we can adapt and update the current implementation of the persistence. Only  this component knows
about the way how it is actually stored, loaded and processed. 

## Current state for this Component

* load a "persisted" file for a given name space
** process string array and provide string array data  

