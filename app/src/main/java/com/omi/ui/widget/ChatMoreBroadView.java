package com.omi.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.omi.R;
import com.omi.bean.chat.ChatMoreBoard;
import com.omi.bean.conversation.ConversationType;
import com.omi.config.Config;
import com.omi.config.OmiAction;
import com.omi.databinding.ItemChatMoreBoardViewBinding;
import com.omi.databinding.WidgetChatMoreViewBinding;
import com.omi.ui.activity.utils.amap.LocationSelectActivity;
import com.omi.ui.adapter.base.BasePagerAdapter;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.WaringDialog;
import com.omi.utils.FileUtils;
import com.omi.utils.IntentUtil;
import com.omi.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.omi.config.OmiAction.ACTION_RECORD_VIDEO;
import static com.omi.config.OmiAction.ACTION_SELECT_LOCATION;

/**
 * Created by SensYang on 2017/03/22 12:47
 */

public class ChatMoreBroadView extends LinearLayout implements ViewPager.OnPageChangeListener {
    private WidgetChatMoreViewBinding chatMoreViewBinding;
    private List<ChatMoreBoard> singleBoardList = new ArrayList<>();
    private int pageCount;
    private String dotString;
    private BasePagerAdapter pagerAdapter;
    private WaringDialog waringDialog;

    public ChatMoreBroadView(Context context) {
        this(context, null);
    }

    public ChatMoreBroadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatMoreBroadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        chatMoreViewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.widget_chat_more_view, this, true);
        pagerAdapter = new BasePagerAdapter();
        chatMoreViewBinding.viewPager.setAdapter(pagerAdapter);
        chatMoreViewBinding.viewPager.addOnPageChangeListener(this);
        waringDialog = new WaringDialog(context);
        waringDialog.setTitleText(R.string.select_head);
        waringDialog.setLeftText(R.string.take_photo);
        waringDialog.setRightText(R.string.select_album);
        waringDialog.setCancelable(true);
        waringDialog.setOnAlterClickListener(new OnAlterClickListener() {
            @Override
            public void onCancelClick() {
                ((Activity) getContext()).startActivityForResult(IntentUtil.getInstance().getPickImageIntent(), OmiAction.ACTION_PICK_PICTURE);
            }

            @Override
            public void onConfirmClick() {
                OmiAction.TEMP_URI = FileUtils.makeImageUri(System.currentTimeMillis() + "");
                ((Activity) getContext()).startActivityForResult(IntentUtil.getInstance().getTakePhotoIntent(OmiAction.TEMP_URI), OmiAction.ACTION_TAKE_PHOTO);
            }
        });
    }

    public void setConversationType(ConversationType conversationType) {
        ChatMoreBoard board;
        singleBoardList.clear();
        switch (conversationType) {
            case SINGLE_CHAT:
                board = new ChatMoreBoard(R.mipmap.chat_board_icon_picture, R.string.chat_board_text_picture);
                singleBoardList.add(board);
                //                board = new ChatMoreBoard(R.mipmap.chat_board_icon_voice, R.string.chat_board_text_voice);
                //                singleBoardList.add(board);
                board = new ChatMoreBoard(R.mipmap.chat_board_icon_video, R.string.chat_board_text_video);
                singleBoardList.add(board);
                board = new ChatMoreBoard(R.mipmap.chat_board_icon_location, R.string.chat_board_text_location);
                singleBoardList.add(board);
                //                board = new ChatMoreBoard(R.mipmap.chat_board_icon_collect, R.string.chat_board_text_collect);
                //                singleBoardList.add(board);
                //                board = new ChatMoreBoard(R.mipmap.chat_board_icon_idcard, R.string.chat_board_text_idcard);
                //                singleBoardList.add(board);
                break;
            case GROUP_CHAT:
                board = new ChatMoreBoard(R.mipmap.chat_board_icon_picture, R.string.chat_board_text_picture);
                singleBoardList.add(board);
                board = new ChatMoreBoard(R.mipmap.chat_board_icon_video, R.string.chat_board_text_video);
                singleBoardList.add(board);
                //                board = new ChatMoreBoard(R.mipmap.chat_board_icon_location, R.string.chat_board_text_location);
                //                singleBoardList.add(board);
                //                board = new ChatMoreBoard(R.mipmap.chat_board_icon_collect, R.string.chat_board_text_collect);
                //                singleBoardList.add(board);
                //                board = new ChatMoreBoard(R.mipmap.chat_board_icon_idcard, R.string.chat_board_text_idcard);
                //                singleBoardList.add(board);
                break;
        }
        initView(singleBoardList);
    }

    private void initView(List<ChatMoreBoard> boardList) {
        pagerAdapter.clear();
        pageCount = boardList.size() % 8 != 0 ? boardList.size() / 8 + 1 : boardList.size() / 8;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pageCount; i++) {
            sb.append("●");
            int fromIndex = i * 8;
            int toIndex = fromIndex + 8;
            if (toIndex > boardList.size()) toIndex = boardList.size();
            ItemChatMoreBoardViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_chat_more_board_view, this, false);
            binding.setChatMoreBroadView(this);
            binding.setList(boardList.subList(fromIndex, toIndex));
            binding.getRoot().setTag(binding);
            pagerAdapter.addData(binding.getRoot());
        }
        dotString = sb.toString();
        pagerAdapter.notifyDataSetChanged();
        resetSpan(0);
    }

    private void resetSpan(int position) {
        if (chatMoreViewBinding.viewPager.getAdapter().getCount() == 0) {
            chatMoreViewBinding.positionTV.setText("");
            return;
        }
        SpannableString spanString = new SpannableString(dotString);
        ForegroundColorSpan span = new ForegroundColorSpan(Config.getMain_color());
        spanString.setSpan(span, position, position + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        chatMoreViewBinding.positionTV.setText(spanString);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        resetSpan(position);
    }

    public void onItemClick(ChatMoreBoard board) {
        switch (board.getTextRes()) {
            case R.string.chat_board_text_picture:
                waringDialog.show();
                break;
            case R.string.chat_board_text_voice:
                ToastUtil.showToast(R.string.chat_board_text_voice);
                break;
            case R.string.chat_board_text_video:
                //开启摄像机
                ((Activity) getContext()).startActivityForResult(IntentUtil.getInstance().getRecordVideoIntent(), ACTION_RECORD_VIDEO);
                break;
            case R.string.chat_board_text_location:
                ((Activity) getContext()).startActivityForResult(new Intent(getContext(), LocationSelectActivity.class), ACTION_SELECT_LOCATION);
                break;
            case R.string.chat_board_text_collect:
                ToastUtil.showToast(R.string.chat_board_text_collect);
                break;
            case R.string.chat_board_text_idcard:
                ToastUtil.showToast(R.string.chat_board_text_idcard);
                break;
        }
    }
}
