package bjfu.it.zhangsixuan.starbuzz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bjfu.it.zhangsixuan.starbuzz.R;
import bjfu.it.zhangsixuan.starbuzz.bean.FavoriteBean;

public class FavoriteAdapter extends BaseAdapter {

    private final ArrayList mData;

    public FavoriteAdapter(Map<Integer, FavoriteBean> hashMap) {
        mData = new ArrayList();
        mData.addAll(hashMap.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap.Entry<String, FavoriteBean> getItem(int position) {
        return (HashMap.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

//        result = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_linear_layout, parent, false);

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.favorite_linear_layout, parent, false);
        } else {
            result = convertView;
        }

        HashMap.Entry<String, FavoriteBean> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((ImageView) result.findViewById(R.id.favorite_image)).setImageResource(item.getValue().getStuffImageId());
        ((TextView) result.findViewById(R.id.favorite_name)).setText(item.getKey());

        return result;
    }
}
