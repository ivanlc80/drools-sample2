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
As the file is huge (aprox. 700MB), we have extracted the first 10 minutes of data using

```
grep -P "2020\-01\-01 00\:00" AIS_2020_01_01.transformed.csv > AIS_2020_01_01_00_00.transformed.csv
```

Then we can test the CSV upload method:

```
curl -ik -X POST "http://localhost:8080/ms-cde/v1/events/insert/csv" --form file='@AIS_2020_01_01_00_0.transformed.csv'
```

Entering the container containing the application we can view the collision log:

```
docker exec -it <container id> sh
# cd log
# tail -F droolssample2-cep.log

...
2021-01-29 18:54:35,975 WARN  - [63d1f69c-750e-415f-b38e-e0134e664580 - 316005613] is too close to [e09da747-fd8e-4c81-a483-ae3632ccccd4 - 316005649] . Time diff: 0 s
2021-01-29 18:54:35,975 WARN  - [e8161d87-24a2-48f0-b0dc-8747c15326c0 - 316005625] is too close to [e09da747-fd8e-4c81-a483-ae3632ccccd4 - 316005649] . Time diff: -22 s
2021-01-29 18:54:36,357 WARN  - [66c619ad-c4f8-4e67-81c2-7f691fa12ff1 - 367685620] is too close to [87f3a796-7641-4bb1-8091-c0b29d01349f - 367710250] . Time diff: 2 s
2021-01-29 18:54:36,436 WARN  - [82b4efcc-36f0-41bf-895f-d5c0d1b783bd - 338218374] is too close to [46ba0878-ae8e-4def-bb34-a2c75b028ac6 - 368051880] . Time diff: 108 s
2021-01-29 18:54:36,437 WARN  - [82b4efcc-36f0-41bf-895f-d5c0d1b783bd - 338218374] is too close to [eba722ac-000b-4f0f-84db-797948c09a3e - 367766070] . Time diff: 154 s
2021-01-29 18:54:36,437 WARN  - [2704c64d-c307-47b2-aac5-f1d273b996ec - 316028584] is too close to [82b4efcc-36f0-41bf-895f-d5c0d1b783bd - 338218374] . Time diff: -131 s
2021-01-29 18:54:36,587 WARN  - [eff7b12f-20bf-4e28-9282-00c3356b764a - 338108411] is too close to [6f2596c0-7f47-4456-aa8b-5d574e5bdccd - 368027000] . Time diff: -27 s
2021-01-29 18:54:36,587 WARN  - [53bee4bd-4143-4a4a-a10f-672990e3c3d5 - 367084860] is too close to [6f2596c0-7f47-4456-aa8b-5d574e5bdccd - 368027000] . Time diff: -42 s
2021-01-29 18:54:36,587 WARN  - [28d996d5-3fbb-4082-a83d-efb3302443e8 - 366287850] is too close to [6f2596c0-7f47-4456-aa8b-5d574e5bdccd - 368027000] . Time diff: -34 s
2021-01-29 18:54:36,617 WARN  - [f1976d8e-11cb-4402-b4e6-5afbcca89351 - 367466650] is too close to [dcc9958a-1369-473a-b353-4aaf9038c00e - 367594190] . Time diff: 105 s
2021-01-29 18:54:36,763 WARN  - [69f4e9ed-7345-4ae5-919b-8c34f444f31a - 367623310] is too close to [a37d2b8e-28f8-4985-9ce3-621ec07fb340 - 367627580] . Time diff: -3 s
2021-01-29 18:54:36,763 WARN  - [f4e6c72d-d600-41d6-9a39-777345fa80a0 - 367377380] is too close to [a37d2b8e-28f8-4985-9ce3-621ec07fb340 - 367627580] . Time diff: -40 s

```

Viewing the console log using `docker logs <container id>` of the application we can see:

```
...
app_1           | 2021-01-29 18:51:44,868 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 1000 regs in 31248 ms, avg: 32.00205 regs/s, total regs: 51000, global average: 39.471546
app_1           | 2021-01-29 18:52:14,594 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 1000 regs in 29726 ms, avg: 33.640587 regs/s, total regs: 52000, global average: 39.340416
app_1           | 2021-01-29 18:52:45,660 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 1000 regs in 31066 ms, avg: 32.189533 regs/s, total regs: 53000, global average: 39.17621
app_1           | 2021-01-29 18:53:16,866 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 1000 regs in 31206 ms, avg: 32.04512 regs/s, total regs: 54000, global average: 39.015423
app_1           | 2021-01-29 18:53:47,137 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 1000 regs in 30271 ms, avg: 33.034916 regs/s, total regs: 55000, global average: 38.88742
app_1           | 2021-01-29 18:54:17,200 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 1000 regs in 30063 ms, avg: 33.263477 regs/s, total regs: 56000, global average: 38.77037
app_1           | 2021-01-29 18:54:36,763 INFO  c.i.d.s.s.c.PositionEventController insertPositionEventsCSV: Added 646 regs in 19563 ms, avg: 33.021523 regs/s, total regs: 56646, global average: 38.693546
```

On the Postgres database container we can obtain some info about the current inserted registers:

```
droolssample2db=# select count(id_owner) from POSITION_EVENT;
 count
-------
 10557
(1 row)

droolssample2db=# select count(distinct id_owner) from POSITION_EVENT;
 count
-------
 10557
(1 row)
```


Test conclusion:
- We reach an average handling rate of  33 registers/s , ie 1980 registers/minute.
- The reduced file contains 56646 registers for 10 minutes of positions.
- In order to handle load the application should be able to handle over 5700 registers/minute.

Performance improvements:
- Database on another machine should vastly improve performance.
- Reduce collision detection area and launch multiple containers each handling a specific region `[lat1, lat2] x [long1, long2]` . Using environment variables this can be achieved. The same database could shared accross the nodes.


## Useful links

- https://medium.com/@xcoulon/deploying-your-first-web-app-on-minikube-6e98d2884b3a
- https://severalnines.com/database-blog/using-kubernetes-deploy-postgresql
- https://medium.com/swlh/how-to-run-locally-built-docker-images-in-kubernetes-b28fbc32cc1d
