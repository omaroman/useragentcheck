# UserAgentCheck module for the play framework

## What is UserAgentCheck ?

UserAgentCheck provides an easy way to notify users when their browser is outdated. It displays a non disruptive banner at the top of the page and drives the user to a page where he/she may download an upgrade.
Optionally, can block access to a browser if the version is older than the required one, e.g., html5 features needed.

![screenshot](https://github.com/orefalo/useragentcheck/raw/master/screenshot.png)


## How does it work ?

It's no rocket science: the module provides a tag than may be included in your templates (typically `main.html`). In a nutshell, the module reads the user-agent header, figures the browser name/version and decides, based on your settings, if the banner should be displayed. 

To avoid redoing the same process on every request, it then stores the result in the session (which in Play translates to a cookie).

## Usage:

### Configuration

By using properties in `conf/application.conf` the developer has full control over when whether the banner should display or the blocking page.

    useragentcheck.block = false
    useragentcheck.minInternetExplorerVersion = 7
    useragentcheck.minOperaVersion = 10
    useragentcheck.minFirefoxVersion = 5
    useragentcheck.minChromeVersion = 13
    useragentcheck.minSafariVersion = 5

With the sample lines above, the banner will show if you use IE6 or prior, Opera 9 or prior ...etc
If you wish to block your web app, set useragentcheck.block = true

### Tag

You should include the `useragentcheck` tag somewhere in your template. Preferable right at the top of the html body.

    …    
    <body>
    #{useragentcheck /}
    #{doLayout /}
    </body>
    ...

### Banner settings

The banner text and url may be configured using the standard `conf/messages` file. The default entries are

    useragentcheck.label=Your browser is no longer supported. Please click here to update...
    useragentcheck.url=http://www.google.com/chrome

### Routes

If you're planning to use the Blocking feature, add the following route in `conf/routes`

    # Import the default UserAgentCheck routes
    *	${ctx}/	module:useragentcheck

### Override Block Template

If you want to customize Block Template, invoke the following command:

    play useragentcheck:ov-blockview

## Sample application

two sample demos are part of the distribution

## Credits

User Agent Utils - Harald Walker - [http://user-agent-utils.java.net/](http://user-agent-utils.java.net/)

play-useragentcheck module - Olivier Refalo - [https://github.com/orefalo](https://github.com/orefalo)

play-useragentcheck module (Blocking Page) - Omar O. Román - [https://github.com/omaroman](https://github.com/omaroman)