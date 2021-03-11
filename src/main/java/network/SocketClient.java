package network;



import com.akb.codeanlyzer.network.SocketMQManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SocketClient {
    @Autowired
    SocketMQManager socketMQManager;
    private Socket soc = null;
    private String ipAddress = "";
    private int port = 0;
    private SendMsgThread sendMsgThread;
    public SocketClient(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
        try{
            soc = new Socket(ipAddress, port);
            sendMsgThread = new SendMsgThread(soc);
            new ReceiveMsgThread(soc).start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg){
        sendMsgThread.setMsgStr(msg);
        sendMsgThread.run();
    }


    // 接收消息线程
    class ReceiveMsgThread extends Thread {

        Socket client;

        public ReceiveMsgThread(Socket client) {
            this.client = client;
        }



        public void processMsg(String msg) throws IOException {
            var t = Tools.getMessagePropertyValue(msg, "Type");
            if("382".equals(t)){
                int seq = Integer.parseInt(Tools.getMessagePropertyValue(msg, "seq"));
                String c = Tools.getMessagePropertyValue(msg, "response");
                socketMQManager.response(seq, c);
            }else if("383".equals(t)){
                int seq = Integer.parseInt(Tools.getMessagePropertyValue(msg, "seq"));
                String code = Tools.getMessagePropertyValue(msg, "code");
                if(socketMQManager.getResponse(seq) != null){}
                else if("-1".equals(code)){
                    socketMQManager.response(seq, code);
                }else{
                    String fName =  Tools.getMessagePropertyValue(msg, "fName");
                    List<String> fileList = (List<String>) socketMQManager.get(seq, "fileList");
                    if(fileList == null)
                    {
                        fileList = new ArrayList<>();
                        fileList.add(fName);
                        socketMQManager.set(seq, "fileList", fileList);
                    }else{
                        fileList.add(fName);
                       // System.out.println("Add new File, total = " + fileList.size());
                        if(fileList.size() == Integer.parseInt(Tools.getMessagePropertyValue(msg, "total"))){
                            socketMQManager.response(seq, "200");
                        }
                    }
                }
            }else if("385".equals(t)){
                int seq = Integer.parseInt(Tools.getMessagePropertyValue(msg, "seq"));
                socketMQManager.response(seq, Tools.getMessagePropertyValue(msg, "projects"));
            }
        }

        @Override
        public void run() {
            try {
                byte[] b= null;
                StringBuilder text = null;
                while (true) {
                    try {
                        InputStream in = soc.getInputStream();
                        b = new byte[1024];
                        int len = 0;
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader br = new BufferedReader(isr);
                        String s = "";
                        while (!"".equals(s =  br.readLine())) {
                            String strText = s;
                            if(!strText.contains("<Type>383"))
                                System.out.println("msg from server = " + strText);
                            processMsg(strText);
                        }
                    } catch (IOException e) {
                        // TODO 自动生成的 catch 块
                        e.printStackTrace();
                        this.stop();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    // 发送消息线程
    class SendMsgThread extends Thread {

        Socket response;
        String msgStr = "";

        public void setMsgStr(String msgStr) {
            this.msgStr = msgStr;
        }

        public SendMsgThread(Socket response) {
            this.response = response;
        }

        @Override
        public void run() {
            try {
                BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));

                // tempStr=URLEncoder.encode(tempStr, "UTF-8");//加码
                writer.write(msgStr);
                writer.flush();
                System.out.println("发送成功 " + msgStr);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
