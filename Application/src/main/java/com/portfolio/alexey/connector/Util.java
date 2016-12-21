package com.portfolio.alexey.connector;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.actionbarcompat.listpopupmenu.Marker;
import com.example.android.actionbarcompat.listpopupmenu.R;
import com.example.android.actionbarcompat.listpopupmenu.RunDataHub;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by lesa on 17.12.2016.
 */

public class Util {
    static final String TAG = "Util";
    public  final static String EXTRAS_NAME = "EXTRAS_NAME";
    public  final static String EXTRAS_NAME_FILTR = "EXTRAS_NAME_FILTR";
    public  final static String EXTRAS_ADDRESS = "EXTRAS_ADDRESS";
    public  final static String EXTRAS_ITEM = "EXTRAS_ITEM";
    public  final static String EXTRAS_LABEL = "EXTRAS_LABEL";
    public  final static String EXTRAS_MAX = "EXTRAS_MAX";

    public  final static String EXTRAS_BAR_TITLE = "EXTRAS_BAR_TITLE";
    public  final static String EXTRAS_FLOAT_MIN = "EXTRAS_FLOAT_MIN";
    public  final static String EXTRAS_FLOAT_MAX = "EXTRAS_FLOAT_MAX";

   // public  final static String EXTRAS_FLOAT_1 = "EXTRAS_FLOAT_1";

//    public  final static String EXTRAS_FLOAT_2 = "EXTRAS_FLOAT_2";
//    public  final static String EXTRAS_FLOAT_3 = "EXTRAS_FLOAT_3";
//
//    public  final static String EXTRAS_INT_1 = "EXTRAS_INT_1";
//    public  final static String EXTRAS_INT_2 = "EXTRAS_INT_2";
//    public  final static String EXTRAS_INT_3 = "EXTRAS_INT_3";

