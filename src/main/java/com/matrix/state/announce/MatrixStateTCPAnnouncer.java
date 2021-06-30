package com.matrix.state.announce;

import com.matrix.state.models.MatrixState;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MatrixStateTCPAnnouncer {

    private final String ip;
    private final Integer port;

    private static final Logger LOG = LoggerFactory.getLogger("MatrixService");

    public MatrixStateTCPAnnouncer(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public void announce(String state) {
        try {
            Socket socket = new Socket(ip, port);
            OutputStream outputStream = socket.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(state);
            bufferedWriter.close();
        } catch (IOException e) {
            LOG.error("FAILED TO SEND MATRIX STATE TO ARDUINO VIA TCP: {}", e.toString());
        }
    }

}