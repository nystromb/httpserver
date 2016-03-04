[![Build Status](https://travis-ci.org/nystromb/httpserver.svg?branch=master)](https://travis-ci.org/nystromb/httpserver)
# Java HTTP Server

A configurable server that you can build web applications with. 
## Dependencies 
  * Gradle 2.2 
  
## Building and Running a Basic Web Server
  1. `git clone https://github.com/nystromb/httpserver.git`
  2. `cd httpserver && gradle build`
  3. `java -jar build/libs/httpserver.jar`

This will start up the server to listen on the default port 5000 and serve directories and files from your current working directory. I have provided the absolute bare minimum you need in order to create a running server. It looks like this:

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("server.logs");
        try {
            new HttpServer(new HTTPConfiguration(), new HashMap<>(), logger).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

To create a new server, you need some implementation of a `ServerConfiguration`, a `HashMap<String, Route>` of routes, and a `Logger`.

## Configuring the Server


There is a `HTTPConfiguration` class for you to use that includes a default port and public directory to serve files from, but you also have some options for configuring the server yourself, which I will go over. You can implement the `ServerConfiguration` interface in a class of your own or you can pass arguments through the command line.

#### Implementing the `ServerConfiguration` interface
With this method, all you do is create your own class then write ` implements ServerConfiguration` next to your class name. If you're using IntelliJ or a similar IDE, it will yell at you to then implement the required `getPort()` and `getPublicDirectory()` methods. Here's some boilerplate for you to copy and paste and fill in the blanks:

    public class MyConfiguration implements ServerConfiguration {
    
        public MyConfiguration() {
        
        }
        
        public int getPort() {
            // return some port number
        }
        
        public Path getPublicDirectory() {
            // return path/to/directory
        }
    }
    
#### Passing in Command Line arguments
Since all you need to run the server is a port and a directory, you can specify a port number, an *absolute* path to a directory in your computer, or both when executing your `.jar` file. If you specify an option without following up with a valid configurable option, you will quickly realize that your server won't work as you would expect. Again, here are some examples for you to look at:

**Overriding just the default port**

  * `java -jar httpserver.jar -p 8000`

**Overriding the default public directory**

  * `java -jar httpserver.jar -d /`

**Overriding all default configurations**

  * `java -jar httpserver.jar -p 7777 -d /`

If you decide to do this, you need to pass the arguments into the `HTTPConfiguration` constructor. 

## Defining Routes
Sometimes serving up file and directories isn't enough, so I have created a way that you could also set up your own route handlers. To create your own custom route, all you need to do is create a class that `extends ApplicationHandler` and override the appropriate request method like so:

    public class MyHandler extends ApplicationHandler { 
        
       public MyHandler() { } 

       protected Response get(Request request) {
           return Response.Builder(200, "This is the body of the response").build();
       } 
    }   

This just shows the `get` method, but you can also add other methods like `post`, `put`, `delete`, `patch`, `head`, and/or `options` to handle these request method types.

Once you create a route, you just need to add a `String` path mapped to a `Route` wrapping this handler before you create and start your server. 

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("server.logs");

        HashMap<String, Route> routes = new HashMap();
        routes.put("/myroute", new Route(new MyHandler()));
        try {
            new HttpServer(new HTTPConfiguration(), routes, logger).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

#### Authorized Routes
If you have a super secret route that you don't want anybody else to get into, you can wrap your `Handler` in a `Authorization` Handler for basic HTTP authentication. All you need is to do is specify a username, a password, a challenge string, and your `Handler`. When a user tries to go to this route, they will be prompted for a username and password. 

    HashMap<String, Route> routes = new HashMap();
    routes.put("/myroute", new Route(
                                new Authorization("myUsername", "myPassword", "myChallenge", 
                                     new MyHandler())));

## Logger 
The logger I use is simply the built in Java `Logger` class. If you would prefer to not have requests logged, just call `setUseParentHandlers(false)` on your logger object.

Please refer to the [Logger Documentation](https://docs.oracle.com/javase/7/docs/api/java/util/logging/package-summary.html) for more information on how to use it.

## Examples
[Cob Spec Server](https://github.com/nystromb/cob-spec-server) - This is a server implementation that passes the [Cob Spec Acceptance Suite](https://github.com/8thlight/cob_spec)

[Tic Tac Toe Server](https://github.com/nystromb/tic-tac-toe-server) - This is an application where I serve up my [Java Tic Tac Toe](https://github.com/nystromb/tic-tac-toe-server)
