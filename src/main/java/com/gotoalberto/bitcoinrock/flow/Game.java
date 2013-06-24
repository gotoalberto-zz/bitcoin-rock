/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gotoalberto.bitcoinrock.flow;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * @author gotoalberto
 */
public class Game {

    private final String mainBTCAddress;
    private final float fee;
    private final String endPoint;
    private final int port;
    private final String user;
    private final String password;

    public Game(String mainBTCAddress, float fee, String endPoint, int port,
            String user, String password) {
        this.mainBTCAddress = mainBTCAddress;
        this.fee = fee;
        this.endPoint = endPoint;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void start() throws Exception {
        Step step = new WelcomeStep(buildPrinter(), buildInput(),
                this.mainBTCAddress, this.fee, this.endPoint, this.port,
                this.user, this.password);
        while (!(step instanceof EndStep)) {
            step = step.run();
        }
        step.run();
    }

    private static final PrintStream buildPrinter() {
        return System.out;
    }

    private static final InputStream buildInput() {
        return System.in;
    }
}
