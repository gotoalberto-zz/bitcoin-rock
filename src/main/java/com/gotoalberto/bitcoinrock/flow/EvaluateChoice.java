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
import java.util.Arrays;
import java.util.List;

import com.gotoalberto.bitcoinrock.game.Choice;
import com.gotoalberto.bitcoinrock.game.Evaluator;

/**
 * @author gotoalberto
 */
class EvaluateChoice implements Step {

    private final PrintStream printer;
    private final InputStream input;
    private final String mainBTCAddress;
    private final float fee;
    private final String endPoint;
    private final int port;
    private final String user;
    private final String password;
    private final Choice userChoice;

    public EvaluateChoice(PrintStream printer, InputStream input,
            String mainBTCAddress, float fee, String endPoint, int port,
            String user, String password, Choice userChoice) {
        this.printer = printer;
        this.input = input;
        this.mainBTCAddress = mainBTCAddress;
        this.fee = fee;
        this.endPoint = endPoint;
        this.port = port;
        this.user = user;
        this.password = password;
        this.userChoice = userChoice;
    }

    public Step run() {
        Evaluator eval = new Evaluator();
        Choice machineChoice = getRandomChoice();
        Choice winner = eval.winnerFrom(this.userChoice, machineChoice);
        if (winner == null) {
            this.printer
                    .print(buildEvenMessage(this.userChoice, machineChoice));
            return new ReadChoice(this.printer, this.input,
                    this.mainBTCAddress, this.fee, this.endPoint, this.port,
                    this.user, this.password);
        } else if (winner == this.userChoice) {
            this.printer.print(buildUserWinsMessage(this.userChoice,
                    machineChoice));
            return new SendMoney(this.printer, this.input, this.mainBTCAddress,
                    this.fee, this.endPoint, this.port, this.user,
                    this.password);

        } else {
            this.printer.print(buildMachineWinsMessage(this.userChoice,
                    machineChoice));
            return new ReceiveMoney(this.printer, this.input,
                    this.mainBTCAddress, this.fee, this.endPoint, this.port,
                    this.user, this.password);
        }
    }

    private static final String buildUserWinsMessage(Choice userChoice,
            Choice machineChoice) {
        StringBuffer message = new StringBuffer();
        message.append("\n\n--------------------------------------------");
        message.append("\nYou choose ").append(userChoice.toString());
        message.append("\nMachine chooses ").append(machineChoice.toString());
        message.append("\nYou win!");
        message.append("\n--------------------------------------------");
        return message.toString();
    }

    private static final String buildMachineWinsMessage(Choice userChoice,
            Choice machineChoice) {
        StringBuffer message = new StringBuffer();
        message.append("\n\n--------------------------------------------");
        message.append("\nYou choose ").append(userChoice.toString());
        message.append("\nMachine chooses ").append(machineChoice.toString());
        message.append("\nYou lose! ha ha!");
        message.append("\n--------------------------------------------");
        return message.toString();
    }

    private static final String buildEvenMessage(Choice userChoice,
            Choice machineChoice) {
        StringBuffer message = new StringBuffer();
        message.append("\n\n--------------------------------------------");
        message.append("\nYou choose ").append(userChoice.toString());
        message.append("\nMachine chooses ").append(machineChoice.toString());
        message.append("\nDead Heat!");
        message.append("\n--------------------------------------------");
        return message.toString();
    }

    private static final Choice getRandomChoice() {
        List<Choice> choices = Arrays.asList(Choice.values());
        int choice = 0 + (int) (Math.random() * choices.size());
        return choices.get(choice);
    }
}
