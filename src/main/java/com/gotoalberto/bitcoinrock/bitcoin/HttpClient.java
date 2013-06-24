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

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * @author gotoalberto
 */
class HttpClient {

    private final WebResource resource;

    HttpClient(String endPoint, int port, String user, String password) {
        Client client = Client.create();
        HTTPBasicAuthFilter authFilter = new HTTPBasicAuthFilter(user, password);
        client.addFilter(authFilter);
        this.resource = client.resource(String.format("http://%s:%s", endPoint,
                port));
    }

    public String post(String payload) {
        ClientResponse response = this.resource.type(MediaType.TEXT_PLAIN_TYPE)
                .post(ClientResponse.class, payload);
        return response.getEntity(String.class);
    }

}
