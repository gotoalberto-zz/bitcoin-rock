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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * @author gotoalberto
 */
class WelcomeStep implements Step {

    private final PrintStream printer;
    private final InputStream input;
    private final String mainBTCAddress;
    private final float fee;
    private final String endPoint;
    private final int port;
    private final String user;
    private final String password;

    public WelcomeStep(PrintStream printer, InputStream input,
            String mainBTCAddress, float fee, String endPoint, int port,
            String user, String password) {
        this.printer = printer;
        this.input = input;
        this.mainBTCAddress = mainBTCAddress;
        this.fee = fee;
        this.endPoint = endPoint;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public Step run() {
        this.printer.print(buildWelcomeMessage());
        this.waitForKey();
        return new ReadChoice(this.printer, this.input, this.mainBTCAddress,
                this.fee, this.endPoint, this.port, this.user, this.password);
    }

    private final void waitForKey() {
        this.printer.println("\n PRESS ENTER KEY TO CONTINUE...");
        InputStreamReader converter = new InputStreamReader(this.input);
        BufferedReader in = new BufferedReader(converter);
        try {
            in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static final String buildWelcomeMessage() {
        StringBuffer header = new StringBuffer();
        try {
            InputStream stream = WelcomeStep.class
                    .getResourceAsStream("/welcome.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    stream));
            String line;
            while ((line = reader.readLine()) != null) {
                header.append(line).append("\n");
            }
            stream.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(String.format(
                    "Exception when loading welcome message. %s",
                    e.getMessage()));
        }
        return header.toString();
    }
}
