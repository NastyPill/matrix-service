package com.matrix.state.models;

import com.matrix.state.announce.MatrixStateTCPAnnouncer;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class MatrixState {

    private static final Logger LOG = LoggerFactory.getLogger("State");

    private final int[][] state;
    private final MatrixStateTCPAnnouncer announcer;

    private Long now;

    public MatrixState(MatrixStateTCPAnnouncer announcer) {
        this.state = new int[8][8];
        this.announcer = announcer;
        initState();
    }

    private void initState() {
        now = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                state[i][j] = 0;
            }
        }
    }

    public void changeState(Boolean isOn, int x, int y) {
        if(isOn) {
            state[x][y] = 1;
        } else {
            state[x][y] = 0;
        }
        if((System.currentTimeMillis() - now) > 5) {
            now = System.currentTimeMillis();
            announcer.announce(this.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(state[i][j]);
            }
        }
        return sb.toString();
    }

}