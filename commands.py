import os, os.path
import shutil

from play.utils import *

# Here you can create play commands that are specific to the module, and extend existing commands

MODULE = 'antify'

# Commands that are specific to your module

COMMANDS = ['antify']

def execute(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == "antify":
        print "~ Generating ant build.xml"

        app.check()
        modules = app.modules()
        for module in modules:
            if module.endswith("antify"):
                modAntify = module

        classpath = app.getClasspath()
        application_name = app.readConf('application.name')

        buildFile = os.path.join(app.path, 'build.xml')
        shutil.copyfile(os.path.join(modAntify, 'src/buildTemplate.xml'), buildFile)
        cpXML = ""

        replaceAll(buildFile, r'%PLAYHOME%', env["basedir"].replace('\\', '/'))
        replaceAll(buildFile, r'%APPLICATION_NAME%', application_name)

        if len(modules):
            mClasses = ""
            for module in modules:
                mClasses += '        <fileset dir="%s/lib" erroronmissingdir="false">\n            <include name="*.jar"/>\n        </fileset>\n' % module
            replaceAll(buildFile, r'%MODULES_CLASSES%', mClasses)
            mSrc = ""
            for module in modules:
                mSrc += '        <pathelement path="%s/app"/>\n' % module
            replaceAll(buildFile, r'%MODULES_SOURCES%', mSrc)
        else:
            replaceAll(buildFile, r'%MODULES_CLASSES%', '')
            replaceAll(buildFile, r'%MODULES_SOURCES%', '')                                

# This will be executed before any command (new, run...)
def before(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")


# This will be executed after any command (new, run...)
def after(**kargs):
    command = kargs.get("command")
    app = kargs.get("app")
    args = kargs.get("args")
    env = kargs.get("env")

    if command == "new":
        pass
