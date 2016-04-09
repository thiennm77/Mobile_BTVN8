package com.example.qlhs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private SQLiteDatabase db;
    private Spinner spnLop;
    private Button btnTai;
    private ListView lstHocSinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDB();

        ArrayList<String> classes = getClasses();

        spnLop = (Spinner) findViewById(R.id.spnLop);
        spnLop.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classes));

        lstHocSinh = (ListView) findViewById(R.id.lstHocSinh);

        btnTai = (Button) findViewById(R.id.btnTai);
        btnTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstHocSinh.removeAllViewsInLayout();

                ArrayList<HocSinh> data = new ArrayList<HocSinh>();
                data.add(new HocSinh("Mã học sinh", "Tên"));

                String className = (String) spnLop.getSelectedItem();
                String query = "select * from HOCSINH, LOP where HOCSINH.MaLop = LOP.MaLop" +
                        " and TenLop = '" + className + "'";
                Cursor cursor = db.rawQuery(query, null);
                cursor.moveToPosition(-1);

                while (cursor.moveToNext()) {
                    String maHS = Integer.toString(cursor.getInt(cursor.getColumnIndex("MaHS")));
                    String tenHS = cursor.getString(cursor.getColumnIndex("TenHS"));
                    data.add(new HocSinh(maHS, tenHS));
                }

                lstHocSinh.setAdapter(new CustomAdapter(getApplicationContext(), data));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


    private void createDB() {
        try {
            File storagePath = getApplication().getFilesDir();
            String myDbPath = storagePath + "/QLHS";
            db = SQLiteDatabase.openDatabase(myDbPath, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);

            db.execSQL("DROP TABLE IF EXISTS LOP;");
            db.execSQL("DROP TABLE IF EXISTS HOCSINH;");

            db.execSQL("create table LOP("
                    + "MaLop integer PRIMARY KEY autoincrement, "
                    + "TenLop text);");

            db.execSQL("create table HOCSINH("
                    + "MaHS integer PRIMARY KEY,"
                    + "TenHS text,"
                    + "MaLop text,"
                    + "foreign key(MaLop) references LOP(MaLop) );");

            db.execSQL("insert into LOP(TenLop) values ('TH2013/01');");
            db.execSQL("insert into LOP(TenLop) values ('TH2013/02');");

            db.execSQL("insert into HOCSINH values (1, 'Dung', 1);");
            db.execSQL("insert into HOCSINH values (2, 'Tan', 2);");
            db.execSQL("insert into HOCSINH values (3, 'Thien', 1);");
            db.execSQL("insert into HOCSINH values (4, 'Toan', 2);");
            db.execSQL("insert into HOCSINH values (5, 'The', 2);");

        } catch (SQLiteException e) {
            Toast.makeText(this.getApplicationContext(), e.getMessage() , Toast.LENGTH_LONG);
        }
    }

    private ArrayList<String> getClasses() {
        ArrayList<String> result = new ArrayList<String>();

        Cursor cur = db.rawQuery("select * from LOP", null);
        cur.moveToPosition(-1);

        while (cur.moveToNext()) {
            result.add(cur.getString(1));
        }
        return result;
    }

    class HocSinh {
        private String maHS;
        private String tenHS;

        public HocSinh(String ma, String ten) {
            maHS = ma;
            tenHS = ten;
        }

        public String getTenHS() {
            return tenHS;
        }

        public String getMaHS() {
            return maHS;
        }
    }
}