    //ввод ФЛОАТ из строки В ЛЮБОЙ ЛОКАЛИ!!!(обрабатывает 10 разделитель ТОЧКУ или ЗАПЯТУЮ!!)
    // при обломе НУЛЛ возвращяет
    static public Float parseFloat(String str) {
        Float value = null;
        if ((str == null) || (str.length() == 0)) return value;
        //
        try {
            value = Float.parseFloat(str);
        } catch (Exception e) {
            Log.e(TAG, "parseFloat> Float.valueOf(str) ERR= " + e);
            //НЕ cовпадение локалей, проблема - 10 разделитель "." или ","
            // просто меняем на обратное
            //Log.i(TAG, "parseFloat> str= " + str);
            if (str.contains(",")) str = str.replace(",", ".");
            else str = str.replace(".", ",");
            //
            //   Log.v(TAG, " str= " + str.replace(",",".") +" / " +str.replace(".",","));
            try {
                value = Float.parseFloat(str);
            } catch (Exception e1) {
                Log.e(TAG, "parseFloat> Float.valueOf(str) ERR= " + e1);
            }
        }
        return value;
    }
    //
    static public Sensor getSensor(int item,Context context){
        BluetoothLeServiceNew bleS = getAppBleService(context);
        if(isNoNull(bleS,bleS.mbleDot) && (bleS.mbleDot.size() > item)){
            return bleS.mbleDot.get(item);
        }
        return null;
    }
    //
    static public Sensor getSensor(int item,Activity activity){
        BluetoothLeServiceNew bleS = getAppBleService(activity);
        if(isNoNull(bleS,bleS.mbleDot) && (bleS.mbleDot.size() > item)){
            return bleS.mbleDot.get(item);
        }
        return null;
    }
    //
    static public ArrayList<Sensor> getListSensor(Context context){
        BluetoothLeServiceNew bleS = getAppBleService(context);
        if(bleS != null)  return bleS.mbleDot;
        return null;
    }
    //
    static public ArrayList<Sensor> getListSensor(Activity activity){
        BluetoothLeServiceNew bleS = getAppBleService(activity);
        if(bleS != null) return bleS.mbleDot;
        return null;
    }
    //
    static public BluetoothLeServiceNew getAppBleService(Context context){
        RunDataHub app = (RunDataHub)context;
        if(isNoNull(app,app.mBluetoothLeServiceM)){
            return app.mBluetoothLeServiceM;
        }
        return null;
    }
    //
    static public BluetoothLeServiceNew getAppBleService(Activity activity){
        RunDataHub app = (RunDataHub) activity.getApplicationContext();
        if(isNoNull(app,app.mBluetoothLeServiceM)){
            return app.mBluetoothLeServiceM;
        }
        return null;
    }
    //// https://geektimes.ru/post/232885/-------------------------------------------
    static public void playerVibrator(int milsec, Activity activity){
        Vibrator  vibrator = (Vibrator) activity.getSystemService (VIBRATOR_SERVICE);
        try {
            vibrator.vibrate(milsec);
        }catch (Exception e){
           ;// Log.e(TAG, " vibrator ERR= " + e);
        }
    }
    //----------------------------------------------------------------------------
    // https://geektimes.ru/post/232885/
// Что бы звук не был тихим:
// stackoverflow.com/questions/8278939/android-mediaplayer-volume-is-very-low-already-adjusted-volume
    //пример: playerRingtone(0.8f, null); рингтонг 0.8 от макимума звука и мелодия по умолчанию
    //пример: playerRingtone(0f, Uri); ГРОМКОСТ звука СИТЕМНОЙ настройки проигрывателя и мелодия по Uri
    static public void playerRingtone(Float setVolume, String uriRingtone , Activity activity,String  tag){
        Uri uri= null;
        if(uriRingtone != null){
            // TODO: 17.12.2016 обработать возможные ИСКЛЮЧЕНИЯ парсера
            uri = Uri.parse(uriRingtone);
        }
        playerRingtone(setVolume, uri ,activity,tag);
    }
    static boolean onRingtoneWork = false;
    static public void playerRingtone(Float setVolume, Uri uriRingtone , Activity activity,String  tag){
        if(onRingtoneWork) return;//если чет о играем, то пока НЕ закончим, новй играть НЕ будем
        onRingtoneWork = true;
        if(uriRingtone == null){// Сигнал по умолчанию
            uriRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        MediaPlayer mediaPlayer= new MediaPlayer();
        if(setVolume == 0){//ГРОМКОСТЬ системных настроек
            setVolume = 1f;
        }else{
            // (Завист только от setVolume)НА ПОЛНУЮ гмкость ВНЕ зависмости от УСТАНОВКИ в СИСТЕМЕ!!!
//http://stackoverflow.com/questions/8278939/android-mediaplayer-volume-is-very-low-already-adjusted-volume
            AudioManager amanager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            // int maxVolume = amanager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            int maxVolume = amanager.getStreamVolume(AudioManager.STREAM_ALARM);
            amanager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM); // this is important.
        }
        //устанавливает ОТ максимума!!!
        mediaPlayer.setVolume(setVolume, setVolume);
        try {
            mediaPlayer.setDataSource(activity.getApplicationContext(), uriRingtone);
            //  mediaPlayer.setLooping(looping);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                //после проигрывания попадает сюда
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //останавливается ПЛЕЕР и выбрасывается из памяти
                    mp.release();//Это закончит, освободить, отпустить
                    onRingtoneWork = false;//разрешили другим вызывать музыку
                }
            });
        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "Error default media ", Toast.LENGTH_LONG).show();
            Log.e(tag, "  Ringtone ERR= " + e);
        }

    }


    //android.support.v7.app.ActionBar
    // getSupportActionBar();??--это решалось в другом методе(getDelegate().getSupportActionBar();)
    static public android.support.v7.app.ActionBar setSupportV7appActionBar(
            android.support.v7.app.ActionBar actionBar,String  tag,String  title) {
        if(actionBar == null){
            Log.e(tag, "actionBar == null--");
            return actionBar;
        }
      //  android.support.v7.app.ActionBar actionBar = getSupportActionBar();
//установить картинку
        actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp); //gtab.setIcon(R.drawable.ic_add);
        //разрешить копку доиой
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //
        //actionBar.setDisplayUseLogoEnabled(true);
        //-- срабатывают только если вместе, отменяют ИКОНКУ, если заменить- достаточно одного
        actionBar.setIcon(null);//actionBar.setIcon(R.drawable.ic_language_black_24dp);
        actionBar.setDisplayUseLogoEnabled(false);
        //
        actionBar.setTitle(title);
        actionBar.setDisplayShowTitleEnabled(true);
        //
        Log.d(tag, "actionBar != null--");
        return actionBar;
    }


    static public boolean setActionBar(ActionBar actionBar,String  tag,String  title) {
        if(actionBar == null){
            Log.e(tag, "actionBar == null--");
            return false;
        }
       // ActionBar actionBar = getActionBar();//getSupportActionBar();??--это решалось в другом методе(getDelegate().getSupportActionBar();)
        //вместо ЗНачка по умолчанию, назначаемого выше, подставляет свой
        // actionBar.setHomeAsUpIndicator(R.drawable.ic_navigate_before_black_24dp);
        //------------------------------
        //  actionBar.
        //разрешить копку доиой
        actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);//устанавливает надпись и иконку как кнопку домой(не требуется
        // actionBar.setDisplayUseLogoEnabled(true);
        //    actionBar.setHomeButtonEnabled(true); метод - actionBar.setDisplayHomeAsUpEnabled(true);)
        //чето не показыввет ее
//-- срабатывают только если вместе, отменяют ИКОНКУ, если заменить- достаточно одного
        actionBar.setIcon(null);//actionBar.setIcon(R.drawable.ic_language_black_24dp);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setTitle(title);
        actionBar.setDisplayShowTitleEnabled(true);
