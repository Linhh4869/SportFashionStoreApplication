package com.example.sportfashionstore.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.example.sportfashionstore.R;
import com.example.sportfashionstore.callback.OnItemClickListener;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinner<T> extends RelativeLayout {
    private List<T> items = new ArrayList<>();
    private List<String> itemsDispplay = new ArrayList<>();
    private Spinner spinner;
    private boolean hasOptionSelect;
    private ArrayAdapter<String> adapter;
    private OnItemClickListener<T> listener;

    public CustomSpinner(Context context) {
        super(context);
        initView(context, null);
    }

    public CustomSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_spinner, this, true);
        spinner = view.findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, itemsDispplay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onItemClicked(getItemSelect());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setData(List<T> datas, boolean hasOptionSelect) {
        items.clear();
        itemsDispplay.clear();
        if (hasOptionSelect) {
            itemsDispplay.add("Chon");
        }
        if (!CollectionUtils.isEmpty(datas)) {
            items.addAll(datas);
            for (T data : datas) {
                itemsDispplay.add(data.toString());
            }
        }
        this.hasOptionSelect = hasOptionSelect;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setData(List<T> datas) {
        setData(datas, true);
    }

    public void setItemSelected(int position) {
        if(position < items.size()) {
            spinner.setSelection(position);
        }
    }

    public T getItemSelect() {
        int posActual = hasOptionSelect ? spinner.getSelectedItemPosition() - 1 : spinner.getSelectedItemPosition();
        if (posActual < 0) {
            return null;
        }
        return CollectionUtils.isEmpty(items) ? null : items.get(posActual);
    }

    public int getActualSelectItemPosition() {
        int posActual = hasOptionSelect ? spinner.getSelectedItemPosition() - 1 : spinner.getSelectedItemPosition();
        return posActual < 0 ? -1 : posActual;
    }

    public List<T> getItems() {
        return items;
    }

    public void resetData() {
        items.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void disableSpinner() {
        if (spinner != null) {
            spinner.setEnabled(false);
            spinner.setClickable(false);
        }
    }

    public void setListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }
}
