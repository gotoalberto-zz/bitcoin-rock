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

import javax.annotation.Nullable;

import com.gotoalberto.bitcoinrock.game.Choice;

/**
 * @author gotoalberto
 */
class ReadChoice implements Step {

    private final static String EXIT_OPTION = "E";
    private final PrintStream printer;
    private final InputStream input;
    private final String mainBTCAddress;
    private final float fee;
    private final String endPoint;
    private final int port;
    private final String user;
    private final String password;

    public ReadChoice(PrintStream printer, InputStream input,
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
        this.printMessage();
        Step step;
        while (true) {
            String choiceStr = this.readChoice().toUpperCase().trim();
            if (choiceStr.equals(EXIT_OPTION)) {
                step = new EndStep(this.printer);
                break;
            } else {
                try {
                    Choice choice = Choice.valueOf(choiceStr);
                    step = new EvaluateChoice(this.printer, this.input,
                            this.mainBTCAddress, this.fee, this.endPoint,
                            this.port, this.user, this.password, choice);
                    break;
                } catch (IllegalArgumentException e) {
                    this.printer.print("\n\n Illegal option.");
                    this.printMessage();
                }
            }
        }
        return step;
    }

    @Nullable
    private String readChoice() {
        InputStreamReader converter = new InputStreamReader(this.input);
        BufferedReader in = new BufferedReader(converter);
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void printMessage() {
        this.printer.print(buildOptionsMessage());
        this.printer.print(buildChoiceMessage());
    }

    private final static String buildChoiceMessage() {
        return "\n\nPick option: ";
    }

    private final static String buildOptionsMessage() {
        StringBuffer message = new StringBuffer();
        message.append("\n\n");
        for (Choice choice : Choice.values()) {
            message.append(choice.toString());
            for (int i = 0; i < Choice.largestNameLength()
                    - choice.toString().length(); i++) {
                message.append(" ");
            }
            message.append(" ->").append(" ").append(choice);
            message.append("\n");
        }
        message.append("Exit: ").append(EXIT_OPTION);
        return message.toString();
    }
}
