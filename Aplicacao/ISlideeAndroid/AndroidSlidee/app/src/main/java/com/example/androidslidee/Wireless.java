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
        mTcpClient.sendMessage("c\n"+x+"\n"+y);
    }
    public void changePos(int antigo , int novo)
    {
        mTcpClient.sendMessage("p\n"+antigo+"\n"+novo);
    }
    public void avancar()
    {
        mTcpClient.sendMessage("s\na");
    }
    public void recuar()
    {
        mTcpClient.sendMessage("s\nr");
    }
    public void acessar(int i)
    {
        mTcpClient.sendMessage("s\n"+i);
    }



}
