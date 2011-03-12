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
        print "~ Generating ant project file ..."

        app.check()
        modules = app.modules()
        for module in modules:
            if module.endswith("antify"):
                modAntify = module
        antifyPath = '%s' % modAntify
        playPath = env["basedir"]
        basedir = app.path
        application_name = app.readConf('application.name')

        buildFile = os.path.join(app.path, 'build.xml')
        shutil.copyfile(os.path.join(modAntify, 'src/buildTemplate.xml'), buildFile)
        replaceAll(buildFile, r'%APPLICATION_NAME%', application_name)
        replaceAll(buildFile, r'%ANTIFYHOME%', normalizePath(antifyPath, playPath, basedir))
        print "~ File %s has been generated!" % buildFile


def normalizePath(path, playPath, basedir):
    basedirParent = os.path.normpath(os.path.join(basedir, ".."))
    path = path.replace(playPath, '${play.path}')
    path = path.replace(basedir, '${basedir}')
    path = path.replace(basedirParent, '${basedir}/..')
    path = path.replace('\\', '/')
    return path
