package me.jessyan.armscomponent.commonsdk.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.IPresenter;

public abstract class BaseLazyFragment<P extends IPresenter> extends BaseFragment {
    /**
     * 缓存View，不需要每次都初始化
     */
    private View rootView;
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    /**
     * Fragment是否初始化视图完毕
     */
    protected boolean isPrepared = false;
    /**
     * Fragment是否第一次加载数据
     */
    protected boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initView(inflater, container, savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        onVisible();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //Do nothing, Because there is no lazy loading
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 视图不可见时候的回调
     */
    protected void onInvisible() {

    }

    /**
     * 视图可见时候的回调
     */
    protected void onVisible() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        isFirst = false;
        initData();
    }
}