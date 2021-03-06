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


## Run docker container with h2

### Build image
```
mvn clean package
docker build -f src/main/docker/Dockerfile-h2 -t ilecreurer/drools-sample2/h2:1.0.0 .
```


### First run
```
docker run -p 8080:8080 ilecreurer/drools-sample2/h2:1.0.0
```

### To stop and re-run

Get the container ID:

```
docker ps -a | grep drools
283516fb18fe        ilecreurer/drools-sample2/h2:1.0.0                                     "java -jar /drools-s…"   5 minutes ago       Up 3 minutes                0.0.0.0:8080->8080/tcp              naughty_golick
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


## Run docker container with postgresql

### Build image

Build using the profile `postgresql`:

```
mvn clean package -Ppostgresql
```

Make sure the `.` is included in the command:

```
docker build -f src/main/docker/Dockerfile-postgresql -t ilecreurer/drools-sample2/postgresql:1.0.0 .
```

### Run with docker-compose

```
cd src/main/docker
docker-compose -f docker-compose.yml up
```

Note that the database configuration "set" within the config file is overriden by the environment variables passed to the container:

```
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://dbpostgresql:5432/droolssample2db
      - SPRING_DATASOURCE_USERNAME=postgres_u
      - SPRING_DATASOURCE_PASSWORD=postgres_p
```

See [https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) to see how this is done.


## Run on Kubernetes

### Install Minikube

```
wget https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
chmod +x minikube-linux-amd64
sudo mv minikube-linux-amd64 /usr/local/bin/minikube
```

### Install kubectl

```
curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin/kubectl
kubectl version -o json
```

### Start minikube

```
minikube start
😄  minikube v1.17.0 on Ubuntu 20.04
✨  Automatically selected the docker driver. Other choices: ssh, virtualbox
👍  Starting control plane node minikube in cluster minikube
🚜  Pulling base image ...
💾  Downloading Kubernetes v1.20.2 preload ...
    > preloaded-images-k8s-v8-v1....: 491.22 MiB / 491.22 MiB  100.00% 32.42 Mi
🔥  Creating docker container (CPUs=2, Memory=3900MB) ...
🐳  Preparing Kubernetes v1.20.2 on Docker 20.10.2 ...
    ▪ Generating certificates and keys ...
    ▪ Booting up control plane ...
    ▪ Configuring RBAC rules ...
🔎  Verifying Kubernetes components...
🌟  Enabled addons: storage-provisioner, default-storageclass
🏄  Done! kubectl is now configured to use "minikube" cluster and "default" namespace by default
```

Get cluster info:

```
kubectl cluster-info
Kubernetes control plane is running at https://192.168.49.2:8443
KubeDNS is running at https://192.168.49.2:8443/api/v1/namespaces/kube-system/services/kube-dns:dns/proxy

To further debug and diagnose cluster problems, use 'kubectl cluster-info dump'.
```


Get nodes info
```
kubectl get nodes
NAME       STATUS   ROLES                  AGE   VERSION
minikube   Ready    control-plane,master   27m   v1.20.2
```

### Deploy PostgreSQL database

```
kubectl create -f ./src/main/k8s/postgres-deployment.yaml
deployment.apps/postgres created
```

Check deployments:

```
kubectl get all
NAME                            READY   STATUS    RESTARTS   AGE
pod/postgres-6f4d74b957-m2t29   1/1     Running   0          96s

NAME                 TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
service/kubernetes   ClusterIP   10.96.0.1    <none>        443/TCP   103m

NAME                       READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/postgres   1/1     1            1           96s

NAME                                  DESIRED   CURRENT   READY   AGE
replicaset.apps/postgres-6f4d74b957   1         1         1       96s
```

Execute command within the pod.

```
kubectl exec -it pod/postgres-6f4d74b957-m2t29 bash
kubectl exec [POD] [COMMAND] is DEPRECATED and will be removed in a future version. Use kubectl exec [POD] -- [COMMAND] instead.
root@postgres-6f4d74b957-m2t29:/#
```

### Expose PostgreSQL database as a service

```
kubectl create -f ./src/main/k8s/postgres-service.yaml
service/dbpostgresql created
```

Check services:

```
kubectl get services
NAME           TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
dbpostgresql   NodePort    10.101.134.156   <none>        5432:32437/TCP   20s
kubernetes     ClusterIP   10.96.0.1        <none>        443/TCP          125m
```


### Create the droolssample2 deployment

First we must set our shell to point to minikube's docker daemon to find the local image:

```
minikube docker-env
export DOCKER_TLS_VERIFY="1"
export DOCKER_HOST="tcp://192.168.49.2:2376"
export DOCKER_CERT_PATH="/home/ilecreurer/.minikube/certs"
export MINIKUBE_ACTIVE_DOCKERD="minikube"

