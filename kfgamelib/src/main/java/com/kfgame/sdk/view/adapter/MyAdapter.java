package com.kfgame.sdk.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kfgame.sdk.KFGameSDK;
import com.kfgame.sdk.util.LogUtil;
import com.kfgame.sdk.util.ResourceUtil;

import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

/**
 * Created by Tobin on 18/2/23.
 */

public class MyAdapter extends BaseAdapter {

    private Context context; // 运行上下文
    private List<Map<String, Object>> listItems; // listview信息集合
    private LayoutInflater listContainer; // 视图容器

    public final class ListItemView { // 自定义控件集合
        public TextView info;
        public ImageView delete;
    }

    public MyAdapter(Context context, List<Map<String, Object>> listItems) {
        super();
        this.context = context;
        this.listItems = listItems;
        listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
    }

    @Override
    public int getCount() {
        // 这里不能使用默认返回0，不然listview没数据
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int selectID = position;
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            // 获取list_item布局文件的视图
            convertView = listContainer.inflate(ResourceUtil.getLayoutId("kfgame_sdk_username_dropdown_item"), null);
            listItemView.info = (TextView) convertView.findViewById(ResourceUtil.getId("tv_account"));
            listItemView.delete = (ImageView) convertView.findViewById(ResourceUtil.getId("iv_delete"));
            // 设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        listItemView.info.setText((String) listItems.get(position).get("name"));

        // delete按钮点击事件
        listItemView.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("Tobin name: " + listItems.get(position).get("name").toString());
                KFGameSDK.getInstance().getDbHelper().delete(listItems.get(position).get("name").toString());
                listItems.remove(position);
                notifyDataSetChanged();
            }
        });

        listItemView.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uesername = listItems.get(position).get("name").toString();
//                Toast.makeText(context, "这是第" + (selectID + 1) + "条item" + uesername, Toast.LENGTH_SHORT).show();
                accountListListener.didSelectItem(uesername);
            }
        });

        return convertView;
    }

    private AccountListListener accountListListener;
    public AccountListListener getAccountListListener() {
        return accountListListener;
    }

    public void setAccountListListener(AccountListListener accountListListener) {
        this.accountListListener = accountListListener;
    }

}
