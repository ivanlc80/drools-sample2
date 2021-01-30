package com.ilecreurer.drools.samples.sample2.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ilecreurer.drools.samples.sample2.conf.AreaProperties;
import com.ilecreurer.drools.samples.sample2.event.PositionEvent;
import com.ilecreurer.drools.samples.sample2.service.CollisionService;
import com.ilecreurer.drools.samples.sample2.service.CollisionServiceException;
import com.ilecreurer.drools.samples.sample2.util.Constants;

/**
 * TransactionController class.
 * @author ilecreurer.
 */
@RestController
@RequestMapping("/events")
public class PositionEventController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(PositionEventController.class);

    @Autowired
    private AreaProperties areaProperties;

    /**
     * Collision service.
     */
    @Autowired
    private CollisionService collisionService;

    @PostMapping(value = "/insert",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<ResponseMessage> insertPositionEvents(
            @RequestBody final MultiplePositionEventsPayload payload,
            final HttpServletResponse response) {


        LOGGER.debug("Entering insertPositionEvents...");
        LOGGER.debug("numberItems: {}", payload.getNumberItems());

        if (payload.getPositionEvents() == null) {
            LOGGER.warn("Missing positionEvents attribute");
            return new ResponseEntity<>(new ResponseMessage("Missing positionEvents attribute",
                    ErrorCode.MISSING_POSITION_EVENTS_ATTR.getCode()),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getNumberItems() == null) {
            return new ResponseEntity<>(new ResponseMessage("Missing numberItems attribute",
                    ErrorCode.MISSING_NUMBER_ITEMS_ATTR.getCode()),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getNumberItems().intValue() != payload.getPositionEvents().size()) {
            return new ResponseEntity<>(new ResponseMessage("Number of items mismatch",
                    ErrorCode.NUMBER_ITEMS_MISMATCH.getCode()),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getNumberItems().intValue() == 0) {
            return new ResponseEntity<>(new ResponseMessage("0 items",
                    ErrorCode.EMPTY_ITEMS_ATTR.getCode()),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getPositionEvents() != null && payload.getPositionEvents().size() > 0) {
            LOGGER.debug("timestamp: {}", payload.getPositionEvents().get(0).getTimestamp());
        }

        try {
            collisionService.insertPositionEvents(payload.getPositionEvents());
        } catch (CollisionServiceException e) {
            return new ResponseEntity<>(new ResponseMessage("Failed: " + e.getMessage(),
                    ErrorCode.SERVICE_ERROR.getCode()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage("Invalid payload: " + e.getMessage(),
                    ErrorCode.SERVICE_ERROR.getCode()),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseMessage("Success", 0),
                HttpStatus.ACCEPTED);
    }


    @PostMapping("/insert/csv")
    public final ResponseEntity<ResponseMessage> insertPositionEventsCSV(final
            @RequestParam("file") MultipartFile file) {
        LOGGER.debug("Entering insertPositionEventsCSV...");
        if (file == null) {
            return new ResponseEntity<>(new ResponseMessage("Invalid payload: file is null",
                    ErrorCode.SERVICE_ERROR.getCode()),
                    HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        try (BufferedReader fileReader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), "UTF-8"))) {

            long t0 = System.currentTimeMillis();
            long t1 = t0;
            long delta = 0;
            int registers = 0;

            long t0Local = t0;
            long t1Local = t0;
            long deltaLocal = 0;
            int registersLocal = 0;

            int registersInserted = 0;
            int registersTotal = 0;

            String[] ar;
            String line;
            List<PositionEvent> positionEvents = new ArrayList<PositionEvent>();
            while ((line = fileReader.readLine()) != null) {
                ar = line.split(",");
                // CHECKSTYLE:OFF - Reason: Allow basic array index
                if (ar.length != 6) {
                    return new ResponseEntity<>(new ResponseMessage("Missing positionEvents attribute",
                            ErrorCode.MISSING_POSITION_EVENTS_ATTR.getCode()),
                            HttpStatus.BAD_REQUEST);
                }

                PositionEvent pe = new PositionEvent(
                        ar[1], ar[2], ar[3], sdf.parse(ar[0]),
                        Double.parseDouble(ar[4]), Double.parseDouble(ar[5])
                        );
                // CHECKSTYLE:ON

                positionEvents.add(pe);
                registersTotal++;

                if (positionEvents.size() >= Constants.MAX_POSITION_EVENTS_SIZE) {
                    registersInserted = collisionService.insertPositionEvents(positionEvents);
                    positionEvents.clear();

                    registers = registers + registersInserted;
                    registersLocal = registersLocal + registersInserted;

                    t1 = System.currentTimeMillis();
                    delta = t1 - t0;

                    t1Local = t1;
                    deltaLocal = t1Local - t0Local;
                    LOGGER.info("Added {} regs in {} ms, avg: {} regs/s, total regs: {}, global average: {}",
                            registersLocal,
                            deltaLocal,
                            ((float) registersLocal / (float) deltaLocal) * Constants.MILLIS_IN_SECOND,
                            registers,
                            ((float) registers / (float) delta) * Constants.MILLIS_IN_SECOND
                    );

                    registersLocal = 0;
                    t0Local = t1Local;
                }
            }

            registersInserted = collisionService.insertPositionEvents(positionEvents);
            positionEvents.clear();

            registers = registers + registersInserted;
            registersLocal = registersLocal + registersInserted;

            t1 = System.currentTimeMillis();
            delta = t1 - t0;

            t1Local = t1;
            deltaLocal = t1Local - t0Local;
            LOGGER.info("Added {} regs in {} ms, avg: {} regs/s, total regs: {}, global average: {}",
                    registersLocal,
                    deltaLocal,
                    ((float) registersLocal / (float) deltaLocal) * Constants.MILLIS_IN_SECOND,
                    registers,
                    ((float) registers / (float) delta) * Constants.MILLIS_IN_SECOND
            );

            LOGGER.info("inserted {} registers out of {}", registers, registersTotal);

        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseMessage("Failed to read file",
                    ErrorCode.SERVICE_ERROR.getCode()),
                    HttpStatus.BAD_REQUEST);
        } catch (ParseException e) {
            return new ResponseEntity<>(
                    new ResponseMessage("Invalid positionEvents attribute: " + e.getMessage(),
                    ErrorCode.MISSING_POSITION_EVENTS_ATTR.getCode()),
                    HttpStatus.BAD_REQUEST);
        } catch (CollisionServiceException e) {
            return new ResponseEntity<>(new ResponseMessage("Failed: " + e.getMessage(),
                    ErrorCode.SERVICE_ERROR.getCode()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage("Invalid payload: " + e.getMessage(),
                    ErrorCode.SERVICE_ERROR.getCode()),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseMessage("Success", 0),
                HttpStatus.ACCEPTED);
    }
}
