/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionbarcompat.listpopupmenu;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
//import android.app.FragmentManager;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.portfolio.alexey.connector.Util;

/**
 * This sample shows you how to use {@link android.support.v7.widget.PopupMenu PopupMenu} from
 * ActionBarCompat to create a list, with each item having a dropdown menu.
 * <p>
 * The interesting part of this sample is in {@link PopupListFragment}.
 *
 * This Activity extends from {@link ActionBarActivity}, which provides all of the function
 * necessary to display a compatible Action Bar on devices running Android v2.1+.
 */
//
// LIB !!=> http://developer.alexanderklimov.ru/android/theory/appcompat.php
//fragments-> https://github.com/codepath/android_guides/wiki/creating-and-using-fragments
public class MainActivity extends AppCompatActivity {// ActionBarActivity {
    public  final static String TAG = "MAIN";
    public  final static String EXTRAS_DEVICE_NAME = "EXTRAS_DEVICE_NAME";
    public  final static String EXTRAS_DEVICE_NAME_FILTR = "EXTRAS_DEVICE_NAME_FILTR";
    public  final static String EXTRAS_DEVICE_ADDRESS = "EXTRAS_DEVICE_ADDRESS";
    public  final static String EXTRAS_DEVICE_ITEM = "EXTRAS_DEVICE_ITEM";

 //ОБЯЗАТЕЛЬНО !! ввести в практику передачу тила для порожденного окна!!
    public  final static String EXTRA_BAR_TITLE = "EXTRA_BAR_TITLE";

    public  final static int MAINACTIVITY = 11111;
    public final  static int ACTIVITY_SETTING_SETTING = 22222;
    public final  static int ACTIVITY_SETTING_MAKER = 444444;
    public final  static int ACTIVITY_FIND_DEVICE = 555555;

    private  final   int mainIdFragment = R.id.mainFragment;

    public PopupListFragment popupListFragment;
    //----------
//    Анимация Floating Action Button в Android
//    https://geektimes.ru/company/nixsolutions/blog/276128/
//    Design
//    Downloads!
//    https://developer.android.com/design/downloads/index.html
//    Design
//    Action Bar
//    https://developer.android.com/design/patterns/actionbar.html
//    Настройка ActionBar — панели действий
//    http://www.fandroid.info/nastrojka-paneli-dejstvij-actionbar/
    //
//    Android Design Support Library — поддержка компонентов Material Design в приложениях с Android 2.1 до Android 5+ (с примерами)
//    http://www.fandroid.info/android-design-support-library-podderzhka-komponentov-material-design-v-prilozheniyah-s-android-2-1-do-android-5-s-primerami/
//
//    Настройка ActionBar вкладки на Android 4
//    http://ru.androids.help/q11418
//    https://www.youtube.com/watch?v=NYVcfa6Bke4
//
//    Меню
//    https://developer.android.com/guide/topics/ui/menus.html#context-menu
//
//    Menu Resource
//    https://developer.android.com/guide/topics/resources/menu-resource.html
    //Android Design Support Library — поддержка компонентов Material Design в приложениях с Android 2.1 до Android 5+ (с примерами)
    // http://www.fandroid.info/android-design-support-library-podderzhka-komponentov-material-design-v-prilozheniyah-s-android-2-1-do-android-5-s-primerami/

