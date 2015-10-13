/**
 * EcisServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.fyps.web.webservice.monternet;

public class EcisServiceLocator extends org.apache.axis.client.Service implements com.fyps.web.webservice.monternet.EcisService {

    public EcisServiceLocator() {
    }


    public EcisServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EcisServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EcisServiceHttpPort
    private java.lang.String EcisServiceHttpPort_address = "http://localhost:8086/ECIS-INF/services/EcisService";

    public java.lang.String getEcisServiceHttpPortAddress() {
        return EcisServiceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EcisServiceHttpPortWSDDServiceName = "EcisServiceHttpPort";

    public java.lang.String getEcisServiceHttpPortWSDDServiceName() {
        return EcisServiceHttpPortWSDDServiceName;
    }

    public void setEcisServiceHttpPortWSDDServiceName(java.lang.String name) {
        EcisServiceHttpPortWSDDServiceName = name;
    }

    public com.fyps.web.webservice.monternet.EcisServicePortType getEcisServiceHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EcisServiceHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEcisServiceHttpPort(endpoint);
    }

    public com.fyps.web.webservice.monternet.EcisServicePortType getEcisServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.fyps.web.webservice.monternet.EcisServiceHttpBindingStub _stub = new com.fyps.web.webservice.monternet.EcisServiceHttpBindingStub(portAddress, this);
            _stub.setPortName(getEcisServiceHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEcisServiceHttpPortEndpointAddress(java.lang.String address) {
        EcisServiceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.fyps.web.webservice.monternet.EcisServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.fyps.web.webservice.monternet.EcisServiceHttpBindingStub _stub = new com.fyps.web.webservice.monternet.EcisServiceHttpBindingStub(new java.net.URL(EcisServiceHttpPort_address), this);
                _stub.setPortName(getEcisServiceHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("EcisServiceHttpPort".equals(inputPortName)) {
            return getEcisServiceHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("EcisService", "EcisService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("EcisService", "EcisServiceHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EcisServiceHttpPort".equals(portName)) {
            setEcisServiceHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
