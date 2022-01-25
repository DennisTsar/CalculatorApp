package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView z;
    Button a1;
    Button a2;
    Button a3;
    Button a4;
    Button a5;
    Button a6;
    Button a7;
    Button a8;
    Button a9;
    Button a0;
    Button p;
    Button m;
    Button t;
    Button d;
    Button c;
    Button eq;
    Button p1;
    Button p2;
    Button exp;
    Button del;
    Button dot;
    Button negpow;
    Button squ;
    Button ee;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        z = findViewById(R.id.a_z);
        z.setMovementMethod(new ScrollingMovementMethod());
        a1 = findViewById(R.id.a_1);
        a2 = findViewById(R.id.a_2);
        a3 = findViewById(R.id.a_3);
        a4 = findViewById(R.id.a_4);
        a5 = findViewById(R.id.a_5);
        a6 = findViewById(R.id.a_6);
        a7 = findViewById(R.id.a_7);
        a8 = findViewById(R.id.a_8);
        a9 = findViewById(R.id.a_9);
        a0 = findViewById(R.id.a_0);
        p = findViewById(R.id.a_plu);
        m = findViewById(R.id.a_min);
        t = findViewById(R.id.a_tim);
        d = findViewById(R.id.a_div);
        eq = findViewById(R.id.a_equ);
        c = findViewById(R.id.a_cle);
        p1 = findViewById(R.id.a_p1);
        p2 = findViewById(R.id.a_p2);
        exp = findViewById(R.id.a_exp);
        del = findViewById(R.id.a_de);
        dot = findViewById(R.id.a_dot);
        negpow = findViewById(R.id.a_negpow);
        squ = findViewById(R.id.a_squ);
        ee = findViewById(R.id.a_ee);
        
        a1.setOnClickListener(this);
        a2.setOnClickListener(this);
        a3.setOnClickListener(this);
        a4.setOnClickListener(this);
        a5.setOnClickListener(this);
        a6.setOnClickListener(this);
        a7.setOnClickListener(this);
        a8.setOnClickListener(this);
        a9.setOnClickListener(this);
        a0.setOnClickListener(this);
        p.setOnClickListener(this);
        m.setOnClickListener(this);
        t.setOnClickListener(this);
        d.setOnClickListener(this);
        p1.setOnClickListener(this);
        p2.setOnClickListener(this);
        exp.setOnClickListener(this);
        dot.setOnClickListener(this);
        negpow.setOnClickListener(this);
        squ.setOnClickListener(this);
        ee.setOnClickListener(this);

        c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                z.setText("");
            }
        });
        eq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> a = new ArrayList<String>(Arrays.asList((z.getText() + "").split("(?=((?<=[+*/^()])|(?=[+*/^\\-)])))")));
                Log.d("a", a.toString());
                double ans = 0;
                try {
                    ans = solve(a);
                }
                catch(Exception e){
                    z.setText("ERROR");
                    return;
                }
                if(ans==Double.POSITIVE_INFINITY || ans==Double.NEGATIVE_INFINITY)
                    z.setText("ERROR");
                else if(ans==(int)ans)
                    z.setText(""+(int)ans);
                else
                    z.setText(""+ans);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    z.setText(z.getText().subSequence(0, z.getText().length() - 1));
                }
                catch(StringIndexOutOfBoundsException e){
                    z.setText("ERROR");
                }
            }
        });
    }
    public void onClick(View v){
        z.setText(z.getText()+""+((Button)v).getText());
    }
    public static double solve(List<String> a) {
        if(a.get(0).equals(""))
            a.remove(0);
        for(int i = 1; i<a.size(); i++)
            if(a.get(i).startsWith("-") && !a.get(i-1).matches("\\D"))
                a.add(i,"+");
        int open = -1;
        int count = 0;
        for(int i = 0; i<a.size(); i++){
            if(count<0)
                return 1/0;
            if(a.get(i).equals("(")) {
                count++;
                if (open < 0)
                    open = i;
            }
            if(a.get(i).equals(")"))
                count--;
            if(count==0 && open>-1){
                ArrayList<String> b= new ArrayList<String>(a.subList(open+1,i));
                a.subList(open,i+1).clear();
                a.add(open,""+solve(b));
                open = -1;
                i-=(b.size()+2);
            }
        }
        for(int i = 1; i<a.size(); i+=2){
            if(a.get(i).equals("^")) {
                a.set(i - 1, "" + Math.pow(Double.parseDouble(a.get(i - 1)), Double.parseDouble(a.get(i + 1))));
                a.remove(i+1);
                a.remove(i);
                i-=2;
            }
        }
        for(int i = 1; i<a.size(); i+=2){
            if(a.get(i).equals("*"))
                a.set(i-1,""+new BigDecimal(a.get(i-1)).multiply(new BigDecimal(a.get(i+1))));
            else if(a.get(i).equals("/")) {
                a.set(i - 1, "" + new BigDecimal(a.get(i - 1)).divide(new BigDecimal(a.get(i + 1)), 10, RoundingMode.HALF_EVEN));
                Log.d("l", "solve: "+a.get(i-1));
            }
            if(a.get(i).matches("[*/]")) {
                a.remove(i+1);
                a.remove(i);
                i-=2;
            }
        }
        for(int i = 1; i<a.size(); i+=2){
            if(a.get(i).equals("+")){
                a.set(i-1,""+new BigDecimal(a.get(i-1)).add(new BigDecimal(a.get(i+1))));
                a.remove(i+1);
                a.remove(i);
                i-=2;
            }
        }
        return Double.parseDouble(a.get(0));
    }
}