# To point your shell to minikube's docker-daemon, run:
# eval $(minikube -p minikube docker-env)
```

```
eval $(minikube -p minikube docker-env)
```

Rebuid image for this registery:

```
docker build -f src/main/docker/Dockerfile-postgresql -t ilecreurer/drools-sample2/postgresql:1.0.0 .
```

Now we can create the deployment:

```
kubectl create -f ./src/main/k8s/droolssample2-deployment.yaml
deployment.apps/droolssample2 created
```

Note that the database configuration "set" within the config file is overriden by the environment variables passed to the container in the yaml file:

```
        env:
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://dbpostgresql:5432/droolssample2db
        - name: SPRING_DATASOURCE_USERNAME
          value: postgres_u
        - name: SPRING_DATASOURCE_PASSWORD
          value: postgres_p
```

See [https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) to see how this is done.

```
kubectl logs pod/droolssample2-fdf9ddd5b-ffkmx

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.0.RELEASE)

2021-01-24 19:26:55,002 INFO  org.springframework.boot.StartupInfoLogger logStarting: Starting SpringbootDroolsApplication v1.0.0-SNAPSHOT on droolssample2-fdf9ddd5b-ffkmx with PID 1 (/drools-sample2.jar started by root in /)
...
2021-01-24 19:27:11,246 INFO  com.ilecreurer.drools.samples.sample2.runner.DataLoader run: Entering run...
2021-01-24 19:27:11,246 INFO  com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Entering preloadSession...
2021-01-24 19:27:11,247 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting state to loading events ...
2021-01-24 19:27:11,247 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Creating dbPositionEventRuleRuntimeListener (inactive)...
2021-01-24 19:27:11,247 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Getting stored position events...
2021-01-24 19:27:11,247 INFO  com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl getPositionEventsFromDB: Entering getPositionEventsFromDB...
2021-01-24 19:27:11,590 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting dbPositionEventRuleRuntimeListener.active to true  ...
2021-01-24 19:27:11,590 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Adding dbPositionEventRuleRuntimeListener to kieSession...
2021-01-24 19:27:11,590 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting state to ready ...
```

### Create the droolssample2 service

```
kubectl create -f ./src/main/k8s/droolssample2-service.yaml
service/droolssample2 created
```

### Testing

```
curl -ik -X POST "http://$(minikube ip):31317/ms-cde/v1/events/insert" -H "Content-Type: application/json" \
-d '{
"numberItems":1,
"positionEvents":[
    {
        "idEvent": "123",
        "idOwner": "1",
        "name": "A",
        "timestamp": "2020-11-29 21:18:01.000+0100",
        "latitude": 42.2199298,
        "longitude": 8.7026243
    }
]
}'
HTTP/1.1 202
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 24 Jan 2021 19:47:07 GMT

{"message":"Success","errorCode":0}
```


### Check logs of pod

Get the pod name:

```
kubectl get all
NAME                                READY   STATUS    RESTARTS   AGE
pod/droolssample2-fdf9ddd5b-jv7gs   1/1     Running   4          22m
pod/postgres-6f4d74b957-w4sqp       1/1     Running   0          20m

NAME                    TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
service/dbpostgresql    NodePort    10.110.31.150   <none>        5432:32548/TCP   38m
service/droolssample2   NodePort    10.99.221.153   <none>        8080:31317/TCP   36m
service/kubernetes      ClusterIP   10.96.0.1       <none>        443/TCP          25h

NAME                            READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/droolssample2   1/1     1            1           36m
deployment.apps/postgres        1/1     1            1           68m

