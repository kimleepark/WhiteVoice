package com.example.myapplicationui.User_Interface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplicationui.Conection.ListViewItem;
import com.example.myapplicationui.R;

import java.util.ArrayList;

/**
 * Created by 박지찬 on 2017-07-04.
 */

public class FavoriteAdapter extends ArrayAdapter{

    int resourceId;

    FavoriteAdapter(Context context, int resource, ArrayList<ListViewItem> list){
        super(context, resource, list);

        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final TextView textTextView = (TextView) convertView.findViewById(R.id.favoriteText);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = (ListViewItem) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        textTextView.setText(listViewItem.getText());

        // button1 클릭 시 TextView(textView1)의 내용 변경.
        Button deleteBtn = (Button) convertView.findViewById(R.id.delete);
        deleteBtn.setTag(position);
        deleteBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LinearLayout itemParent = (LinearLayout)v.getParent();
                int nPosition = (int) v.getTag();
                ((FavoriteActivity)FavoriteActivity.mContext).RemoveData(nPosition);
            }
        });

        return convertView;
    }
}
