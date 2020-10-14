package io.drew.record.fragments_pad;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moor.imkf.ChatListener;
import com.moor.imkf.IMChat;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.IMMessage;
import com.moor.imkf.model.entity.FromToMessage;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.drew.base.MyLog;
import io.drew.base.ToastManager;
import io.drew.record.R;
import io.drew.record.adapters.CustomerAdapter;
import io.drew.record.model.MessageEvent;

/**
 * @Author: KKK
 * @CreateDate: 2020/7/8 9:51 AM
 */
public class CustomerDialogFragment extends BaseDialogFragment {

    @BindView(R.id.iv_call)
    protected ImageView iv_call;
    @BindView(R.id.chat_input)
    protected EditText chat_input;
    @BindView(R.id.chat_send)
    protected TextView chat_send;
    @BindView(R.id.tv_num)
    protected TextView tv_num;
    @BindView(R.id.other_name)
    protected TextView other_name;
    @BindView(R.id.recycle_message)
    protected RecyclerView recycle_message;
    @BindView(R.id.freshLayout)
    protected SmartRefreshLayout freshLayout;
    @BindView(R.id.relay_back)
    protected RelativeLayout relay_back;

    private LinearLayoutManager linearLayoutManager;
    private int currentPage = 1;
    private CustomerAdapter adapter;
    private LocalBroadcastManager localBroadcastManager;
    private BroadcastReceiver localReceiver;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_customer_dialog;
    }


    @Override
    protected void initData() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        localReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("new_message_customer")) {
                    MyLog.e("收到新消息本地广播");
                    currentPage = 1;
                    List<FromToMessage> fromToMessages = IMChatManager.getInstance().getMessages(currentPage);
                    List<FromToMessage> messages = new ArrayList<>();
                    for (FromToMessage fromToMessage : fromToMessages) {
                        messages.add(0, fromToMessage);
                    }
                    adapter.setNewData(messages);
                    recycle_message.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        };
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("new_message_customer");
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    @Override
    protected void initView() {
        relay_back.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);

        List<FromToMessage> fromToMessages = IMChatManager.getInstance().getMessages(currentPage);
        List<FromToMessage> messages = new ArrayList<>();
        for (FromToMessage fromToMessage : fromToMessages) {
            messages.add(0, fromToMessage);
        }
        recycle_message.setLayoutManager(linearLayoutManager);
        adapter = new CustomerAdapter(messages);
        recycle_message.setAdapter(adapter);
        recycle_message.scrollToPosition(adapter.getItemCount() - 1);
        chat_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null)
                    tv_num.setText("" + s.length() + "/100");
            }
        });

        freshLayout.setEnableLoadMore(false);
        freshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                boolean noMore = IMChatManager.getInstance().isReachEndMessage(adapter.getData().size());
                if (noMore) {
                    MyLog.e("没有更多数据了" + adapter.getData().size());
                    refreshLayout.finishRefresh();
                    return;
                }
                currentPage += 1;
                List<FromToMessage> fromToMessages = IMChatManager.getInstance().getMessages(currentPage);
                if (fromToMessages != null && fromToMessages.size() > 0) {
                    List<FromToMessage> messages = new ArrayList<>();
                    for (int i = (currentPage - 1) * 15; i < fromToMessages.size(); i++) {
                        messages.add(0, fromToMessages.get(i));
                    }
                    adapter.addData(0, messages);
                }
                refreshLayout.finishRefresh();
            }
        });
        IMChatManager.getInstance().resetMsgUnReadCount();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(MessageEvent event) {
        super.receiveEvent(event);
    }

    @OnClick({R.id.relay_back, R.id.iv_call, R.id.chat_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relay_back:
                dismiss();
                break;
            case R.id.chat_send:
                String content = chat_input.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    FromToMessage fromToMessage = IMMessage.createTxtMessage(content);
                    IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
                        @Override
                        public void onSuccess() {
                            chat_input.setText("");
                            MyLog.e(content + "发送成功");
                            adapter.addData(fromToMessage);
                            recycle_message.scrollToPosition(adapter.getItemCount() - 1);
                        }

                        @Override
                        public void onFailed() {
                            ToastManager.showDiyShort("发送失败请稍后重试");
                        }

                        @Override
                        public void onProgress(int i) {

                        }
                    });
                }
                break;
            case R.id.iv_call:
                Dialog addDialog = new Dialog(getContext(), R.style.mdialog);
                View con = LayoutInflater.from(getContext()).inflate(R.layout.dialog_call, null);
                Button btn_sure = con.findViewById(R.id.btn_sure);
                addDialog.setContentView(con);
                btn_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDialog.dismiss();
                    }
                });
                Window dialogWindow = addDialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = getResources().getDimensionPixelOffset(R.dimen.dp_180);
                lp.height = getResources().getDimensionPixelOffset(R.dimen.dp_100);
                dialogWindow.setAttributes(lp);
                addDialog.show();
                break;
        }
    }
}
