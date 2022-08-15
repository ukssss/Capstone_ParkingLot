package com.example.parking;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class FindElapsedTimeTask extends AsyncTask<String, Void, String> {
    Context context;
    String[] arrParametersName = new String[6];
    String[] arrJsonKeys = new String[3];

    public FindElapsedTimeTask(Context context) {
        super();
        this.context = context;
    }

    private String MinuteToSecond(int nSecond) {
        String strText = null;
        try {
            if (nSecond >= 3600) {
                int minute = (nSecond / 3600);
                int second = (nSecond % 3600 / 60);

                strText = String.format(("%d시간 %d분"), minute, second);
            }
            else {
                int second = (nSecond / 60);
                strText = String.format(("%d분"), second);
            }
            return strText;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String time) {
        super.onPreExecute();
        String strTime = MinuteToSecond(Integer.parseInt(time));

        Toast.makeText(context, "시간 : " + strTime + "", Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String[] args) {
        setArrays();

        TMapWebService tMapWebService = new TMapWebService("https://api2.sktelecom.com/tmap/routes");
        tMapWebService.setParameters(arrParametersName, args, 6);
        String totalTime = tMapWebService.connectWebService(arrJsonKeys);

        return totalTime;
    }

    private void setArrays() {
        arrParametersName[0] = "version";
        arrParametersName[1] = "appKey";
        arrParametersName[2] = "startX";
        arrParametersName[3] = "startY";
        arrParametersName[4] = "endX";
        arrParametersName[5] = "endY";

        arrJsonKeys[0] = "features";
        arrJsonKeys[1] = "properties";
        arrJsonKeys[2] = "totalTime";
    }
}
