package com.misao.exifinterfaceexample;

import java.io.IOException;

import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.TextView;

public class ExifInfoActivity extends Activity {
    public static final String FILE_PATH_KEY = "file_path";

    String attrLATITUDE;
    String attrLATITUDE_REF;
    String attrLONGITUDE;
    String attrLONGITUDE_REF;
    ExifInterface exif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exif);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String filepath = bundle.getString(FILE_PATH_KEY);

            try {
                exif = new ExifInterface(filepath);

                attrLATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                attrLATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                attrLONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                attrLONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

                GeoDegree geoDegreeObj = new GeoDegree(exif);
                String latlong = geoDegreeObj.toString();

                StringBuilder builder = new StringBuilder();

                builder.append("Date & Time: " + getExifTag(exif, ExifInterface.TAG_DATETIME) + "\n");
                builder.append("Flash: " + getExifTag(exif, ExifInterface.TAG_FLASH) + "\n");
                builder.append("Focal Length: " + getExifTag(exif, ExifInterface.TAG_FOCAL_LENGTH) + "\n");
                builder.append("GPS Datestamp: " + getExifTag(exif, ExifInterface.TAG_FLASH) + "\n");
                builder.append("GPS Latitude: " + getExifTag(exif, ExifInterface.TAG_GPS_LATITUDE) + "\n");
                builder.append("GPS Latitude Ref: " + getExifTag(exif, ExifInterface.TAG_GPS_LATITUDE_REF) + "\n");
                builder.append("GPS Longitude: " + getExifTag(exif, ExifInterface.TAG_GPS_LONGITUDE) + "\n");
                builder.append("GPS Longitude Ref: " + getExifTag(exif, ExifInterface.TAG_GPS_LONGITUDE_REF) + "\n");
                builder.append("Latitude and Longitude: " + latlong + "\n");
                builder.append("GPS Processing Method: " + getExifTag(exif, ExifInterface.TAG_GPS_PROCESSING_METHOD) + "\n");
                builder.append("GPS Timestamp: " + getExifTag(exif, ExifInterface.TAG_GPS_TIMESTAMP) + "\n");
                builder.append("Image Length: " + getExifTag(exif, ExifInterface.TAG_IMAGE_LENGTH) + "\n");
                builder.append("Image Width: " + getExifTag(exif, ExifInterface.TAG_IMAGE_WIDTH) + "\n");
                builder.append("Camera Make: " + getExifTag(exif, ExifInterface.TAG_MAKE) + "\n");
                builder.append("Camera Model: " + getExifTag(exif, ExifInterface.TAG_MODEL) + "\n");
                builder.append("Camera Orientation: " + getExifTag(exif, ExifInterface.TAG_ORIENTATION) + "\n");
                builder.append("Camera White Balance: " + getExifTag(exif, ExifInterface.TAG_WHITE_BALANCE) + "\n");


                TextView info = (TextView) findViewById(R.id.exifinfo);

                info.setText(builder.toString());

                builder = null;


            } catch (IOException e) {
                e.printStackTrace();
            }

            setTitle(filepath);
        }
    }

    private String getExifTag(ExifInterface exif, String tag) {
        String attribute = exif.getAttribute(tag);
        return (null != attribute ? attribute : "");
    }

    private void setTitle(String filepath) {
        int pos = filepath.lastIndexOf("/");
        String title = filepath;

        if (-1 != pos) {
            title = filepath.substring(pos + 1);
        }

        super.setTitle("Exif Info: " + title);
    }



    private class GeoDegree {
        private boolean valid = false;
        Float Latitude, Longitude;

        public GeoDegree(ExifInterface exif){

            if((attrLATITUDE !=null)
                    && (attrLATITUDE_REF !=null)
                    && (attrLONGITUDE != null)
                    && (attrLONGITUDE_REF !=null))
            {
                valid = true;

                if(attrLATITUDE_REF.equals("N")){
                    Latitude = convertToDegree(attrLATITUDE);
                }
                else{
                    Latitude = 0 - convertToDegree(attrLATITUDE);
                }

                if(attrLONGITUDE_REF.equals("E")){
                    Longitude = convertToDegree(attrLONGITUDE);
                }
                else{
                    Longitude = 0 - convertToDegree(attrLONGITUDE);
                }

            }
        }

        private Float convertToDegree(String stringDMS){
            Float result = null;
            String[] DMS = stringDMS.split(",", 3);

            String[] stringD = DMS[0].split("/", 2);
            Double D0 = new Double(stringD[0]);
            Double D1 = new Double(stringD[1]);
            Double FloatD = D0/D1;

            String[] stringM = DMS[1].split("/", 2);
            Double M0 = new Double(stringM[0]);
            Double M1 = new Double(stringM[1]);
            Double FloatM = M0/M1;

            String[] stringS = DMS[2].split("/", 2);
            Double S0 = new Double(stringS[0]);
            Double S1 = new Double(stringS[1]);
            Double FloatS = S0/S1;

            result = new Float(FloatD + (FloatM/60) + (FloatS/3600));

            return result;


        };

        public boolean isValid()
        {
            return valid;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return (String.valueOf(Latitude)
                    + ", "
                    + String.valueOf(Longitude));
        }

        public int getLatitude(){
            return (int)(Latitude*1000000);
        }

        public int getLongitude(){
            return (int)(Longitude*1000000);
        }

    }


}