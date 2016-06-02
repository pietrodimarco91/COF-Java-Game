package controller;

import java.rmi.Remote;

/**
 * Created by pietro on 01/06/16.
 */
public interface ConnectorInt extends Remote,ClientSideRMIConnectorInt, ServerSideRMIConnectorInt {
    void setMatchHandler(MatchHandler matchHandler);
}
