
package com.luojc.weather;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Test extends Activity {
    // 页面控件
    private Spinner spProvince, spCity, spArea;
    private Button btOk, btSearch;
    private EditText etSearch;
    private TextView tv;
    // Adapter将使用的一些列表项
    private List<String> provinceId, cityId, areaId;
    private List<String> provinceName, cityName, areaName;
    // 将传给MainActivity的一些参数
    private String province;
    private String city;
    private String area;
    private String cityCode;
    private String cityCodeName;
    // 数据库管理工具和数据库对象
    private SQLiteDatabase db = null;
    private CityCodeDB mCityCodeDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        init();
        initProvinceSpinner(db);
        btSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String cityName=etSearch.getText().toString();
                cityCode=searchCityCodeByName(db, cityName);
                cityCodeName=cityName;
                if(cityCode!=null){
                    Intent intent=new Intent(Test.this, MainActivity.class);
                    intent.putExtra("cityCode", cityCode);
                    intent.putExtra("cityCodeName", cityCodeName);
                    startActivity(intent);
                }else{
                    tv.setText("该地区不存在");
                }

            }

        });
        btOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Test.this, MainActivity.class);
                intent.putExtra("cityCode", cityCode);
                intent.putExtra("cityCodeName", cityCodeName);
                startActivity(intent);
            }

        });
    }

    /**
     * 初始化全局使用的一些布局控件、数据库、List等
     */
    private void init() {
        spProvince = (Spinner) findViewById(R.id.province);
        spCity = (Spinner) findViewById(R.id.city);
        spArea = (Spinner) findViewById(R.id.area);
        btOk = (Button) findViewById(R.id.btnOK);
        btSearch = (Button) findViewById(R.id.btnSearch);
        tv = (TextView) findViewById(R.id.citycode);
        etSearch = (EditText) findViewById(R.id.search);
        provinceId = new ArrayList<String>();
        cityId = new ArrayList<String>();
        areaId = new ArrayList<String>();
        provinceName = new ArrayList<String>();
        cityName = new ArrayList<String>();
        areaName = new ArrayList<String>();
        mCityCodeDB = new CityCodeDB(Test.this);
        db = mCityCodeDB.getDatabase("data.db");
    }

    /**
     * 根据名字查找对应地区的citycode
     * 
     * @param db
     * @param name
     * @return cityCode
     */
    private String searchCityCodeByName(SQLiteDatabase db, String name) {
        String cityCode = null;
        cityCode = mCityCodeDB.getCityCodeByName(db, name);
        return cityCode;
    }

    /**
     * 根据城市Id初始化下属区域列表，并为该列表绑定adapter和事件监听器 点击时获得该区域对应的城市码及城市名放在全局变量cityCode/cityCodeName中
     * 
     * @param db
     * @param cityId
     */
    private void initAreaSpinner(SQLiteDatabase database, String cityId) {
        Cursor areaCursor = mCityCodeDB.getAllArea(database, cityId);
        if (areaCursor != null) {
            // 因为选择的城市每次都不一样所以要清空列表
            areaId.clear();
            areaName.clear();
            if (areaCursor.moveToFirst()) {

                do {
                    String area_id = areaCursor.getString(areaCursor
                            .getColumnIndex("id"));
                    String area_name = areaCursor.getString(areaCursor
                            .getColumnIndex("name"));
                    areaId.add(area_id);
                    areaName.add(area_name);
                } while (areaCursor.moveToNext());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Test.this,
                    R.layout.myspinner, android.R.id.text1, areaName);
            // 为地区的Spinner设置adapter
            spArea.setAdapter(adapter);
            OnItemSelectedListener listener = new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                    cityCodeName = areaName.get(position);
                    cityCode = mCityCodeDB.getCityCodeById(Test.this.db, areaId.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            };
            // 为地区的Spinner设置监听器
            spArea.setOnItemSelectedListener(listener);

        }
    }

    private void initCitySpinner(SQLiteDatabase database, String provinceId) {
        Cursor cityCusor = mCityCodeDB.getAllCity(database, provinceId);
        if (cityCusor != null) {
            cityId.clear();
            cityName.clear();
            if (cityCusor.moveToFirst()) {
                do {
                    String city_id = cityCusor.getString(cityCusor
                            .getColumnIndex("id"));
                    String city_name = cityCusor.getString(cityCusor
                            .getColumnIndex("name"));
                    cityId.add(city_id);
                    cityName.add(city_name);
                } while (cityCusor.moveToNext());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Test.this,
                R.layout.myspinner, android.R.id.text1, cityName);
        spCity.setAdapter(adapter);

        OnItemSelectedListener listener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                initAreaSpinner(db, cityId.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        };

        spCity.setOnItemSelectedListener(listener);
    }

    private void initProvinceSpinner(SQLiteDatabase database) {
        Cursor provinceCursor = mCityCodeDB.getAllProvince(database);
        if (provinceCursor != null) {
            provinceId.clear();
            provinceName.clear();
            if (provinceCursor.moveToFirst()) {
                do {
                    String province_id = provinceCursor.getString(provinceCursor
                            .getColumnIndex("id"));
                    String province_name = provinceCursor.getString(provinceCursor
                            .getColumnIndex("name"));
                    provinceId.add(province_id);
                    provinceName.add(province_name);
                } while (provinceCursor.moveToNext());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Test.this,
                R.layout.myspinner, android.R.id.text1, provinceName);
        spProvince.setAdapter(adapter);

        OnItemSelectedListener listener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                initCitySpinner(db, provinceId.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        };

        spProvince.setOnItemSelectedListener(listener);

    }

}
