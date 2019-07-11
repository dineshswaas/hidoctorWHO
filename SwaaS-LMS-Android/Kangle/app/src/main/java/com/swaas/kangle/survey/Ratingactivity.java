package com.swaas.kangle.survey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.swaas.kangle.R;

import java.util.ArrayList;

public class Ratingactivity extends AppCompatActivity {
    RelativeLayout layout;
    Button button;
    Context mContext = Ratingactivity.this;
    MyListAdapter myListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_activity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        layout =  (RelativeLayout) findViewById(R.id.linear);
        ArrayList<String> schoolbag = new ArrayList<>();
        schoolbag.add(" Your interaction with our staff ");
        schoolbag.add(" The services we provide");
        schoolbag.add(" Delivery options offered");
        schoolbag.add(" Condition of the package delivered to you.");
        schoolbag.add(" Quality of the product and service ");
        schoolbag.add(" Cost of product and delivery");
        schoolbag.add(" Customer support response");
        final AlertDialog alert = new myCustomAlertDialog(this , R.style.DialogTheme,schoolbag);
        alert.show();

    }

    private class myCustomAlertDialog extends AlertDialog {

        protected myCustomAlertDialog(final Context context, int dialogTheme, ArrayList<String> schoolbag) {
            super(context,dialogTheme);

            View view = LayoutInflater.from(context).inflate(R.layout.rating_view,null);
            setView(view);

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listview);
            MyListAdapter adapter = new MyListAdapter(schoolbag);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
            Button ok = (Button) view.findViewById(R.id.ok);
            setCancelable(false);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    Ratingactivity.this.finish();
                }
            });
        }

    }
}
