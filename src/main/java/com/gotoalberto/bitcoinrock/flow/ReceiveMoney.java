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
import java.util.Locale;
import java.util.Random;

import com.gotoalberto.bitcoinrock.bitcoin.BitcoinClient;

/**
 * @author gotoalberto
 */
class ReceiveMoney implements Step {

    private final PrintStream printer;
    private final InputStream input;
    private final String mainBTCAddress;
    private final float fee;
    private final String endPoint;
    private final int port;
    private final String user;
    private final String password;

    private final static int CONFIRMATIONS_REQUIRED = 0;

    public ReceiveMoney(PrintStream printer, InputStream input,
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

    public Step run() throws Exception {
        BitcoinClient bclient = new BitcoinClient(this.endPoint, this.port,
                this.user, this.password);
        String account = randomString(8).concat("-received");
        String address = bclient.createAddress(account);
        this.printer.print(buildPaymentMessage(address, this.fee));

        while (bclient.getBalance(account, CONFIRMATIONS_REQUIRED) < this.fee) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(String.format("Waiting payment: %s",
                        e.getMessage()));
            }
        }
        return new ReadChoice(this.printer, this.input, this.mainBTCAddress,
                this.fee, this.endPoint, this.port, this.user, this.password);
    }

    private static final String buildPaymentMessage(String address, float fee) {
        String payMessage = String.format(
                "\n\n Pay %s BTC to -> %s \n\nWaiting for payment...",
                String.format(Locale.US, "%.8f", fee), address);
        return payMessage;
    }

    private static final String randomString(int len) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
