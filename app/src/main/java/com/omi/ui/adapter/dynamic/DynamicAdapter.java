package com.omi.ui.adapter.dynamic;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omi.BR;
import com.omi.R;
import com.omi.bean.User;
import com.omi.bean.dynamic.Dynamic;
import com.omi.bean.dynamic.DynamicLikes;
import com.omi.config.OmiAction;
import com.omi.databinding.DynamicHeadBinding;
import com.omi.databinding.ItemDynamicDoubleImgBinding;
import com.omi.databinding.ItemDynamicForwardBinding;
import com.omi.databinding.ItemDynamicMoreImgsBinding;
import com.omi.databinding.ItemDynamicSingleImgBinding;
import com.omi.databinding.ItemDynamicWordsBinding;
import com.omi.net.ApiByHttp;
import com.omi.ui.activity.dynamic.DynamicActivity;
import com.omi.ui.activity.dynamic.PublishDynamicActivity;
import com.omi.ui.activity.user.SelfInfoActivity;
import com.omi.ui.activity.user.UserInfoActivity;
import com.omi.ui.activity.utils.ImageBrowseActivity;
import com.omi.ui.adapter.base.BaseRecyclerViewHolder;
import com.omi.ui.widget.listener.OnAlterClickListener;
import com.omi.ui.widget.window.WaringDialog;
import com.omi.utils.CommonUtils;
import com.omi.utils.IMEUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by SensYang on 2017/03/09 14:29
 */

