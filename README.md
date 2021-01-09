# Springboot Drools stateful sample2

## Description
This is a Springboot project with integrated Drools rule engine.
A singleton service holds the knowledge session which is maintained through the life of the service.
A RESTful resource is used to insert events within the knowledge session.
Events are position events from two groups of things or people. The rule defined within the rule engine detects when a position from group A is too close to a position from group B.
The positioning uses standard GPS positioning, ie,  Latitude, Longitude pairs.

Note that latidudes are positive when within the northen hemisphere and negative in the southern hemisphere.
Longitudes are taken as positive when going West from Greenwich and negative when going East.

## Technical details

- Springboot version 2.3.0-RELEASE
- Drools version 7.46.0.Final

The knowledge session is set up to use streaming option is order to be able to related the timestamp of the events to the timestamp within the knowledge session.

The knowledge is using a pseudo-clock is order to advance in time using the event's timestamp.
This provides better control of the session and allows to reproduce results for a given set of events.

### Testing

```
curl -ik -X POST "http://localhost:8080/ms-cde/v1/events/insert" -H "Content-Type: application/json" -d '{"numberItems":10}'
```

```
curl -ik -X POST "http://localhost:8080/ms-cde/v1/events/insert" -H "Content-Type: application/json" \
-d '{
"numberItems":1,
"positionEvents":[
    {
        "idEvent": "123",
        "idOwner": 78136481234,
        "type": "A",
        "timestamp": "2020-11-29 21:18:01.000+0100",
        "latitude": 42.2199298,
        "longitude": 8.7026243
    }
]
}'
```

## Generate docker image

```
docker build -t ilecreurer/drools-sample2:1.0.0 .
```

## Run docker container


### First run
```
docker run -p 8080:8080 ilecreurer/drools-sample2:1.0.0
```

### To stop and re-run

Get the container ID:

```
docker ps -a | grep drools
283516fb18fe        ilecreurer/drools-sample2:1.0.0                                     "java -jar /drools-s…"   5 minutes ago       Up 3 minutes                0.0.0.0:8080->8080/tcp              naughty_golick
```

Start:

```
docker start 283516fb18fe
```

Check logs written to the console:

```
docker logs -f 283516fb18fe

...
2021-01-09 19:29:48,393 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Check number of facts...
2021-01-09 19:29:48,393 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: fc: 0
2021-01-09 19:29:48,393 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Inserting transaction...
2021-01-09 19:29:48,402 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Advancing time by 1606681082000
2021-01-09 19:29:48,403 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Clock time: 29/11/2020T20:18:02.000+0000
2021-01-09 19:29:48,403 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Firing rules!
2021-01-09 19:29:48,451 DEBUG com.ilecreurer.drools.samples.sample2.event.Rule_insertion918283534 defaultConsequence: New PE: 125, 1, A
2021-01-09 19:29:48,452 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Inserting transaction...
2021-01-09 19:29:48,452 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Advancing time by 1000
2021-01-09 19:29:48,452 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Clock time: 29/11/2020T20:18:03.000+0000
2021-01-09 19:29:48,452 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Firing rules!
2021-01-09 19:29:48,457 DEBUG com.ilecreurer.drools.samples.sample2.event.Rule_insertion918283534 defaultConsequence: New PE: 126, 2, B
2021-01-09 19:29:48,459 WARN  com.ilecreurer.drools.samples.sample2.event.Rule_collision782321111 defaultConsequence: 1 is too close to 2
2021-01-09 19:29:48,459 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting dbPositionEventRuleRuntimeListener.active to true  ...
2021-01-09 19:29:48,459 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Adding dbPositionEventRuleRuntimeListener to kieSession...
2021-01-09 19:29:48,459 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting state to ready ...

```

Note that facts are restored from the database, ie, the ksession is redydrated.


Stop:

```
docker stop 283516fb18fe
```
