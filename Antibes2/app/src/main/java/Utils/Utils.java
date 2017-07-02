package Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mycompany.antibes.FeedReaderContract;
import com.mycompany.antibes.FeedReaderDbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by elio-profumo on 02/07/17.
 */
public class Utils {



    public static int month_int_conversion(String month){

        Map<String, Integer> aMap = new HashMap<>();
        aMap.put("Jan",1);
        aMap.put("Feb",2);
        aMap.put("Mar",3);
        aMap.put("Apr",4);
        aMap.put("May",5);
        aMap.put("Jun",6);
        aMap.put("Jul",7);
        aMap.put("Aug",8);
        aMap.put("Sept",9);
        aMap.put("Oct",10);
        aMap.put("Nov",11);
        aMap.put("Dec",12);
        return aMap.get(month);
    }


    public static boolean check_vacances(String day, String month) {
        int day_int = Integer.parseInt(day);
        int month_int=0;
        month_int = month_int_conversion(month);
        int integer_date = month_int*100+day_int;
        int first_down =6+200;
        int first_up=21+200;
        int second_down = 2+400;
        int second_up = 17+400;
        int third_down = 17+1000;
        int third_up =1+1100;

        if (  ((first_down <= integer_date ) & (first_up >= integer_date))
                |((second_down <= integer_date ) & (second_up >= integer_date))
                |((third_down <= integer_date ) & (third_up >= integer_date)))
        {
            return true;
        }
        else
            return false;

    }


    public static boolean check_joursferies(String day, String month) {
        int day_int = Integer.parseInt(day);
        int month_int=0;
        month_int = month_int_conversion(month);
        int integer_date = month_int*100+day_int;
        String[] array_joursferies = {"01/01","28/03","01/05","08/05","05/05","16/05","14/07","15/08","01/11",
                "11/11","25/12"};
        int[] array_joursferies_int= new int[11];
        boolean jourferie=false;
        for (int i=0;i<11;i++) {
            array_joursferies_int[i] = Integer.parseInt(array_joursferies[i].split("/")[0])
                    + 100 * Integer.parseInt(array_joursferies[i].split("/")[1]);

            if (array_joursferies_int[i]==integer_date) {
                jourferie= true;
            }
        }
        return jourferie;
    }



    public static Cursor cursor(FeedReaderDbHelper mDbHelper, String string){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry.STOP,
                FeedReaderContract.FeedEntry.LINE,
                FeedReaderContract.FeedEntry.SCHEDULE,
                FeedReaderContract.FeedEntry.DIRECTION,

        };

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String currentDateTimeString = currentDateandTime;
        String[] currentTime = currentDateTimeString.split(" ");
        String day_new = currentTime[0];
        String month_new = currentTime[1];


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String tableName = checkTable(day, day_new, month_new);


        Cursor cursor = db.query(
                tableName,
                projection,
                FeedReaderContract.FeedEntry.STOP + " = \'" + string + "\'"
                        + " OR " + FeedReaderContract.FeedEntry.STOP + " LIKE \'%-" + string + "-%\'"
                        + " OR " + FeedReaderContract.FeedEntry.STOP + " LIKE \'" + string + "-%\'"
                        + " OR " + FeedReaderContract.FeedEntry.STOP + " LIKE \'" + string + "-%\'", null,
                null,
                null,
                null
        );

