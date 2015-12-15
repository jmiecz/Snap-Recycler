package com.swipetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.snaprecycler.ColumnHandler;
import com.snaprecycler.SnapRecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SnapRecyclerView swipeRecycler = (SnapRecyclerView)findViewById(R.id.swipeRecylcer);
        swipeRecycler.setColumnHandler(new ColumnHandler.Builder().setVisibleItemCount(3)
                                            .setPaddingLeft(20)
                                            .setPaddiingRight(20)
                                            .setPercentToShowOfOffViews(.5f)
                                            .build());

        swipeRecycler.setAdapter(new SwipeAdapter());

    }

}