NAME                                      DESIRED   CURRENT   READY   AGE
replicaset.apps/droolssample2-fdf9ddd5b   1         1         1       36m
replicaset.apps/postgres-6f4d74b957       1         1         1       68m
```

Check the logs:

```
kubectl logs -f pod/droolssample2-fdf9ddd5b-jv7gs
...
2021-01-25 17:32:40,797 INFO  com.ilecreurer.drools.samples.sample2.runner.DataLoader run: Entering run...
2021-01-25 17:32:40,798 INFO  com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Entering preloadSession...
2021-01-25 17:32:40,798 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting state to loading events ...
2021-01-25 17:32:40,798 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Creating dbPositionEventRuleRuntimeListener (inactive)...
2021-01-25 17:32:40,798 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Getting stored position events...
2021-01-25 17:32:40,798 INFO  com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl getPositionEventsFromDB: Entering getPositionEventsFromDB...
2021-01-25 17:32:41,128 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting dbPositionEventRuleRuntimeListener.active to true  ...
2021-01-25 17:32:41,129 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Adding dbPositionEventRuleRuntimeListener to kieSession...
2021-01-25 17:32:41,129 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl preloadSession: Setting state to ready ...
2021-01-25 17:48:35,631 INFO  org.apache.juli.logging.DirectJDKLog log: Initializing Spring DispatcherServlet 'dispatcherServlet'
2021-01-25 17:48:35,631 INFO  org.springframework.web.servlet.FrameworkServlet initServletBean: Initializing Servlet 'dispatcherServlet'
2021-01-25 17:48:35,638 INFO  org.springframework.web.servlet.FrameworkServlet initServletBean: Completed initialization in 7 ms
2021-01-25 17:48:35,745 DEBUG com.ilecreurer.drools.samples.sample2.controller.PositionEventController insertPositionEvents: Entering insertPositionEvents...
2021-01-25 17:48:35,746 DEBUG com.ilecreurer.drools.samples.sample2.controller.PositionEventController insertPositionEvents: numberItems: 1
2021-01-25 17:48:35,746 DEBUG com.ilecreurer.drools.samples.sample2.controller.PositionEventController insertPositionEvents: timestamp: Sun Nov 29 20:18:01 GMT 2020
2021-01-25 17:48:35,746 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Entering insertPositionEvents...
2021-01-25 17:48:35,746 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Check number of facts...
2021-01-25 17:48:35,747 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: fc: 0
2021-01-25 17:48:35,747 DEBUG com.ilecreurer.drools.samples.sample2.service.CollisionServiceImpl insertPositionEvents: Inserting transaction...
2021-01-25 17:48:35,760 DEBUG com.ilecreurer.drools.samples.sample2.listener.DBPositionEventRuleRuntimeListener objectInserted: Inserting event: 123
```

### Testing container recovery

Set replicas to 0 to remove all containers:

```
kubectl scale --replicas=0 -f ./src/main/k8s/postgres-deployment.yaml
kubectl scale --replicas=0 -f ./src/main/k8s/droolssample2-deployment.yaml
```

Set replicas to 1 on application to watch attempts to start the application. They fail as the DB is not up.

```
kubectl scale --replicas=1 -f ./src/main/k8s/droolssample2-deployment.yaml
```

Set the replicas to 1 on the database to start an instance and watch how the application does eventually start up:

```
kubectl scale --replicas=1 -f ./src/main/k8s/postgres-deployment.yaml
```

## Using AIS sample data

At https://marinecadastre.gov/ais/ we can download ships's AIS positioning data reported to US marine traffic in csv format for each day since 2009. We have selected the day [01/01/2020](https://coast.noaa.gov/htdata/CMSP/AISDataHandler/2020/AIS_2020_01_01.zip) to do some more intensive testing.

Go the downloads folder and save the file there. Extract the csv from the zip file and run:

```
./prepare_ais_file.sh AIS_2020_01_01.csv
```

The file `AIS_2020_01_01.transformed.csv` will be generated.
As the file is huge (aprox. 700MB), we have extracted the first 20 minutes of data using

```
grep -P "2020\-01\-01 00\:0[01]" AIS_2020_01_01.transformed.csv > AIS_2020_01_01_00_0__00_2.transformed.csv
```

Then we can test the CSV upload method:

```
curl -ik -X POST "http://localhost:8080/ms-cde/v1/events/insert/csv" --form file='@AIS_2020_01_01_00_0__00_2.transformed.csv'
```

Entering the container containing the application we can view the collision log:

```
docker exec -it <container id> sh
# cd log
# tail -F droolssample2-cep.log

