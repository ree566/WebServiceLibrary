/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.webaccess;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author MFG.ESOP
 */
@Component
public class WebAccessRESTful {

    @Autowired
    private WebAccessClient client;

    public String setTagValue(Map m) {
        return client.sendRequest("/Json/SetTagValue/DongHuSystem", m, String.class);
    }
}
