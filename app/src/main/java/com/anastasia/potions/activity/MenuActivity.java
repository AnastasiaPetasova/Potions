package com.anastasia.potions.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anastasia.potions.R;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void play(View v) {
        Intent playIntent = new Intent(this, GameActivity.class);
        startActivity(playIntent);
    }

    public void info(View v) {
        // будет активитей, так как Настя хочет здесь всю инструкцию
        AlertDialog infoDialog = new AlertDialog.Builder(MenuActivity.this)
                .setTitle("Инфа о том, как играть")
                .setMessage("Типа инструкция.\nАвтор: Настя Петасова")
                .setCancelable(true)
                .setPositiveButton("ОК", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();

        infoDialog.show();
    }

    public void exit(View v) {
        AlertDialog exitDialog = new AlertDialog.Builder(MenuActivity.this)
                .setTitle("Выход из приложения. Вы уверены?")
                .setCancelable(false)
                .setNegativeButton("Отмена", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Выход", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MenuActivity.this.finish();
                    }
                }).create();

        exitDialog.show();
    }
}
