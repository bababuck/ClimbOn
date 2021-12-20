package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PanelViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* */
        UniversalData saved_data = ((ClimbOnApplication) this.getApplication()).data;
        Panel current_panel = saved_data.panels.get(saved_data.current_panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_viewer);
    }
}