package com.lsj.hdmi.materialtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lsj.hdmi.materialtest.bean.HeaderBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<String> dataSet;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        dataSet=new ArrayList<String>();
        initData();

        recyclerView= (RecyclerView) findViewById(R.id.recycleview);
        toolbar = (Toolbar) findViewById(R.id.mian_toolbar);
        myAdapter=new MyAdapter(dataSet);
        myAdapter.addHeader(new HeaderBean(1,"head",0));



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initData(){
        for (int i=0;i<10;i++){
            dataSet.add(i+"");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mian,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.recycle_add:
                dataSet.add(0,String.valueOf(new Random().nextInt(100)) );
                myAdapter.notifyItemInserted(0);
                break;
            case R.id.recycle_delete:
                if(dataSet.size()>0){
                    dataSet.remove(0);
                    myAdapter.notifyItemRemoved(0);
                }

                break;
            case R.id.recycle_move:
                String outdata=dataSet.remove(3);
                dataSet.add(0,outdata);
                myAdapter.notifyItemMoved(3,0);
                break;
        }

        return true;
    }
}