    //  программное создоние и подключения слоя
    //  https://github.com/codepath/android_guides/wiki/creating-and-using-fragments
    //http://developer.alexanderklimov.ru/android/theory/layout.php
    //Экземпляр фрагмента связан с активностью. Активность может вызывать методы фрагмента
    // через ссылку на объект фрагмента. Доступ к фрагменту можно получить через
    // методы findFragmentById() или findFragmentByTag().
    // Фрагмент в свою очередь может получить доступ к своей активности через
    // метод Fragment.getActivity().
    //--
    //взаимодействие АКТИВНОсТИ и фрагмента, вызов явно метода из фрагмента, по ссылке на него!
    //там же взаимодействи обратное, работа с АкшионБар и КНОПКА НАЗАД!
    // http://developer.alexanderklimov.ru/android/theory/fragments.php

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.sample_main);
        setContentView(R.layout.sample_main);
        //на 2 секунды показываем заставку релсиба --------------
        //-------------------------------------------------------
        RunDataHub app = ((RunDataHub) getApplicationContext());
        if(app == null)finish();
        //------------------------------
        app.mainActivity = this;
        Log.e(TAG,"--app != null");
        //
        if(app.getStartApp()){// "ЭТО первый запуск
            //прячем наш бар на время
            getSupportActionBar().hide();
             //заставка релсиба --------------
            Util.changeFragment(mainIdFragment, new  FragmentHeadBand()
                    , getSupportFragmentManager());
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //сбрасываем первый пуск
                    // гасим сразу, чтоб не дергалось изображение
                    findViewById(mainIdFragment).setVisibility(View.GONE);
                    ((RunDataHub) getApplicationContext()).resetStartApp();
                    setWork();
                }
            }, 3000);
        }else {
            setWork();
        }
        Log.e(TAG, "----onCreate END-----");
    }
    private void setWork(){
       // // установка ИЗОБРАЖЕНИЕ на всь экран, УБИРАЕМ СВЕРХУ И СНИЗУ панели системные
        findViewById(mainIdFragment).getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        //настраиваем и включаем тулбар
        Util.setSupportV7appActionBar(getSupportActionBar(),TAG,"  B4/B5 v2.4");

        Util.changeFragment(mainIdFragment, new PopupListFragment()
                , getSupportFragmentManager());

//        popupListFragment = new PopupListFragment();
//        Util.changeFragment(mainIdFragment, popupListFragment
//                , getSupportFragmentManager());
        //ОБЯЗАТЕЛЬНО, ВВерху выключаем!!! видимость, здесь надо включить!!
        findViewById(mainIdFragment).setVisibility(View.VISIBLE);
    }
    //инициализация фрейма
    public void init(){
        Log.e(TAG, "----init() ----------");
        RunDataHub app = ((RunDataHub) getApplicationContext());
        if((app != null) && (app.mBluetoothLeServiceM != null)){
            if(app.mBluetoothLeServiceM.initialize()) {
                Log.e(TAG, "----init() ---------- OK OK");

            } else{
                Log.e(TAG, "----init() ---------- ERROR!");
            }
        }
 //!!       popupListFragment.initList();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "----onResume() ----------");
        RunDataHub app = ((RunDataHub) getApplicationContext());
        //а первый запуск показываем заставку несколько секунд, там и потом убираем системный бар
        // в случа нуля и если мы не первый раз уже просыпаемся то тогда надо убират
        // установка ИЗОБРАЖЕНИЕ на всь экран, УБИРАЕМ СВЕРХУ И СНИЗУ панели системные
        if((app == null) || (app.getStartApp() == false)){
            findViewById(R.id.mainFragment).getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

//Develop API Guides User Interface Меню
    // https://developer.android.com/guide/topics/ui/menus.html#context-menu
final int iconActionEdit = 12345678;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
 //       MenuInflater menuInflater= getMenuInflater();
 //       menuInflater.inflate(R.menu.poplist_menu,menu);
        // inflater.inflate(R.menu.myfragment_options, menu);
       //пока отключил редактирование НЕ к чему, ДА программно ПОРОЖДАЯ- встает в нужном месте
//        menu.add(Menu.NONE,iconActionEdit,Menu.NONE,"Edit")
//                .setIcon(R.drawable.ic_clear_black_24dp)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    @Override//сюда прилетают ответы при возвращении из других ОКОН активити
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        Log.v(TAG,"onActivityResult requestCode= " + requestCode + "   resultCode= " +resultCode);
//        if (requestCode == MAINACTIVITY && resultCode == Activity.RESULT_CANCELED) {
//            finish();
//            return;
//        }
        if (requestCode == MAINACTIVITY && resultCode == Activity.RESULT_OK) {
            Log.w(TAG,"NAME= " + data.getStringExtra(EXTRAS_DEVICE_NAME)
            +"   EXTRAS_DEVICE_ADDRESS= " + data.getStringExtra(EXTRAS_DEVICE_ADDRESS));
            //запуск на коннект!!
            RunDataHub app = ((RunDataHub) getApplicationContext());
            if(app.mBluetoothLeServiceM != null){
                app.mBluetoothLeServiceM.connect(data.getStringExtra(EXTRAS_DEVICE_ADDRESS),true);
            }
//!!            if(mBluetoothLeServiceM != null){
//!!                mBluetoothLeServiceM.connect(data.getStringExtra(EXTRAS_DEVICE_ADDRESS),true);
//!!            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onScanDevice(int i){
        final Intent intent = new Intent(this, DeviceScanActivity.class);
          intent.putExtra(MainActivity.EXTRAS_DEVICE_ITEM, i);
        //  intent.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        Log.i(TAG,"startActivity SCAN");
        startActivityForResult(intent,MAINACTIVITY);//на подклшючение к устройству
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG,"Menu-edit_a  item= " +item );

        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(TAG,"android.R.id.home--");
    // Добавить вызов ЗАСТАВКИ!!релсиба

//
//                RunDataHub app = ((RunDataHub) getApplicationContext());
//                if(app.mBluetoothLeServiceM != null){
//                    app.mBluetoothLeServiceM.settingPutFile();
//                }
                return false;//установили ФАЛШ, чтоб вызов попал в ФРАГМЕНТ, в которм будет обработан!!
            case iconActionEdit:
                Log.i(TAG,"edit-");
//        //вызов активного окна для сканирования
//             //   onScanDevice(1);
//                //-------Setting --
//                final Intent intent = new Intent(this, MainSettingSetting.class);
//                intent.putExtra(MainActivity.EXTRAS_DEVICE_ITEM, 0);
//
//                startActivityForResult(intent,MAINACTIVITY);//

                return true;
            case R.id.edit_a://.new_game_:
                View v =((View)findViewById(R.id.textViewName));
                if(v != null){
                    v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                }
//                FragmentManager fm= getSupportFragmentManager();
//
//                Fragment f =  fm.findFragmentById(R.layout.list_item);
//
//                LayoutInflater lf = getLayoutInflater();
//                Resources r =getResources();

                System.out.println("Menu-edit_a  v=" + v);
                ;

                return true;
            case R.id.add_a://
                //взаимодействие АКТИВНОсТИ и фрагмента, вызов явно метода из фрагмента, по ссылке на него!
                //там же взаимодействи обратное, работа с АкшионБар и КНОПКА НАЗАД!
                // http://developer.alexanderklimov.ru/android/theory/fragments.php
                popupListFragment.addNoInitObject();

                android.app.ActionBar  ab = getActionBar();
                System.out.println("Menu-add_a  ab=" + getActionBar());
                ;return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            Log.e(TAG,"onDestroy() ==============isFinishing() =========== isFinishing() ======");
 //!!           mBluetoothLeServiceM = null;
        } else {
            Log.e(TAG,"onDestroy() -----------WORK ------------- WORK -------------");
           ; //It's an orientation change.
        }
    }
}
