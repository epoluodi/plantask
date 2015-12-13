package Common;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.epoluodi.plantask.R;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 14-10-7.
 */
public class Common {
    public final static String SCAN_ACTION = "scanseuic";//扫描结束action
//    com.android.server.scannerservice.broadcast


    public final static Object olock = new Object();
    public static String ServerIP;
    public static String ServerWCF;
    public static Context Appcontext;
    //popwindows方法
    static PopupWindow popupWindow;
    public static View popview;

    public static String projectcode="";


    public static String GetconfigServer()
    {
        try {
            File file = new File(Environment.getExternalStorageDirectory()+"/zhongyuan/config.json");
            if (!file.exists())
                return "";
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            String strbuffer = EncodingUtils.getString(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(strbuffer);
            return jsonObject.get("server").toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }


    //显示popwindows
    public static void ShowPopWindow(View v, LayoutInflater inflater, String text) {


        popview = inflater.inflate(R.layout.popwindows, null);

        ((TextView) popview.findViewById(R.id.poptext)).setText(text);
        popupWindow = new PopupWindow(popview, 330, 120);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.Animationpopwindows);
        popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);

    }

    //设置popwindows中的文本
    public static void Setpoptext(String text) {
        ((TextView) popview.findViewById(R.id.poptext)).setText(text);
    }

    //关闭POPwindows
    public static void CLosePopwindow() {
        popupWindow.dismiss();
    }

    public static NotificationManager getNotificationManager() {
        return (NotificationManager) Appcontext.getSystemService(Context.NOTIFICATION_SERVICE);
    }






    public static String GetSysTime()
    {
        SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

    public static String GetSysTimeshort()
    {
        SimpleDateFormat sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd");
        String date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

    public static String GetSysOnlyTime()
    {
        SimpleDateFormat sDateFormat   =   new SimpleDateFormat("HH:mm:ss");
        String date   =   sDateFormat.format(new   java.util.Date());
        return date;
    }

}
