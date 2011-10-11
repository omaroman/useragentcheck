# UserAgentCheck

import getopt
from play.utils import *

COMMANDS = ['useragentcheck:ov-blockview']

HELP = {
    'useragentcheck:ov-blockview': ' Override the Block Template'
}

# ~~~~~~~~~~~~~~~~~~~~~~ [useragentcheck:ov] Override a view
def execute(**kargs):
    app = kargs.get("app")

    try:
        app.override('app/views/useragentcheck/WebUserAgent/block.html', 'app/views/useragentcheck/WebUserAgent/block.html')
        return

    except getopt.GetoptError, err:
        print "~ %s" % str(err)
        print "~ "
        sys.exit(-1)

