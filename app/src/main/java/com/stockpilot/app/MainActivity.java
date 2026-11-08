package com.stockpilot.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout layout;
    EditText store, username, password;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("StockPilot", MODE_PRIVATE);

        if (prefs.getBoolean("registered", false)) {
            showLogin();
        } else {
            showRegistration();
        }
    }

    void setup(String title) {
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(35, 50, 35, 35);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.rgb(12, 22, 45));

        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextSize(30);
        heading.setTextColor(Color.WHITE);
        heading.setGravity(Gravity.CENTER);

        layout.addView(heading);
        setContentView(layout);
    }

    EditText input(String hint) {
        EditText field = new EditText(this);
        field.setHint(hint);
        field.setSingleLine(true);
        field.setTextColor(Color.WHITE);
        field.setHintTextColor(Color.LTGRAY);
        layout.addView(field);
        return field;
    }

    void button(String text, Runnable action) {
        Button b = new Button(this);
        b.setText(text);
        layout.addView(b);
        b.setOnClickListener(v -> action.run());
    }

    void showRegistration() {
        setup("STOCKPILOT");

        store = input("Supermarket / Store Name");
        username = input("Create Username");
        password = input("Create Password");
        password.setInputType(129);

        button("REGISTER", () -> {
            String s = store.getText().toString().trim();
            String u = username.getText().toString().trim();
            String p = password.getText().toString();

            if (s.isEmpty() || u.isEmpty() || p.length() < 6) {
                Toast.makeText(this,
                    "Enter all details. Password must be 6+ characters.",
                    Toast.LENGTH_LONG).show();
                return;
            }

            prefs.edit()
                .putString("store", s)
                .putString("username", u)
                .putString("password", p)
                .putBoolean("registered", true)
                .apply();

            showLogin();
        });
    }

    void showLogin() {
        setup("STOCKPILOT LOGIN");

        username = input("Username");
        password = input("Password");
        password.setInputType(129);

        button("LOGIN", () -> {
            String u = username.getText().toString().trim();
            String p = password.getText().toString();

            if (u.equals(prefs.getString("username", "")) &&
                p.equals(prefs.getString("password", ""))) {
                showDashboard();
            } else {
                Toast.makeText(this,
                    "Incorrect username or password",
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    void showDashboard() {
        setup("STOCKPILOT");

        TextView name = new TextView(this);
        name.setText("Welcome to " +
            prefs.getString("store", "Your Store"));
        name.setTextColor(Color.WHITE);
        name.setTextSize(20);
        layout.addView(name);

        button("☰ MENU", () -> showMenu());
    }

    void showMenu() {
        setup("STOCKPILOT MENU");

        button("Supplier Details", () -> message("Supplier Details"));
        button("Stock Registers", () -> message("Stock Registers"));
        button("Billing", () -> message("Billing"));
        button("Alerts", () -> message("Alerts"));
        button("Logout", () -> showLogin());
    }

    void message(String text) {
        Toast.makeText(this, text + " – Coming next",
            Toast.LENGTH_SHORT).show();
    }
}
