package com.chh.adminshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chh.adminshop.Adapter.MarketingAdapter;
import com.chh.adminshop.Domain.Marketing;
import com.chh.adminshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    LineChartView lineChartView;
    LinearLayout prodManagerBtn, userManagerBtn, signOutBtn;
    private int[] monthly = new int[31];
    FirebaseUser userLogin = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        if(userLogin == null){
            startActivity(new Intent(this,LoginActivity.class));
        }

        init();
        recyclerViewInit();
        handle();
        //testBieudo();
    }

    private void init(){
//        lineChartView = findViewById(R.id.chart);
        prodManagerBtn = findViewById(R.id.prodManagerBtn);
        userManagerBtn = findViewById(R.id.userManagerBtn);
        signOutBtn = findViewById(R.id.signOutBtn);
    }

    private void handle(){
        signOutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        prodManagerBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProductActivity.class)));
        userManagerBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserActivity.class)));
    }

    private void testBieudo(){
        int currentMonth = 12;
        int daysInMonth = 0;

        List<PointValue> values = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();

        switch (currentMonth) {
            case 2: // Tháng 2
                daysInMonth = 29;
                break;
            case 4: case 6: case 9: case 11:
                daysInMonth = 30;
                break;
            default:
                daysInMonth = 31;
                break;
        }

        for (int i = 0; i < daysInMonth; i++) {
            values.add(new PointValue(i, monthly[i]));
            axisValues.add(new AxisValue(i).setLabel(i+1+""));
            // Label là số tháng
        }

        Line line = new Line(values);
        List<Line> lines = new ArrayList<>();
//        line.setColor(getResources().getColor(R.color.lavendar));
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis(axisValues);
        Axis axisY = new Axis().setHasLines(true);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        lineChartView.setLineChartData(data);
    }
    private void recyclerViewInit() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList<Marketing> marketingArrayList = new ArrayList<>();
        marketingArrayList.add(new Marketing("Mã Giảm Giá Của Shop", "mk1", "Công cụ tăng đơn hàng bằng cách tạo mã giảm giá tặng cho người mua"));
        marketingArrayList.add(new Marketing("Chương Trình Của Shop", "mk2", "Công cụ tăng đơn hàng bằng cách tạo chương trình giảm giá"));
        marketingArrayList.add(new Marketing("Combo Khuyến Mãi", "mk3", "Tạo Combo Khuyến Mãi để tăng giá trị đơn hàng trên mỗi Người mua"));
        marketingArrayList.add(new Marketing("Flash Sale Của Shop", "mk4", "Công cụ giúp tăng doanh số bằng cách tạo khuyến mãi khủng trong các khung giờ nhất định"));
        marketingArrayList.add(new Marketing("Ưu Đãi Follower", "mk5", "Khuyến khích người mua theo dõi Shop bằng cách tặng voucher cho Người theo dõi mới"));

        adapter = new MarketingAdapter(marketingArrayList);
        recyclerView.setAdapter(adapter);
    }
}