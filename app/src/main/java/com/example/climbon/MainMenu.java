package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button panel_view = (Button) findViewById(R.id.panel_view);
        panel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PanelView.class);

                view.getContext().startActivity(intent);}
        });

        Button create_wall = (Button) findViewById(R.id.create_wall);
        create_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateWallActivity.class);

                view.getContext().startActivity(intent);}
        });

        Button route_view = (Button) findViewById(R.id.create_route);
        route_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RouteView.class);

                view.getContext().startActivity(intent);}
        });
    }
}