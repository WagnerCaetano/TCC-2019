package com.example.androidslidee;

public class Wireless {

    private String IpAddress="";
    public Wireless(String IpAddress)
    {
        this.IpAddress = IpAddress;
        testConection();
    }
    private void testConection() {
        run();
    }
    public void run() {
        //we create a TCPClient object
        TcpClass mTcpClient = new TcpClass(new TcpClass.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                System.out.println(message);
            }
        },IpAddress);
        mTcpClient.run();
        if (mTcpClient != null) {
            mTcpClient.sendMessage("testing");
        }
    }
}
