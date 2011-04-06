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
        antifyPath = os.path.dirname(os.path.abspath(__file__))
        app.check()
        playPath = env["basedir"]
        basedir = app.path
        application_name = app.readConf('application.name')

        buildFile = os.path.join(app.path, 'build.xml')
        shutil.copyfile(os.path.join(antifyPath, 'src/buildTemplate.xml'), buildFile)
        replaceAll(buildFile, r'%APPLICATION_NAME%', application_name)
        replaceAll(buildFile, r'%ANTIFYHOME%', normalizePath(antifyPath, playPath, basedir))
        replaceAll(buildFile, r'%PLAYPATH%', normalizePath(playPath, '', basedir))
        print "~ File %s has been generated!" % buildFile


def normalizePath(path, playPath, basedir):
    basedirParent = os.path.normpath(os.path.join(basedir, ".."))
    if len(playPath) > 0:
        path = path.replace(playPath, '${play.path}')
    path = path.replace(basedir, '${basedir}')
    path = path.replace(basedirParent, '${basedir}/..')
    path = path.replace('\\', '/')
    return path
