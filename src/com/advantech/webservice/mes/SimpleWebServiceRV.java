/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.mes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.tempuri.RvResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Wei.Cheng
 */
public class SimpleWebServiceRV {
    
    private WsClient client;
    
    public void setWsClient(WsClient client){
        this.client = client;
    }

    //Get data from WebService
    public List<Object> getWebServiceData(String queryString) {
        RvResponse response = client.simpleRvSendAndReceive(queryString);
        RvResponse.RvResult result = response.getRvResult();
        return result.getAny();
    }

    public Document getWebServiceDataForDocument(String queryString) {
        List data = getWebServiceData(queryString);
        return ((Node) data.get(1)).getOwnerDocument();
    }

    public String getFieldValue(String requestXml, String fieldName) {

        List l = this.getWebServiceData(requestXml);

        Document doc = ((Node) l.get(1)).getOwnerDocument();
        String childTagName = fieldName;
        Element rootElement = doc.getDocumentElement();
        String requestQueueName = getString(childTagName, rootElement);
        return requestQueueName;
    }

    public <T> T getMarshalResult(String requestXml, Class<T> type) throws JAXBException {

        List l = this.getWebServiceData(requestXml);
        Document doc = ((Node) l.get(1)).getOwnerDocument();
        //Skip the <diffgr:diffgram> tag, read QryData tag directly.
        Node node = doc.getFirstChild().getFirstChild().getFirstChild();

        return this.unmarshalFromList(node, type);

    }

    public <T extends RvQueryResult> List getMarshalResults(String requestXml, Class<T> type) throws JAXBException {

        List l = this.getWebServiceData(requestXml);

        Document doc = ((Node) l.get(1)).getOwnerDocument();
        //Skip the <diffgr:diffgram> tag, read QryData tag directly.
        Node node = doc.getFirstChild().getFirstChild();

        Object o = this.unmarshalFromList(node, type);
        return o == null ? new ArrayList() : ((T) o).getQryData();

    }

    private <T> T unmarshalFromList(Node node, Class<T> type) throws JAXBException {

        //Unmarshal the data into javaObject.
        JAXBContext jc = JAXBContext.newInstance(type);
        Unmarshaller u = jc.createUnmarshaller();

        return node == null ? null : (T) u.unmarshal(node);
    }

    private String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();
            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }

}
