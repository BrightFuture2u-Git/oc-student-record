package io.drew.record.fragments_pad;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.R;
import io.drew.record.adapters.AddressAdapter;
import io.drew.record.base.BaseCallback;
import io.drew.record.base.BaseFragment;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AddressList;

import static io.drew.record.ConfigConstant.EB_ADDRESS_ADDED;
import static io.drew.record.ConfigConstant.EB_ADDRESS_EDITED;
import static io.drew.record.ConfigConstant.EB_ADDRESS_REMOVE;

/**
 * 收货地址
 */
public class AddressListCustomFragment extends BaseFragment {

    @BindView(R.id.recycle_view)
    protected RecyclerView recycle_view;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private List<AddressList.Address> addressList;
    private LinearLayoutManager linearLayoutManager;
    private AddressAdapter addressAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_address_list_custom;
    }

    @Override
    protected void initData() {
        if (addressList == null) {
            getAddressList();
        }
    }

    @Override
    protected void initView() {
        title.setText("收货地址");
        relay_back.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycle_view.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressAdapter(context, R.layout.item_address, addressList);
        recycle_view.setAdapter(addressAdapter);
        View empty = LayoutInflater.from(context).inflate(R.layout.view_empty, null);
        addressAdapter.setEmptyView(empty);
        addressAdapter.addChildClickViewIds(R.id.tv_edit);
        addressAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_edit) {
                    new AddAddressFragment(2, true,
                            adapter.getData().size() <= 1,
                            (AddressList.Address) adapter.getData().get(position)
                    ).show(getParentFragmentManager(), "editAddress");
                }
            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        switch (event.getCode()) {
            case EB_ADDRESS_ADDED:
            case EB_ADDRESS_EDITED:
            case EB_ADDRESS_REMOVE:
                getAddressList();
                break;
        }
    }

    private void getAddressList() {
        RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).getAddressList(1, 100).enqueue(new BaseCallback<>(list -> {
            if (list != null) {
                addressList = list.getList();
                addressAdapter.setNewData(addressList);
            }
        }, throwable -> {
            MyLog.e("收件地址列表获取失败" + throwable.getMessage());
        }));
    }

    @OnClick({R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                new AddAddressFragment(2).show(getParentFragmentManager(), "addAddress");
                break;
        }
    }
}
