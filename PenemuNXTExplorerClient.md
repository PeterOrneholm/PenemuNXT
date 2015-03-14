# Introduction #

This is the robot that uses [PenemuNXTExplorerClient](http://code.google.com/p/penemunxt/source/browse/#svn/trunk/PenemuNXTExplorerClient) to collect data and send it to the server.

## TOC ##



# Media #

## Images ##

| http://lh3.ggpht.com/_QXahOUsz5iM/StdzQFsvucI/AAAAAAAAAWM/1RTp3DQ8BcE/s128/DSC04503.JPG | http://lh6.ggpht.com/_QXahOUsz5iM/StdzPhiQDzI/AAAAAAAAAWI/gPSo1k0jfzc/s128/DSC04502.JPG | http://lh6.ggpht.com/_QXahOUsz5iM/StdzQexF4OI/AAAAAAAAAWQ/iRS8gBqRLOg/s128/DSC04504.JPG | http://lh5.ggpht.com/_QXahOUsz5iM/StnN-BjD0aI/AAAAAAAAAYA/MyCncH5rTdI/s128/DSC04513.JPG |
|:----------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|:----------------------------------------------------------------------------------------|
| [Original size](http://lh3.ggpht.com/_QXahOUsz5iM/StdzQFsvucI/AAAAAAAAAWM/1RTp3DQ8BcE/s640/DSC04503.JPG) | [Original size](http://lh6.ggpht.com/_QXahOUsz5iM/StdzPhiQDzI/AAAAAAAAAWI/gPSo1k0jfzc/s640/DSC04502.JPG) | [Original size](http://lh6.ggpht.com/_QXahOUsz5iM/StdzQexF4OI/AAAAAAAAAWQ/iRS8gBqRLOg/DSC04504.JPG) | [Original size](http://lh5.ggpht.com/_QXahOUsz5iM/StnN-BjD0aI/AAAAAAAAAYA/MyCncH5rTdI/DSC04513.JPG) |

## Videos ##

### First working map robot ###
<a href='http://www.youtube.com/watch?feature=player_embedded&v=yf8brdukyRM' target='_blank'><img src='http://img.youtube.com/vi/yf8brdukyRM/0.jpg' width='425' height=344 /></a>

# Software #

Runs the app [PenemuNXTExplorerClient](http://code.google.com/p/penemunxt/source/browse/#svn/trunk/PenemuNXTExplorerClient).

## Navigation ##

The robot uses leJOS and the classes in [lejos.robotics.navigation](http://lejos.sourceforge.net/nxt/nxj/api/lejos/robotics/navigation/package-summary.html) to navigate and to determine the current (relative) position.
As for now we are using [TachoPilot](http://lejos.sourceforge.net/nxt/nxj/api/lejos/robotics/navigation/TachoPilot.html), but we plan to use the [CompassPilot](http://lejos.sourceforge.net/nxt/nxj/api/lejos/robotics/navigation/CompassPilot.html).

## Communication ##

Uses the [PenemuNXTFramework](http://code.google.com/p/penemunxt/source/browse/#svn/trunk/PenemuNXTFramework) to communicate with the PC.

# Hardware #

## Design ##

The robot is based on the design of the [Explore](http://www.nxtprograms.com/explorer/index.html) from [nxtpograms.com](http://www.nxtprograms.com/). We've modified it to be more stable and to support more sensors etc.

## Sensors ##

We use some sensors to collect data with the robot.
They are listed below.

### Distance ###

| **Name:** | **High Precision Long Range Infrared distance sensor for NXT (DIST-Nx-Long-v2)** |
|:----------|:---------------------------------------------------------------------------------|
| **Image:** | ![http://www.mindsensors.com/images/pagemaster/DISTNxlong.png](http://www.mindsensors.com/images/pagemaster/DISTNxlong.png) |
| **Manufacturer:** | Mindsensors |
| **Link:** | http://www.mindsensors.com/index.php?module=pagemaster&PAGE_user_op=view_page&PAGE_id=73 |
| **Usage:** | Measures the distance to the walls around it. The map is based on this data. |

| **Name:** | **Ultrasonic Sensor (#9846)** |
|:----------|:------------------------------|
| **Image:** | ![http://cache.lego.com/images/shop/prod/9846-0000-XX-12-1.jpg](http://cache.lego.com/images/shop/prod/9846-0000-XX-12-1.jpg) |
| **Manufacturer:** | Lego |
| **Link:** | http://shop.lego.com/ByCategory/Product.aspx?p=9846&cn=389&d=292 |
| **Usage:** | Measures the distance to the wall ahead, so that we don't drive itnto it. |

### Touch ###

| **Name:** | **Touch Sensor (#9843)** |
|:----------|:-------------------------|
| **Image:** | ![http://cache.lego.com/images/shop/prod/9843-0000-XX-12-1.jpg](http://cache.lego.com/images/shop/prod/9843-0000-XX-12-1.jpg) |
| **Manufacturer:** | Lego |
| **Link:** | http://shop.lego.com/ByCategory/Product.aspx?p=9843&cn=389&d=292 |
| **Usage:** | Used to see if the robot drives into a wall, connected to a bumper. |

### Compass ###

| **Name:** | **NXT Compass Sensor (NMC1034)** |
|:----------|:---------------------------------|
| **Image:** | ![http://www.hitechnic.com/PDGImages/Mindstorms%20Compass.jpg](http://www.hitechnic.com/PDGImages/Mindstorms%20Compass.jpg) |
| **Manufacturer:** | HiTechnic |
| **Link:** | http://www.hitechnic.com/cgi-bin/commerce.cgi?preadd=action&key=NMC1034 |
| **Usage:** | Used to make correct turns. Currently not in use though. |