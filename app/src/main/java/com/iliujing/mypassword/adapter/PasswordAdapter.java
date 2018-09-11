package com.iliujing.mypassword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iliujing.mypassword.R;
import com.iliujing.mypassword.vo.PasswordItem;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by liuji on 2018/9/7.
 */

public class PasswordAdapter extends BaseAdapter {
    private List<PasswordItem> list;
    private Context mContext;
    public PasswordAdapter(Context c,List<PasswordItem> l) {
        this.mContext = c;
        this.list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PasswordItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_password,null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.account = (TextView) convertView.findViewById(R.id.account);
            holder.password = (TextView) convertView.findViewById(R.id.password);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        PasswordItem item = getItem(position);
        holder.name.setText(item.getName());
        holder.account.setText(item.getAccount());
        holder.password.setText(item.getPassword());
        return convertView;
    }

    class ViewHolder{
        TextView name;
        TextView account;
        TextView password;
    }
}
