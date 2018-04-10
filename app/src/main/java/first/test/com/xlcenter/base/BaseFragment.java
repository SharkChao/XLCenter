package first.test.com.xlcenter.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import first.test.com.xlcenter.R;
import first.test.com.xlcenter.annotation.ContentView;
import first.test.com.xlcenter.model.ResponseError;
import first.test.com.xlcenter.presenter.MainPresenter;
import first.test.com.xlcenter.utils.DensityUtil;


/**
 *
 */
public abstract class BaseFragment<UC> extends Fragment implements BasePresenter.BaseUi<UC>{

    public static int ivBack= R.mipmap.wn_iv_top_back;
    private TextView mTvTitle;
    private ImageView mBtnLeft;
    private ImageView mBtnRight;
    private Toolbar mToolbar;
    private LinearLayout mLLContent;
    private TextView mTvLeft;
    private TextView mTvRight;
    protected LinearLayout mLlLeft;
    private LinearLayout mLlRight;
    private ViewDataBinding mBind;
    private UC mCallback;
    private MainPresenter mMainController;

    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainController = MyLeanCloudApp.getInstance().getMainPresenter();
        View view = inflater.inflate(R.layout.layout_base_activity, container, false);
        ViewGroup content = view.findViewById(R.id.content);
        mBind = DataBindingUtil.inflate(getLayoutInflater(),getLayoutResId(),content,true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BasePresenter)mMainController).attachUi(this);
        setToolbar(view,savedInstanceState);
        handleArguments(getArguments());
        initTitle();
        initViews(mBind,savedInstanceState);
        initData();
        initEvent();

        isInit = true;
        /**初始化的时候去加载数据**/
        isCanLoadData();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }
    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }
    protected abstract void handleArguments(Bundle arguments);
    protected abstract void initTitle();
    protected abstract void initViews(ViewDataBinding viewDataBinding,Bundle savedInstanceState);
    protected abstract void initData();
    protected abstract void initEvent();

    protected int getLayoutResId() {
        for (Class c = getClass(); c != Fragment.class; c = c.getSuperclass()) {
            ContentView annotation = (ContentView) c.getAnnotation(ContentView.class);
            if (annotation != null) {
                return annotation.value();
            }
        }
        return 0;
    }

    /**
     * 1.设置沉浸式
     * 2.初始化toolbar中控件
     */
    private  void setToolbar(View view,Bundle bundle){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTvTitle = (TextView) view.findViewById(R.id.toolbar_title);
        mBtnRight = (ImageView) view.findViewById(R.id.btnRight);
        mBtnLeft = (ImageView) view.findViewById(R.id.btnLeft);
        mLLContent= (LinearLayout) view.findViewById(R.id.content);
        mLlLeft= (LinearLayout) view.findViewById(R.id.llLeft);
        mLlRight= (LinearLayout) view.findViewById(R.id.llRight);
        mTvLeft=(TextView) view.findViewById(R.id.tvLeft);
        mTvRight=(TextView) view.findViewById(R.id.tvRight);
        setLeftDefault();
    }
    protected void isShowToolBar(boolean isShow){
        mToolbar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    protected void isShowLeft(boolean isShow){
        mBtnLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTvLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    protected void isShowRight(boolean isShow){
        mBtnRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTvRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
    protected void setCenterTitle(String title){
        mTvTitle.setText(title == null || "".equalsIgnoreCase(title) ? "" : title);
    }
    protected  void setRightTitle(String title, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(title);
            mBtnRight.setVisibility(View.GONE);
            mLlRight.setOnClickListener(listener);
        }
    }
    protected void setRightTitleAndIcon(String title,@DrawableRes int icon, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvRight.setVisibility(View.VISIBLE);
            mTvRight.setText(title);
        }
        mBtnRight.setVisibility(View.VISIBLE);
        mBtnRight.setImageResource(icon);
        ViewGroup.LayoutParams linearParams = mBtnRight.getLayoutParams();
        linearParams.height = DensityUtil.dip2px(getActivity(),15);
        linearParams.width = DensityUtil.dip2px(getActivity(),15);
        mBtnRight.setLayoutParams(linearParams);
        mLlRight.setOnClickListener(listener);
    }
    protected  void setLeftTitle(String title, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvLeft.setVisibility(View.VISIBLE);
            mTvLeft.setText(title);
            mBtnLeft.setVisibility(View.GONE);
            mLlLeft.setOnClickListener(listener);
        }
    }
    protected  void setLeftTitleColor(int resId){
        int color=getResources().getColor(resId);
        mTvLeft.setVisibility(View.VISIBLE);
        mTvLeft.setTextColor(color);
    }
    protected  void setRightTitleColor(int resId){
        int color=getResources().getColor(resId);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setTextColor(color);
    }
    protected void setLeftTitleAndIcon(String title,@DrawableRes int icon, View.OnClickListener listener){
        if (title != null && !"".equalsIgnoreCase(title)){
            mTvLeft.setVisibility(View.VISIBLE);
            mTvLeft.setText(title);
        }
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setBackgroundResource(icon);
        ViewGroup.LayoutParams linearParams = mBtnLeft.getLayoutParams();
        linearParams.height = DensityUtil.dip2px(getActivity(),15);
        linearParams.width = DensityUtil.dip2px(getActivity(),15);
        mBtnLeft.setLayoutParams(linearParams);
        mLlLeft.setOnClickListener(listener);
    }
    protected ImageView getLeftButton(){
        return mBtnLeft;
    }
    protected ImageView getRightButton(){
        return mBtnRight;
    }
    protected void  setLeftDefault(){
        mTvLeft.setVisibility(View.VISIBLE);
        mTvLeft.setText("返回");
        mBtnLeft.setVisibility(View.VISIBLE);
        mBtnLeft.setBackgroundResource(ivBack);
        ViewGroup.LayoutParams linearParams = mBtnLeft.getLayoutParams();
        linearParams.height = DensityUtil.dip2px(getActivity(),15);
        linearParams.width = DensityUtil.dip2px(getActivity(),15);
        mBtnLeft.setLayoutParams(linearParams);
        mLlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.currentActivity.finish();
            }
        });
    }
    @Override
    public void onResponseError(ResponseError error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onResume() {
        super.onResume();
        mMainController.init();
        ((BasePresenter)mMainController).startUi(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMainController.suspend();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BasePresenter)mMainController).detachUi(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }

    @Override
    public void setCallbacks(UC callbacks) {
        mCallback = callbacks;
    }

    @Override
    public UC getCallbacks() {
        return mCallback;
    }
    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }
}
