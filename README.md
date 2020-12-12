# Springbot Drools stateful sample2

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