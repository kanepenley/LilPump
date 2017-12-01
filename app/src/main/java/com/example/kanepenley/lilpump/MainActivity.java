package com.example.kanepenley.lilpump;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter btAdapter = null;
    private final static int REQUEST_ENABLE_BT = 1;
    public BluetoothManager bm = null;
    public SettingsPageFragment sp;
    public List<String> boluses = new ArrayList<>();
    public ArrayAdapter<String> adapter;
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBTState();
        bm = new BluetoothManager();
        bm.ConnectToPump();

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, boluses);

    }

    @Override
    public void onResume(){
        super.onResume();
        bm.ConnectToPump();
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages.
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for a particular page.
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomePageFragment.newInstance();
                case 1:
                    return Rewind.newInstance();
                case 2:
                    return HistoryPageFragment.newInstance();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Bolus";
                case 1:
                    return "Rewind";
                case 2:
                    return "History";
                default:
                    return null;
            }

        }
    }
    private void CheckBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {

        } else {
            if (btAdapter.isEnabled()) {

            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    public void sendToLilPump(View view) {
        //MainActivity tmp = (MainActivity) getActivity();
        EditText editText = findViewById(R.id.editText);
        String str = editText.getText().toString();
        int num = Integer.parseInt(str);
        byte sendbyte = (byte) ((0 | 1 << 7) | (num & 0x7f));
        bm.PumpSend(sendbyte);

        //boluses.add(Calendar.getInstance().getTime().toString() + " " + num);
        adapter.add(Calendar.getInstance().getTime().toString() + " " + num);
    }

    public void Calculate(View view){
        EditText carbsEditText = findViewById(R.id.editText6);
        EditText editText = findViewById(R.id.editText);
        EditText i2cEditText = findViewById(R.id.editText8);
        EditText i2bgEditText = findViewById(R.id.editText7);
        EditText bgEditText = findViewById(R.id.editText5);

        int carbs = Integer.parseInt(carbsEditText.getText().toString());
        int bg = Integer.parseInt(bgEditText.getText().toString());
        int i2c = Integer.parseInt(i2cEditText.getText().toString());
        int i2bg = Integer.parseInt(i2bgEditText.getText().toString());

        int result = (carbs / i2c) + ((bg-120) / i2bg);

        editText.setText(Integer.toString(result));
    }

    public void Rew(View view){
        bm.PumpSend((byte) 0);
    }
}


