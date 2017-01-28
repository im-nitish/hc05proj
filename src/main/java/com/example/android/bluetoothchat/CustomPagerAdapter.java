package com.example.android.bluetoothchat;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static java.security.AccessController.getContext;

/**
 * Created by Nitish on 31-Dec-16.
 */

class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    String[] website = {
        "file:///android_asset/mywater.html", "file:///android_asset/myjuice.html"
    };

    String page = "<html><head>\n" +
            "\t\t<style type=\"text/css\">\n" +
            "#water1{background: linear-gradient(to bottom, #aa0000 0%,#ff0000 50%,#aa0000 51%,#ff0000 100%); height: 100%; width: 100%; position: relative; bottom: -50%; border-radius: 10px;}\n" +
            "#water{background: linear-gradient(to bottom, #1e5799 0%,#2989d8 50%,#207cca 51%,#7db9e8 100%); height: 100%; width: 100%; position: relative; bottom: -50%; border-radius: 10px;}\n" +
            "#container{background: linear-gradient(to right, rgba(226,226,226,1) 0%,rgba(219,219,219,1) 50%,rgba(209,209,209,1) 51%,rgba(254,254,254,1) 100%); width:100%;height:110%;margin: auto; position:relative; border-radius: 0%; overflow: hidden; bottom:7%;}\n" +
            "#shape{width:100%;height:100%;overflow:hidden;}\n" +
            "\t\t</style>\n" +
            "\t\t<script type=\"text/javascript\">\n" +
            "function showlevel(level)\n" +
            "{\n" +
            "\tlevel = level*92/100+2;\n" +
            "\n" +
            "\tif(level <= 21)\n" +
            "\t{document.getElementById('water1').style.height=level+\"%\";\n" +
            "\tdocument.getElementById('water1').style.bottom=(level-100)+\"%\";\n" +
            "\tdocument.getElementById('water').style.display='none';\n" +
            "\tdocument.getElementById('water1').style.display='block';\n" +
            "\t}else\n" +
            "\t{\n" +
            "\tdocument.getElementById('water').style.height=level+\"%\";\n" +
            "\tdocument.getElementById('water').style.bottom=(level-100)+\"%\";\n" +
            "\tdocument.getElementById('water1').style.display='none';\n" +
            "\tdocument.getElementById('water').style.display='block';\n" +
            "}\n" +
            "}\n" +
            "\t\t</script>\n" +
            "\t</head>\n" +
            "\t<body style=\"width:250px;height:250px;\">\n" +
            "\t<div id=\"shape\">\n" +
            "\t\t<div id=\"container\">\n" +
            "\t\t\t<div id=\"water1\" style=\"display:none;\"></div>\n" +
            "\t\t\t<div id=\"water\"></div>\n" +
            "\t\t</div>\n" +
            "\t</div>\n" +
            "\n" +
            "\t\t\n" +
            "\t\n" +
            "</body></html>";

    int[] mResources = {
            R.drawable.bottle25,
            R.drawable.bottle50,
            R.drawable.bottle75,
            R.drawable.bottle100,
            R.drawable.jbottle,
            R.drawable.jbottle,
            R.drawable.jbottle,
            R.drawable.jbottle,
    };

    String [] Bottles = {
      "Bottle 1", "Bottle 2"
    };

    String [] Cquan = {
            "40", "60"
    };

    String [] LD = {
            "12 litres", "5 litres"
    };

    String [] Stat = {
            "Good", "Need to improve"
    };

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Stat.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        TextView CQuality = (TextView)itemView.findViewById(R.id.editText);
        TextView LDAYQ = (TextView)itemView.findViewById(R.id.editText2);
        TextView SQ = (TextView)itemView.findViewById(R.id.editText3);
        TextView CQualityA = (TextView)itemView.findViewById(R.id.editText4);
        TextView LDAYA = (TextView)itemView.findViewById(R.id.editText5);
        TextView SA = (TextView)itemView.findViewById(R.id.editText6);
        TextView bottle = (TextView)itemView.findViewById(R.id.textView);
        Button button = (Button) itemView.findViewById(R.id.b2);
        WebView webview = (WebView) itemView.findViewById(R.id.webv1);

     //   AssetManager as = CQuality.getContext().getAssets();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL(null, page, "text/html", "utf-8", null);

        // webview.loadURL("javascript:sowlevel(percentage)");


        Typeface typeface= Typeface.createFromAsset(CQuality.getContext().getAssets(), "fonts/OpenSansRegular.ttf");
        CQuality.setTypeface(typeface);
        LDAYQ.setTypeface(typeface);
        LDAYA.setTypeface(typeface);
        SQ.setTypeface(typeface);
        SA.setTypeface(typeface);
        bottle.setTypeface(typeface);
        CQualityA.setTypeface(typeface);
        button.setTypeface(typeface);


        int a[] = new int[2];
        int i[] = new int[2];
        CQuality.setText("Current Quantity");
        LDAYQ.setText("Last Day Cons.");
        SQ.setText("Status");
        CQualityA.setText(Cquan[position]+"%");
        a[position] = Integer.parseInt(Cquan[position]);

      if(a[position] <=33)
            i[position] = 0;
        else if(a[position]<=63)
            i[position] = 1;
        else if(a[position]<=88)
            i[position] = 2;
        else
            i[position] = 3;

        LDAYA.setText(LD[position]);
        SA.setText(Stat[position]);
        bottle.setText(Bottles[position]);
        button.setText("SEND DATA TO ARDUINO");
        imageView.setImageResource(mResources[position*4 + i[position]]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }



}