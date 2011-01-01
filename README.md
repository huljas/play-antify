Play Antify Module
==================

Generates Ant project file for your Play application that can be used for example with continuous integration server
like Hudson.

Tested with play 1.1 on windows and linux.

## Features

- Generates classpath that is required to run your application
- Tries to make the paths relative to either play.home or basedir
- Contains normal ant tasks for play run, play test, play auto-test etc to be used as such or as examples for your own tasks.
- Contains macrodef for running the play python script from ant

## Usage

Add the module to your application `application.conf`

    module.antify=[path to module]

Run the command in your application directory

    play antify

Now you should have a `build.xml` that can be used to run the play with ant.

    ant play-run

You can list the targets

    ant -p

and you can of course modify the `build.xml` to suite your needs.