        return cursor;
    }

    private static String checkTable(int day, String dayNew, String monthNew){
        boolean vacances_scolaire = Utils.check_vacances(dayNew, monthNew);
        if (day== 7 && vacances_scolaire) {
            return FeedReaderContract.FeedEntry.TABLE_NAME5;
        }
        else if(day==1 && vacances_scolaire){
            return FeedReaderContract.FeedEntry.TABLE_NAME4;
        }
        else if(vacances_scolaire){
            return FeedReaderContract.FeedEntry.TABLE_NAME0;
        }
        else if((day == 1)|(Utils.check_joursferies(dayNew, monthNew))){
            return FeedReaderContract.FeedEntry.TABLE_NAME2;
        }
        else if(day==7){
            return FeedReaderContract.FeedEntry.TABLE_NAME3;
        }
        else {
            return FeedReaderContract.FeedEntry.TABLE_NAME;
        }
    }

    public static ObjectRow getRowElements(Cursor cursor) {

        ObjectRow objectRow = null;
        int iRow = cursor.getColumnIndex(FeedReaderContract.FeedEntry.STOP);
        int iName = cursor.getColumnIndex(FeedReaderContract.FeedEntry.LINE);
        int iSchedule = cursor.getColumnIndex(FeedReaderContract.FeedEntry.SCHEDULE);
        int iDirection = cursor.getColumnIndex(FeedReaderContract.FeedEntry.DIRECTION);


        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        String[] currentTime = currentDateTimeString.split(" ");
        String time = currentTime[3];

        int actualMins;
        int actualHours;

        String[] timeSplitted = time.split(":");
        int seconds = Integer.parseInt(timeSplitted[0]) * 3600 + Integer.parseInt(timeSplitted[1]) * 60
                + Integer.parseInt(timeSplitted[2]);

        String pm = "am";
        try {
            pm = currentTime[4];
        } catch (Exception e) {

        }

        if (pm.equals("pm")) {
            seconds += 3600 * 12;
        }
        actualMins = Integer.parseInt(timeSplitted[1]);
        actualHours = Integer.parseInt(timeSplitted[0]);

        String[] parts = cursor.getString(iSchedule).split(" ");
        int[] times = new int[parts.length];

        int j = 0;
        int flag = -1;
        int[] nextTimes;
        nextTimes = new int[]{-1, -1};

        for (int i = 0; i < parts.length; i++) {
            try {
                String[] partsSplit = parts[i].split(":");
                times[i] = Integer.parseInt(partsSplit[0]) * 3600 + Integer.parseInt(partsSplit[1]) * 60;

                if ((times[i] > seconds) & (j < 2)) {
                    nextTimes[j] = times[i];
                    j += 1;
                    flag += 1;
                }

                if (j == 2) break;
            } catch (NumberFormatException nfe) {
            }
            ;
        }
        String nextTimesChrono = "";
        String newiName = "";
        String newiRow = "";

        int minsNext = 0;
        int hoursNext = 0;
        if (flag > -1) {
            String nextTimesString = "";
            System.out.println(nextTimes);

            for (int i = 0; i < 2; i++) {

                if (nextTimes[i] == -1)
                    break;

                int hours = nextTimes[i] / 3600;
                int mins = (nextTimes[i] - hours * 3600) / 60;

                if (i == 0) {
                    minsNext = mins - actualMins;
                    hoursNext = hours - actualHours;

                    if (pm.equals("pm"))
                        hoursNext -= 12;

                    if (minsNext < 0) {
                        minsNext += 60;
                        hoursNext -= 1;
                    }
                }

                String formattedHours = Integer.toString(hours);
                if (formattedHours.length() == 1)
                    formattedHours = "0" + formattedHours;

                String formattedMins = Integer.toString(mins);
                if (formattedMins.length() == 1)
                    formattedMins = "0" + formattedMins;


                nextTimesChrono += formattedHours + ":" + formattedMins + " ";

                nextTimesString += Integer.toString(nextTimes[i]) + " ";
            }
            newiName += cursor.getString(iName).replace('+', ' ');
            newiRow += cursor.getString(iRow).replace('-', ' ');
            System.out.println("new" + newiRow);
            objectRow = new ObjectRow();
            objectRow.setDirection(cursor.getString(iDirection));
            objectRow.setName(newiName);
            objectRow.setRow(newiRow);
            objectRow.setTime(nextTimesChrono + "(in " + Integer.toString(hoursNext) + " hr " + Integer.toString(minsNext) + " mn)");
        }



        return objectRow;
    }


    public static class ObjectRow{
        private String time;
        private String direction;
        private String name;
        private String row;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRow() {
            return row;
        }

        public void setRow(String row) {
            this.row = row;
        }
    }

}
