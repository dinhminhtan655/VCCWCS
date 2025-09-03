package com.wcs.vcc.main;

import static com.wcs.vcc.main.vo.MenuItem.MENU_ITEM_NAME;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wcs.wcs.R;
import com.wcs.wcs.databinding.ItemMenuBinding;
import com.wcs.vcc.main.vo.MenuItem;
import com.wcs.vcc.recyclerviewadapter.DataBoundListAdapter;


public class MenuAdapter extends DataBoundListAdapter<MenuItem, ItemMenuBinding> {

    private Context context;

    public MenuAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected ItemMenuBinding createBinding(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_menu, parent, false);
    }

    @Override
    protected void bind(ItemMenuBinding binding, final MenuItem item, int position) {
        if (item != null) {
            binding.setItem(item);
            binding.txtName.setCompoundDrawablesWithIntrinsicBounds(0, item.getIcon(), 0, 0);

            binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View viewMenuItem, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        viewMenuItem.setAlpha(0.5f);
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        gotoMenuDetail(viewMenuItem.getContext(), item);
                        viewMenuItem.setAlpha(1f);
                    } else
                        viewMenuItem.setAlpha(1f);
                    return true;
                }
            });
        }
    }

    private void gotoMenuDetail(Context context, MenuItem item) {
        Class<?> destination = item.getMenuDetailActivity();
        Intent intent = new Intent(context, destination);
        intent.putExtra(MENU_ITEM_NAME, item.getName());
        intent.putExtra("type", item.getStrTypeDORO());
        context.startActivity(intent);
    }


    @Override
    protected boolean areItemsTheSame(MenuItem oldItem, MenuItem newItem) {
        return oldItem.getName() == newItem.getName();
    }

    @Override
    protected boolean areContentsTheSame(MenuItem oldItem, MenuItem newItem) {
        return true;
    }
}
