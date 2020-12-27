package com.ilecreurer.drools.samples.sample2.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

public class PositionGeneratorCSVMain {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

    public static void main(String[] args) {
        try {

            String initialDateAsString = "2020-12-06 20:21:00.000+0100";
            Date date = sdf.parse(initialDateAsString);
            int deltaTime = 1000;//1s

            int numberA = 10;

            int steps = numberA;

            double[] positionTopLeft = {42.236355274017164, 8.73173187617322};
            double[] positionBottomRight = {42.22942825216788, 8.719543919087279};

            double deltaLat = (positionBottomRight[0] - positionTopLeft[0])/(steps - 1);
            double deltaLon = (positionBottomRight[1] - positionTopLeft[1])/(steps - 1);

            System.out.println("deltaLat=" + deltaLat);
            System.out.println("deltaLon=" + deltaLon);

            // Generate initial positions
            List<PositionEvent> positionEventsA = new ArrayList<PositionEvent>();
            List<PositionEvent> positionEventsB = new ArrayList<PositionEvent>();

            String filename = "data.csv";
            File dir = new File("output");
            if (!dir.exists()) {
                dir.mkdir();
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(dir.getName() + "/" + filename))) {
                for (int i = 0; i < numberA; i++) {
                    long idOwnerA = 1000 + i;
                    long idOwnerB = 20000 + i;

                    // A people are on the same latitude
                    PositionEvent peA = new PositionEvent(UUID.randomUUID().toString(),
                            idOwnerA, "A", date,
                            positionTopLeft[0], positionTopLeft[1] + i * deltaLon
                            );

                    positionEventsA.add(peA);
                    System.out.println(peA.getIdEvent() + "," + peA.getIdOwner() + "," +
                            peA.getLatitude() + "," + peA.getLongitude()
                            );

                    writeRegisterToFile(writer, peA);

                    PositionEvent peB = new PositionEvent(UUID.randomUUID().toString(),
                            idOwnerB, "B", date,
                            positionTopLeft[0] + i * deltaLat, positionTopLeft[1]
                            );

                    System.out.println(peB.getIdEvent() + "," + peB.getIdOwner() + "," +
                            peB.getLatitude() + "," + peB.getLongitude()
                            );
                    positionEventsB.add(peB);

                    writeRegisterToFile(writer, peB);
                }

                Calendar calendar = Calendar.getInstance();
                for (int sIndex = 0; sIndex < steps - 1; sIndex++) {

                    // Add one second to the current time
                    calendar.setTime(date);
                    calendar.add(Calendar.MILLISECOND, deltaTime);
                    date = calendar.getTime();

                    // A people move down
                    for (int i = 0; i < positionEventsA.size(); i++) {
                        PositionEvent peA = positionEventsA.get(i);
                        peA.setIdEvent(UUID.randomUUID().toString());
                        peA.setTimestamp(date);
                        peA.setLatitude(peA.getLatitude() + deltaLat);

                        writeRegisterToFile(writer, peA);
                    }

                    // B people move right
                    for (int i = 0; i < positionEventsB.size(); i++) {
                        PositionEvent peB = positionEventsB.get(i);
                        peB.setIdEvent(UUID.randomUUID().toString());
                        peB.setTimestamp(date);
                        peB.setLongitude(peB.getLongitude() + deltaLon);

                        writeRegisterToFile(writer, peB);
                    }
                }

                writer.flush();
            }

        } catch (Exception e) {

        }

    }

    private static void writeRegisterToFile(BufferedWriter writer, PositionEvent pe) throws IOException {
        writer.write(sdf.format(pe.getTimestamp()));
        writer.write(',');
        writer.write(pe.getIdEvent());
        writer.write(',');
        writer.write(String.valueOf(pe.getIdOwner()));
        writer.write(',');
        writer.write(pe.getType());
        writer.write(',');
        writer.write(String.valueOf(pe.getLatitude()));
        writer.write(',');
        writer.write(String.valueOf(pe.getLongitude()));
        writer.write('\n');
    }

}
