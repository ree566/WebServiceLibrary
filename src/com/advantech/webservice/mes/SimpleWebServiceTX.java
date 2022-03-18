/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.mes;

import java.io.IOException;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
public class SimpleWebServiceTX {

    private WsClient client;

    public void setWsClient(WsClient client) {
        this.client = client;
    }

    public String sendData(String data, UploadType uploadType) throws IOException {

        TxResponse response = client.simpleTxSendAndReceive(data, uploadType);
        return response.getTxResult();

    }

}
