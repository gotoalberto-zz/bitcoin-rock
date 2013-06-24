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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author gotoalberto
 */
@RunWith(MockitoJUnitRunner.class)
public class BitcoinClientTest {

    private BitcoinClient instance;

    @Mock
    private HttpClient httpClient;

    @Before
    public void setUp() {
        this.instance = new BitcoinClient(this.httpClient);
    }

    @Test
    public void getPublicKeyTest() throws Exception {
        String payload = "{\"jsonrpc\": \"1.0\", \"id\":\"rpc\", "
                + "\"method\": \"getaddressesbyaccount\", "
                + "\"params\": [\"test\"] }";
        String response = "{\"result\":[\"19LFZh3czYp8nfdNeuNJpPE"
                + "TvAAbLEht4L\"],\"error\":null,\"id\":\"rpc\"}";
        Mockito.when(this.httpClient.post(payload)).thenReturn(response);
        String publicKey = this.instance.getPublicKey("test");
        Mockito.verify(this.httpClient, Mockito.times(1)).post(payload);
        Assert.assertEquals("19LFZh3czYp8nfdNeuNJpPETvAAbLEht4L", publicKey);
    }

    @Test
    public void getPrivateKeyTest() throws Exception {
        String payload = "{\"jsonrpc\": \"1.0\", \"id\":\"rpc\", "
                + "\"method\": \"dumpprivkey\", "
                + "\"params\": [\"19LFZh3czYp8nfdNeuNJpPETvAAbLEht4L\"] }";
        String response = "{\"result\":\"5KS77srczaJFkvjYHoEE5pNPHHp8FYnS"
                + "SUC8EGx1bYNayQoCQj8\",\"error\":null,\"id\":\"rpc\"}";
        Mockito.when(this.httpClient.post(payload)).thenReturn(response);
        String publicKey = this.instance
                .getPrivateKey("19LFZh3czYp8nfdNeuNJpPETvAAbLEht4L");
        Mockito.verify(this.httpClient, Mockito.times(1)).post(payload);
        Assert.assertEquals(
                "5KS77srczaJFkvjYHoEE5pNPHHp8FYnSSUC8EGx1bYNayQoCQj8",
                publicKey);
    }

    @Test
    public void getBalanceTest() throws Exception {
        String payload = "{\"jsonrpc\": \"1.0\", \"id\":\"rpc\", "
                + "\"method\": \"getbalance\", " + "\"params\": [\"test\",0] }";
        String response = "{\"result\":0.50000000,\"error\":null,\"id\":\"rpc\"}";
        Mockito.when(this.httpClient.post(payload)).thenReturn(response);
        float balance = this.instance.getBalance("test", 0);
        Mockito.verify(this.httpClient, Mockito.times(1)).post(payload);
        Assert.assertEquals(0.5f, balance);
    }

    @Test
    public void createAddressTest() throws Exception {
        String payload = "{\"jsonrpc\": \"1.0\", \"id\":\"rpc\", "
                + "\"method\": \"getnewaddress\", "
                + "\"params\": [\"test\"] }";
        String response = "{\"result\":\"19LFZh3czYp8nfdNeuNJpPETvAAbLEht4L"
                + "\",\"error\":null,\"id\":\"rpc\"}";
        Mockito.when(this.httpClient.post(payload)).thenReturn(response);
        String address = this.instance.createAddress("test");
        Mockito.verify(this.httpClient, Mockito.times(1)).post(payload);
        Assert.assertEquals("19LFZh3czYp8nfdNeuNJpPETvAAbLEht4L", address);
    }

    @Test
    public void sendTest() throws Exception {
        String payload = "{\"jsonrpc\": \"1.0\", \"id\":\"rpc\", "
                + "\"method\": \"sendfrom\", "
                + "\"params\": [\"test\",\"19LFZh3czYp8nfdNeuNJpP"
                + "ETvAAbLEht4L\",0.00040000] }";
        String response = "{\"result\":\"6cee15e6e58b72483a4a7e56"
                + "a8c910937471f9a47ae2bb64d8dbdb3974c20ed3\",\"err"
                + "or\":null,\"id\":\"rpc\"}";
        Mockito.when(this.httpClient.post(payload)).thenReturn(response);
        this.instance.send("test", "19LFZh3czYp8nfdNeuNJpPETvAAbLEht4L",
                0.0004f);
        Mockito.verify(this.httpClient, Mockito.times(1)).post(payload);
    }
}
