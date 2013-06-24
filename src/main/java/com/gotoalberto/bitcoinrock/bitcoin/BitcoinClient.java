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
package com.gotoalberto.bitcoinrock.bitcoin;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author gotoalberto
 */
public class BitcoinClient {

    private final HttpClient httpClient;

    /**
     * 
     * @param endPoint
     *            of minner.
     * @param port
     *            for RPC calls
     * @param user
     *            for RPC calls
     * @param password
     *            for RPC calls
     * @param mainBTCAddress
     *            , the client will send money from this account.
     */
    public BitcoinClient(String endPoint, int port, String user, String password) {
        this.httpClient = new HttpClient(endPoint, port, user, password);
    }

    /**
     * Constructor for test.
     * 
     * @param httpClient
     */
    BitcoinClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Returns a publicKey from a account ID Name.
     * 
     * @param accountId
     *            , for example "myAccount" or your account name on minner.
     * @return BTC Public Key as String format.
     * @throws Exception
     *             if there are any problem parsing result.
     */
    public String getPublicKey(String accountId) throws Exception {
        String params = String.format("\"%s\"", accountId);
        String responseStr = this.httpClient.post(buildPayload(
                "getaddressesbyaccount", params));
        return parseArrayResult(responseStr);
    }

    /**
     * Returns a private key from a Public Key String.
     * 
     * @param publicKey
     *            for example '1JLC7PdrFE7bZZAwXQHw2XuzHmc9XV4gXm'
     * @return Private Key stored on miner for Public Key param.
     * @throws Exception
     *             if there are any problem parsing result.
     */
    public String getPrivateKey(String publicKey) throws Exception {
        String params = String.format("\"%s\"", publicKey);
        String responseStr = this.httpClient.post(buildPayload("dumpprivkey",
                params));
        return parseResult(responseStr);
    }

    /**
     * Returns a balance with 8 precision positions of accountID passed as param
     * 
     * @param accountId
     *            name of account on miner, example 'myAccount'
     * @param confirmations
     *            required for check transactions as valid.
     * @return float that represents balance with 8 precision positions.
     * @throws Exception
     *             if there are any problem parsing result.
     */
    public float getBalance(String accountId, int confirmations)
            throws Exception {
        String params = String.format("\"%s\",%s", accountId, confirmations);
        String responseStr = this.httpClient.post(buildPayload("getbalance",
                params));
        return Float.parseFloat(parseResult(responseStr));
    }

    /**
     * Create and store a new BTC Account (Public and Private key) on miner
     * 
     * @param accountId
     *            for reference this account on minner
     * @return created public key of BTC Account
     * @throws Exception
     *             if there are any problem parsing result.
     */
    public String createAddress(String accountId) throws Exception {
        String params = String.format("\"%s\"", accountId);
        String responseStr = this.httpClient.post(buildPayload("getnewaddress",
                params));
        return parseResult(responseStr);
    }

    /**
     * Sends BTC from between accounts.
     * 
     * @param accountId
     *            Name of account for reference on minner, example 'myAccount'
     * @param toAddress
     *            , BTC Address (Public Key) to send BTC. Example
     *            '1JLC7PdrFE7bZZAwXQHw2XuzHmc9XV4gXm'
     * @param amount
     *            of money to send
     * @throws Exception
     *             if there are any problem parsing result.
     * @return Transaction ID
     */
    public String send(String accountId, String toAddress, float amount)
            throws Exception {
        String params = String.format("\"%s\",\"%s\",%s", accountId, toAddress,
                String.format(Locale.US, "%.8f", amount));
        String responseStr = this.httpClient.post(buildPayload("sendfrom",
                params));
        String transactionId = parseResult(responseStr);
        if (transactionId == null || transactionId.equals("")) {
            new Exception(new Error(String.format(
                    "Call '%s' , Response: '%s', Error: '%s'",
                    buildPayload("sendfrom", params), responseStr,
                    parseError(responseStr))));
        }
        return transactionId;
    }

    private static final String buildPayload(String method, String params) {
        return String
                .format("{\"jsonrpc\": \"1.0\", \"id\":\"rpc\", "
                        + "\"method\": \"%s\", " + "\"params\": [%s] }",
                        method, params);
    }

    private static final String parseArrayResult(String response)
            throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            String[] resultValues = mapper.readValue(json.get("result"),
                    String[].class);
            return resultValues[0];
        } catch (Exception e) {
            throw new RuntimeException(String.format(
                    "Error parsing response '%s' : %s", response,
                    e.getMessage()));
        }
    }

    private static final String parseResult(String response) throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            String resultValue = mapper.readValue(json.get("result"),
                    String.class);
            return resultValue;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static final String parseError(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            String resultValue = mapper.readValue(json.get("error"),
                    String.class);
            return resultValue;
        } catch (Exception e) {
            return "";
        }
    }
}
