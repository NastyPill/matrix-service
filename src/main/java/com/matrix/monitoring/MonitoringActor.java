package com.matrix.monitoring;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.matrix.operation.message.WorkReportMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MonitoringActor extends AbstractActor {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringActor.class);

    private Long totalMillis;
    private Integer totalRequestsCount;

    private int messagesCounter;

    public MonitoringActor() {
        this.totalMillis = 0L;
        this.totalRequestsCount = 0;
        this.messagesCounter = 0;
    }

    public static Props props() {
        return Props.create(MonitoringActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WorkReportMessage.class, msg -> handleWorkReportMessage(msg))
                .build();
    }

    private void handleWorkReportMessage(WorkReportMessage msg) {
        if(msg.getRequestCount() > 0) {
            messagesCounter++;
            if (messagesCounter >= 10000) {
                LOG.info("Writing metrics to file");
                messagesCounter = 0;
                writeToFile();
                reset();
            }
            collect(msg);
        }
    }

    private void collect(WorkReportMessage message) {
        this.totalMillis += message.getTime();
        this.totalRequestsCount += message.getRequestCount();
    }

    private void reset() {
        this.totalMillis = 0L;
        this.totalRequestsCount = 0;
    }

    private void writeToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("metrics.txt", true));
            bw.write(String.format("t=%d r=%d", this.totalMillis, this.totalRequestsCount));
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            LOG.error("Something went wrong while saving metrics to file: {}", e.toString());
        }
    }
}
