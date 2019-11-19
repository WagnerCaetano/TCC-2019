package com.example.androidslidee;

public class Wireless {
    private String IpAddress="";
    private TcpClass mTcpClient;

    public Wireless(String IpAddress)
    {
        this.IpAddress = IpAddress;
        testConection();
    }
    private void testConection() {
        mTcpClient = new TcpClass(new TcpClass.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                //this method calls the onProgressUpdate
                System.out.println(message);
            }
        }, IpAddress);
        mTcpClient.run();
        if (mTcpClient != null) {
            mTcpClient.sendMessage("testing");
            mTcpClient.stopClient();
        }
    }

    public void sendCursor(int x , int y)
    {
        mTcpClient.sendMessage("CURSOR\n"+x+"\n"+y);
    }
    public void avancar()
    {
        mTcpClient.sendMessage("AVANCAR");
    }
    public void recuar()
    {
        mTcpClient.sendMessage("RECUAR");
    }



}