...
2021-01-30 18:57:10,067 WARN  - [89f01631-4d10-49e3-a021-e0d39be4e130 - 367516950] is too close to [562600d4-1439-493b-bddf-55dace5dbcc4 - 367618310] . Time diff: -13 s
2021-01-30 18:57:10,067 WARN  - [a2df0899-4c99-40ef-a5dd-d7a713f0801c - 367551340] is too close to [562600d4-1439-493b-bddf-55dace5dbcc4 - 367618310] . Time diff: -25 s
2021-01-30 18:57:10,093 WARN  - [87b0fbfa-bad7-43f3-9e26-379a7628b9eb - 367476430] is too close to [f71c4438-f04b-4546-8280-8f330ff18bf1 - 367528810] . Time diff: -56 s
2021-01-30 18:57:10,194 WARN  - [d2079fd0-cc0d-43fe-b912-4853d19a5a1b - 366873860] is too close to [3f336531-45b0-46ef-92df-9c13063bb25c - 368092280] . Time diff: 59 s
2021-01-30 18:57:10,194 WARN  - [7c09be67-a8db-4573-8a52-c7437bb1e034 - 366867540] is too close to [d2079fd0-cc0d-43fe-b912-4853d19a5a1b - 366873860] . Time diff: -145 s
2021-01-30 18:57:10,207 WARN  - [2e43bbec-0f24-4480-a597-061794f594da - 368119660] is too close to [2c753917-3d34-4516-8649-609b36a64016 - 369131000] . Time diff: 40 s
2021-01-30 18:57:10,230 WARN  - [967d41df-7389-426c-9cf9-ac6c3d281392 - 367712620] is too close to [897d9bcd-95d2-478d-86b5-453ecc279cd3 - 367714750] . Time diff: 3 s
2021-01-30 18:57:10,230 WARN  - [baa69357-65d7-4f7f-818e-becd947bf362 - 367712610] is too close to [967d41df-7389-426c-9cf9-ac6c3d281392 - 367712620] . Time diff: -15 s
2021-01-30 18:57:10,277 WARN  - [38c5085b-04f4-4023-ade7-19629526e969 - 367402250] is too close to [53d95a41-e64d-4586-8133-7b5d7dcc5f3d - 367759510] . Time diff: 38 s
2021-01-30 18:57:10,278 WARN  - [38c5085b-04f4-4023-ade7-19629526e969 - 367402250] is too close to [bf50b8d8-a01b-4d85-9550-f8df64dc1098 - 367659970] . Time diff: 67 s
```

Viewing the console log using `docker logs <container id>` of the application we can see:

```
app_1           | 2021-01-30 18:55:35,611 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 138 regs in 1262 ms, avg: 109.350235 regs/s, total regs: 138, global average: 109.350235
...
app_1           | 2021-01-30 18:57:08,606 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 102 regs in 868 ms, avg: 117.51152 regs/s, total regs: 11376, global average: 120.6913
app_1           | 2021-01-30 18:57:09,777 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 112 regs in 1171 ms, avg: 95.64475 regs/s, total regs: 11488, global average: 120.38396
app_1           | 2021-01-30 18:57:10,278 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 52 regs in 501 ms, avg: 103.79241 regs/s, total regs: 11540, global average: 120.2973
app_1           | 2021-01-30 18:57:10,278 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: inserted 11540 registers out of 108495
```

On the Postgres database container we can obtain some info about the current inserted registers:

```
droolssample2db=# select count(id_owner) from POSITION_EVENT;
 count
-------
   952
(1 row)

droolssample2db=# select count(distinct id_owner) from POSITION_EVENT;
 count
-------
   952
(1 row)
```


Test conclusion:
- Area is considering the East side of the US thus discarding many registers from the West side.
- We have an average handling rate of approx. 100 registers/s or approx. 6000 registers/min.
- The reduced file contains 108495 registers for 20 minutes of positions.
- Inserted 11540 registers out of 108495 in less than 2 minutes.
- By restricting the area of collision detection we can achieve a viable collision detection for a specific area.


Performance improvements:
- Database on another machine should vastly improve performance.
- The CEP has O(n x n) operations for the eviction process. Hence splitting into obvious disjoint areas imporoves the processing speed..


## Useful links

- https://medium.com/@xcoulon/deploying-your-first-web-app-on-minikube-6e98d2884b3a
- https://severalnines.com/database-blog/using-kubernetes-deploy-postgresql
- https://medium.com/swlh/how-to-run-locally-built-docker-images-in-kubernetes-b28fbc32cc1d
