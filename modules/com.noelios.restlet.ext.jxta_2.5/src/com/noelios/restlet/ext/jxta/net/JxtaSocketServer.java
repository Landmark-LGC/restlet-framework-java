/*
 * Copyright 2007 Noelios Consulting.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package com.noelios.restlet.ext.jxta.net;

import net.jxta.ext.network.NetworkException;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaServerSocket;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author james todd [james dot w dot todd at gmail dot com]
 */

public class JxtaSocketServer
        extends JxtaServer {

    private ServerSocket server;

    public JxtaSocketServer(String name, PeerGroup group) {
        this(name, group, null);
    }

    public JxtaSocketServer(String name, PeerGroup group, PipeAdvertisement pipe) {
        super(name, group, pipe);

        if (pipe == null ||
                (!pipe.getType().equals(PipeService.UnicastType) &&
                        !pipe.getType().equals(PipeService.UnicastSecureType))) {
            throw new IllegalArgumentException("invalid pipe: " + pipe);
        }
    }

    // todo: implement
    public void startServer() throws NetworkException {
        try {
            server = new JxtaServerSocket(getPeerGroup(), getPipeAdvertisement());
        } catch (IOException ioe) {
            throw new NetworkException("unable to create socket", ioe);
        }
    }

    // todo: implement
    public void stopServer() throws NetworkException {
    }
}
