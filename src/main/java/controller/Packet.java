package controller;

import client.actions.Action;
import model.ConfigObject;

import java.io.Serializable;

/**
 * Created by pietro on 12/06/16.
 */
public class Packet implements Serializable {

    private String header;

    private Action action;

    private String messageString;

    private Integer configId;

    private ConfigObject configObject;

    private MarketEvent marketEvent;

    private String cityLink;

    public Packet(ConfigObject configObject){
        header="CONFIGOBJECT";
        this.configObject=configObject;
    }
    
    public Packet() {
    	header="BOARDSTATUS";
    }

    public Packet(Action action){
        header="ACTION";
        this.action=action;
    }

    public Packet(String city1, String city2,String typeOfAction){
        if(typeOfAction.equals("ADD"))
        header="ADDLINK";
        else header="REMOVELINK";
        cityLink=city1+" "+city2;
    }

    public Packet(String messageString){
        header="MESSAGESTRING";
        this.messageString=messageString;
    }

    public Packet(Integer configId){
        header="CONFIGID";
        this.configId=configId;
    }


    public Packet(MarketEvent marketEvent){
        header="MARKET";
        this.marketEvent=marketEvent;
    }

    public String getHeader() {
        return this.header;
    }

    public Action getAction() {
        return this.action;
    }

    public String getMessageString(){
        return this.messageString;
    }

    public int getConfigId() {
        return this.configId;
    }

    public MarketEvent getMarketEvent() {
        return this.marketEvent;
    }
}
