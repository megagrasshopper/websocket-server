# Websocket server

It is created the application that receives information about the user's current url.

A search for users who have linked to some url and are idle from 1.5 to 2 minutes is organized.

## Subprojects description

#### common
Common classes and configurations

#### client
Websocket client application example.

When changing url, the client sends to the server user data in the format
``
{"ip": "192.168.1.1", "userAgent": "Google-chrome", "url": "http://ya.ru"}
``

#### api
Rest-api provides access to find idle users

* Get `/ api / v1 / sleepinguser`

#### socket
Server application that accepts user data over a websocket. Hazelcast is used to store data.
To scale out `socket` sticky sessions should be used on a load balancer