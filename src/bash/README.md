                                         ██████╗ ███╗   ██╗██╗██████╗ ██████╗ ███████╗
                                        ██╔════╝ ████╗  ██║██║██╔══██╗██╔══██╗██╔════╝
                                        ██║  ███╗██╔██╗ ██║██║██████╔╝██████╔╝███████╗
                                        ██║   ██║██║╚██╗██║██║██╔══██╗██╔═══╝ ╚════██║
                                        ╚██████╔╝██║ ╚████║██║██║  ██║██║     ███████║
                                         ╚═════╝ ╚═╝  ╚═══╝╚═╝╚═╝  ╚═╝╚═╝     ╚══════╝
                                                        __               __  
                                                       / /_  ____ ______/ /_ 
                                                      / __ \/ __ `/ ___/ __ \
                                                     / /_/ / /_/ (__  ) / / /
                                                    /_.___/\__,_/____/_/ /_/ 
                                                                             
               
## Summary

This ***GNIRPS*** module offers an interface to start and interact with bash scripts - or through bash, any kind of 
script (or else, since anything is possible through bash).

## Contents

- [Parallel or sequential execution](
https://github.com/REDLab-Team/gnirps/tree/master/src/bash#parallel-or-sequential-execution)
- [Input, error and output redirecting](
https://github.com/REDLab-Team/gnirps/tree/master/src/bash#input,-error,-and-output-redirection)
- [Config and monitoring properties](
https://github.com/REDLab-Team/gnirps/tree/master/src/bash#config-and-monitoring-properties)
    
## Parallel or sequential execution

Through the use of a `com.gnirps.bash.service.BashExec` component, we access the possibility of launching scripts in 
parallel or sequentially.

In the case of sequential calls, the user may choose to interrupt (or keep going) if an error 
arises.

The case of parallel calls shall undergo improvements as soon as Kotlin coroutines reach a stable release state, however 
proper they might be on a Java perspective.

## Input, error and output redirecting

Scripts requiring interactive input can be fed through an input stream.

Scripts producing outputs and (hopefully not) errors can be set to redirect those either to the standard output or to a 
String object.

## Config and monitoring properties

A process can be prepared and run in two distinct steps thanks to the `com.gnirps.bash.service.Process` class.

Some of its properties handle its setup:
```
val cmd: String .................................. script (about-to-be or already) run by the process
val workDir: String = "/" ........................ run context of the script
val output: ProcessOutput = ProcessOutput.STDOUT . redirection of the script's outputs
val error: ProcessOutput = ProcessOutput.STDOUT .. redirection of the script's errors
val timeout: Long = 300 .......................... max time allowed for the script to run
val inputStream: String? = null .................. stream to pass as standard input during the script's execution
```

Others provide feedback:
```
var exitValue: Int = -1 ........ returned value of the script
var execTime: Long? = null ..... time taken to get things done
var pid: Long = -1.............. of the process who ran the script
var outputString: String = "" .. if the redirection was set to string, the script's outputs come here
var errorString: String = "" ... if the redirection was set to string, the script's errors come here
```