//------------------------------------------------------
        //actionBar.setCustomView(null);
        //actionBar.setLogo(null);
        Log.d(tag, "actionBar != null--");
        return true;
    }
    //заносит данные во вювер с на МИНУС работаем с уровнями и,
    // А ТАКЖЕ- если там тоже самое- не записывает в Вювер ,эеономия времени ОТРИСОВКИ
    static public boolean setLevelToImageView(int level, View view) {
        if (isNoNull(level, view) && (view instanceof ImageView)) {
            //Отрицательное число уровеня- ломает вывод ИЗОБРАЖЕНИЯ по уровням
            if (level < 0) level = ~level + 1;
            ImageView iv = ((ImageView) view);
            //--
            if((iv.getTag() == null) || ((Integer)iv.getTag() != level)){
                iv.setImageLevel(level);
                iv.setTag(level);
            }
            return true;
        }
        return false;
    }
    //ПОИСК глобадьный по АКТИВНОСТИ- берет ПЕРВЫЙ попашийся элемент!
    static public boolean setLevelToImageView(int level, int viewID, Activity activity) {
        return setLevelToImageView(level,  activity.findViewById(viewID));
    }
    ////ПОИСК ЛОКАЛЬНЫЙ внутри  viewRoot- берет ПЕРВЫЙ попашийся элемент!
    static public boolean setLevelToImageView(int level, int viewID, View viewRoot) {
        return setLevelToImageView(level,  viewRoot.findViewById(viewID));
    }
    //--------------------------------------------------------------------------------
    //заносит данные во вювер с на МИНУС работаем с уровнями и,
    // А ТАКЖЕ- если там тоже самое- не записывает в Вювер ,эеономия времени ОТРИСОВКИ
    static public boolean setDrawableToImageView(int level, View view) {
        if (isNoNull(level, view) && (view instanceof ImageView)) {
            ImageView iv = ((ImageView) view);
            //если ранее не запоминали то обновляем
            if((iv.getTag() == null) || ((Integer)iv.getTag() != level)){
                iv.setBackgroundResource(Marker.getIdImg( level));
                iv.setTag( level);//чтоб не перезаписывать
            }
            return true;
        }
        return false;
    }
    //ПОИСК глобадьный по АКТИВНОСТИ- берет ПЕРВЫЙ попашийся элемент!
    static public boolean setDrawableToImageView(int level, int viewID, Activity activity) {
        return setDrawableToImageView(level,  activity.findViewById(viewID));
    }
    ////ПОИСК ЛОКАЛЬНЫЙ внутри  viewRoot- берет ПЕРВЫЙ попашийся элемент!
    static public boolean setDrawableToImageView(int level, int viewID, View viewRoot) {
        return setDrawableToImageView(level,  viewRoot.findViewById(viewID));
    }
    //-------------------------------------------------------------------------------
    //заносит данные во вювер с контролем на НУЛЛ и длинну строки,
    // А ТАКЖЕ- если там тоже самое- не записывает в Вювер текст,эеономия времени ОТРИСОВКИ
    static public boolean setTextToTextView(String str, View view, String def){
        if((view != null) && (view instanceof TextView) && ((str != null) || (def != null))){
            if(str == null) str = def;
            else {
                if(str.length() > 64) str = str.substring(0,63);
            }
            // А ТАКЖЕ- если там тоже самое- не записывает в Вювер текст,эеономия времени ОТРИСОВКИ
            if(((TextView)view).getText().toString().compareTo(str) != 0){
                ((TextView)view).setText(str);
            }
            return true;
        }
        return false;
    }
//ПОИСК глобадьный по АКТИВНОСТИ- берет ПЕРВЫЙ попашийся элемент!
    static public boolean setTextToTextView(String str, int viewID, Activity activity) {
        return setTextToTextView(str,activity.findViewById(viewID),null);
    }

    static public boolean setTextToTextView(String str, int viewID, Activity activity, String def) {
        return setTextToTextView(str, activity.findViewById(viewID),def);
    }

////ПОИСК ЛОКАЛЬНЫЙ внутри  viewRoot- берет ПЕРВЫЙ попашийся элемент!
    static public boolean setTextToTextView(String str, int viewID, View viewRoot) {
        return setTextToTextView(str,viewRoot.findViewById(viewID),null);
    }

    static public boolean setTextToTextView(String str, int viewID,  View viewRoot, String def) {
        return setTextToTextView(str, viewRoot.findViewById(viewID),def);
    }
    ///-------------------------------------------
    static public boolean isNoNull(Object ob1) {
        return ((ob1 != null) );
    }
    static public boolean isNoNull(Object ob1, Object ob2) {
        return ((ob1 != null) && (ob2 != null));
    }
    static public boolean isNoNull(Object ob1, Object ob2, Object ob3) {
        return ((ob1 != null) && (ob2 != null) && (ob3 != null));
    }
}
