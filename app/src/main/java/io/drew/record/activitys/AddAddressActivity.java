package io.drew.record.activitys;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lljjcoder.style.citypickerview.CityPickerView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.base.network.RetrofitManager;
import io.drew.record.BuildConfig;
import io.drew.record.ConfigConstant;
import io.drew.record.EduApplication;
import io.drew.record.R;
import io.drew.record.base.BaseActivity;
import io.drew.record.base.BaseCallback;
import io.drew.record.model.AddressData;
import io.drew.record.model.CurrentAddress;
import io.drew.record.model.MessageEvent;
import io.drew.record.service.AppService;
import io.drew.record.service.bean.response.AddressList;
import io.drew.record.util.FileUtils;
import io.drew.record.util.SoftKeyBoardListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 新建地址
 */
public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.et_name)
    protected EditText et_name;
    @BindView(R.id.et_phone)
    protected EditText et_phone;
    @BindView(R.id.tv_address_main)
    protected TextView tv_address_main;
    @BindView(R.id.et_address_else)
    protected EditText et_address_else;
    @BindView(R.id.switchBtn)
    protected ImageView switchBtn;
    @BindView(R.id.btn_delect)
    protected TextView btn_delect;

    private boolean editModel = false;//编辑模式
    private boolean isDefault = false;
    private boolean onlyOneAddress = false;
    private AddressList.Address address;
    private CurrentAddress currentAddress;
    private CityPickerView mPicker = new CityPickerView();

    private List<AddressData> addressDataList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initData() {
        if (getIntent().getExtras() != null) {
            editModel = getIntent().getExtras().getBoolean("editModel", false);
            address = (AddressList.Address) getIntent().getExtras().getSerializable("address");
            onlyOneAddress = getIntent().getExtras().getBoolean("onlyOneAddress");
        }
        //预先加载仿iOS滚轮实现的全部数据
        mPicker.init(this);
        addressDataList = FileUtils.getAddressDatas(this);
    }

    @Override
    protected void initView() {
        if (editModel) {
            initActionBar("编辑地址", true);
            btn_delect.setVisibility(View.VISIBLE);
            et_name.setText(address.getName());
            et_phone.setText(address.getPhone());
            tv_address_main.setText(address.getDistrict());
            et_address_else.setText(address.getAddress());
            switchBtn.setImageResource(address.getIsDefault() == 1 ? R.drawable.switch_on : R.drawable.switch_off);
            isDefault = address.getIsDefault() == 1;

            currentAddress = new CurrentAddress();
            currentAddress.provinceId = address.getProvinceId();
            currentAddress.provinceName = address.getProvinceName();
            currentAddress.cityId = address.getCityId();
            currentAddress.cityName = address.getCityName();
            currentAddress.districtId = address.getDistrictId();
            currentAddress.districtName = address.getDistrictName();

        } else {
            initActionBar("新建地址", true);
            btn_delect.setVisibility(View.INVISIBLE);
        }
        setActionBarRight("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        setActionBarRightColor("#6A48F5");
    }

    private void save() {
        String name = et_name.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String address_else = et_address_else.getText().toString().trim();
        ;
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address_else)
                || currentAddress == null) {
            ToastManager.showDiyShort("请先完善信息");
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("address", address_else);
            map.put("cityId", currentAddress.cityId);
            map.put("district", currentAddress.districtName);
            map.put("districtId", currentAddress.districtId);
            map.put("provinceId", currentAddress.provinceId);
            if (editModel) {
                map.put("id", address.getId());
            }
            map.put("isDefault", isDefault ? 1 : 0);
            map.put("name", name);
            map.put("phone", phone);
            map.put("status", 1);
            map.put("userId", EduApplication.instance.authInfo.getUser().getId());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                    new JSONObject(map).toString());
            RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).submitAddress(body).enqueue(new BaseCallback<>(result -> {
                if ("true".equals(result)) {
                    ToastManager.showDiyShort("提交成功");
                    if (editModel) {
                        EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_ADDRESS_EDITED));
                    } else {
                        EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_ADDRESS_ADDED));
                    }
                    finish();
                } else {
                    ToastManager.showDiyShort("提交失败");
                }
            }, throwable -> {
                MyLog.e("提交收件地址失败" + throwable.getMessage());
                ToastManager.showDiyShort("提交异常");
            }));
        }
    }

    @OnClick({R.id.tv_address_main, R.id.btn_delect, R.id.switchBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchBtn:
                if (onlyOneAddress) {
                    ToastManager.showDiyShort("当前只有一个地址，不可取消默认");
                } else {
                    isDefault = !isDefault;
                    switchBtn.setImageResource(isDefault ? R.drawable.switch_on : R.drawable.switch_off);
                }

                break;
            case R.id.tv_address_main:
                SoftKeyBoardListener.hideInput(AddAddressActivity.this);
                showAddressPicker();
//                CityConfig cityConfig = new CityConfig.Builder()
//                        .title("所在地区")
//                        .titleTextSize(18)
//                        .titleBackgroundColor("#FFFFFF")
//                        .confirTextColor("#6A48F5")
//                        .confirmTextSize(16)
//                        .cancelTextColor("#999999")
//                        .cancelTextSize(16)
//                        .build();
//                mPicker.setConfig(cityConfig);
//                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
//                    @Override
//                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
//                        provinceBean_selected = province;
//                        cityBean_selected = city;
//                        districtBean_selected = district;
//                        tv_address_main.setText(province.getName() + "" + cityBean_selected.getName() + "" + districtBean_selected.getName());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                    }
//                });
//                mPicker.showCityPicker();
                break;
            case R.id.btn_delect:
                RetrofitManager.instance().getService(BuildConfig.API_URL, AppService.class).removeAddress(address.getId()).enqueue(new BaseCallback<>(result -> {
                    if ("true".equals(result)) {
                        ToastManager.showDiyShort("删除成功");
                        EventBus.getDefault().post(new MessageEvent(ConfigConstant.EB_ADDRESS_REMOVE));
                        finish();
                    } else {
                        ToastManager.showDiyShort("删除失败");
                    }
                }, throwable -> {
                    MyLog.e("删除收件地址失败" + throwable.getMessage());
                    ToastManager.showDiyShort(throwable.getMessage());
                }));
                break;
        }
    }

    private void showAddressPicker() {
        List<String> provinces = new ArrayList<>();
        ArrayList<ArrayList<String>> citys = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> districts = new ArrayList<>();

        for (int i = 0; i < addressDataList.size(); i++) {
            MyLog.e(addressDataList.get(i).getCode() + "/" + addressDataList.get(i).getName());
            if (addressDataList.get(i).getName().equals("海外")) break;
            provinces.add(addressDataList.get(i).getName());

            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int j = 0; j < addressDataList.get(i).getSubRegion().size(); j++) {
                if (addressDataList.get(i).getSubRegion().get(j).getName() == null) {
                    cityList.add("");
                } else {
                    cityList.add(addressDataList.get(i).getSubRegion().get(j).getName());
                }
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                for (int k = 0; k < addressDataList.get(i).getSubRegion().get(j).getSubRegion().size(); k++) {
                    if (addressDataList.get(i).getSubRegion().get(j).getSubRegion().get(k).getName() == null) {
                        city_AreaList.add("");
                    } else {
                        city_AreaList.add(addressDataList.get(i).getSubRegion().get(j).getSubRegion().get(k).getName());
                    }
                }
                province_AreaList.add(city_AreaList);
            }
            citys.add(cityList);
            districts.add(province_AreaList);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                int provinceId = 0;
                String provinceName = "";
                int cityId = 0;
                String cityName = "";
                int districtId = 0;
                String districtName = "";
                if (addressDataList.get(options1) != null) {
                    provinceId = addressDataList.get(options1).getId();
                    provinceName = addressDataList.get(options1).getName();
                    if (addressDataList.get(options1).getSubRegion() != null && addressDataList.get(options1).getSubRegion().size() > 0) {
                        cityId = addressDataList.get(options1).getSubRegion().get(option2).getId();
                        cityName = addressDataList.get(options1).getSubRegion().get(option2).getName();
                        if (addressDataList.get(options1).getSubRegion().get(option2).getSubRegion() != null &&
                                addressDataList.get(options1).getSubRegion().get(option2).getSubRegion().size() > 0) {
                            districtId = addressDataList.get(options1).getSubRegion().get(option2).getSubRegion().get(options3).getId();
                            districtName = addressDataList.get(options1).getSubRegion().get(option2).getSubRegion().get(options3).getName();
                        }
                    }
                }
                MyLog.e("选择结果=" + provinceName + cityName + districtName);
                tv_address_main.setText(provinceName + cityName + districtName);

                currentAddress = new CurrentAddress();
                currentAddress.provinceId = provinceId;
                currentAddress.provinceName = provinceName;
                currentAddress.cityId = cityId;
                currentAddress.cityName = cityName;
                currentAddress.districtId = districtId;
                currentAddress.districtName = districtName;
            }
        })
                .setTitleText("所在地区")
                .setTitleSize(18)
                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .setSubmitColor(Color.parseColor("#6A48F5"))
                .setCancelColor(Color.parseColor("#999999"))
                .build();
        pvOptions.setPicker(provinces, citys, districts);
        pvOptions.show();
    }
}