public class DynamicAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private User.Data user;
    private List<Dynamic.Data> dynamicList = new ArrayList<>();
    private boolean isSelfDynamic;

    /**
     * 是否是自己的动态
     */
    public DynamicAdapter(boolean isSelfDynamic) {
        this.isSelfDynamic = isSelfDynamic;
    }

    public void clear() {
        dynamicList.clear();
    }

    public void setUser(User.Data user) {
        this.user = user;
    }

    public void addDynamic(Collection<Dynamic.Data> dynamicList) {
        if (dynamicList == null) return;
        this.dynamicList.addAll(dynamicList);
    }

    @Override
    public int getItemViewType(int position) {
        if (user != null) {
            if (position == 0) return -1;
            position -= 1;
        }
        Dynamic.Data dynamic = dynamicList.get(position);
        if (dynamic.getDiscover_didForward() == 1) return 4;
        if (dynamic.getDiscover_haveImage() == 1) {
            if (dynamic.getImages() != null) {
                if (dynamic.getImages().size() == 1) return 1;
                else if (dynamic.getImages().size() == 2) return 2;
                else return 3;
            }
            return 0;
        } else {
            return 0;
        }
    }

    /**
     * 动态类型
     * -1.头部
     * 0.文本类型
     * 1.单图类型
     * 2.双图类型
     * 3.多图类型
     * 4.转发类型
     */
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case -1: {
                DynamicHeadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dynamic_head, parent, false);
                binding.setDynamicAdapter(this);
                return new HeadViewHolder(binding);
            }
            case 0: {
                ItemDynamicWordsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_words, parent, false);
                binding.setDynamicAdapter(this);
                binding.setOverLineTextView(binding.contentTV);
                binding.setCommentAdapter(new DynamicCommentAdapter());
                return new WordsDynamicViewHolder(binding);
            }
            case 1: {
                ItemDynamicSingleImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_single_img, parent, false);
                binding.setDynamicAdapter(this);
                binding.setOverLineTextView(binding.contentTV);
                binding.setCommentAdapter(new DynamicCommentAdapter());
                return new SingleImageDynamicViewHolder(binding);
            }
            case 2: {
                ItemDynamicDoubleImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_double_img, parent, false);
                binding.setDynamicAdapter(this);
                binding.setOverLineTextView(binding.contentTV);
                binding.setCommentAdapter(new DynamicCommentAdapter());
                return new DoubleImageDynamicViewHolder(binding);
            }
            case 3: {
                ItemDynamicMoreImgsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_more_imgs, parent, false);
                binding.setDynamicAdapter(this);
                binding.setOverLineTextView(binding.contentTV);
                binding.setCommentAdapter(new DynamicCommentAdapter());
                return new MoreImagesDynamicViewHolder(binding);
            }
            case 4: {
                ItemDynamicForwardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dynamic_forward, parent, false);
                binding.setDynamicAdapter(this);
                binding.setOverLineTextView(binding.contentTV);
                binding.setCommentAdapter(new DynamicCommentAdapter());
                return new ForwardDynamicViewHolder(binding);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (user != null) {
            if (position == 0) {
                holder.bindData(user);
                return;
            }
            holder.bindData(dynamicList.get(position - 1));
        } else {
            holder.bindData(dynamicList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dynamicList.size() + (user == null ? 0 : 1);
    }

    class HeadViewHolder extends BaseRecyclerViewHolder<User.Data> {
        DynamicHeadBinding headBinding;

        public HeadViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            headBinding = (DynamicHeadBinding) dataBinding;
        }

        public void bindData(User.Data user) {
            headBinding.setUser(user);
        }
    }

    class ForwardDynamicViewHolder extends BaseRecyclerViewHolder<Dynamic.Data> {
        ItemDynamicForwardBinding forwardBinding;

        public ForwardDynamicViewHolder(ItemDynamicForwardBinding dataBinding) {
            super(dataBinding.getRoot());
            forwardBinding = dataBinding;
        }

        public void bindData(Dynamic.Data dynamic) {
            forwardBinding.getCommentAdapter().setPinglun(dynamic.getPinglun());
            forwardBinding.setDynamic(dynamic);
        }
    }

    class WordsDynamicViewHolder extends BaseRecyclerViewHolder<Dynamic.Data> {
        ItemDynamicWordsBinding wordsBinding;

        public WordsDynamicViewHolder(ViewDataBinding dataBinding) {
            super(dataBinding.getRoot());
            wordsBinding = (ItemDynamicWordsBinding) dataBinding;
        }

        public void bindData(Dynamic.Data dynamic) {
            wordsBinding.getCommentAdapter().setPinglun(dynamic.getPinglun());
            wordsBinding.setDynamic(dynamic);
        }
    }

    class SingleImageDynamicViewHolder extends BaseRecyclerViewHolder<Dynamic.Data> {
        ItemDynamicSingleImgBinding binding;

        public SingleImageDynamicViewHolder(ItemDynamicSingleImgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(Dynamic.Data dynamic) {
            binding.getCommentAdapter().setPinglun(dynamic.getPinglun());
            binding.setDynamic(dynamic);
        }
    }

    class DoubleImageDynamicViewHolder extends BaseRecyclerViewHolder<Dynamic.Data> {
        ItemDynamicDoubleImgBinding binding;

        public DoubleImageDynamicViewHolder(ItemDynamicDoubleImgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(Dynamic.Data dynamic) {
            binding.getCommentAdapter().setPinglun(dynamic.getPinglun());
            binding.setDynamic(dynamic);
        }
    }

    class MoreImagesDynamicViewHolder extends BaseRecyclerViewHolder<Dynamic.Data> {
        ItemDynamicMoreImgsBinding imgsBinding;
        DynamicImagesAdapter adapter;

        public MoreImagesDynamicViewHolder(ItemDynamicMoreImgsBinding dataBinding) {
            super(dataBinding.getRoot());
            imgsBinding = dataBinding;
            adapter = new DynamicImagesAdapter(DynamicAdapter.this);
            imgsBinding.setAdapter(adapter);
        }

        public void bindData(Dynamic.Data dynamic) {
            adapter.setDynamic(dynamic);
            imgsBinding.getCommentAdapter().setPinglun(dynamic.getPinglun());
            imgsBinding.setDynamic(dynamic);
        }
    }

    public void onSelfHeadClick(View view) {
        if (!isSelfDynamic) {
            Intent intent = new Intent(view.getContext(), DynamicActivity.class);
            intent.putExtra("isSelf", true);
            view.getContext().startActivity(intent);
        }
    }

    public View.OnLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            onDynamicLongClick(v, (Dynamic.Data) v.getTag());
            return true;
        }
    };

    private WaringDialog waringDialog;

    public void onDynamicLongClick(View view, Dynamic.Data dynamic) {
        if (!dynamic.getMember_id().equalsIgnoreCase(ApiByHttp.getInstance().getMember_id()))
            return;
        if (waringDialog == null) {
            waringDialog = new WaringDialog(view.getContext());
            waringDialog.setTitleText("是否删除该动态？");
            waringDialog.setOnAlterClickListener(new OnAlterClickListener() {
                @Override
                public void onCancelClick() { }

                @Override
                public void onConfirmClick() {
                    Dynamic.Data dynamic = (Dynamic.Data) waringDialog.getTag();
                    if (dynamic == null) return;
                    int position = 0;
                    for (int i = 0; i < dynamicList.size(); i++) {
                        if (dynamic == dynamicList.get(i)) {
                            position = i + 1;
                            break;
                        }
                    }
                    if (position != 0) {
                        dynamicList.remove(dynamic);
                        notifyItemRemoved(position);
                    }
                    ApiByHttp.getInstance().delDynamic(dynamic.getDiscover_id(), null);
                }
            });
        }
        waringDialog.setTag(dynamic);
        waringDialog.show();
        //        ToastUtil.showToast("长按");
    }

    public void onUserClick(View view, String userid) {
        if (userid == null) return;
        if (userid.equalsIgnoreCase(ApiByHttp.getInstance().getPhone())) {
            Intent intent = new Intent(view.getContext(), SelfInfoActivity.class);
            view.getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(view.getContext(), UserInfoActivity.class);
            intent.putExtra("userid", userid);
            view.getContext().startActivity(intent);
        }
    }

    public void onTextClick(Dynamic.Data dynamic) {
        //        ToastUtil.showToast("点击了文本");
    }

    public void onShowAllClick(Dynamic.Data dynamic) {
        dynamic.setShowAll(!dynamic.isShowAll());
    }

    public void onShareClick(View view, Dynamic.Data dynamic) {
        Intent intent = new Intent(view.getContext(), PublishDynamicActivity.class);
        intent.putExtra("dynamic", dynamic);
        ((Activity) view.getContext()).startActivityForResult(intent, OmiAction.ACTION_PUBLISH_DYNAMIC);
    }

    public void onImageClick(View view, Dynamic.Data dynamic, int position) {
        String[] imageurls = new String[dynamic.getImages().size()];
        for (int i = 0; i < dynamic.getImages().size(); i++) {
            imageurls[i] = dynamic.getImages().get(i).getImage_name();
        }
        Intent intent = new Intent(view.getContext(), ImageBrowseActivity.class);
        intent.putExtra("imageurls", imageurls);
        intent.putExtra("position", position);
        view.getContext().startActivity(intent);
        //        ToastUtil.showToast("点击了第" + position + "张图片");
    }

    private View inputView;
    private View editView;

    public void setInputView(View inputView) {
        this.inputView = inputView;
    }

    public void setEditView(View editView) {
        this.editView = editView;
    }

    public void onCommentClick(Dynamic.Data dynamic) {
        if (inputView != null) {
            inputView.setVisibility(View.VISIBLE);
            inputView.setTag(dynamic);
        }
        if (editView != null) {
            IMEUtil.getInstance().showSoftKeyboard(editView);
            editView.setTag(dynamic);
        }
        //        ToastUtil.showToast("点击了评论");
    }

    public void onPraiseClick(Dynamic.Data dynamic) {
        if (CommonUtils.isFastDoubleClick()) return;
        DynamicLikes.Data like = null;
        if (dynamic.getZan() != null) for (DynamicLikes.Data data : dynamic.getZan()) {
            if (ApiByHttp.getInstance().getMember_id().equalsIgnoreCase(data.getMember_id() + "")) {
                like = data;
                break;
            }
        }
        if (like != null) {
            ApiByHttp.getInstance().delZanDynamic(dynamic.getDiscover_id(), like.getZan_id(), null);
            dynamic.getZan().remove(like);
            dynamic.notifyPropertyChanged(BR.zan);
            dynamic.notifyPropertyChanged(BR.zanNames);
            dynamic.notifyPropertyChanged(BR.hasPraise);
        } else {
            ApiByHttp.getInstance().zanDynamic(dynamic.getDiscover_id(), null);
            like = new DynamicLikes.Data();
            like.setDiscover_id(Integer.parseInt("0" + dynamic.getDiscover_id()));
            like.setZan_id(0);
            like.setMember_id(Integer.parseInt("0" + ApiByHttp.getInstance().getUser().getMember_id()));
            like.setMember_name(ApiByHttp.getInstance().getUser().getMember_name());
            like.setMember_nickname(ApiByHttp.getInstance().getUser().getMember_nickname());
            dynamic.getZan().add(like);
            dynamic.notifyPropertyChanged(BR.zan);
            dynamic.notifyPropertyChanged(BR.zanNames);
            dynamic.notifyPropertyChanged(BR.hasPraise);
        }
    }

    public void onVideoClick(Dynamic.Data dynamic) {
        //        ToastUtil.showToast("点击了视频");
    }
}
