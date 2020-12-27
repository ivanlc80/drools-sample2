package com.ilecreurer.drools.samples.sample2.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.ilecreurer.drools.samples.sample2.service.CollisionService;
import com.ilecreurer.drools.samples.sample2.service.CollisionServiceException;

/**
 * TransactionController class.
 * @author ilecreurer.
 */
@RestController
@RequestMapping("/events/insert")
public class PositionEventController {

    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(PositionEventController.class);

    /**
     * Collision service.
     */
    @Autowired
    private CollisionService collisionService;

    @PostMapping(value = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<ResponseMessage> insertPositionEvents (
            @RequestBody final MultiplePositionEventsPayload payload,
            final HttpServletResponse response) {


        LOGGER.info("insertPositionEvents...");
        LOGGER.info("numberItems: {}", payload.getNumberItems());

        if (payload.getPositionEvents() == null) {
            LOGGER.warn("Missing positionEvents attribute");
            return new ResponseEntity<>(new ResponseMessage("Missing positionEvents attribute",100),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getNumberItems() == null) {
            return new ResponseEntity<>(new ResponseMessage("Missing numberItems attribute",101),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getNumberItems().intValue() != payload.getPositionEvents().size()) {
            return new ResponseEntity<>(new ResponseMessage("Number of items mismatch",102),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getNumberItems().intValue() == 0) {
            return new ResponseEntity<>(new ResponseMessage("0 items",103),
                    HttpStatus.BAD_REQUEST);
        }
        if (payload.getPositionEvents() != null && payload.getPositionEvents().size() > 0) {
            LOGGER.info("timestamp: {}", payload.getPositionEvents().get(0).getTimestamp());
        }

        try {
            collisionService.insertPositionEvents(payload.getPositionEvents());
        } catch (CollisionServiceException e) {
            return new ResponseEntity<>(new ResponseMessage("Failed: " + e.getMessage(),0),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage("Invalid payload: " + e.getMessage(),0),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseMessage("Success",0),
                HttpStatus.ACCEPTED);
    }
}
